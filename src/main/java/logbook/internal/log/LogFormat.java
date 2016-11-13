package logbook.internal.log;

/**
 * ログ情報
 *
 */
public interface LogFormat<T> {

    /**
     * ログ名を取得します。
     * @return ログ名
     */
    String name();

    /**
     * ファイル名を取得します。
     * @return ファイル名
     */
    default String fileName() {
        return name() + ".csv";
    }

    /**
     * 代替ファイル名を取得します。
     * @return 代替ファイル名
     */
    default String alterFileName() {
        return name() + "_alternativefile.csv";
    }

    /**
     * ヘッダーを取得します。
     * @return ヘッダー
     */
    String header();

    /**
     * 文字列表現を取得します。
     * @return 文字列表現
     */
    String format(T value);
}
