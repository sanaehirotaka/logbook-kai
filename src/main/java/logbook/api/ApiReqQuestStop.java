package logbook.api;

import java.util.Optional;

import javax.json.JsonObject;

import logbook.bean.AppQuestCollection;
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
        Optional.ofNullable(req.getParameter("api_quest_id"))
                .map(Integer::valueOf)
                .ifPresent(AppQuestCollection.get().getQuest()::remove);
    }

}
