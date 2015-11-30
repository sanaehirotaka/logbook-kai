package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 艦娘の名前と種別を表します
 *
 */
public class ShipInfoCollection {

    /** 艦娘の情報 */
    private Map<Integer, ShipInfo> shipInfoMap = new HashMap<>();

    /**
     * 艦娘の情報を取得します。
     * @return 艦娘の情報
     */
    public Map<Integer, ShipInfo> getShipInfoMap() {
        return this.shipInfoMap;
    }

    /**
     * 艦娘の情報を設定します。
     * @param shipInfoMap 艦娘の情報
     */
    public void setShipInfoMap(Map<Integer, ShipInfo> shipInfoMap) {
        this.shipInfoMap = shipInfoMap;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>ShipInfoCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipInfoCollection.class)</code>
     * </blockquote>
     *
     * @return <code>ShipInfoCollection</code>
     */
    public static ShipInfoCollection get() {
        return Config.getDefault().get(ShipInfoCollection.class);
    }
}
