package logbook.internal.proxy;

import java.nio.ByteBuffer;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Request.ContentListener;

/**
 * リクエストをキャプチャします
 *
 */
public final class RequestContentListener implements ContentListener {

    private final HttpServletRequest httpRequest;

    /**
     * @param request {@link HttpServletRequest}
     */
    public RequestContentListener(HttpServletRequest request) {
        this.httpRequest = request;
    }

    /*
     * 必要なPOSTデータの場合キャプチャします
     */
    @Override
    public void onContent(Request request, ByteBuffer buffer) {
        int length = buffer.remaining();

        if (((length > 0) && (length <= Filter.MAX_POST_FIELD_SIZE))) {
            byte[] bytes = new byte[length];
            buffer.get(bytes);

            CaptureHolder holder = (CaptureHolder) this.httpRequest.getAttribute(Filter.CONTENT_HOLDER);
            if (holder == null) {
                holder = new CaptureHolder();
                this.httpRequest.setAttribute(Filter.CONTENT_HOLDER, holder);
            }
            holder.putRequest(bytes);
        }
    }
}
