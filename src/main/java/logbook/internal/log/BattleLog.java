package logbook.internal.log;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import logbook.bean.BattleResult;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;

/**
 * 戦闘ログ
 *
 */
public class BattleLog implements Serializable {

    private static final long serialVersionUID = 1316215154770888337L;

    /** 開始/進撃 */
    private MapStartNext next;

    /** 戦闘(昼戦、特殊夜戦) */
    private IFormation battle;

    /** 夜戦 */
    private IMidnightBattle midnight;

    /** 戦闘結果 */
    private BattleResult result;

    /** 艦隊スナップショット */
    private Map<Integer, List<Ship>> deckMap;

    /**
     * 開始/進撃を取得します。
     * @return 開始/進撃
     */
    public MapStartNext getNext() {
        return this.next;
    }

    /**
     * 開始/進撃を設定します。
     * @param next 開始/進撃
     */
    public void setNext(MapStartNext next) {
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

}
