package logbook.api;

import java.util.Optional;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleResult;
import logbook.bean.BattleTypes.IFormation;
import logbook.internal.Logs;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_practice/battle_result
 *
 */
@API("/kcsapi/api_req_practice/battle_result")
public class ApiReqPracticeBattleresult implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        try {
            JsonObject data = json.getJsonObject("api_data");
            if (data != null) {
                BattleResult result = BattleResult.toBattleResult(data);
                BattleLog log = AppCondition.get().getPracticeBattleResult();
                if (log != null) {
                    log.setResult(result);
                    log.setTime(Logs.nowString());
                    // 出撃艦隊
                    Integer dockId = Optional.ofNullable(log.getBattle())
                            .map(IFormation::getDockId)
                            .orElse(1);
                    // 艦隊スナップショットを作る
                    BattleLog.snapshot(log, dockId);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
