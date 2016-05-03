package logbook.api;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.json.JsonObject;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleResult;
import logbook.bean.BattleResult.Escape;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_combined_battle/goback_port
 *
 */
@API("/kcsapi/api_req_combined_battle/goback_port")
public class ApiReqCombinedBattleGobackPort implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        BattleLog log = AppCondition.get().getBattleResultConfirm();
        if (log != null) {
            BattleResult result = log.getResult();
            Escape escape = result.getEscape();

            Set<Integer> escapeSet = AppCondition.get()
                    .getEscape();

            // 退避
            Optional.of(escape.getEscapeIdx())
                    .map(e -> e.get(0))
                    .map(this::getShipId)
                    .ifPresent(escapeSet::add);
            // 護衛
            Optional.of(escape.getTowIdx())
                    .map(e -> e.get(0))
                    .map(this::getShipId)
                    .ifPresent(escapeSet::add);
        }
    }

    /**
     * 退避した艦娘のIDを返します
     *
     * @param index 第1艦隊が1～6、第2艦隊が7～12
     * @return 退避した艦娘のID
     */
    private Integer getShipId(Integer index) {
        Map<Integer, DeckPort> deckMap = DeckPortCollection.get()
                .getDeckPortMap();
        DeckPort deck;
        if (index <= 6) {
            deck = deckMap.get(1);
        } else {
            deck = deckMap.get(2);
        }
        return deck.getShip().get((index - 1) % 6);
    }
}
