package logbook.api;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.BattleResult;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.BattleLogs;
import logbook.internal.LogWriter;
import logbook.internal.Logs;
import logbook.internal.PhaseState;
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
                // 削除
                AppCondition.get().setBattleResult(null);

                log.setResult(BattleResult.toBattleResult(data));
                log.setTime(Logs.nowString());
                // 艦隊スナップショットを作る
                log.setDeckMap(BattleLog.deckMap(1, 2));
                // 戦闘ログの保存
                BattleLogs.write(log);

                new LogWriter()
                        .header(Logs.BATTLE_RESULT_HEADER)
                        .file(Logs.BATTLE_RESULT)
                        .alterFile(Logs.BATTLE_RESULT_ALT)
                        .write(log, Logs::formatBattleLog);
                if (AppConfig.get().isApplyResult()) {
                    // 艦隊を更新
                    PhaseState p = new PhaseState(log);
                    p.apply(log.getBattle());
                    p.apply(log.getMidnight());
                    ShipCollection.get()
                            .getShipMap()
                            .putAll(p.getAfterFriend().stream()
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toMap(Ship::getId, v -> v)));
                }
            }
        }
    }
}
