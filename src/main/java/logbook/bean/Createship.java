package logbook.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import logbook.proxy.RequestMetaData;
import lombok.Data;

/**
 * api_req_kousyou/createship
 *
 */
@Data
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
