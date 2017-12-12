package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_kousyou/destroyitem2
 *
 */
@API("/kcsapi/api_req_kousyou/destroyitem2")
public class ApiReqKousyouDestroyitem2 implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        List<String> apiSlotitemIds = req.getParameterMap()
                .get("api_slotitem_ids");
        if (apiSlotitemIds != null) {
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();
            for (String apiSlotitemId : apiSlotitemIds.get(0).split(",")) {
                Integer itemId = Integer.valueOf(apiSlotitemId);
                // 装備を廃棄する
                itemMap.remove(itemId);
            }
        }
    }

}
