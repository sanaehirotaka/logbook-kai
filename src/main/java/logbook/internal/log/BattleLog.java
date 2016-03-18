package logbook.internal.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;

import logbook.Messages;
import logbook.bean.BattleResult;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.internal.Ships;

/**
 * 戦闘ログ
 *
 */
public class BattleLog implements Serializable {

    private static final long serialVersionUID = -697474296673703371L;

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

    /**
     * 開始/進撃を取得します。
     * @return 開始/進撃
     */
    public List<MapStartNext> getNext() {
        return this.next;
    }

    /**
     * 開始/進撃を設定します。
     * @param next 開始/進撃
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

    @Override
    public String toString() {
        if (this.result != null) {
            return toLine(this);
        } else {
            return "";
        }
    }

    private static String bossText(BattleLog log) {
        MapStartNext first = log.next.get(0);
        MapStartNext last = log.next.get(log.next.size() - 1);

        boolean start = Objects.isNull(first.getFromNo());
        boolean boss = last.getNo().equals(last.getBosscellNo()) || last.getEventId() == 5;

        StringJoiner joiner = new StringJoiner("&");
        if (start) {
            joiner.add("出撃");
        }
        if (boss) {
            joiner.add("ボス");
        }
        return joiner.toString();
    }

    private static String toLine(BattleLog log) {
        StringJoiner joiner = new StringJoiner(",");
        // 海域
        joiner.add(log.result.getQuestName());
        // マス
        joiner.add(String.valueOf(log.next.get(log.next.size() - 1).getNo()));
        // ボス
        joiner.add(bossText(log));
        // ランク
        joiner.add(log.result.getWinRank());
        // 艦隊行動
        joiner.add(BattleTypes.Intercept.toIntercept(log.battle.getFormation().get(2)).toString());
        // 味方陣形
        joiner.add(BattleTypes.Formation.toFormation(log.battle.getFormation().get(0)).toString());
        // 敵陣形
        joiner.add(BattleTypes.Formation.toFormation(log.battle.getFormation().get(1)).toString());
        // 敵艦隊
        joiner.add(log.result.getEnemyInfo().getDeckName());
        // ドロップ艦種
        joiner.add(log.result.getGetShip().getShipType());
        // ドロップ艦娘
        joiner.add(log.result.getGetShip().getShipName());
        // 味方艦
        List<Ship> friendFleet = log.deckMap.get(log.battle.getDockId());
        for (int i = 0; i < friendFleet.size(); i++) {
            Ship ship = friendFleet.get(i);
            if (ship != null) {
                // 名前
                String name = Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse("");
                joiner.add(Messages.getString("ship.name", name, ship.getLv())); //$NON-NLS-1$
                // HP
                joiner.add(log.battle.getNowhps().get(i + 1) + "/" + log.battle.getMaxhps().get(i + 1));
            } else {
                joiner.add("");
                joiner.add("");
            }
        }
        // 敵艦
        List<Integer> enemyFleet = log.battle.getShipKe();
        for (int i = 1; i < enemyFleet.size(); i++) {
            ShipMst shipMst = ShipMstCollection.get()
                    .getShipMap()
                    .get(enemyFleet.get(i));

            String name = Optional.ofNullable(shipMst)
                    .map(mst -> mst.getName() + (StringUtils.isEmpty(mst.getYomi()) ? "" : "(" + mst.getYomi() + ")"))
                    .orElse("");
            joiner.add(name);
            joiner.add(log.battle.getNowhps().get(i) + "/" + log.battle.getMaxhps().get(i));
        }
        return joiner.toString();
    }
}
