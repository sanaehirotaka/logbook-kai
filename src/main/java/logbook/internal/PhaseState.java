package logbook.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import logbook.bean.AppCondition;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.AirBaseAttack;
import logbook.bean.BattleTypes.AtType;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.FriendlyBattle;
import logbook.bean.BattleTypes.FriendlyInfo;
import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.IAirbattle;
import logbook.bean.BattleTypes.IBattle;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.ICombinedEcBattle;
import logbook.bean.BattleTypes.ICombinedEcMidnightBattle;
import logbook.bean.BattleTypes.IHougeki;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.BattleTypes.INSupport;
import logbook.bean.BattleTypes.INightToDayBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.MidnightHougeki;
import logbook.bean.BattleTypes.MidnightSpList;
import logbook.bean.BattleTypes.Raigeki;
import logbook.bean.BattleTypes.SortieAtType;
import logbook.bean.BattleTypes.SortieAtTypeRaigeki;
import logbook.bean.BattleTypes.Stage3;
import logbook.bean.BattleTypes.SupportAiratack;
import logbook.bean.BattleTypes.SupportHourai;
import logbook.bean.BattleTypes.SupportInfo;
import logbook.bean.Chara;
import logbook.bean.Enemy;
import logbook.bean.Friend;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 戦闘フェイズにおける味方と敵のステータス
 */
@Getter
public class PhaseState {

    /** 連合艦隊 */
    private CombinedType combinedType = CombinedType.未結成;

    /** 連合艦隊での出撃 */
    private boolean combined;

    /** フェイズ後友軍艦隊(第1艦隊) */
    private List<Friend> afterFriendly = new ArrayList<>();

    /** フェイズ後味方(第1艦隊) */
    private List<Ship> afterFriend = new ArrayList<>();

    /** フェイズ後味方(第2艦隊) */
    private List<Ship> afterFriendCombined = new ArrayList<>();

    /** フェイズ後敵 */
    private List<Enemy> afterEnemy = new ArrayList<>();

    /** フェイズ後敵(第2艦隊) */
    private List<Enemy> afterEnemyCombined = new ArrayList<>();

    /** 攻撃/ダメージ詳細 */
    private List<AttackDetail> attackDetails = new ArrayList<>();

    /** 装備 */
    private Map<Integer, SlotItem> itemMap;

    /** 退避艦ID */
    private Set<Integer> escape;

    /**
     * 戦闘から新規フェイズを作成します
     * @param log 戦闘ログ
     */
    public PhaseState(BattleLog log) {
        this(log.getCombinedType(), log.getBattle(), log.getDeckMap(), log.getItemMap(), log.getEscape());
    }

