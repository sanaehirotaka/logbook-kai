package logbook.internal.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpRequest;
import org.eclipse.jetty.client.api.ProxyConfiguration;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpVersion;

import logbook.bean.AppConfig;
import logbook.internal.LoggerHolder;
import logbook.internal.ThreadManager;
import logbook.plugin.PluginServices;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * リバースプロキシ
 *
 */
public final class ReverseProxyServlet extends ProxyServlet {

    private static final long serialVersionUID = 1L;

    private static final int BUFFER_SIZE = 4096;

    /** ライブラリバグ対応 (HttpRequest#queryを上書きする) */
    private static final Field QUERY_FIELD = getDeclaredField(HttpRequest.class, "query");

    /** リスナー */
    transient private List<ContentListenerSpi> listeners;

    /*
     * リモートホストがローカルループバックアドレス以外の場合400を返し通信しない
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        if (AppConfig.get().isAllowOnlyFromLocalhost()) {
            if (!InetAddress.getByName(request.getRemoteAddr()).isLoopbackAddress()) {
                response.setStatus(400);
                return;
            }
        }
        super.service(request, response);
    }

    /*
     * Hop-by-Hop ヘッダーを除去します
     */
    @Override
    protected void customizeProxyRequest(Request proxyRequest, HttpServletRequest request) {
        proxyRequest.onRequestContent(new RequestContentListener(request));

        if (!AppConfig.get().isUseProxy()) { // アップストリームプロキシがある場合は除外

            // HTTP/1.1 ならkeep-aliveを追加します
            if (proxyRequest.getVersion() == HttpVersion.HTTP_1_1) {
                proxyRequest.header(HttpHeader.CONNECTION, "keep-alive");
            }

            // Pragma: no-cache はプロキシ用なので Cache-Control: no-cache に変換します
            String pragma = proxyRequest.getHeaders().get(HttpHeader.PRAGMA);
            if ((pragma != null) && pragma.equals("no-cache")) {
                proxyRequest.header(HttpHeader.PRAGMA, null);
                if (!proxyRequest.getHeaders().containsKey(HttpHeader.CACHE_CONTROL.asString())) {
                    proxyRequest.header(HttpHeader.CACHE_CONTROL, "no-cache");
                }
            }
        }

        String queryString = ((org.eclipse.jetty.server.Request) request).getQueryString();
        fixQueryString(proxyRequest, queryString);

        super.customizeProxyRequest(proxyRequest, request);
    }

    @Override
    protected String filterResponseHeader(HttpServletRequest request,
            String headerName,
            String headerValue) {
        // Content Encoding を取得する
        if (headerName.compareToIgnoreCase("Content-Encoding") == 0) {
            request.setAttribute(Filter.CONTENT_ENCODING, headerValue);
        }
        return super.filterResponseHeader(request, headerName, headerValue);
    }

    /*
     * レスポンスが帰ってきた
     */
    @Override
    protected void onResponseContent(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse,
            byte[] buffer, int offset, int length) throws IOException {

        ByteArrayOutputStream stream = (ByteArrayOutputStream) request.getAttribute(Filter.RESPONSE_BODY);
        if (stream == null) {
            stream = new ByteArrayOutputStream();
            request.setAttribute(Filter.RESPONSE_BODY, stream);
        }
        // ストリームに書き込む
        stream.write(buffer, offset, length);

        super.onResponseContent(request, response, proxyResponse, buffer, offset, length);
    }

