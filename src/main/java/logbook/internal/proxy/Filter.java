package logbook.internal.proxy;

/**
 * 動作に必要なデータのみ取得するためのフィルターです。
 *
 */
public class Filter {

    /** キャプチャーするリクエストのバイトサイズ上限 */
    public static final int MAX_POST_FIELD_SIZE = 1024 * 1024 * 12;

    /** setAttribute用のキー(CaptureHolder) */
    public static final String CONTENT_HOLDER = "logbook.content-holder";
}
