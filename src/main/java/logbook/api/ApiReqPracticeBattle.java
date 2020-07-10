package logbook.api;

import java.util.Optional;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.SortieBattle;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_practice/battle
 *
 */
@API("/kcsapi/api_req_practice/battle")
public class ApiReqPracticeBattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        try {
            JsonObject data = json.getJsonObject("api_data");
            if (data != null) {
                AppCondition condition = AppCondition.get();
                BattleLog log = new BattleLog();
                condition.setPracticeBattleResult(log);
                if (log != null) {
                    log.setBattle(SortieBattle.toBattle(data));
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
