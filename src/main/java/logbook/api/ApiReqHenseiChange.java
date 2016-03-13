package logbook.api;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_hensei/change
 *
 */
@API("/kcsapi/api_req_hensei/change")
public class ApiReqHenseiChange implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        Integer portId = Integer.valueOf(req.getParameterMap().get("api_id").get(0));
        Integer shipId = Integer.parseInt(req.getParameterMap().get("api_ship_id").get(0));
        int shipIdx = Integer.parseInt(req.getParameterMap().get("api_ship_idx").get(0));

        DeckPort deckPort = DeckPortCollection.get()
                .getDeckPortMap()
                .get(portId)
                .clone();
        List<Integer> ships = new ArrayList<>(deckPort.getShip());
        if (shipId == -1) {
            ships.remove(shipIdx);
            ships.add(-1);
        } else if (shipId == -2) {
            Integer first = ships.get(0);
            ships.replaceAll(ship -> first.equals(ship) ? ship : -1);
        } else {
            // 艦娘の元の位置
            int beforeIdx = ships.indexOf(shipId);
            // 入れ替え
            Integer to = ships.set(shipIdx, shipId);
            if (beforeIdx != -1) {
                ships.set(beforeIdx, to);
            }
        }
        deckPort.setShip(ships);
        DeckPortCollection.get()
                .getDeckPortMap()
                .put(portId, deckPort);
    }

}
