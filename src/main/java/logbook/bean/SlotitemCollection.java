package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class SlotitemCollection {

    /** アイテム */
    private Map<Integer, Slotitem> slotitemMap = new HashMap<>();

    /**
     * アイテムを取得します。
     * @return アイテム
     */
    public Map<Integer, Slotitem> getSlotitemMap() {
        return this.slotitemMap;
    }

    /**
     * アイテムを設定します。
     * @param slotitemMap アイテム
     */
    public void setSlotitemMap(Map<Integer, Slotitem> slotitemMap) {
        this.slotitemMap = slotitemMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotitemCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotitemCollection.class)</code>
     * </blockquote>
     *
     * @return {@link SlotitemCollection}
     */
    public static SlotitemCollection get() {
        return Config.getDefault().get(SlotitemCollection.class);
    }
}
