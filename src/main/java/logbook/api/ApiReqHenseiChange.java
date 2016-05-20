package logbook.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

        Map<Integer, DeckPort> deckMap = DeckPortCollection.get()
                .getDeckPortMap();

        Integer portId = Integer.valueOf(req.getParameterMap().get("api_id").get(0));
        Integer shipId = Integer.parseInt(req.getParameterMap().get("api_ship_id").get(0));
        int shipIdx = Integer.parseInt(req.getParameterMap().get("api_ship_idx").get(0));

        DeckPort deckPort = deckMap.get(portId)
                .clone();
        List<Integer> ships = new ArrayList<>(deckPort.getShip());
        deckPort.setShip(ships);
        deckMap.put(portId, deckPort);

        if (shipId == -1) {
            ships.remove(shipIdx);
            ships.add(-1);
        } else if (shipId == -2) {
            Integer first = ships.get(0);
            ships.replaceAll(ship -> first.equals(ship) ? ship : -1);
        } else {
            Integer from = ships.get(shipIdx);
            for (Entry<Integer, DeckPort> entry : deckMap.entrySet()) {
                if (entry.getValue().getShip().contains(shipId)) {
                    DeckPort port2 = entry.getValue().clone();
                    List<Integer> ships2 = new ArrayList<>(port2.getShip());
                    port2.setShip(ships2);
                    deckMap.put(port2.getId(), port2);

                    if (from == -1) {
                        ships2.removeIf(id -> id.equals(shipId));
                        ships2.add(-1);
                    } else {
                        ships2.set(ships2.indexOf(shipId), from);
                    }
                    break;
                }
            }
            deckMap.get(portId).getShip().set(shipIdx, shipId);
        }
    }

}
