package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 艦娘のコレクション
 *
 */
public class ShipMstCollection implements Serializable {

    private static final long serialVersionUID = 3473178293202796312L;

    /** 艦娘 */
    private Map<Integer, ShipMst> shipMap = new LinkedHashMap<>();

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public Map<Integer, ShipMst> getShipMap() {
        return this.shipMap;
    }

    /**
     * 艦娘を設定します。
     * @param shipMap 艦娘
     */
    public void setShipMap(Map<Integer, ShipMst> shipMap) {
        this.shipMap = shipMap;
    }

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
