package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class SlotItemCollection implements Serializable {

    private static final long serialVersionUID = -2530569251712024161L;

    /** アイテム */
    private Map<Integer, SlotItem> slotitemMap = new LinkedHashMap<>();

    /**
     * アイテムを取得します。
     * @return アイテム
     */
    public Map<Integer, SlotItem> getSlotitemMap() {
        return this.slotitemMap;
    }

    /**
     * アイテムを設定します。
     * @param slotitemMap アイテム
     */
    public void setSlotitemMap(Map<Integer, SlotItem> slotitemMap) {
        this.slotitemMap = slotitemMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotItemCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotItemCollection.class, SlotItemCollection::new)</code>
     * </blockquote>
     *
     * @return {@link SlotItemCollection}
     */
    public static SlotItemCollection get() {
        return Config.getDefault().get(SlotItemCollection.class, SlotItemCollection::new);
    }
}
