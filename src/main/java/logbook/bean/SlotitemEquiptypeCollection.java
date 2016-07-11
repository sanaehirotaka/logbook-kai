package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * アイテム種類のコレクション
 *
 */
@Data
public class SlotitemEquiptypeCollection implements Serializable {

    private static final long serialVersionUID = 1623208831316913362L;

    /** アイテム種類 */
    private Map<Integer, SlotitemEquiptype> equiptypeMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link SlotitemEquiptypeCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(SlotitemEquiptypeCollection.class, SlotitemEquiptypeCollection::new)</code>
     * </blockquote>
     *
     * @return {@link SlotitemEquiptypeCollection}
     */
    public static SlotitemEquiptypeCollection get() {
        return Config.getDefault().get(SlotitemEquiptypeCollection.class, SlotitemEquiptypeCollection::new);
    }
}
