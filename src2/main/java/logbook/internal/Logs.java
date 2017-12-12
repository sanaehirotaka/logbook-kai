package logbook.internal;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

import logbook.Messages;
import logbook.bean.BattleLog;
import logbook.bean.BattleResult;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.Stage1;
import logbook.bean.MapStartNext;
import logbook.bean.Material;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;

/**
 * ログの表示、書き込みで使用する
 *
 */
public final class Logs {

    /** 日付書式 */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 海戦・ドロップ報告書ヘッダー */
    public static final String BATTLE_RESULT_HEADER = new StringJoiner(",")
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
            .add("敵艦1").add("敵艦1HP")
            .add("敵艦2").add("敵艦2HP")
            .add("敵艦3").add("敵艦3HP")
            .add("敵艦4").add("敵艦4HP")
            .add("敵艦5").add("敵艦5HP")
            .add("敵艦6").add("敵艦6HP")
            .toString();

    /** 海戦・ドロップ報告書.csv */
    public static final String BATTLE_RESULT = "海戦・ドロップ報告書.csv";

    /** 海戦・ドロップ報告書_alternativefile.csv */
    public static final String BATTLE_RESULT_ALT = "海戦・ドロップ報告書_alternativefile.csv";

    /** 資材ログヘッダー */
    public static final String MATERIAL_HEADER = new StringJoiner(",")
            .add("日付")
            .add("燃料")
            .add("弾薬")
            .add("鋼材")
            .add("ボーキ")
            .add("高速修復材")
            .add("高速建造材")
            .add("開発資材")
            .add("改修資材")
            .toString();

    /** 資材ログ.csv */
    public static final String MATERIAL = "資材ログ.csv";

    /** 資材ログ_alternativefile.csv */
    public static final String MATERIAL_ALT = "資材ログ_alternativefile.csv";

    /**
     * 戦闘ログを文字列にします
     *
     * @param log 戦闘ログ
     * @return 戦闘ログの文字列
     */
    public static String formatBattleLog(BattleLog log) {
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

        if (battle instanceof IKouku) {
            Kouku kouku = ((IKouku) battle).getKouku();
            Stage1 stage1 = kouku.getStage1();

            if (stage1 != null) {
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
        List<Ship> friendFleet = log.getDeckMap().get(battle.getDockId());
        for (int i = 0; i < friendFleet.size(); i++) {
            Ship ship = friendFleet.get(i);
            if (ship != null) {
                // 名前
                String name = Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse("");
                joiner.add(Messages.getString("ship.name", name, ship.getLv())); //$NON-NLS-1$
                // HP
                joiner.add(battle.getNowhps().get(i + 1) + "/" + battle.getMaxhps().get(i + 1));
            } else {
                joiner.add("");
                joiner.add("");
            }
        }
        // 敵艦
        List<Integer> enemyFleet = battle.getShipKe();
        for (int i = 1; i < enemyFleet.size(); i++) {
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
                joiner.add(battle.getNowhps().get(i + 6) + "/" + battle.getMaxhps().get(i + 6));
            } else {
                joiner.add("");
                joiner.add("");
            }
        }
        return joiner.toString();
    }

    /**
     * 資材ログを文字列にします
     *
     * @param material 資材ログ
     * @return 資材ログの文字列
     */
    public static String formatMaterial(Map<Integer, Material> material) {
        StringJoiner joiner = new StringJoiner(",");
        // 日付
        joiner.add(nowString());
        // 燃料
        joiner.add(String.valueOf(material.get(1).getValue()));
        // 弾薬
        joiner.add(String.valueOf(material.get(2).getValue()));
        // 鋼材
        joiner.add(String.valueOf(material.get(3).getValue()));
        // ボーキ
        joiner.add(String.valueOf(material.get(4).getValue()));
        // 高速修復材=6
        joiner.add(String.valueOf(material.get(6).getValue()));
        // 高速建造材=5
        joiner.add(String.valueOf(material.get(5).getValue()));
        // 開発資材
        joiner.add(String.valueOf(material.get(7).getValue()));
        // 改修資材
        joiner.add(String.valueOf(material.get(8).getValue()));

        return joiner.toString();
    }

    /**
     * タイムゾーンをJSTとして現在の日付/時間を取得します
     *
     * @return 現在の日付/時間
     */
    public static ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of("Asia/Tokyo"));
    }

    /**
     * タイムゾーンをJSTとして現在の日付/時間を"yyyy-MM-dd HH:mm:ss"形式の文字列として取得します
     *
     * @return 現在の日付/時間
     */
    public static String nowString() {
        return DATE_FORMAT.format(now());
    }
}
