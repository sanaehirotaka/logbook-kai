package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class UseitemCollection {

    /** アイテムの情報 */
    private Map<Integer, Useitem> useitemMap = new HashMap<>();

    /**
     * アイテムの情報を取得します。
     * @return アイテムの情報
     */
    public Map<Integer, Useitem> getUseitemMap() {
        return this.useitemMap;
    }

    /**
     * アイテムの情報を設定します。
     * @param useitemMap アイテムの情報
     */
    public void setUseitemMap(Map<Integer, Useitem> useitemMap) {
        this.useitemMap = useitemMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link UseitemCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(UseitemCollection.class)</code>
     * </blockquote>
     *
     * @return {@link UseitemCollection}
     */
    public static UseitemCollection get() {
        return Config.getDefault().get(UseitemCollection.class);
    }
}
