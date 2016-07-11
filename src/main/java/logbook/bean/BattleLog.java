package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import lombok.Data;

/**
 * 戦闘ログ
 *
 */
@Data
public class BattleLog implements Serializable {

    private static final long serialVersionUID = -6163406897520116392L;

    /** 連合艦隊 */
    private CombinedType combinedType = CombinedType.未結成;

    /** 開始/進撃(順番に複数存在する) */
    private List<MapStartNext> next = new ArrayList<>();

    /** 戦闘(昼戦、特殊夜戦) */
    private IFormation battle;

    /** 夜戦 */
    private IMidnightBattle midnight;

    /** 戦闘結果 */
    private BattleResult result;

    /** 艦隊スナップショット */
    private Map<Integer, List<Ship>> deckMap;

    /** 日時(戦闘結果の取得日時) */
    private String time;

    /**
     * 艦隊スナップショットを作成します
     * @param dockIds 艦隊ID
     * @return 艦隊スナップショット
     */
    public static Map<Integer, List<Ship>> deckMap(Integer... dockIds) {
        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        Map<Integer, List<Ship>> deckMap = new HashMap<>();
        for (Integer dockId : dockIds) {
            deckMap.put(dockId, DeckPortCollection.get()
                    .getDeckPortMap()
                    .get(dockId)
                    .getShip()
                    .stream()
                    .map(shipMap::get)
                    .map(ship -> ship != null ? ship.clone() : null)
                    .collect(Collectors.toList()));
        }
        return deckMap;
    }
}
