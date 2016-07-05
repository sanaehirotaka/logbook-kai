package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/remodel_slot
 *
 */
@API("/kcsapi/api_req_kousyou/remodel_slot")
public class ApiReqKousyouRemodelSlot implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();

            // 改修後装備
            JsonObject afterSlot = data.getJsonObject("api_after_slot");
            if (afterSlot != null) {
                SlotItem replace = SlotItem.toSlotItem(afterSlot);
                itemMap.put(replace.getId(), replace);
            }

            // 消費装備
            JsonArray useSlotId = data.getJsonArray("api_use_slot_id");
            if (useSlotId != null) {
                for (Integer slotId : JsonHelper.toIntegerList(useSlotId)) {
                    itemMap.remove(slotId);
                }
            }
        }
    }

}
