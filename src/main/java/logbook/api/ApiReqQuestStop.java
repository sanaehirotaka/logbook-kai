package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppQuestCollection;
import logbook.bean.AppQuestCondition;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_quest/stop
 *
 */
@API("/kcsapi/api_req_quest/stop")
public class ApiReqQuestStop implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        String id = req.getParameter("api_quest_id");
        if (id != null) {
            Integer key = Integer.valueOf(id);
            AppQuestCollection.get().getQuest().remove(key);
            AppQuestCondition.get().unset(key);
        }
    }

}
