package logbook.api;

import java.util.HashMap;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleResult;
import logbook.bean.DeckPortCollection;
import logbook.internal.log.BattleLog;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_combined_battle/battleresult
 *
 */
@API("/kcsapi/api_req_combined_battle/battleresult")
public class ApiReqCombinedBattleBattleresult implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setResult(BattleResult.toBattleResult(data));
                log.setDeckMap(new HashMap<>(DeckPortCollection.get().getDeckPortMap()));
            }
        }
    }

}
