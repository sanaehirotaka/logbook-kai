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
public class SlotitemMstCollection implements Serializable {

    private static final long serialVersionUID = 8409969154221160905L;

    /** アイテム */
    private Map<Integer, SlotitemMst> slotitemMap = new LinkedHashMap<>();

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
