package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class UseitemCollection implements Serializable {

    private static final long serialVersionUID = -3813660780247992556L;

    /** アイテム */
    private Map<Integer, Useitem> useitemMap = new LinkedHashMap<>();

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
     *     <code>Config.getDefault().get(UseitemCollection.class, UseitemCollection::new)</code>
     * </blockquote>
     *
     * @return {@link UseitemCollection}
     */
    public static UseitemCollection get() {
        return Config.getDefault().get(UseitemCollection.class, UseitemCollection::new);
    }
}
