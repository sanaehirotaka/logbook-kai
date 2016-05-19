package logbook.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import logbook.proxy.RequestMetaData;

/**
 * api_req_kousyou/createship
 *
 */
public class Createship implements Serializable {

    private static final long serialVersionUID = -6347833469189820203L;

    /** api_kdock_id */
    private Integer kdockId;

    /** api_item1 */
    private Integer item1;

    /** api_item2 */
    private Integer item2;

    /** api_item3 */
    private Integer item3;

    /** api_item4 */
    private Integer item4;

    /** api_item5 */
    private Integer item5;

    /** api_highspeed */
    private Integer highspeed;

    /** api_large_flag */
    private Integer largeFlag;

    /** 秘書艦 */
    private Ship secretary;

    /**
     * api_kdock_idを取得します。
     * @return api_kdock_id
     */
    public Integer getKdockId() {
        return this.kdockId;
    }

    /**
     * api_kdock_idを設定します。
     * @param kdockId api_kdock_id
     */
    public void setKdockId(Integer kdockId) {
        this.kdockId = kdockId;
    }

    /**
     * api_item1を取得します。
     * @return api_item1
     */
    public Integer getItem1() {
        return this.item1;
    }

    /**
     * api_item1を設定します。
     * @param item1 api_item1
     */
    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    /**
     * api_item2を取得します。
     * @return api_item2
     */
    public Integer getItem2() {
        return this.item2;
    }

    /**
     * api_item2を設定します。
     * @param item2 api_item2
     */
    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    /**
     * api_item3を取得します。
     * @return api_item3
     */
    public Integer getItem3() {
        return this.item3;
    }

    /**
     * api_item3を設定します。
     * @param item3 api_item3
     */
    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    /**
     * api_item4を取得します。
     * @return api_item4
     */
    public Integer getItem4() {
        return this.item4;
    }

    /**
     * api_item4を設定します。
     * @param item4 api_item4
     */
    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    /**
     * api_item5を取得します。
     * @return api_item5
     */
    public Integer getItem5() {
        return this.item5;
    }

    /**
     * api_item5を設定します。
     * @param item5 api_item5
     */
    public void setItem5(Integer item5) {
        this.item5 = item5;
    }

    /**
     * api_highspeedを取得します。
     * @return api_highspeed
     */
    public Integer getHighspeed() {
        return this.highspeed;
    }

    /**
     * api_highspeedを設定します。
     * @param highspeed api_highspeed
     */
    public void setHighspeed(Integer highspeed) {
        this.highspeed = highspeed;
    }

    /**
     * api_large_flagを取得します。
     * @return api_large_flag
     */
    public Integer getLargeFlag() {
        return this.largeFlag;
    }

    /**
     * api_large_flagを設定します。
     * @param largeFlag api_large_flag
     */
    public void setLargeFlag(Integer largeFlag) {
        this.largeFlag = largeFlag;
    }

    /**
     * 秘書艦を取得します。
     * @return 秘書艦
     */
    public Ship getSecretary() {
        return this.secretary;
    }

    /**
     * 秘書艦を設定します。
     * @param secretary 秘書艦
     */
    public void setSecretary(Ship secretary) {
        this.secretary = secretary;
    }

    /**
     * RequestMetaDataから{@link Createship}を構築します
     *
     * @param req RequestMetaData
     * @return {@link Createship}
     */
    public static Createship toCreateship(RequestMetaData req) {
        Map<String, List<String>> param = req.getParameterMap();

        Createship bean = new Createship();
        bean.setKdockId(Integer.valueOf(param.get("api_kdock_id").get(0)));
        bean.setItem1(Integer.valueOf(param.get("api_item1").get(0)));
        bean.setItem2(Integer.valueOf(param.get("api_item2").get(0)));
        bean.setItem3(Integer.valueOf(param.get("api_item3").get(0)));
        bean.setItem4(Integer.valueOf(param.get("api_item4").get(0)));
        bean.setItem5(Integer.valueOf(param.get("api_item5").get(0)));
        bean.setHighspeed(Integer.valueOf(param.get("api_highspeed").get(0)));
        bean.setLargeFlag(Integer.valueOf(param.get("api_large_flag").get(0)));

        Ship secretary = null;
        DeckPort port = DeckPortCollection.get()
                .getDeckPortMap()
                .get(1);
        if (port != null) {
            List<Integer> ships = port.getShip();
            if (ships != null) {
                Integer id = ships.get(0);
                secretary = ShipCollection.get()
                        .getShipMap()
                        .get(id);
            }
        }
        bean.setSecretary(secretary);

        return bean;
    }
}
