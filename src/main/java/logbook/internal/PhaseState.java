package logbook.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IAirbattle;
import logbook.bean.BattleTypes.IBattle;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IHougeki;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.Raigeki;
import logbook.bean.BattleTypes.Stage3;
import logbook.bean.BattleTypes.Stage3Combined;
import logbook.bean.BattleTypes.SupportAiratack;
import logbook.bean.BattleTypes.SupportHourai;
import logbook.bean.BattleTypes.SupportInfo;
import logbook.bean.Enemy;
import logbook.bean.Ship;

/**
 * 戦闘フェイズにおける味方と敵のステータス
 */
public class PhaseState {

    /** 連合艦隊 */
    private CombinedType combinedType = CombinedType.未結成;

    /** フェイズ後味方(第1艦隊) */
    private List<Ship> afterFriend = new ArrayList<>();

    /** フェイズ後味方(第2艦隊) */
    private List<Ship> afterFriendCombined = new ArrayList<>();

    /** フェイズ後敵 */
    private List<Enemy> afterEnemy = new ArrayList<>();

    /**
     * 戦闘から新規フェイズを作成します
     * @param log 戦闘ログ
     */
    public PhaseState(BattleLog log) {
        this(log.getCombinedType(), log.getBattle(), log.getDeckMap());
    }

