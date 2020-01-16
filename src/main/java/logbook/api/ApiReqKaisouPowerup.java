package logbook.api;

import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kaisou/powerup
 *
 */
@API("/kcsapi/api_req_kaisou/powerup")
public class ApiReqKaisouPowerup implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        String apiIdItems = req.getParameter("api_id_items");
        if (apiIdItems != null) {
            for (String apiIdItem : apiIdItems.split(",")) {
                Integer shipId = Integer.valueOf(apiIdItem);
                // 艦娘を外す
                Ship ship = ShipCollection.get()
                        .getShipMap()
                        .remove(shipId);
                if (ship != null && !"0".equals(req.getParameter("api_slot_dest_flag"))) {
                    Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                            .getSlotitemMap();
                    // 持っている装備を廃棄する
                    for (Integer itemId : ship.getSlot()) {
                        itemMap.remove(itemId);
                    }
                    // 補強増設
                    itemMap.remove(ship.getSlotEx());
                }
            }
        }
    }

}
