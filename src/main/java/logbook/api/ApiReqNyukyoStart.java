package logbook.api;

import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_nyukyo/start
 *
 */
@API("/kcsapi/api_req_nyukyo/start")
public class ApiReqNyukyoStart implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        Integer shipId = Integer.valueOf(req.getParameter("api_ship_id"));
        Ship ship = map.get(shipId)
                .clone();

        if ("1".equals(req.getParameter("api_highspeed"))) {
            ship.setNowhp(ship.getMaxhp());
            ship.setNdockTime(0);
        }
        // 高速修復材未使用時でも入れ替える(艦隊タブを更新するため)
        map.put(shipId, ship);
    }
}
