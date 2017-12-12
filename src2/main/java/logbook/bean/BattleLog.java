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

/**
 * 戦闘ログ
 *
 */
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
     * 連合艦隊を取得します。
     * @return 連合艦隊
     */
    public CombinedType getCombinedType() {
        return this.combinedType;
    }

    /**
     * 連合艦隊を設定します。
     * @param combinedType 連合艦隊
     */
    public void setCombinedType(CombinedType combinedType) {
        this.combinedType = combinedType;
    }

    /**
     * 開始/進撃(順番に複数存在する)を取得します。
     * @return 開始/進撃(順番に複数存在する)
     */
    public List<MapStartNext> getNext() {
        return this.next;
    }

    /**
     * 開始/進撃(順番に複数存在する)を設定します。
     * @param next 開始/進撃(順番に複数存在する)
     */
    public void setNext(List<MapStartNext> next) {
        this.next = next;
    }

    /**
     * 戦闘(昼戦、特殊夜戦)を取得します。
     * @return 戦闘(昼戦、特殊夜戦)
     */
    public IFormation getBattle() {
        return this.battle;
    }

    /**
     * 戦闘(昼戦、特殊夜戦)を設定します。
     * @param battle 戦闘(昼戦、特殊夜戦)
     */
    public void setBattle(IFormation battle) {
        this.battle = battle;
    }

    /**
     * 夜戦を取得します。
     * @return 夜戦
     */
    public IMidnightBattle getMidnight() {
        return this.midnight;
    }

    /**
     * 夜戦を設定します。
     * @param midnight 夜戦
     */
    public void setMidnight(IMidnightBattle midnight) {
        this.midnight = midnight;
    }

    /**
     * 戦闘結果を取得します。
     * @return 戦闘結果
     */
    public BattleResult getResult() {
        return this.result;
    }

    /**
     * 戦闘結果を設定します。
     * @param result 戦闘結果
     */
    public void setResult(BattleResult result) {
        this.result = result;
    }

    /**
     * 艦隊スナップショットを取得します。
     * @return 艦隊スナップショット
     */
    public Map<Integer, List<Ship>> getDeckMap() {
        return this.deckMap;
    }

    /**
     * 艦隊スナップショットを設定します。
     * @param deckMap 艦隊スナップショット
     */
    public void setDeckMap(Map<Integer, List<Ship>> deckMap) {
        this.deckMap = deckMap;
    }

    /**
     * 日時(戦闘結果の取得日時)を取得します。
     * @return 日時(戦闘結果の取得日時)
     */
    public String getTime() {
        return this.time;
    }

    /**
     * 日時(戦闘結果の取得日時)を設定します。
     * @param time 日時(戦闘結果の取得日時)
     */
    public void setTime(String time) {
        this.time = time;
    }

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
