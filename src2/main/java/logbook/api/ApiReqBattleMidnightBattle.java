package logbook.api;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.BattleMidnightBattle;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.PhaseState;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_battle_midnight/battle
 *
 */
@API("/kcsapi/api_req_battle_midnight/battle")
public class ApiReqBattleMidnightBattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setMidnight(BattleMidnightBattle.toBattle(data));
                if (AppConfig.get().isApplyBattle()) {
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
