package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 艦娘のコレクション
 *
 */
@Data
public class ShipMstCollection implements Serializable {

    private static final long serialVersionUID = 3473178293202796312L;

    /** 艦娘 */
    private Map<Integer, ShipMst> shipMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link ShipMstCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipMstCollection.class, ShipMstCollection::new)</code>
     * </blockquote>
     *
     * @return {@link ShipMstCollection}
     */
    public static ShipMstCollection get() {
        return Config.getDefault().get(ShipMstCollection.class, ShipMstCollection::new);
    }
}
