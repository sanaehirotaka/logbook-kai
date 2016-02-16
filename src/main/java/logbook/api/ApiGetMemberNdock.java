package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/ndock
 *
 */
@API("/kcsapi/api_get_member/ndock")
public class ApiGetMemberNdock implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray array = json.getJsonArray("api_data");
        if (array != null) {
            Map<Integer, Ndock> map = NdockCollection.get()
                    .getNdockMap();
            map.clear();
            map.putAll(JsonHelper.toMap(array, Ndock::getId, Ndock::toNdock));
        }
    }

}
