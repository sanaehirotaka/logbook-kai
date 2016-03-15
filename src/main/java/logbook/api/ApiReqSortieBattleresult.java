package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleResult;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_sortie/battleresult
 *
 */
@API("/kcsapi/api_req_sortie/battleresult")
public class ApiReqSortieBattleresult implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        AppCondition.get()
                .setBattleResult(BattleResult.toBattleResult(json));
    }

}
