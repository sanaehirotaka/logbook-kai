package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * アイテムのコレクション
 *
 */
@Data
public class SlotItemCollection implements Serializable {

    private static final long serialVersionUID = -2530569251712024161L;

    /** アイテム */
    private Map<Integer, SlotItem> slotitemMap = new LinkedHashMap<>();

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
