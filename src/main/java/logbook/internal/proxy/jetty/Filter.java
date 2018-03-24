package logbook.internal.proxy.jetty;

/**
 * 動作に必要なデータのみ取得するためのフィルターです。
 *
 */
public class Filter {

    /** キャプチャーするリクエストのバイトサイズ上限 */
    public static final int MAX_POST_FIELD_SIZE = 1024 * 1024 * 12;

    /** setAttribute用のキー(Response) */
    public static final String RESPONSE_BODY = "res-body";

    /** setAttribute用のキー(Request) */
    public static final String REQUEST_BODY = "req-body";

    /** setAttribute用のキー(Content-Encoding) */
    public static final String CONTENT_ENCODING = "logbook.content-encoding";
}
