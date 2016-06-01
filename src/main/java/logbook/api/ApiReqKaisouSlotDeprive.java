package logbook.api;

import javax.json.JsonObject;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kaisou/slot_deprive
 *
 */
@API("/kcsapi/api_req_kaisou/slot_deprive")
public class ApiReqKaisouSlotDeprive implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            JsonObject shipData = data.getJsonObject("api_ship_data");
            if (shipData != null) {
                this.replace(shipData.getJsonObject("api_set_ship"));
                this.replace(shipData.getJsonObject("api_unset_ship"));
            }
        }
    }

    private void replace(JsonObject json) {
        Ship ship = Ship.toShip(json);
        ShipCollection.get()
                .getShipMap().put(ship.getId(), ship);
    }
}
