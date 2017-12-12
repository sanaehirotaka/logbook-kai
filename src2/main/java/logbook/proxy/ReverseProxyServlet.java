package logbook.proxy;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpProxy;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.proxy.ProxyServlet;
import org.eclipse.jetty.util.Callback;

import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.internal.ThreadManager;

/**
 * リバースプロキシ サーブレット
 *
 */
final class ReverseProxyServlet extends ProxyServlet {

    private static final long serialVersionUID = -1;

    private static final String REQUEST_BODY = "req-body"; //$NON-NLS-1$

    private static final String RESPONSE_BODY = "res-body"; //$NON-NLS-1$

    private final List<ContentListenerSpi> services;

    public ReverseProxyServlet(List<ContentListenerSpi> services) {
        this.services = services;
    }

    @Override
    protected void sendProxyRequest(
            HttpServletRequest clientRequest,
            HttpServletResponse proxyResponse,
            Request proxyRequest) {
        try {
            RequestMetaData requestMetaData = RequestMetaDataWrapper.build(clientRequest);

            boolean require = this.services.stream()
                    .anyMatch(l -> l.test(requestMetaData));
            if (require) {
                proxyRequest.onRequestContent((r, b) -> {
                    clientRequest.setAttribute(REQUEST_BODY, Arrays.copyOf(b.array(), b.limit()));
                });
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("ReverseProxyServlet.2"), e); //$NON-NLS-1$
        }
        super.sendProxyRequest(clientRequest, proxyResponse, proxyRequest);
    }

    /*
     * レスポンスが帰ってきた
     */
    @Override
    protected void onResponseContent(
            HttpServletRequest request,
            HttpServletResponse response,
            Response proxyResponse,
            byte[] buffer,
            int offset,
            int length,
            Callback callback) {
        try {
            RequestMetaData requestMetaData = RequestMetaDataWrapper.build(request);

            boolean require = this.services.stream()
                    .anyMatch(l -> l.test(requestMetaData));
            if (require) {
                ByteArrayOutputStream stream = (ByteArrayOutputStream) request.getAttribute(RESPONSE_BODY);
                if (stream == null) {
                    stream = new ByteArrayOutputStream();
                    request.setAttribute(RESPONSE_BODY, stream);
                }
                // ストリームに書き込む
                stream.write(buffer, offset, length);
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("ReverseProxyServlet.3"), e); //$NON-NLS-1$
        }

        super.onResponseContent(request, response, proxyResponse, buffer, offset, length, callback);
    }

    /*
     * レスポンスが完了した
     */
    @Override
    protected void onProxyResponseSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Response proxyResponse) {
        ByteArrayOutputStream res = (ByteArrayOutputStream) request.getAttribute(RESPONSE_BODY);

        try {
            if (res != null) {
                byte[] reqBytes = (byte[]) request.getAttribute(REQUEST_BODY);
                byte[] resBytes = res.toByteArray();
                for (ContentListenerSpi listener : this.services) {
                    RequestMetaData requestMetaData = RequestMetaDataWrapper.build(request, reqBytes);
                    ResponseMetaData responseMetaData = ResponseMetaDataWrapper.build(response, resBytes);

                    Runnable task = () -> {
                        try {
                            if (listener.test(requestMetaData)) {
                                listener.accept(requestMetaData, responseMetaData);
                            }
                        } catch (Exception e) {
                            LoggerHolder.LOG.warn(Messages.getString("ReverseProxyServlet.4"), e); //$NON-NLS-1$
                        }
                    };
                    ThreadManager.getExecutorService().submit(task);
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("ReverseProxyServlet.5"), e); //$NON-NLS-1$
        }
        super.onProxyResponseSuccess(request, response, proxyResponse);
    }

    /*
     * プロキシヘッダの追加
     */
    @Override
    protected void addProxyHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
        if (AppConfig.get().isConnectionClose()) {
            // 通信エラーを抑止する Connection: closeヘッダを追加
            proxyRequest.header(HttpHeader.CONNECTION, "close");
        }
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
            client.getProxyConfiguration().getProxies().add(new HttpProxy(host, port));
        }
        return client;
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ReverseProxyServlet.class);
    }
}
