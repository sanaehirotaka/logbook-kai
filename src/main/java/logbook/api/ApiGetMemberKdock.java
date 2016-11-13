package logbook.api;

import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Createship;
import logbook.bean.CreateshipCollection;
import logbook.bean.Kdock;
import logbook.internal.log.CreateshipLogFormat;
import logbook.internal.log.LogWriter;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/kdock
 *
 */
@API("/kcsapi/api_get_member/kdock")
public class ApiGetMemberKdock implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray data = json.getJsonArray("api_data");
        if (data != null) {
            Map<Integer, Createship> createshipMap = CreateshipCollection.get()
                    .getCreateshipMap();
            if (!createshipMap.isEmpty()) {
                Map<Integer, Kdock> kdockMap = data.stream()
                        .map(Kdock::toKdock)
                        .collect(Collectors.toMap(Kdock::getId, d -> d));

                int emptyDock = 0;
                for (Kdock kdock : kdockMap.values()) {
                    if (kdock.getState() == 0) {
                        emptyDock++;
                    }
                }

                for (Createship createship : createshipMap.values()) {
                    Kdock kdock = kdockMap.get(createship.getKdockId());
                    createship.setKdock(kdock);
                    createship.setEmptyDock(emptyDock);

                    LogWriter.getInstance(CreateshipLogFormat::new)
                            .write(createship);
                }

                createshipMap.clear();
            }
        }
    }

}
