package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテム種類のコレクション
 *
 */
public class SlotitemEquiptypeCollection {

    /** アイテム種類 */
    private Map<Integer, SlotitemEquiptype> equiptypeMap = new HashMap<>();

    /**
     * アイテム種類を取得します。
     * @return アイテム種類
     */
    public Map<Integer, SlotitemEquiptype> getEquiptypeMap() {
        return this.equiptypeMap;
    }

    /**
     * アイテム種類を設定します。
     * @param equiptypeMap アイテム種類
     */
    public void setEquiptypeMap(Map<Integer, SlotitemEquiptype> equiptypeMap) {
        this.equiptypeMap = equiptypeMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotitemEquiptypeCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotitemEquiptypeCollection.class)</code>
     * </blockquote>
     *
     * @return {@link SlotitemEquiptypeCollection}
     */
    public static SlotitemEquiptypeCollection get() {
        return Config.getDefault().get(SlotitemEquiptypeCollection.class);
    }
}
