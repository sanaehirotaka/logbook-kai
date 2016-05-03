package logbook.api;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.CombinedBattleBattle;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.PhaseState;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_combined_battle/battle
 *
 */
@API("/kcsapi/api_req_combined_battle/battle")
public class ApiReqCombinedBattleBattle implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {

            BattleLog log = AppCondition.get().getBattleResult();
            if (log != null) {
                log.setBattle(CombinedBattleBattle.toBattle(data));
                // 艦隊スナップショットを作る
                log.setDeckMap(BattleLog.deckMap(1, 2));
                if (AppConfig.get().isApplyBattle()) {
                    // 艦隊を更新
                    PhaseState p = new PhaseState(log);
                    p.apply(log.getBattle());
                    ShipCollection.get()
                            .getShipMap()
                            .putAll(Stream.of(p.getAfterFriend(), p.getAfterFriendCombined())
                                    .flatMap(List::stream)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toMap(Ship::getId, v -> v)));
                }
            }
        }
    }
}
