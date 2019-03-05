package logbook.internal.log;

import java.util.Arrays;
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

        Format format = new Format();
        format.日付 = log.getTime();
        format.海域 = result.getQuestName();
        format.マス = String.valueOf(log.getNext().get(log.getNext().size() - 1).getNo());
        format.ボス = bossText.apply(log);
        format.ランク = result.getWinRank();
        format.艦隊行動 = BattleTypes.Intercept.toIntercept(battle.getFormation().get(2)).toString();
        format.味方陣形 = BattleTypes.Formation.toFormation(battle.getFormation().get(0)).toString();
        format.敵陣形 = BattleTypes.Formation.toFormation(battle.getFormation().get(1)).toString();

        if (battle.isIKouku()) {
            Kouku kouku = battle.asIKouku().getKouku();

            if (kouku != null && kouku.getStage1() != null) {
                Stage1 stage1 = kouku.getStage1();
                Map<Integer, SlotitemMst> slotitemMst = SlotitemMstCollection.get()
                        .getSlotitemMap();
                format.制空権 = BattleTypes.DispSeiku.toDispSeiku(stage1.getDispSeiku()).toString();
                format.味方触接 = Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(0)))
                        .map(SlotitemMst::getName)
                        .orElse("");
                format.敵触接 = Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(1)))
                        .map(SlotitemMst::getName)
                        .orElse("");
            }
        }
        format.敵艦隊 = result.getEnemyInfo().getDeckName();
        format.ドロップ艦種 = Optional.ofNullable(result.getGetShip()).map(BattleResult.GetShip::getShipType).orElse("");
        format.ドロップ艦娘 = Optional.ofNullable(result.getGetShip()).map(BattleResult.GetShip::getShipName).orElse("");
        // 味方艦
        if (!(battle.isICombinedBattle())) {
            // 通常艦隊はDockIdの艦隊
            List<Ship> friendFleet = log.getDeckMap().get(battle.getDockId());
            for (int i = 0; i < 6; i++) {
                if (friendFleet.size() > i) {
                    Ship ship = friendFleet.get(i);
                    if (ship != null) {
                        format.味方艦[i] = Ships.toName(ship);
                        format.味方艦HP[i] = battle.getFNowhps().get(i) + "/" + battle.getFMaxhps().get(i);
                    }
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
                        format.味方艦[i] = Ships.toName(ship);
                        format.味方艦HP[i] = combinedBattle.getFNowhps().get(i) + "/" + combinedBattle.getFMaxhps().get(i);
                    }
                }
            }
            friendFleet = log.getDeckMap().get(2);
            for (int i = 0; i < 6; i++) {
                if (friendFleet.size() > i) {
                    Ship ship = friendFleet.get(i);
                    if (ship != null) {
                        format.味方艦[i + 6] = Ships.toName(ship);
                        format.味方艦HP[i + 6] = combinedBattle.getFNowhpsCombined().get(i) + "/"
                                + combinedBattle.getFMaxhpsCombined().get(i);
                    }
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
                    String name;
                    if ("".equals(flagship) || "-".equals(flagship)) {
                        name = shipMst.getName();
                    } else {
                        name = shipMst.getName() + "(" + flagship + ")";
                    }
                    format.敵艦[i] = name;
                    format.敵艦HP[i] = battle.getENowhps().get(i) + "/" + battle.getEMaxhps().get(i);
                }
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
                        String name;
                        if ("".equals(flagship) || "-".equals(flagship)) {
                            name = shipMst.getName();
                        } else {
                            name = shipMst.getName() + "(" + flagship + ")";
                        }
                        format.敵艦[i + 6] = name;
                        format.敵艦HP[i + 6] = ((ICombinedEcBattle) battle).getENowhpsCombined().get(i) + "/"
                                + ((ICombinedEcBattle) battle).getEMaxhpsCombined().get(i);
                    }
                }
            }
        }
        // ドロップアイテム
        format.ドロップアイテム = Optional.ofNullable(result.getGetUseitem())
                .map(BattleResult.Useitem::getUseitemId)
                .map(id -> Optional.ofNullable(UseitemCollection.get().getUseitemMap().get(id)))
                .map(o -> o.map(Useitem::getName).orElse("不明"))
                .orElse("");
        return format.toString();
    }

    private static class Format {
        String 日付 = "";
        String 海域 = "";
        String マス = "";
        String ボス = "";
        String ランク = "";
        String 艦隊行動 = "";
        String 味方陣形 = "";
        String 敵陣形 = "";
        String 制空権 = "";
        String 味方触接 = "";
        String 敵触接 = "";
        String 敵艦隊 = "";
        String ドロップ艦種 = "";
        String ドロップ艦娘 = "";
        String[] 味方艦 = new String[12];
        String[] 味方艦HP = new String[12];
        String[] 敵艦 = new String[12];
        String[] 敵艦HP = new String[12];
        String ドロップアイテム = "";

        public Format() {
            Arrays.fill(this.味方艦, "");
            Arrays.fill(this.味方艦HP, "");
            Arrays.fill(this.敵艦, "");
            Arrays.fill(this.敵艦HP, "");
        }

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add(this.日付);
            joiner.add(this.海域);
            joiner.add(this.マス);
            joiner.add(this.ボス);
            joiner.add(this.ランク);
            joiner.add(this.艦隊行動);
            joiner.add(this.味方陣形);
            joiner.add(this.敵陣形);
            joiner.add(this.制空権);
            joiner.add(this.味方触接);
            joiner.add(this.敵触接);
            joiner.add(this.敵艦隊);
            joiner.add(this.ドロップ艦種);
            joiner.add(this.ドロップ艦娘);
            for (int i = 0; i < this.味方艦.length; i++) {
                joiner.add(this.味方艦[i]);
                joiner.add(this.味方艦HP[i]);
            }
            for (int i = 0; i < this.敵艦.length; i++) {
                joiner.add(this.敵艦[i]);
                joiner.add(this.敵艦HP[i]);
            }
            joiner.add(this.ドロップアイテム);
            return joiner.toString();
        }
    }
}
