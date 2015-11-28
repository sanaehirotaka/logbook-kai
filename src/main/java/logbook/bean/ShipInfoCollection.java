package logbook.bean;

import java.util.HashMap;
import java.util.Map;

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
        return shipInfoMap;
    }

    /**
     * 艦娘の情報を設定します。
     * @param shipInfoMap 艦娘の情報
     */
    public void setShipInfoMap(Map<Integer, ShipInfo> shipInfoMap) {
        this.shipInfoMap = shipInfoMap;
    }
}
