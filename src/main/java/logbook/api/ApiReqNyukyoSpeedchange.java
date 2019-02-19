package logbook.api;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_nyukyo/speedchange
 *
 */
@API("/kcsapi/api_req_nyukyo/speedchange")
public class ApiReqNyukyoSpeedchange implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Map<Integer, Ndock> ndockMap = NdockCollection.get()
                .getNdockMap();
        Integer ndockId = Integer.valueOf(req.getParameter("api_ndock_id"));
        Ndock ndock = ndockMap.get(ndockId);

        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        Integer shipId = ndock.getShipId();
        Ship ship = shipMap.get(shipId).clone();

        // 艦娘を修理完了状態にする
        ship.setNowhp(ship.getMaxhp());
        ship.setNdockTime(0);
        if (ship.getCond() < 40) {
            ship.setCond(40);
        }

        shipMap.put(shipId, ship);

        // 高速修復材を使った入渠ドックを初期化
        ndock.setCompleteTime(0L);
        ndock.setShipId(-1);
        ndock.setState(0);

        ndockMap.put(ndockId, ndock);

        // 入渠中の艦娘
        NdockCollection.get()
                .setNdockSet(ndockMap.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .map(Ndock::getShipId)
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

}
