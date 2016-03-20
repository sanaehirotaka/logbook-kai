package logbook.api;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/slot_item
 *
 */
@API("/kcsapi/api_get_member/slot_item")
public class ApiGetMemberSlotItem implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray array = json.getJsonArray("api_data");
        if (array != null) {
            SlotItemCollection.get()
                    .setSlotitemMap(JsonHelper.toMap(array, SlotItem::getId, SlotItem::toSlotItem));
        }
    }

}
