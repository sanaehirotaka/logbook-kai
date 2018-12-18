package logbook.internal.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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

    /** リスナー */
    private transient List<ContentListenerSpi> listeners;

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

    /*
     * レスポンスが帰ってきた
     */
    @Override
    protected void onResponseContent(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse,
            byte[] buffer, int offset, int length) throws IOException {

        CaptureHolder holder = (CaptureHolder) request.getAttribute(Filter.CONTENT_HOLDER);
        if (holder == null) {
            holder = new CaptureHolder();
            request.setAttribute(Filter.CONTENT_HOLDER, holder);
        }
        // ストリームに書き込む
        holder.putResponse(buffer);

        super.onResponseContent(request, response, proxyResponse, buffer, offset, length);
    }

    /*
     * レスポンスが完了した
     */
    @Override
    protected void onResponseSuccess(HttpServletRequest request, HttpServletResponse response,
            Response proxyResponse) {
        try {
            CaptureHolder holder = (CaptureHolder) request.getAttribute(Filter.CONTENT_HOLDER);
            if (holder != null) {
                if (this.listeners == null) {
                    this.listeners = PluginServices.instances(ContentListenerSpi.class).collect(Collectors.toList());
                }

                for (ContentListenerSpi listener : this.listeners) {
                    RequestMetaData requestMetaData = RequestMetaDataWrapper.build(request, holder.getRequest());
                    if (listener.test(requestMetaData)) {
                        ResponseMetaData responseMetaData = ResponseMetaDataWrapper.build(response,
                                holder.getResponse());
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
                holder.clear();
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("リバースプロキシ サーブレットで例外が発生 req=" + request, e);
        } finally {
            // Help GC
            request.removeAttribute(Filter.CONTENT_HOLDER);
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
     * <p>
     * ライブラリのバグを修正します<br>
     * URLにマルチバイト文字が含まれている場合にURLが正しく組み立てられないバグを修正します
     * </p>
     */
    private static void fixQueryString(Request proxyRequest, String queryString) {
        if (queryString != null && !queryString.isEmpty()) {
            if (proxyRequest instanceof HttpRequest) {
                try {
                    FieldHolder.QUERY_FIELD.set(proxyRequest, queryString);
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

        static RequestMetaData build(HttpServletRequest req, InputStream body)
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
            String bodystr = "";
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(body, StandardCharsets.UTF_8))) {
                bodystr = URLDecoder.decode(reader.readLine(), "UTF-8");
            } catch (Exception e) {
                // NOP
            }

            meta.setParameterMap(Arrays.stream(bodystr.split("&"))
                    .map(kv -> kv.split("="))
                    .collect(LinkedHashMap::new, accumulator, combiner));

            meta.setQueryString(req.getQueryString());
            meta.setRequestURI(req.getRequestURI());
            meta.setRequestBody(Optional.of(body));

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

        static ResponseMetaData build(HttpServletResponse res, InputStream body) throws IOException {
            ResponseMetaDataWrapper meta = new ResponseMetaDataWrapper();

            meta.setStatus(res.getStatus());
            meta.setContentType(res.getContentType());
            meta.setResponseBody(Optional.of(ungzip(body)));

            return meta;
        }

        private static InputStream ungzip(InputStream body) throws IOException {
            if (body.available() > 1) {
                body.mark(Short.BYTES);
                int magicbyte = body.read() << 8 ^ body.read();
                body.reset();
                if (magicbyte == 0x1f8b) {
                    return new GZIPInputStream(body);
                }
            }
            body.reset();
            return body;
        }
    }

    private static class FieldHolder {
        /** ライブラリバグ対応 (HttpRequest#queryを上書きする) */
        static final Field QUERY_FIELD = getDeclaredField(HttpRequest.class, "query");

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
    }
}