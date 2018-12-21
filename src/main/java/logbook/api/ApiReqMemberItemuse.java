package logbook.api;

import java.util.Map;
import java.util.Optional;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_member/itemuse
 *
 */
@API("/kcsapi/api_req_member/itemuse")
public class ApiReqMemberItemuse implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            Optional.ofNullable(data.getJsonArray("api_getitem"))
                    .ifPresent(this::apiGetitem);
        }
    }

    /**
     * api_data.api_getitem
     *
     * @param array api_getitem
     */
    private void apiGetitem(JsonArray array) {
        if (array != null) {
            Map<Integer, SlotItem> map = SlotItemCollection.get()
                    .getSlotitemMap();
            for (JsonValue value : array) {
                if (value != null && !JsonValue.NULL.equals(value)) {
                    JsonObject obj = (JsonObject) value;
                    Optional.ofNullable(obj.getJsonObject("api_slotitem"))
                            .map(SlotItem::toSlotItem)
                            .ifPresent(item -> {
                                item.setLevel(0);
                                item.setLocked(false);
                                map.put(item.getId(), item);
                            });
                }
            }
        }
    }
}
