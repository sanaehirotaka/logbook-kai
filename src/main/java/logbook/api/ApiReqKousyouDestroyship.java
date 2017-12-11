package logbook.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/destroyship
 *
 */
@API("/kcsapi/api_req_kousyou/destroyship")
public class ApiReqKousyouDestroyship implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        List<String> apiShipId = req.getParameterMap()
                .get("api_ship_id");
        List<String> apiSlotDestFlag = req.getParameterMap()
                .get("api_slot_dest_flag");

        if (apiShipId != null) {
            boolean slotDest = "1".equals(apiSlotDestFlag.stream().findFirst().get());
            Arrays.stream(apiShipId.get(0).split(","))
                    .map(Integer::parseInt)
                    .forEach(id -> this.destroyShip(id, slotDest));
        }
    }

    private void destroyShip(Integer shipId, boolean slotDest) {
        // 艦娘を外す
        Ship ship = ShipCollection.get()
                .getShipMap()
                .remove(shipId);
        if (slotDest && ship != null) {
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
