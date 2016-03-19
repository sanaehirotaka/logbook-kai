package logbook.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleLogCollection;
import logbook.bean.BattleResult;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.log.LogWriter;
import logbook.internal.log.Logs;
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
                // 艦隊スナップショットを作る
                Map<Integer, Ship> shipMap = ShipCollection.get()
                        .getShipMap();
                Map<Integer, List<Ship>> deckMap = new HashMap<>();
                for (Map.Entry<Integer, DeckPort> entry : DeckPortCollection.get()
                        .getDeckPortMap().entrySet()) {
                    deckMap.put(entry.getKey(), entry.getValue()
                            .getShip()
                            .stream()
                            .map(shipMap::get)
                            .collect(Collectors.toList()));
                }
                log.setTime(Logs.nowString());
                log.setDeckMap(deckMap);
                // 保存
                BattleLogCollection.get()
                        .getLogs().add(log);
                try {
                    new LogWriter()
                            .header(Logs.BATTLE_RESULT_HEADER)
                            .file(Logs.BATTLE_RESULT)
                            .alterFile(Logs.BATTLE_RESULT_ALT)
                            .write(log, Logs::formatBattleLog);
                } catch (IOException e) {
                    LoggerHolder.LOG.warn(Logs.BATTLE_RESULT + "に書き込めませんでした", e);
                }
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ApiReqCombinedBattleBattleresult.class);
    }
}
