package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

public class ShipCollection {

    /** 艦娘 */
    private Map<Integer, Ship> shipMap = new HashMap<>();

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public Map<Integer, Ship> getShipMap() {
        return this.shipMap;
    }

    /**
     * 艦娘を設定します。
     * @param shipMap 艦娘
     */
    public void setShipMap(Map<Integer, Ship> shipMap) {
        this.shipMap = shipMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link ShipCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipCollection.class)</code>
     * </blockquote>
     *
     * @return {@link ShipCollection}
     */
    public static ShipCollection get() {
        return Config.getDefault().get(ShipCollection.class);
    }
}
