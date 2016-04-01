package logbook.api;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Basic;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/require_info
 *
 */
@API("/kcsapi/api_get_member/require_info")
public class ApiGetMemberRequireInfo implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiBasic(data.getJsonObject("api_basic"));
            this.apiSlotItem(data.getJsonArray("api_slot_item"));
        }
    }

    /**
     * api_data.api_basic
     *
     * @param object api_basic
     */
    private void apiBasic(JsonObject object) {
        Basic.updateBasic(Basic.get(), object);
    }

    /**
     * api_data.api_slot_item
     *
     * @param array api_slot_item
     */
    private void apiSlotItem(JsonArray array) {
        SlotItemCollection.get()
                .setSlotitemMap(JsonHelper.toMap(array, SlotItem::getId, SlotItem::toSlotItem));
    }

}
