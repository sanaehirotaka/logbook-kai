package logbook.internal.proxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyAdapter;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ThreadPoolConfiguration;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.CompositeByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.internal.ThreadManager;
import logbook.plugin.PluginServices;
import logbook.proxy.ContentListenerSpi;
import logbook.proxy.ProxyServerSpi;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

public class NettyProxyServer implements ProxyServerSpi {

    @Override
    public void run() {
        try {
            int port = AppConfig.get()
                    .getListenPort();
            boolean allowLocalOnly = AppConfig.get()
                    .isAllowOnlyFromLocalhost();

            ThreadPoolConfiguration conf = new ThreadPoolConfiguration()
                    .withAcceptorThreads(1)
                    .withClientToProxyWorkerThreads(2)
                    .withProxyToServerWorkerThreads(2);

            HttpProxyServerBootstrap serverBuilder = DefaultHttpProxyServer.bootstrap()
                    .withPort(port)
                    .withAllowLocalOnly(allowLocalOnly)
                    .withConnectTimeout(30000)
                    .withThreadPoolConfiguration(conf)
                    .withFiltersSource(new CaptureAdapter())
                    .withProxyAlias("logbook-proxy");

            // 上流プロキシを設定する
            if (AppConfig.get().isUseProxy()) {
                serverBuilder.withChainProxyManager((req, proxies) -> {
                    ChainedProxy proxy = new ChainedProxyAdapter() {
                        @Override
                        public InetSocketAddress getChainedProxyAddress() {
                            String host = AppConfig.get().getProxyHost();
                            int port = AppConfig.get().getProxyPort();
                            return new InetSocketAddress(host, port);
                        }
                    };
                    proxies.add(proxy);
                });
            }
            HttpProxyServer server = serverBuilder.start();
            try {
                Thread.currentThread().join();
            } catch (InterruptedException ex) {
                server.abort();
            }
        } catch (Exception e) {
            Throwable c = e.getCause();
            this.displayAlert(c != null ? c : e);
        }
    }

    private void displayAlert(Throwable e) {
        // Title
        String title = Messages.getString("ProxyServer.7"); //$NON-NLS-1$
        // Message
        StringBuilder sb = new StringBuilder(Messages.getString("ProxyServer.8")); //$NON-NLS-1$
        if (e instanceof BindException) {
            sb.append("\n"); //$NON-NLS-1$
            sb.append(Messages.getString("ProxyServer.10")); //$NON-NLS-1$
        }
        String message = sb.toString();
        // StackTrace
        StringWriter w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        String stackTrace = w.toString();

        Runnable runnable = () -> {
            Alert alert = new Alert(AlertType.WARNING);
            TextArea textArea = new TextArea(stackTrace);
            alert.getDialogPane().setExpandableContent(textArea);

            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        };

        Platform.runLater(runnable);
    }

    static class Interceptor {

        List<ContentListenerSpi> listeners;

        public Interceptor() {
            this.listeners = PluginServices.instances(ContentListenerSpi.class)
                    .collect(Collectors.toList());
        }

        public void intercept(HttpResponse res, byte[] resbody, HttpRequest req, byte[] reqbody) {
            try {
                for (ContentListenerSpi listener : this.listeners) {
                    RequestMetaData requestMetaData = RequestMetaDataWrapper.build(req, resbody);
                    if (listener.test(requestMetaData)) {
                        ResponseMetaData responseMetaData = ResponseMetaDataWrapper.build(res, reqbody);
                        Runnable task = () -> {
                            try {
                                listener.accept(requestMetaData, responseMetaData);

                            } catch (Exception e) {
                                LoggerHolder.LOG.warn("リバースプロキシ サーブレットで例外が発生", e);
                            }
                        };
                        ThreadManager.getExecutorService().submit(task);
                    }
                }
            } catch (Exception e) {
                LoggerHolder.LOG.warn("リバースプロキシ サーブレットで例外が発生", e);
            }
        }
    }

    static class CaptureAdapter extends HttpFiltersSourceAdapter {

        private Interceptor interceptor = new Interceptor();

