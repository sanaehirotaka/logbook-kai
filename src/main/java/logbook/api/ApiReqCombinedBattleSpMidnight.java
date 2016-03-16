package logbook.api;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.CombinedBattleSpMidnight;
import logbook.internal.log.BattleLog;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_combined_battle/sp_midnight
 *
 */
@API("/kcsapi/api_req_combined_battle/sp_midnight")
public class ApiReqCombinedBattleSpMidnight implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setBattle(CombinedBattleSpMidnight.toBattle(data));
            }
        }
    }

}
