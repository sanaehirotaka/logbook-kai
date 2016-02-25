package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class SlotitemDescriptionCollection {

    /** アイテム */
    private Map<Integer, SlotitemDescription> slotitemMap = new HashMap<>();

    /**
     * アイテムを取得します。
     * @return アイテム
     */
    public Map<Integer, SlotitemDescription> getSlotitemMap() {
        return this.slotitemMap;
    }

    /**
     * アイテムを設定します。
     * @param slotitemMap アイテム
     */
    public void setSlotitemMap(Map<Integer, SlotitemDescription> slotitemMap) {
        this.slotitemMap = slotitemMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotitemDescriptionCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotitemDescriptionCollection.class, SlotitemDescriptionCollection::new)</code>
     * </blockquote>
     *
     * @return {@link SlotitemDescriptionCollection}
     */
    public static SlotitemDescriptionCollection get() {
        return Config.getDefault().get(SlotitemDescriptionCollection.class, SlotitemDescriptionCollection::new);
    }
}
