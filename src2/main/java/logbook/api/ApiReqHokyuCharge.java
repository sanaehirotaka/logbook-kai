package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_hokyu/charge
 *
 */
@API("/kcsapi/api_req_hokyu/charge")
public class ApiReqHokyuCharge implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            Map<Integer, Ship> map = ShipCollection.get()
                    .getShipMap();

            List<Ship> ships = JsonHelper.toList(data.getJsonArray("api_ship"), Ship::toShip);
            for (Ship ship : ships) {
                Ship oldShip = map.get(ship.getId());
                Ship newShip = oldShip.clone();
                newShip.setBull(ship.getBull());
                newShip.setFuel(ship.getFuel());
                newShip.setOnslot(ship.getOnslot());
                map.put(ship.getId(), newShip);
            }
        }
    }

}