    /**
     * 戦闘から新規フェイズを作成します
     * @param combinedType 連合艦隊
     * @param b 戦闘
     * @param deckMap 艦隊スナップショット
     * @param itemMap 装備スナップショット
     * @param escape 退避艦IDスナップショット
     */
    public PhaseState(CombinedType combinedType, IBattle b,
            Map<Integer, List<Ship>> deckMap, Map<Integer, SlotItem> itemMap, Set<Integer> escape) {
        this.itemMap = itemMap != null ? itemMap : SlotItemCollection.get().getSlotitemMap();
        this.escape = escape != null ? escape : AppCondition.get().getEscape();

        // 連合艦隊
        this.combinedType = combinedType;
        this.combined = combinedType != CombinedType.未結成 &&
                (b instanceof ICombinedBattle || b instanceof ICombinedEcMidnightBattle);

        // 味方
        if ((b instanceof ICombinedBattle || b instanceof ICombinedEcMidnightBattle)
                && this.combinedType != CombinedType.未結成 && b.getDockId() == 1) {
            for (Ship ship : deckMap.get(1)) {
                this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
            for (Ship ship : deckMap.get(2)) {
                this.afterFriendCombined.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
        } else {
            List<Ship> ships;
            if (b instanceof ICombinedEcMidnightBattle) {
                ICombinedEcMidnightBattle ecmb = (ICombinedEcMidnightBattle) b;
                ships = deckMap.get(ecmb.getActiveDeck().get(0));
            } else {
                ships = deckMap.get(b.getDockId());
            }
            for (Ship ship : ships) {
                this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
            }
        }
        // 敵
        for (int i = 0, s = b.getShipKe().size(); i < s; i++) {
            if (b.getShipKe().get(i) != -1) {
                Enemy e = new Enemy();
                e.setShipId(b.getShipKe().get(i));
                e.setLv(b.getShipLv().get(i));
                e.setSlot(b.getESlot().get(i));
                e.setOrder(i);

                this.afterEnemy.add(e);
            }
        }
        // 敵(第2艦隊)
        if (b instanceof ICombinedEcBattle) {
            ICombinedEcBattle ecb = (ICombinedEcBattle) b;
            for (int i = 0, s = ecb.getShipKeCombined().size(); i < s; i++) {
                if (ecb.getShipKeCombined().get(i) != -1) {
                    Enemy e = new Enemy();
                    e.setShipId(ecb.getShipKeCombined().get(i));
                    e.setLv(ecb.getShipLvCombined().get(i));
                    e.setSlot(ecb.getESlotCombined().get(i));
                    e.setOrder(i + 6);

                    this.afterEnemyCombined.add(e);
                }
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
        this.itemMap = ps.itemMap;
        this.escape = ps.escape;
        this.combinedType = ps.combinedType;
        this.combined = ps.combined;

        for (Friend friend : ps.afterFriendly) {
            this.afterFriendly.add(Optional.ofNullable(friend).map(Friend::clone).orElse(null));
        }
        for (Ship ship : ps.afterFriend) {
            this.afterFriend.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
        }
        for (Ship ship : ps.afterFriendCombined) {
            this.afterFriendCombined.add(Optional.ofNullable(ship).map(Ship::clone).orElse(null));
        }
        for (Enemy enemy : ps.afterEnemy) {
            this.afterEnemy.add(Optional.ofNullable(enemy).map(Enemy::clone).orElse(null));
        }
        for (Enemy enemy : ps.afterEnemyCombined) {
            this.afterEnemyCombined.add(Optional.ofNullable(enemy).map(Enemy::clone).orElse(null));
        }
    }

    /**
     * 基地航空隊戦フェイズ(噴式強襲)を適用します
     *
     * @param airBaseAttack 基地航空隊戦
     */
    public void applyAirBaseInject(IAirBaseAttack airBaseAttack) {
        if (airBaseAttack.getAirBaseInjection() != null) {
            this.applyAirBaseAttack(Arrays.asList(airBaseAttack.getAirBaseInjection()));
        }
    }

    /**
     * 基地航空隊戦フェイズを適用します
     *
     * @param airBaseAttack 基地航空隊戦
     */
    public void applyAirBaseAttack(IAirBaseAttack airBaseAttack) {
        this.applyAirBaseAttack(airBaseAttack.getAirBaseAttack());
    }

    /**
     * 航空戦フェイズ(噴式強襲)を適用します
     * @param battle 航空戦
     */
    public void applyInjectionKouku(IKouku battle) {
        this.applyKouku(battle.getInjectionKouku());
    }

    /**
     * 航空戦フェイズを適用します
     * @param battle 航空戦
     */
    public void applyKouku(IKouku battle) {
        this.applyKouku(battle.getKouku());
    }

    /**
     * 支援フェイズを適用します
     * @param battle 支援フェイズ
     */
    public void applySupport(ISupport battle) {
        this.applySupport(battle.getSupportInfo());
    }

    /**
     * 昼戦砲雷撃戦フェイズを適用します
     * @param battle 昼戦砲雷撃戦フェイズ
     */
    public void applySortieHougeki(ISortieHougeki battle) {
        // 先制対潜攻撃
        this.applyHougeki(battle.getOpeningTaisen());
        // 開幕雷撃
        this.applyRaigeki(battle.getOpeningAtack());
        if (!this.combined && battle instanceof ICombinedEcBattle) {
            // 1巡目
            this.applyHougeki(battle.getHougeki1());
            // 雷撃
            this.applyRaigeki(battle.getRaigeki());
            // 1巡目
            this.applyHougeki(battle.getHougeki2());
            // 2巡目
            this.applyHougeki(battle.getHougeki3());
        } else if (!this.combined) {
            // 連合艦隊以外
            // 1巡目
            this.applyHougeki(battle.getHougeki1());
            // 2巡目
            this.applyHougeki(battle.getHougeki2());
            // 雷撃
            this.applyRaigeki(battle.getRaigeki());
        } else if (this.combinedType == CombinedType.機動部隊 || this.combinedType == CombinedType.輸送部隊) {
            // 空母機動部隊、輸送護衛部隊
            // 第2艦隊1巡目
            this.applyHougeki(battle.getHougeki1());
            // 第2艦隊雷撃
            this.applyRaigeki(battle.getRaigeki());
            // 第1艦隊1巡目
            this.applyHougeki(battle.getHougeki2());
            // 第1艦隊2巡目
            this.applyHougeki(battle.getHougeki3());
        } else if (this.combinedType == CombinedType.水上部隊) {
            // 水上打撃部隊
            // 第1艦隊1巡目
            this.applyHougeki(battle.getHougeki1());
            // 第1艦隊2巡目
            this.applyHougeki(battle.getHougeki2());
            // 第2艦隊1巡目
            this.applyHougeki(battle.getHougeki3());
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
     * 支援フェイズを適用します
     * @param battle 支援フェイズ
     */
    public void applySupport(INSupport battle) {
        this.applySupport(battle.getNSupportInfo());
    }

    /**
     * 友軍艦隊の砲撃戦フェイズを適用します
     * @param battle 夜戦
     */
    public void applyFriendlyHougeki(IMidnightBattle battle) {
        if (battle.getFriendlyBattle() != null) {
            this.afterFriendly.clear();
            if (battle.getFriendlyInfo() != null) {
                FriendlyInfo friendlyInfo = battle.getFriendlyInfo();
                for (int i = 0, s = friendlyInfo.getShipId().size(); i < s; i++) {
                    Friend f = new Friend();
                    f.setShipId(friendlyInfo.getShipId().get(i));
                    f.setLv(friendlyInfo.getShipLv().get(i));
                    f.setSlot(friendlyInfo.getSlot().get(i));
                    f.setMaxhp(friendlyInfo.getMaxhps().get(i));
                    f.setNowhp(friendlyInfo.getNowhps().get(i));

                    this.afterFriendly.add(f);
                }
            }
            this.applyFriendlyHougeki(battle.getFriendlyBattle());
        }
    }

    /**
     * 夜戦を適用します
     * @param battle 夜戦
     */
    public void applyMidnightBattle(IMidnightBattle battle) {
        // 1巡目
        this.applyHougeki(battle.getHougeki());
    }

    /**
     * 夜戦を適用します
     * @param battle 夜戦
     */
    public void applyMidnightBattle(INightToDayBattle battle) {
        // 1巡目
        this.applyHougeki(battle.getNHougeki1());
        // 2巡目
        this.applyHougeki(battle.getNHougeki2());
    }

    /**
     * 全てのフェイズを適用します
     * @param battle 戦闘
     */
    public void apply(IBattle battle) {
        if (battle == null) {
            return;
        }

        if (!(battle instanceof INightToDayBattle)) {
            // 夜戦→昼戦以外

            // 基地航空隊戦フェイズ(噴式強襲)
            if (battle instanceof IAirBaseAttack) {
                this.applyAirBaseInject((IAirBaseAttack) battle);
            }
            // 航空戦フェイズ(噴式強襲)
            if (battle instanceof IKouku) {
                this.applyInjectionKouku((IKouku) battle);
            }
            // 基地航空隊戦フェイズ
            if (battle instanceof IAirBaseAttack) {
                this.applyAirBaseAttack((IAirBaseAttack) battle);
            }
            // 航空戦フェイズ
            if (battle instanceof IKouku) {
                this.applyKouku((IKouku) battle);
            }
            // 支援フェイズ
            if (battle instanceof ISupport) {
                this.applySupport((ISupport) battle);
            }
            // 砲雷撃戦フェイズ
            if (battle instanceof ISortieHougeki) {
                this.applySortieHougeki((ISortieHougeki) battle);
            }
            // 航空戦
            if (battle instanceof IAirbattle) {
                this.applyAirbattle((IAirbattle) battle);
            }
            // 夜戦支援
            if (battle instanceof INSupport) {
                this.applySupport((INSupport) battle);
            }
            // 夜戦
            if (battle instanceof IMidnightBattle) {
                this.applyFriendlyHougeki((IMidnightBattle) battle);
                this.applyMidnightBattle((IMidnightBattle) battle);
            }
        } else {
            // 夜戦→昼戦

            // 夜戦支援
            if (battle instanceof INSupport) {
                this.applySupport((INSupport) battle);
            }
            // 夜戦
            if (battle instanceof INightToDayBattle) {
                this.applyMidnightBattle((INightToDayBattle) battle);
            }
            // 基地航空隊戦フェイズ(噴式強襲)
            if (battle instanceof IAirBaseAttack) {
                this.applyAirBaseInject((IAirBaseAttack) battle);
            }
            // 航空戦フェイズ(噴式強襲)
            if (battle instanceof IKouku) {
                this.applyInjectionKouku((IKouku) battle);
            }
            // 基地航空隊戦フェイズ
            if (battle instanceof IAirBaseAttack) {
                this.applyAirBaseAttack((IAirBaseAttack) battle);
            }
            // 航空戦フェイズ
            if (battle instanceof IKouku) {
                this.applyKouku((IKouku) battle);
            }
            // 支援フェイズ
            if (battle instanceof ISupport) {
                this.applySupport((ISupport) battle);
            }
            // 砲雷撃戦フェイズ
            if (battle instanceof ISortieHougeki) {
                this.applySortieHougeki((ISortieHougeki) battle);
            }
        }
    }

    /**
     * 基地航空隊戦フェイズを適用します
     *
     * @param attacks 基地航空隊戦フェイズ
     */
    private void applyAirBaseAttack(List<AirBaseAttack> attacks) {
        if (attacks != null) {
            for (AirBaseAttack attack : attacks) {
                Stage3 stage3 = attack.getStage3();
                if (stage3 != null) {
                    List<Double> edam = stage3.getEdam();
                    if (edam != null) {
                        this.applyEnemyDamage(edam);
                    }
                }
                Stage3 stage3Combined = attack.getStage3Combined();
                if (stage3Combined != null) {
                    List<Double> edam = stage3Combined.getEdam();
                    if (edam != null) {
                        this.applyEnemyDamageCombined(edam);
                    }
                }
            }
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
            this.applyFriendDamage(stage3.getFdam());
            this.applyEnemyDamage(stage3.getEdam());
        }
        Stage3 stage3Combined = kouku.getStage3Combined();
        if (stage3Combined != null) {
            if (stage3Combined.getFdam() != null) {
                this.applyFriendDamageCombined(stage3Combined.getFdam());
            }
            if (stage3Combined.getEdam() != null) {
                this.applyEnemyDamageCombined(stage3Combined.getEdam());
            }
        }
    }

    /**
     * 支援フェイズを適用します
     * @param support 支援
     */
    private void applySupport(SupportInfo support) {
        if (support != null) {
            SupportAiratack air = support.getSupportAiratack();
            if (air != null) {
                Stage3 stage3 = air.getStage3();
                if (stage3 != null) {
                    this.applyEnemyDamage(stage3.getEdam());
                }
            }
            SupportHourai hou = support.getSupportHourai();
            if (hou != null) {
                this.applyEnemyDamage(hou.getDamage());
            }
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
        this.addDetailRaigeki(raigeki);
        // 新API
        this.applyFriendDamage(raigeki.getFdam());
        // 敵
        this.applyEnemyDamage(raigeki.getEdam());
    }

    /**
     * 砲撃戦フェイズを適用します
     * @param hougeki 砲撃戦フェイズ
     */
    private void applyHougeki(IHougeki hougeki) {
        this.applyHougeki(hougeki, false);
    }

    /**
     * 友軍艦隊の砲撃戦フェイズを適用します
     * @param friendlyBattle 砲撃戦フェイズ
     */
    private void applyFriendlyHougeki(FriendlyBattle friendlyBattle) {
        if (friendlyBattle != null) {
            this.applyHougeki(friendlyBattle.getHougeki(), true);
        }
    }

    /**
     * 砲撃戦フェイズを適用します
     * @param hougeki 砲撃戦フェイズ
     * @param isFriendlyBattle 友軍艦隊フラグ
     */
    private void applyHougeki(IHougeki hougeki, boolean isFriendlyBattle) {
        if (hougeki == null || hougeki.getAtEflag() == null) {
            return;
        }

        for (int i = 0, s = hougeki.getDamage().size(); i < s; i++) {
            int index = i;
            // 攻撃側インデックス
            int at = hougeki.getAtList().get(i);
            // 攻撃種別
            AtType atType;
            if (hougeki instanceof MidnightHougeki) {
                atType = Optional.ofNullable(((MidnightHougeki) hougeki).getSpList())
                        .map(l -> l.get(index))
                        .map(MidnightSpList::toMidnightSpList)
                        .orElse(MidnightSpList.toMidnightSpList(0));
            } else {
                atType = Optional.ofNullable(hougeki.getAtType())
                        .map(l -> l.get(index))
                        .map(SortieAtType::toSortieAtType)
                        .orElse(SortieAtType.toSortieAtType(0));
            }
            // 攻撃側が味方の場合true
            boolean atkfriend = hougeki.getAtEflag().get(i) == 0;

            Map<Integer, Integer> dfMap = new LinkedHashMap<>();
            List<Integer> dfList = hougeki.getDfList().get(i);
            List<Double> damageList = hougeki.getDamage().get(i);
            for (int j = 0; j < dfList.size(); j++) {
                if (dfList.get(j) >= 0) {
                    int damage = Math.max(damageList.get(j).intValue(), 0);
                    dfMap.compute(dfList.get(j), (k, v) -> v != null ? v + damage : damage);
                }
            }

            for (Entry<Integer, Integer> dfDamage : dfMap.entrySet()) {
                // 防御側インデックス
                int df = dfDamage.getKey();
                // ダメージ
                int damage = dfDamage.getValue();
                Chara attacker = null;
                Chara defender = null;

                if (atkfriend) {
                    if (isFriendlyBattle) {
                        attacker = this.afterFriendly.get(at);
                    } else {
                        if (Math.max(this.afterFriend.size(), 6) > at) {
                            attacker = this.afterFriend.get(at);
                        } else {
                            attacker = this.afterFriendCombined.get(at - 6);
                        }
                    }
                    if (Math.max(this.afterEnemy.size(), 6) > df) {
                        defender = this.afterEnemy.get(df);
                    } else {
                        defender = this.afterEnemyCombined.get(df - 6);
                    }
                } else {
                    if (Math.max(this.afterEnemy.size(), 6) > at) {
                        attacker = this.afterEnemy.get(at);
                    } else {
                        attacker = this.afterEnemyCombined.get(at - 6);
                    }
                    if (isFriendlyBattle) {
                        defender = this.afterFriendly.get(df);
                    } else {
                        if (Math.max(this.afterFriend.size(), 6) > df) {
                            defender = this.afterFriend.get(df);
                        } else {
                            defender = this.afterFriendCombined.get(df - 6);
                        }
                    }
                }

                this.damage(defender, damage);
                this.addDetail(attacker, defender, damage, atType);
            }
        }
    }

    /**
     * ダメージを適用します(味方第1,2艦隊)
     * @param damages ダメージ(zero-based)
     */
    private void applyFriendDamage(List<Double> damages) {
        for (int i = 0, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Ship ship = Math.max(this.afterFriend.size(), 6) > i
                        ? this.afterFriend.get(i)
                        : this.afterFriendCombined.get(i - 6);
                if (ship != null) {
                    this.damage(ship, damage);
                }
            }
        }
    }

    /**
     * ダメージを適用します(味方第2艦隊)
     * @param damages ダメージ(zero-based)
     */
    private void applyFriendDamageCombined(List<Double> damages) {
        for (int i = 0, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Ship ship = this.afterFriendCombined.get(i);
                if (ship != null) {
                    this.damage(ship, damage);
                }
            }
        }
    }

    /**
     * ダメージを適用します(敵第1,2艦隊)
     * @param damages ダメージ
     */
    private void applyEnemyDamage(List<Double> damages) {
        for (int i = 0, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Enemy enemy = Math.max(this.afterEnemy.size(), 6) > i
                        ? this.afterEnemy.get(i)
                        : this.afterEnemyCombined.get(i - 6);
                if (enemy != null) {
                    this.damage(enemy, damage);
                }
            }
        }
    }

    /**
     * ダメージを適用します(敵第2艦隊)
     * @param damages ダメージ
     */
    private void applyEnemyDamageCombined(List<Double> damages) {
        for (int i = 0, s = damages.size(); i < s; i++) {
            int damage = damages.get(i).intValue();
            if (damage != 0) {
                Enemy enemy = this.afterEnemyCombined.get(i);
                if (enemy != null) {
                    this.damage(enemy, damage);
                }
            }
        }
    }

    /**
     * HPをセットします
     * @param b 戦闘
     */
    private void setInitialHp(IBattle b) {
        for (int i = 0, s = b.getFMaxhps().size(); i < s; i++) {
            if (b.getFMaxhps().get(i) == -1) {
                continue;
            }
            if (this.afterFriend.get(i) != null) {
                this.afterFriend.get(i).setMaxhp(b.getFMaxhps().get(i));
                this.afterFriend.get(i).setNowhp(b.getFNowhps().get(i));
            }
        }
        for (int i = 0, s = b.getEMaxhps().size(); i < s; i++) {
            if (b.getEMaxhps().get(i) == -1) {
                continue;
            }
            if (this.afterEnemy.get(i) != null) {
                this.afterEnemy.get(i).setMaxhp(b.getEMaxhps().get(i));
                this.afterEnemy.get(i).setNowhp(b.getENowhps().get(i));
            }
        }
        if (b instanceof ICombinedBattle) {
            List<Integer> fNowHps = ((ICombinedBattle) b).getFNowhpsCombined();
            if (fNowHps != null) {
                for (int i = 0, s = fNowHps.size(); i < s; i++) {
                    if (fNowHps.get(i) == -1) {
                        continue;
                    }
                    Chara chara = this.afterFriendCombined.get(i);
                    if (chara != null) {
                        chara.setNowhp(fNowHps.get(i));
                    }
                }
            }
        }
        if (b instanceof ICombinedEcBattle) {
            List<Integer> eMaxHps = ((ICombinedEcBattle) b).getEMaxhpsCombined();
            List<Integer> eNowHps = ((ICombinedEcBattle) b).getENowhpsCombined();
            if (eNowHps != null) {
                for (int i = 0, s = eNowHps.size(); i < s; i++) {
                    if (eNowHps.get(i) == -1) {
                        continue;
                    }
                    Chara chara = this.afterEnemyCombined.get(i);
                    if (chara != null) {
                        chara.setMaxhp(eMaxHps.get(i));
                        chara.setNowhp(eNowHps.get(i));
                    }
                }
            }
        }
    }

    /**
     * ダメージ計算
     *
     * @param defender 防御側
     * @param damage ダメージ
     */
    private void damage(Chara defender, int damage) {
        int nowHp;
        if (defender.getNowhp() - damage <= 0 && defender instanceof Ship) {
            Ship ship = (Ship) defender;
            // 最初に消費される応急修理要員
            Optional<SlotitemMst> mst = Stream.concat(Stream.of(ship.getSlotEx()), ship.getSlot().stream())
                    .map(this.itemMap::get)
                    .map(Items::slotitemMst)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(SlotItemType.応急修理要員::equals)
                    .findFirst();
            if (mst.isPresent()) {
                if (mst.get().getName().equals("応急修理女神")) {
                    // 応急修理女神
                    // 女神発動では、艦の最大HPに回復する
                    nowHp = defender.getMaxhp();
                } else {
                    // 応急修理要員
                    // 要員発動では、艦の最大HPの20%に回復する(小数点以下切り捨て)
                    nowHp = (int) ((double) defender.getMaxhp() * 0.2D);
                }
            } else {
                nowHp = defender.getNowhp() - damage;
            }
        } else {
            nowHp = defender.getNowhp() - damage;
        }
        defender.setNowhp(nowHp);
    }

    /**
     * ダメージ詳細(雷撃)
     *
     * @param raigeki
     */
    private void addDetailRaigeki(Raigeki raigeki) {
        // 敵→味方
        this.addDetailRaigeki0(this.afterEnemy, this.afterEnemyCombined, this.afterFriend,
                this.afterFriendCombined,
                raigeki.getErai(), raigeki.getEydam());
        // 味方→敵
        this.addDetailRaigeki0(this.afterFriend, this.afterFriendCombined, this.afterEnemy, this.afterEnemyCombined,
                raigeki.getFrai(), raigeki.getFydam());
    }

    /**
     * ダメージ詳細(雷撃)
     *
     * @param attackerFleet 攻撃側艦隊
     * @param attackerFleetCombined 攻撃側艦隊(第2艦隊)
     * @param defenderFleet 防御側艦隊
     * @param defenderFleetCombined 防御側艦隊(第2艦隊)
     * @param index 攻撃対象インデックス
     * @param ydam 与ダメージ
     */
    private void addDetailRaigeki0(List<? extends Chara> attackerFleet, List<? extends Chara> attackerFleetCombined,
            List<? extends Chara> defenderFleet, List<? extends Chara> defenderFleetCombined,
            List<Integer> index, List<Double> ydam) {

        if (defenderFleet != null)
            defenderFleet = defenderFleet.stream()
                    .map(c -> c != null ? c.clone() : null)
                    .collect(Collectors.toList());
        if (defenderFleetCombined != null)
            defenderFleetCombined = defenderFleetCombined.stream()
                    .map(c -> c != null ? c.clone() : null)
                    .collect(Collectors.toList());
        for (int i = 0; i < index.size(); i++) {
            if (index.get(i) >= 0) {
                Chara attacker = Math.max(attackerFleet.size(), 6) > i
                        ? attackerFleet.get(i)
                        : attackerFleetCombined.get(i - 6);
                Chara defender = Math.max(defenderFleet.size(), 6) > index.get(i)
                        ? defenderFleet.get(index.get(i))
                        : defenderFleetCombined.get(index.get(i) - 6);
                int damage = (int) ydam.get(i).doubleValue();

                defender.setNowhp(defender.getNowhp() - damage);

                this.addDetail(attacker, defender, damage, SortieAtTypeRaigeki.通常雷撃);
            }
        }
    }

    /**
     * ダメージ詳細を追加する
     *
     * @param attacker 攻撃側
     * @param defender 防御側
     * @param damage ダメージ
     * @param atType 攻撃種別
     */
    private void addDetail(Chara attacker, Chara defender, int damage, AtType atType) {
        this.attackDetails.add(new AttackDetail(
                Optional.ofNullable(attacker).map(Chara::clone).orElse(null),
                Optional.ofNullable(defender).map(Chara::clone).orElse(null), damage, atType));
    }

    /**
     * 味方のHP合計を取得する
     * @return 味方のHP合計
     */
    public double friendTotalHp() {
        return Stream.concat(this.afterFriend.stream(), this.afterFriendCombined.stream())
                .filter(Objects::nonNull)
                .mapToInt(Chara::getNowhp)
                .map(hp -> Math.max(hp, 0))
                .sum();
    }

    /**
     * 敵のHP合計を取得する
     * @return 敵のHP合計
     */
    public double enemyTotalHp() {
        return Stream.concat(this.afterEnemy.stream(), this.afterEnemyCombined.stream())
                .filter(Objects::nonNull)
                .mapToInt(Chara::getNowhp)
                .map(hp -> Math.max(hp, 0))
                .sum();
    }

    /**
     * 味方のHP1以上の隻数を取得する
     * @return 味方のHP1以上の隻数
     */
    public int friendAliveCount() {
        return (int) Stream.concat(this.afterFriend.stream(), this.afterFriendCombined.stream())
                .filter(Objects::nonNull)
                .mapToInt(Chara::getNowhp)
                .filter(hp -> hp > 0)
                .count();
    }

    /**
     * 敵のHP1以上の隻数を取得する
     * @return 敵のHP1以上の隻数
     */
    public int enemydAliveCount() {
        return (int) Stream.concat(this.afterEnemy.stream(), this.afterEnemyCombined.stream())
                .filter(Objects::nonNull)
                .mapToInt(Chara::getNowhp)
                .filter(hp -> hp > 0)
                .count();
    }

    @Data
    @AllArgsConstructor
    public static class AttackDetail {

        /** 攻撃側 */
        private Chara attacker;

        /** 防御側 */
        private Chara defender;

        /** ダメージ */
        private int damage;

        /** 攻撃種別 */
        private AtType atType;
    }
}