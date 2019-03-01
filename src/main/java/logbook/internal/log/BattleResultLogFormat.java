package logbook.internal.log;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

import logbook.bean.BattleLog;
import logbook.bean.BattleResult;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.ICombinedEcBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.Stage1;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.bean.Useitem;
import logbook.bean.UseitemCollection;
import logbook.internal.Ships;

/**
 * 海戦・ドロップ報告書
 *
 */
public class BattleResultLogFormat extends LogFormatBase<BattleLog> {

    @Override
    public String name() {
        return "海戦・ドロップ報告書";
    }

    @Override
    public String header() {
        return new StringJoiner(",")
                .add("日付")
                .add("海域").add("マス").add("ボス").add("ランク")
                .add("艦隊行動").add("味方陣形").add("敵陣形")
                .add("制空権")
                .add("味方触接")
                .add("敵触接")
                .add("敵艦隊")
                .add("ドロップ艦種").add("ドロップ艦娘")
                .add("味方艦1").add("味方艦1HP")
                .add("味方艦2").add("味方艦2HP")
                .add("味方艦3").add("味方艦3HP")
                .add("味方艦4").add("味方艦4HP")
                .add("味方艦5").add("味方艦5HP")
                .add("味方艦6").add("味方艦6HP")
                .add("味方艦7").add("味方艦7HP")
                .add("味方艦8").add("味方艦8HP")
                .add("味方艦9").add("味方艦9HP")
                .add("味方艦10").add("味方艦10HP")
                .add("味方艦11").add("味方艦11HP")
                .add("味方艦12").add("味方艦12HP")
                .add("敵艦1").add("敵艦1HP")
                .add("敵艦2").add("敵艦2HP")
                .add("敵艦3").add("敵艦3HP")
                .add("敵艦4").add("敵艦4HP")
                .add("敵艦5").add("敵艦5HP")
                .add("敵艦6").add("敵艦6HP")
                .add("敵艦7").add("敵艦7HP")
                .add("敵艦8").add("敵艦8HP")
                .add("敵艦9").add("敵艦9HP")
                .add("敵艦10").add("敵艦10HP")
                .add("敵艦11").add("敵艦11HP")
                .add("敵艦12").add("敵艦12HP")
                .add("ドロップアイテム")
                .toString();
    }

