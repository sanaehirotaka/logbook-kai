package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * アイテムのコレクション
 *
 */
public class SlotitemMstCollection implements Serializable {

    private static final long serialVersionUID = 8409969154221160905L;

    /** アイテム */
    private Map<Integer, SlotitemMst> slotitemMap = new LinkedHashMap<>();

    /**
     * アイテムを取得します。
     * @return アイテム
     */
    public Map<Integer, SlotitemMst> getSlotitemMap() {
        return this.slotitemMap;
    }

    /**
     * アイテムを設定します。
     * @param slotitemMap アイテム
     */
    public void setSlotitemMap(Map<Integer, SlotitemMst> slotitemMap) {
        this.slotitemMap = slotitemMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotitemMstCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotitemMstCollection.class, SlotitemMstCollection::new)</code>
     * </blockquote>
     *
     * @return {@link SlotitemMstCollection}
     */
    public static SlotitemMstCollection get() {
        return Config.getDefault().get(SlotitemMstCollection.class, SlotitemMstCollection::new);
    }
}
