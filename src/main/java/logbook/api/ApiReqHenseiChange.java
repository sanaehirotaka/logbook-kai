package logbook.api;

import java.util.ArrayList;
import java.util.List;
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
        Integer portId = Integer.valueOf(req.getParameterMap().get("api_id").get(0));
        Integer shipId = Integer.parseInt(req.getParameterMap().get("api_ship_id").get(0));
        int shipIdx = Integer.parseInt(req.getParameterMap().get("api_ship_idx").get(0));

        DeckPort deckPort = DeckPortCollection.get()
                .getDeckPortMap()
                .get(portId)
                .clone();
        List<Integer> ships = new ArrayList<>(deckPort.getShip());
        deckPort.setShip(ships);
        DeckPortCollection.get()
                .getDeckPortMap()
                .put(portId, deckPort);

        if (shipId == -1) {
            ships.remove(shipIdx);
            ships.add(-1);
        } else if (shipId == -2) {
            Integer first = ships.get(0);
            ships.replaceAll(ship -> first.equals(ship) ? ship : -1);
        } else {
            Integer from = ships.get(shipIdx);
            for (Entry<Integer, DeckPort> entry : DeckPortCollection.get().getDeckPortMap().entrySet()) {
                if (entry.getValue().getShip().contains(shipId)) {
                    DeckPort port2 = entry.getValue().clone();
                    List<Integer> ships2 = new ArrayList<>(port2.getShip());
                    port2.setShip(ships2);
                    DeckPortCollection.get()
                            .getDeckPortMap()
                            .put(port2.getId(), port2);

                    ships2.set(ships2.indexOf(shipId), from);
                    break;
                }
            }
            ships.set(shipIdx, shipId);
        }
    }

}
