package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonObject;

import logbook.bean.AppQuestCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_quest/clearitemget
 *
 */
@API("/kcsapi/api_req_quest/clearitemget")
public class ApiReqQuestClearitemget implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            Map<String, List<String>> param = req.getParameterMap();
            Integer questId = Integer.valueOf(param.get("api_quest_id").get(0));

            AppQuestCollection.get()
                    .getQuest()
                    .remove(questId);
        }
    }

}
