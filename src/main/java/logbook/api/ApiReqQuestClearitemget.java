package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppQuestCollection;
import logbook.bean.AppQuestDuration;
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
        String id = req.getParameter("api_quest_id");
        if (id != null) {
            Integer key = Integer.valueOf(id);
            AppQuestCollection.get().getQuest().remove(key);
            AppQuestDuration.get().remove(key);
        }
    }

}
