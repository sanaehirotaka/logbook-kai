package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.SortieLdAirbattle;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_sortie/ld_airbattle
 *
 */
@API("/kcsapi/api_req_sortie/ld_airbattle")
public class ApiReqSortieLdAirbattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setBattle(SortieLdAirbattle.toBattle(data));
            }
        }
    }

}