    /*
     * レスポンスが完了した
     */
    @Override
    protected void onResponseSuccess(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse) {
        try {
            byte[] postField = (byte[]) request.getAttribute(Filter.REQUEST_BODY);
            ByteArrayOutputStream stream = (ByteArrayOutputStream) request.getAttribute(Filter.RESPONSE_BODY);
            if (stream != null) {
                if (this.listeners == null) {
                    this.listeners = PluginServices.instances(ContentListenerSpi.class)
                            .collect(Collectors.toList());
                }

                for (ContentListenerSpi listener : this.listeners) {
                    RequestMetaData requestMetaData = RequestMetaDataWrapper.build(request, postField);
                    if (listener.test(requestMetaData)) {
                        ResponseMetaData responseMetaData = ResponseMetaDataWrapper.build(response,
                                stream.toByteArray());
                        Runnable task = () -> {
                            try {
                                listener.accept(requestMetaData, responseMetaData);
                            } catch (Exception e) {
                                LoggerHolder.get().warn("リバースプロキシ サーブレットで例外が発生", e);
                            }
                        };
                        ThreadManager.getExecutorService().submit(task);
                    }
                }
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("リバースプロキシ サーブレットで例外が発生 req=" + request, e);
        }
        super.onResponseSuccess(request, response, proxyResponse);
    }

    /*
     * HttpClientを作成する
     */
    @Override
    protected HttpClient newHttpClient() {
        HttpClient client = super.newHttpClient();
        // プロキシを設定する
        if (AppConfig.get().isUseProxy()) {
            // ポート
            int port = AppConfig.get().getProxyPort();
            // ホスト
            String host = AppConfig.get().getProxyHost();
            // 設定する
            client.setProxyConfiguration(new ProxyConfiguration(host, port));
        }
        return client;
    }

    /**
     * private フィールドを取得する
     * @param clazz クラス
     * @param string フィールド名
     * @return フィールドオブジェクト
     */
    private static <T> Field getDeclaredField(Class<T> clazz, String string) {
        try {
            Field field = clazz.getDeclaredField(string);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     * ライブラリのバグを修正します<br>
     * URLにマルチバイト文字が含まれている場合にURLが正しく組み立てられないバグを修正します
     * </p>
     */
    private static void fixQueryString(Request proxyRequest, String queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            if (proxyRequest instanceof HttpRequest) {
                try {
                    QUERY_FIELD.set(proxyRequest, queryString);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    static class RequestMetaDataWrapper implements RequestMetaData {

        private String contentType;

        private String method;

        private Map<String, List<String>> parameterMap;

        private String queryString;

        private String requestURI;

        private Optional<InputStream> requestBody;

        @Override
        public String getContentType() {
            return this.contentType;
        }

        void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public String getMethod() {
            return this.method;
        }

        void setMethod(String method) {
            this.method = method;
        }

        @Override
        public Map<String, List<String>> getParameterMap() {
            return this.parameterMap;
        }

        void setParameterMap(Map<String, List<String>> parameterMap) {
            this.parameterMap = parameterMap;
        }

        @Override
        public String getQueryString() {
            return this.queryString;
        }

        void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public String getRequestURI() {
            return this.requestURI;
        }

        void setRequestURI(String requestURI) {
            this.requestURI = requestURI;
        }

        @Override
        public Optional<InputStream> getRequestBody() {
            return this.requestBody;
        }

        void setRequestBody(Optional<InputStream> requestBody) {
            this.requestBody = requestBody;
        }

        static RequestMetaData build(HttpServletRequest req, byte[] body)
                throws UnsupportedEncodingException, URISyntaxException {
            RequestMetaDataWrapper meta = new RequestMetaDataWrapper();

            meta.setContentType(req.getContentType());
            meta.setMethod(req.getMethod().toString());

            BiConsumer<Map<String, List<String>>, String[]> accumulator = (m, v) -> {
                if (v.length == 2) {
                    m.computeIfAbsent(v[0], k -> new ArrayList<>())
                            .add(v[1]);
                }
            };
            BiConsumer<Map<String, List<String>>, Map<String, List<String>>> combiner = (m1, m2) -> {
                m2.entrySet()
                        .stream()
                        .forEach(entry -> m1.merge(entry.getKey(), entry.getValue(), (l1, l2) -> {
                            l1.addAll(l2);
                            return l1;
                        }));
            };
            if (body == null) {
                body = new byte[0];
            }
            String bodystr = URLDecoder.decode(new String(body, StandardCharsets.UTF_8), "UTF-8");
            meta.setParameterMap(Arrays.stream(bodystr.split("&"))
                    .map(kv -> kv.split("="))
                    .collect(LinkedHashMap::new, accumulator, combiner));

            meta.setQueryString(req.getQueryString());
            meta.setRequestURI(req.getRequestURI());
            meta.setRequestBody(Optional.of(new ByteArrayInputStream(body)));

            return meta;
        }
    }

    static class ResponseMetaDataWrapper implements ResponseMetaData {

        private int status;

        private String contentType;

        private Optional<InputStream> responseBody;

        @Override
        public int getStatus() {
            return this.status;
        }

        void setStatus(int status) {
            this.status = status;
        }

        @Override
        public String getContentType() {
            return this.contentType;
        }

        void setContentType(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public Optional<InputStream> getResponseBody() {
            return this.responseBody;
        }

        void setResponseBody(Optional<InputStream> responseBody) {
            this.responseBody = responseBody;
        }

        static ResponseMetaData build(HttpServletResponse res, byte[] body) throws IOException {
            ResponseMetaDataWrapper meta = new ResponseMetaDataWrapper();

            meta.setStatus(res.getStatus());
            meta.setContentType(res.getContentType());
            meta.setResponseBody(Optional.of(new ByteArrayInputStream(ungzip(body))));

            return meta;
        }

        private static byte[] ungzip(byte[] data) throws IOException {
            try (InputStream in = new ByteArrayInputStream(data)) {
                if (in.available() > 1) {
                    in.mark(Short.BYTES);
                    int magicbyte = in.read() << 8 ^ in.read();
                    in.reset();
                    InputStream wrap;
                    if (magicbyte == 0x1f8b) {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        wrap = new GZIPInputStream(in);
                        try {
                            byte[] buffer = new byte[BUFFER_SIZE];
                            int n;
                            while (-1 != (n = wrap.read(buffer))) {
                                out.write(buffer, 0, n);
                            }
                        } finally {
                            wrap.close();
                        }
                        return out.toByteArray();
                    }
                }
            }
            return data;
        }
    }
}