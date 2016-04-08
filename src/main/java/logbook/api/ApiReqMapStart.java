package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.MapStartNext;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_map/start
 *
 */
@API("/kcsapi/api_req_map/start")
public class ApiReqMapStart implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            BattleLog log = new BattleLog();
            log.setCombinedType(CombinedType.toCombinedType(AppCondition.get().getCombinedType()));
            log.getNext().add(MapStartNext.toMapStartNext(data));
            AppCondition.get()
                    .setBattleResult(log);

            Integer deckId = Integer.valueOf(req.getParameterMap()
                    .get("api_deck_id")
                    .get(0));

            AppCondition.get()
                    .setMapStart(Boolean.TRUE);
            AppCondition.get()
                    .setDeckId(deckId);
        }
    }

}
