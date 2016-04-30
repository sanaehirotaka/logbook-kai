package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/getship
 *
 */
@API("/kcsapi/api_req_kousyou/getship")
public class ApiReqKousyouGetship implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiShip(data.getJsonObject("api_ship"));
            this.apiSlotitem(data.getJsonArray("api_slotitem"));
        }
    }

    /**
     * api_data.api_ship
     *
     * @param object api_ship
     */
    private void apiShip(JsonObject object) {
        Ship ship = Ship.toShip(object);
        ShipCollection.get()
                .getShipMap().put(ship.getId(), ship);
    }

    /**
     * api_data.api_slotitem
     *
     * @param array api_slotitem
     */
    private void apiSlotitem(JsonArray array) {
        if (array != null) {
            Map<Integer, SlotItem> map = SlotItemCollection.get()
                    .getSlotitemMap();
            for (JsonValue value : array) {
                SlotItem item = SlotItem.toSlotItem((JsonObject) value);
                item.setLevel(0);
                item.setLocked(false);
                map.put(item.getId(), item);
            }
        }
    }
}
