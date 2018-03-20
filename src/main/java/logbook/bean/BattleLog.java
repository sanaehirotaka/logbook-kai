package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /** 装備スナップショット */
    private Map<Integer, SlotItem> itemMap;

    /** 退避艦IDスナップショット */
    private Set<Integer> escape;

    /** 日時(戦闘結果の取得日時) */
    private String time;

    /**
     * 艦隊スナップショットを作成します
     * @param log 戦闘ログ
     * @param dockIds 艦隊ID
     */
    public static void snapshot(BattleLog log, Integer... dockIds) {
        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();

        Map<Integer, List<Ship>> deckMap = new HashMap<>();
        Map<Integer, SlotItem> cloneItem = new HashMap<>();

        for (Integer dockId : dockIds) {
            List<Ship> ships = new ArrayList<>();
            for (Integer shipId : DeckPortCollection.get()
                    .getDeckPortMap()
                    .get(dockId)
                    .getShip()) {
                Ship ship = shipMap.get(shipId);
                if (ship != null) {
                    ship = ship.clone();
                    if (ship.getSlot() != null) {
                        for (Integer itemId : ship.getSlot()) {
                            SlotItem item = itemMap.get(itemId);
                            if (item != null) {
                                cloneItem.put(itemId, item);
                            }
                        }
                        {
                            SlotItem item = itemMap.get(ship.getSlotEx());
                            if (item != null) {
                                cloneItem.put(ship.getSlotEx(), item);
                            }
                        }
                    }
                }
                ships.add(ship);
            }
            deckMap.put(dockId, ships);
        }
        log.setDeckMap(deckMap);
        log.setItemMap(cloneItem);
        log.setEscape(AppCondition.get().getEscape());
    }
}
