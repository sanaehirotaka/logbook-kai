package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 艦娘
 *
 */
@Data
public class ShipCollection implements Serializable {

    private static final long serialVersionUID = -8680643608671594758L;

    /** 艦娘 */
    private Map<Integer, Ship> shipMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link ShipCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipCollection.class, ShipCollection::new)</code>
     * </blockquote>
     *
     * @return {@link ShipCollection}
     */
    public static ShipCollection get() {
        return Config.getDefault().get(ShipCollection.class, ShipCollection::new);
    }
}
