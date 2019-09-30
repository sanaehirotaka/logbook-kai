package logbook.api;

import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.Createitem;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.log.CreateitemLogFormat;
import logbook.internal.log.LogWriter;
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
            Createitem createitem = Createitem.toCreateitem(data, req);

            // 開発装備を反映
            for (SlotItem item : createitem.getGetItems()) {
                if (item != null && item.getId() > 0) {
                    Map<Integer, SlotItem> map = SlotItemCollection.get()
                            .getSlotitemMap();
                    item.setLevel(0);
                    item.setLocked(false);
                    map.put(item.getId(), item);
                }
                // 開発ログに書き込む
                createitem.setSlotItem(item);
                LogWriter.getInstance(CreateitemLogFormat::new)
                        .write(createitem);
            }
        }
    }
}
