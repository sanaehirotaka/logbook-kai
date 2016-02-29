package logbook.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 艦娘のコレクション
 *
 */
public class ShipDescriptionCollection implements Serializable {

    private static final long serialVersionUID = 3473178293202796312L;

    /** 艦娘 */
    private Map<Integer, ShipDescription> shipMap = new HashMap<>();

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public Map<Integer, ShipDescription> getShipMap() {
        return this.shipMap;
    }

    /**
     * 艦娘を設定します。
     * @param shipMap 艦娘
     */
    public void setShipMap(Map<Integer, ShipDescription> shipMap) {
        this.shipMap = shipMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link ShipDescriptionCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipDescriptionCollection.class, ShipDescriptionCollection::new)</code>
     * </blockquote>
     *
     * @return {@link ShipDescriptionCollection}
     */
    public static ShipDescriptionCollection get() {
        return Config.getDefault().get(ShipDescriptionCollection.class, ShipDescriptionCollection::new);
    }
}
