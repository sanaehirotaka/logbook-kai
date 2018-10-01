package logbook.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.Stype;
import logbook.internal.Ships;
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

        // 変化した艦隊
        Set<Integer> changed = new HashSet<>();

        Map<Integer, DeckPort> deckMap = DeckPortCollection.get()
                .getDeckPortMap();

        Integer portId = Integer.valueOf(req.getParameter("api_id"));
        Integer shipId = Integer.valueOf(req.getParameter("api_ship_id"));
        int shipIdx = Integer.parseInt(req.getParameter("api_ship_idx"));

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
                    changed.add(port2.getId());
                    break;
                }
            }
            deckMap.get(portId).getShip().set(shipIdx, shipId);
        }
        changed.add(portId);

        // 随伴艦一括解除以外の場合に、変化した艦隊の旗艦に工作艦が存在する場合は泊地修理タイマーをセットする
        if (shipId != -2) {
            for (Integer port : changed) {
                List<Integer> changedShips = deckMap.get(port).getShip();
                if (changedShips.size() > 0) {
                    Integer shipid = changedShips.get(0);
                    Ship ship = ShipCollection.get().getShipMap().get(shipid);
                    if (ship != null) {
                        String type = Ships.stype(ship).map(Stype::getName).orElse("");
                        if ("工作艦".equals(type)) {
                            AppCondition.get().setAkashiTimer(System.currentTimeMillis());
                            break;
                        }
                    }
                }
            }
        }
    }

}
