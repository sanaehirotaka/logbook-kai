package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class UseitemCollection {

    /** アイテム */
    private Map<Integer, Useitem> useitemMap = new HashMap<>();

    /**
     * アイテムを取得します。
     * @return アイテム
     */
    public Map<Integer, Useitem> getUseitemMap() {
        return this.useitemMap;
    }

    /**
     * アイテムを設定します。
     * @param useitemMap アイテム
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