    /**
     * 戦闘から新規フェイズを作成します
     * @param combinedType 連合艦隊
     * @param b 戦闘
     * @param deckMap 艦隊スナップショット
     */
    public PhaseState(CombinedType combinedType, IBattle b, Map<Integer, List<Ship>> deckMap) {
        // 連合艦隊
        this.combinedType = combinedType;
        // 味方
        if (b instanceof ICombinedBattle) {
            for (Ship ship : deckMap.get(1)) {
                this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
            for (Ship ship : deckMap.get(2)) {
                this.afterFriendCombined.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
        } else {
            List<Ship> ships = deckMap.get(b.getDockId());
            for (Ship ship : ships) {
                this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
        }
        // 敵
        for (int i = 1, s = b.getShipKe().size(); i < s; i++) {
            if (b.getShipKe().get(i) != -1) {
                Enemy e = new Enemy();
                e.setShipId(b.getShipKe().get(i));
                e.setLv(b.getShipLv().get(i));
                e.setSlot(b.getESlot().get(i - 1));
                e.setKyouka(b.getEKyouka().get(i - 1));

                this.afterEnemy.add(e);
            }
        }
        this.setInitialHp(b);
    }

    /**
     * フェイズから新規フェイズを作成します<br>
     * 引数psのフェイズ後が新規フェイズのフェイズ前とフェイズ後にコピーされます
     *
     * @param ps フェイズ
     */
    public PhaseState(PhaseState ps) {
        this.combinedType = ps.combinedType;
        for (Ship ship : ps.afterFriend) {
            this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
        }
        for (Ship ship : ps.afterFriendCombined) {
            this.afterFriendCombined.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
        }
        for (Enemy enemy : ps.afterEnemy) {
            this.afterEnemy.add(Optional.ofNullable(enemy).map(Enemy::clone).orElse(null));
        }
    }

    /**
     * 航空戦フェイズを適用します
     * @param battle 航空戦フェイズ
     */
    public void applyKouku(IKouku battle) {
        this.applyKouku(battle.getKouku());
    }

    /**
     * 支援フェイズを適用します
     * @param battle 支援フェイズ
     */
    public void applySupport(ISupport battle) {
        SupportInfo support = battle.getSupportInfo();
        if (support != null) {
            SupportAiratack air = support.getSupportAiratack();
            if (air != null) {
                Stage3 stage3 = air.getStage3();
                if (stage3 != null) {
                    this.applyEnemyDamage(stage3.getEdam(), this.afterEnemy);
                }
            }
            SupportHourai hou = support.getSupportHourai();
            if (hou != null) {
                this.applyEnemyDamage(hou.getDamage(), this.afterEnemy);
            }
        }
    }

    /**
     * 昼戦砲雷撃戦フェイズを適用します
     * @param battle 昼戦砲雷撃戦フェイズ
     */
    public void applySortieHougeki(ISortieHougeki battle) {
        // 開幕雷撃
        this.applyRaigeki(battle.getOpeningAtack());
        if (this.combinedType == CombinedType.未結成) {
            // 連合艦隊以外
            // 1巡目
            this.applyHougeki(battle.getHougeki1(), this.afterFriend);
            // 2巡目
            this.applyHougeki(battle.getHougeki2(), this.afterFriend);
            // 雷撃
            this.applyRaigeki(battle.getRaigeki());
        } else if (this.combinedType == CombinedType.機動部隊 || this.combinedType == CombinedType.輸送部隊) {
            // 空母機動部隊、輸送護衛部隊
            // 第2艦隊1巡目
            this.applyHougeki(battle.getHougeki1(), this.afterFriendCombined);
            // 第2艦隊雷撃
            this.applyRaigeki(battle.getRaigeki());
            // 第1艦隊1巡目
            this.applyHougeki(battle.getHougeki2(), this.afterFriend);
            // 第1艦隊2巡目
            this.applyHougeki(battle.getHougeki3(), this.afterFriend);
        } else if (this.combinedType == CombinedType.水上部隊) {
            // 水上打撃部隊
            // 第1艦隊1巡目
            this.applyHougeki(battle.getHougeki1(), this.afterFriend);
            // 第1艦隊2巡目
            this.applyHougeki(battle.getHougeki2(), this.afterFriend);
            // 第2艦隊1巡目
            this.applyHougeki(battle.getHougeki3(), this.afterFriendCombined);
            // 第2艦隊雷撃
            this.applyRaigeki(battle.getRaigeki());
        }
    }

    /**
     * 航空戦を適用します
     * @param battle 航空戦
     */
    public void applyAirbattle(IAirbattle battle) {
        this.applyKouku(battle.getKouku2());
    }

    /**
     * 夜戦を適用します
     * @param battle 夜戦
     */
    public void applyMidnightBattle(IMidnightBattle battle) {
        if (this.combinedType == CombinedType.未結成) {
            // 連合艦隊以外
            this.applyHougeki(battle.getHougeki(), this.afterFriend);
        } else {
            // 連合艦隊
            this.applyHougeki(battle.getHougeki(), this.afterFriendCombined);
        }
    }

    /**
     * 全てのフェイズを適用します
     * @param battle 戦闘
     */
    public void apply(IBattle battle) {
        if (battle == null) {
            return;
        }

        // 航空戦フェイズ
        if (battle instanceof IKouku) {
            if (((IKouku) battle).getKouku() != null) {
                this.applyKouku((IKouku) battle);
            }
        }
        // 支援フェイズ
        if (battle instanceof ISupport) {
            if (((ISupport) battle).getSupportInfo() != null) {
                this.applySupport((ISupport) battle);
            }
        }
        // 砲雷撃戦フェイズ
        if (battle instanceof ISortieHougeki) {
            this.applySortieHougeki((ISortieHougeki) battle);
        }
        // 航空戦
        if (battle instanceof IAirbattle) {
            this.applyAirbattle((IAirbattle) battle);
        }
        // 夜戦
        if (battle instanceof IMidnightBattle) {
            this.applyMidnightBattle((IMidnightBattle) battle);
        }
    }

    /**
     * 航空戦フェイズを適用します
     * @param kouku 航空戦フェイズ
     */
    private void applyKouku(Kouku kouku) {
        if (kouku == null) {
            return;
        }

        Stage3 stage3 = kouku.getStage3();
        if (stage3 != null) {
            this.applyFriendDamage(stage3.getFdam(), this.afterFriend);
            this.applyEnemyDamage(stage3.getEdam(), this.afterEnemy);
        }
        Stage3Combined stage3Combined = kouku.getStage3Combined();
        if (stage3Combined != null) {
            this.applyFriendDamage(stage3Combined.getFdam(), this.afterFriendCombined);
        }
    }

    /**
     * 雷撃戦フェイズを適用します
     * @param raigeki 雷撃戦フェイズ
     */
    private void applyRaigeki(Raigeki raigeki) {
        if (raigeki == null) {
            return;
        }

        if (this.combinedType != CombinedType.未結成) {
            // 連合艦隊の場合、第2艦隊にダメージを適用
            this.applyFriendDamage(raigeki.getFdam(), this.afterFriendCombined);
        } else {
            // 連合艦隊以外の場合
            this.applyFriendDamage(raigeki.getFdam(), this.afterFriend);
        }
        // 敵
        this.applyEnemyDamage(raigeki.getEdam(), this.afterEnemy);
    }

    /**
     * 砲撃戦フェイズを適用します
     * @param hougeki 砲撃戦フェイズ
     * @param friend 交戦する味方
     */
    private void applyHougeki(IHougeki hougeki, List<Ship> friend) {
        if (hougeki == null) {
            return;
        }

        for (int i = 1, s = hougeki.getDamage().size(); i < s; i++) {
            // 防御側インデックス(1-6,7-12)
            int df = hougeki.getDfList().get(i).get(0);
            int damage = hougeki.getDamage().get(i)
                    .stream()
                    .mapToInt(Double::intValue)
                    .sum();
            if (df <= 6) {
                // 6以下は味方
                Ship ship = friend.get(df - 1);
                if (ship != null) {
                    ship.setNowhp(ship.getNowhp() - damage);
                }
            } else {
                // 7以上は敵
                Enemy enemy = this.afterEnemy.get(df - 1 - 6);
                enemy.setNowhp(enemy.getNowhp() - damage);
            }
        }
    }

    /**
     * ダメージを適用します
     * @param damage ダメージ(one-based)
     * @param friend 味方
     */
    private void applyFriendDamage(List<Double> damages, List<Ship> friend) {
        for (int i = 1, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Ship ship = friend.get(i - 1);
                if (ship != null) {
                    ship.setNowhp(ship.getNowhp() - damage);
                }
            }
        }
    }

    /**
     * ダメージを適用します
     * @param damage ダメージ(one-based)
     * @param enemies 敵
     */
    private void applyEnemyDamage(List<Double> damages, List<Enemy> enemies) {
        for (int i = 1, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Enemy enemy = enemies.get(i - 1);
                if (enemy != null) {
                    enemy.setNowhp(enemy.getNowhp() - damage);
                }
            }
        }
    }

    /**
     * HPをセットします
     * @param b 戦闘
     */
    private void setInitialHp(IBattle b) {
        for (int i = 1, s = b.getMaxhps().size(); i < s; i++) {
            if (b.getMaxhps().get(i) == -1) {
                continue;
            }
            if (i <= 6) {
                int idx = i - 1;
                if (this.afterFriend.get(idx) != null) {
                    this.afterFriend.get(idx).setMaxhp(b.getMaxhps().get(i));
                    this.afterFriend.get(idx).setNowhp(b.getNowhps().get(i));
                }
            } else {
                int idx = i - 1 - 6;
                if (this.afterEnemy.get(idx) != null) {
                    this.afterEnemy.get(idx).setMaxhp(b.getMaxhps().get(i));
                    this.afterEnemy.get(idx).setNowhp(b.getNowhps().get(i));
                }
            }
        }
        if (b instanceof ICombinedBattle) {
            ICombinedBattle cb = (ICombinedBattle) b;
            for (int i = 1, s = cb.getMaxhpsCombined().size(); i < s; i++) {
                int idx = i - 1;
                if (cb.getMaxhpsCombined().get(i) == -1) {
                    continue;
                }
                if (this.afterFriendCombined.get(idx) != null) {
                    this.afterFriendCombined.get(idx).setMaxhp(cb.getMaxhpsCombined().get(i));
                    this.afterFriendCombined.get(idx).setNowhp(cb.getNowhpsCombined().get(i));
                }
            }
        }
    }

    /**
     * フェイズ後味方(第1艦隊)を取得します。
     * @return フェイズ後味方(第1艦隊)
     */
    public List<Ship> getAfterFriend() {
        return this.afterFriend;
    }

    /**
     * フェイズ後味方(第1艦隊)を設定します。
     * @param afterFriend フェイズ後味方(第1艦隊)
     */
    public void setAfterFriend(List<Ship> afterFriend) {
        this.afterFriend = afterFriend;
    }

    /**
     * フェイズ後味方(第2艦隊)を取得します。
     * @return フェイズ後味方(第2艦隊)
     */
    public List<Ship> getAfterFriendCombined() {
        return this.afterFriendCombined;
    }

    /**
     * フェイズ後味方(第2艦隊)を設定します。
     * @param afterFriendCombined フェイズ後味方(第2艦隊)
     */
    public void setAfterFriendCombined(List<Ship> afterFriendCombined) {
        this.afterFriendCombined = afterFriendCombined;
    }

    /**
     * フェイズ後敵を取得します。
     * @return フェイズ後敵
     */
    public List<Enemy> getAfterEnemy() {
        return this.afterEnemy;
    }

    /**
     * フェイズ後敵を設定します。
     * @param afterEnemy フェイズ後敵
     */
    public void setAfterEnemy(List<Enemy> afterEnemy) {
        this.afterEnemy = afterEnemy;
    }
}