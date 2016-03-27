package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.CombinedBattleLdAirbattle;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_combined_battle/ld_airbattle
 *
 */
@API("/kcsapi/api_req_combined_battle/ld_airbattle")
public class ApiReqCombinedBattleLdAirbattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setBattle(CombinedBattleLdAirbattle.toBattle(data));
            }
        }
    }

}
