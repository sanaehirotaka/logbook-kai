package logbook.api;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

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
            // 入渠
            Map<Integer, Ndock> map = JsonHelper.toMap(array, Ndock::getId, Ndock::toNdock);
            NdockCollection.get()
                    .setNdockMap(map);
            // 入渠中の艦娘
            NdockCollection.get()
                    .setNdockSet(map.entrySet()
                            .stream()
                            .map(Map.Entry::getValue)
                            .map(Ndock::getShipId)
                            .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
    }

}