    @Override
    public String format(BattleLog log) {
        IFormation battle = log.getBattle();
        BattleResult result = log.getResult();

        if (battle == null || result == null) {
            return "";
        }

        Function<BattleLog, String> bossText = l -> {
            MapStartNext first = l.getNext().get(0);
            MapStartNext last = l.getNext().get(l.getNext().size() - 1);

            boolean start = Objects.nonNull(first.getFromNo());
            boolean boss = last.getNo().equals(last.getBosscellNo()) || last.getEventId() == 5;

            StringJoiner joiner = new StringJoiner("&");
            if (start) {
                joiner.add("出撃");
            }
            if (boss) {
                joiner.add("ボス");
            }
            return joiner.toString();
        };

        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(log.getTime());
        // 海域
        joiner.add(result.getQuestName());
        // マス
        joiner.add(String.valueOf(log.getNext().get(log.getNext().size() - 1).getNo()));
        // ボス
        joiner.add(bossText.apply(log));
        // ランク
        joiner.add(result.getWinRank());
        // 艦隊行動
        joiner.add(BattleTypes.Intercept.toIntercept(battle.getFormation().get(2)).toString());
        // 味方陣形
        joiner.add(BattleTypes.Formation.toFormation(battle.getFormation().get(0)).toString());
        // 敵陣形
        joiner.add(BattleTypes.Formation.toFormation(battle.getFormation().get(1)).toString());

        if (battle.isIKouku()) {
            Kouku kouku = battle.asIKouku().getKouku();

            if (kouku != null && kouku.getStage1() != null) {
                Stage1 stage1 = kouku.getStage1();
                Map<Integer, SlotitemMst> slotitemMst = SlotitemMstCollection.get()
                        .getSlotitemMap();
                // 制空権
                joiner.add(BattleTypes.DispSeiku.toDispSeiku(stage1.getDispSeiku()).toString());
                // 味方触接
                joiner.add(Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(0)))
                        .map(SlotitemMst::getName)
                        .orElse(""));
                // 敵触接
                joiner.add(Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(1)))
                        .map(SlotitemMst::getName)
                        .orElse(""));
            } else {
                // 制空権
                joiner.add("");
                // 味方触接
                joiner.add("");
                // 敵触接
                joiner.add("");
            }
        } else {
            // 制空権
            joiner.add("");
            // 味方触接
            joiner.add("");
            // 敵触接
            joiner.add("");
        }

        // 敵艦隊
        joiner.add(result.getEnemyInfo().getDeckName());
        // ドロップ艦種
        joiner.add(Optional.ofNullable(result.getGetShip()).map(BattleResult.GetShip::getShipType).orElse(""));
        // ドロップ艦娘
        joiner.add(Optional.ofNullable(result.getGetShip()).map(BattleResult.GetShip::getShipName).orElse(""));
        // 味方艦
        if (!(battle.isICombinedBattle())) {
            // 通常艦隊はDockIdの艦隊
            List<Ship> friendFleet = log.getDeckMap().get(battle.getDockId());
            for (int i = 0; i < 6; i++) {
                if (friendFleet.size() > i) {
                    Ship ship = friendFleet.get(i);
                    if (ship != null) {
                        // 名前
                        joiner.add(Ships.toName(ship));
                        // HP
                        joiner.add(battle.getFNowhps().get(i) + "/" + battle.getFMaxhps().get(i));
                    } else {
                        joiner.add("");
                        joiner.add("");
                    }
                } else {
                    joiner.add("");
                    joiner.add("");
                }
            }
        } else {
            List<Ship> friendFleet;
            ICombinedBattle combinedBattle = battle.asICombinedBattle();
            // 連合艦隊は 1(第一艦隊),2(第二艦隊) で固定
            friendFleet = log.getDeckMap().get(1);
            for (int i = 0; i < 6; i++) {
                if (friendFleet.size() > i) {
                    Ship ship = friendFleet.get(i);
                    if (ship != null) {
                        // 名前
                        joiner.add(Ships.toName(ship));
                        // HP
                        joiner.add(combinedBattle.getFNowhps().get(i) + "/"
                                + combinedBattle.getFMaxhps().get(i));
                    } else {
                        joiner.add("");
                        joiner.add("");
                    }
                } else {
                    joiner.add("");
                    joiner.add("");
                }
            }
            friendFleet = log.getDeckMap().get(2);
            for (int i = 0; i < 6; i++) {
                if (friendFleet.size() > i) {
                    Ship ship = friendFleet.get(i);
                    if (ship != null) {
                        // 名前
                        joiner.add(Ships.toName(ship));
                        // HP
                        joiner.add(combinedBattle.getFNowhpsCombined().get(i) + "/"
                                + combinedBattle.getFMaxhpsCombined().get(i));
                    } else {
                        joiner.add("");
                        joiner.add("");
                    }
                } else {
                    joiner.add("");
                    joiner.add("");
                }
            }
        }
        // 敵艦
        List<Integer> enemyFleet = battle.getShipKe();
        for (int i = 0; i < 6; i++) {
            if (enemyFleet.size() > i) {
                ShipMst shipMst = ShipMstCollection.get()
                        .getShipMap()
                        .get(enemyFleet.get(i));

                if (shipMst != null) {
                    String flagship = shipMst.getYomi();
                    if ("".equals(flagship) || "-".equals(flagship)) {
                        joiner.add(shipMst.getName());
                    } else {
                        joiner.add(shipMst.getName() + "(" + flagship + ")");
                    }
                    joiner.add(battle.getENowhps().get(i) + "/" + battle.getEMaxhps().get(i));
                } else {
                    joiner.add("");
                    joiner.add("");
                }
            } else {
                joiner.add("");
                joiner.add("");
            }
        }
        if (battle.isICombinedEcBattle()) {
            List<Integer> enemyFleetCombined = battle.asICombinedEcBattle().getShipKeCombined();
            for (int i = 0; i < 6; i++) {
                if (enemyFleetCombined.size() > i) {
                    ShipMst shipMst = ShipMstCollection.get()
                            .getShipMap()
                            .get(enemyFleetCombined.get(i));

                    if (shipMst != null) {
                        String flagship = shipMst.getYomi();
                        if ("".equals(flagship) || "-".equals(flagship)) {
                            joiner.add(shipMst.getName());
                        } else {
                            joiner.add(shipMst.getName() + "(" + flagship + ")");
                        }
                        joiner.add(((ICombinedEcBattle) battle).getENowhpsCombined().get(i) + "/"
                                + ((ICombinedEcBattle) battle).getEMaxhpsCombined().get(i));
                    } else {
                        joiner.add("");
                        joiner.add("");
                    }
                } else {
                    joiner.add("");
                    joiner.add("");
                }
            }
        } else {
            for (int i = 0; i < 6; i++) {
                joiner.add("");
                joiner.add("");
            }
        }
        //ドロップアイテム
        joiner.add(Optional.ofNullable(result.getGetUseitem())
                .map(BattleResult.Useitem::getUseitemId)
                .map(id -> Optional.ofNullable(UseitemCollection.get().getUseitemMap().get(id)))
                .map(o -> o.map(Useitem::getName).orElse("不明"))
                .orElse(""));
        return joiner.toString();
    }

}
