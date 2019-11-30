package logbook.internal.log;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import logbook.internal.Logs;

public abstract class LogFormatBase<T> implements LogFormat<T> {

    /** 日付書式 */
    protected static final DateTimeFormatter DATE_FORMAT = Logs.DATE_FORMAT;

    /**
     * タイムゾーンをJSTとして現在の日付/時間を取得します
     *
     * @return 現在の日付/時間
     */
    protected static ZonedDateTime now() {
        return Logs.now();
    }

    /**
     * タイムゾーンをJSTとして現在の日付/時間を"yyyy-MM-dd HH:mm:ss"形式の文字列として取得します
     *
     * @return 現在の日付/時間
     */
    protected static String nowString() {
        return Logs.nowString();
    }
}
