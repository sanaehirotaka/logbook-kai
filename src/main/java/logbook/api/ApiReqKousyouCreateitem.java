package logbook.api;

import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/createitem
 *
 */
@API("/kcsapi/api_req_kousyou/createitem")
public class ApiReqKousyouCreateitem implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiSlotItem(data.getJsonObject("api_slot_item"));
        }
    }

    /**
     * api_data.api_slot_item
     *
     * @param object api_slot_item
     */
    private void apiSlotItem(JsonObject object) {
        if (object != null) {
            Map<Integer, SlotItem> map = SlotItemCollection.get()
                    .getSlotitemMap();
            SlotItem item = SlotItem.toSlotItem(object);
            item.setLevel(0);
            item.setLocked(false);
            map.put(item.getId(), item);
        }
    }
}