        @Override
        public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
            return new CaptureFilters(originalRequest, ctx, this.interceptor);
        }
    }

    static class CaptureFilters extends HttpFiltersAdapter {

        private Interceptor interceptor;

        private boolean released;

        private CompositeByteBuf requestBuf = this.ctx.alloc()
                .compositeBuffer();

        private CompositeByteBuf responseBuf = this.ctx.alloc()
                .compositeBuffer();

        private HttpRequest request;

        private HttpResponse response;

        CaptureFilters(HttpRequest originalRequest, ChannelHandlerContext ctx, Interceptor interceptor) {
            super(originalRequest, ctx);
            this.interceptor = interceptor;
        }

        @Override
        public HttpResponse proxyToServerRequest(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.requestBuf, httpObject);
            }
            return super.proxyToServerRequest(httpObject);
        }

        @Override
        public HttpObject serverToProxyResponse(HttpObject httpObject) {
            if (!this.released) {
                this.add(this.responseBuf, httpObject);
            }
            return super.serverToProxyResponse(httpObject);
        }

        @Override
        public HttpObject proxyToClientResponse(HttpObject httpObject) {
            if (httpObject instanceof HttpResponse) {
                HttpResponse res = (HttpResponse) httpObject;
                this.response = res;
                if (!HttpResponseStatus.OK.equals(res.getStatus())) {
                    this.release();
                }
            }
            return super.proxyToClientResponse(httpObject);
        }

        @Override
        public HttpResponse clientToProxyRequest(HttpObject httpObject) {
            if (httpObject instanceof HttpRequest) {
                this.request = (HttpRequest) httpObject;
            }
            return super.clientToProxyRequest(httpObject);
        }

        @Override
        public void serverToProxyResponseReceived() {
            if (!this.released) {
                try {
                    if (this.request != null && this.response != null) {
                        byte[] resbody = this.toByteArray(this.requestBuf.duplicate());
                        byte[] reqbody = this.toByteArray(this.responseBuf.duplicate());
                        this.interceptor.intercept(this.response, resbody, this.request, reqbody);
                    }
                } catch (Exception e) {
                    LoggerHolder.LOG.error("リバースプロキシ サーブレットで例外が発生", e);
                } finally {
                    this.release();
                }
            }
        }

        private void add(CompositeByteBuf buf, HttpObject httpObject) {
            if (httpObject instanceof HttpContent) {
                HttpContent httpContent = (HttpContent) httpObject;
                ByteBuf content = httpContent.content();
                if (content.isReadable()) {
                    buf.addComponent(content.retain());
                    buf.writerIndex(buf.writerIndex() + content.readableBytes());
                }
            }
        }

        private void release() {
            if (!this.released) {
                this.released = true;
                this.requestBuf.release();
                this.responseBuf.release();
                this.request = null;
                this.response = null;
            }
        }

        private byte[] toByteArray(ByteBuf buf) throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (ByteBufInputStream in = new ByteBufInputStream(buf)) {
                if (in.available() > 1) {
                    in.mark(Short.BYTES);
                    int magicbyte = in.readUnsignedShort();
                    in.reset();
                    InputStream wrap;
                    if (magicbyte == 0x1f8b) {
                        wrap = new GZIPInputStream(in);
                    } else {
                        wrap = in;
                    }
                    IOUtils.copy(wrap, out);
                }
            }
            return out.toByteArray();
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
        public Map<String, Collection<String>> getHeaders() {
            throw new UnsupportedOperationException();
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
        public String getProtocol() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getQueryString() {
            return this.queryString;
        }

        void setQueryString(String queryString) {
            this.queryString = queryString;
        }

        @Override
        public String getRemoteAddr() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getRemotePort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getRequestURI() {
            return this.requestURI;
        }

        void setRequestURI(String requestURI) {
            this.requestURI = requestURI;
        }

        @Override
        public String getRequestURL() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getScheme() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getServerName() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getServerPort() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<InputStream> getRequestBody() {
            return this.requestBody;
        }

        void setRequestBody(Optional<InputStream> requestBody) {
            this.requestBody = requestBody;
        }

        static RequestMetaData build(HttpRequest req, byte[] body)
                throws UnsupportedEncodingException, URISyntaxException {
            HttpHeaders header = req.headers();
            RequestMetaDataWrapper meta = new RequestMetaDataWrapper();

            meta.setContentType(header.get(Names.CONTENT_TYPE));
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
            String bodystr = URLDecoder.decode(new String(body, StandardCharsets.UTF_8), "UTF-8");
            meta.setParameterMap(Arrays.stream(bodystr.split("&"))
                    .map(kv -> kv.split("="))
                    .collect(LinkedHashMap::new, accumulator, combiner));

            URI url = new URI(req.getUri());
            meta.setQueryString(url.getQuery());
            meta.setRequestURI(url.getPath());
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
        public Map<String, Collection<String>> getHeaders() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<InputStream> getResponseBody() {
            return this.responseBody;
        }

        void setResponseBody(Optional<InputStream> responseBody) {
            this.responseBody = responseBody;
        }

        static ResponseMetaData build(HttpResponse res, byte[] body)
                throws UnsupportedEncodingException, URISyntaxException {
            HttpHeaders header = res.headers();
            ResponseMetaDataWrapper meta = new ResponseMetaDataWrapper();

            meta.setStatus(res.getStatus().code());
            meta.setContentType(header.get(Names.CONTENT_TYPE));
            meta.setResponseBody(Optional.of(new ByteArrayInputStream(body)));

            return meta;
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(NettyProxyServer.class);
    }
}
