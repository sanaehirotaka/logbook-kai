package logbook.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import javafx.scene.image.Image;
import logbook.bean.AppCondition;
import logbook.bean.Basic;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.bean.Stype;
import logbook.bean.StypeCollection;

/**
 * 艦娘に関するメソッドを集めたクラス
 *
 */
public class Ships {

    /** 小破(75%) */
    private static final double SLIGHT_DAMAGE = 0.75D;
    /** 中破(50%) */
    private static final double HALF_DAMAGE = 0.5D;
    /** 大破(25%) */
    private static final double BADLY_DAMAGE = 0.25D;
    /** 疲労赤色 */
    private static final int RED = 19;
    /** 疲労オレンジ色 */
    private static final int ORANGE = 29;
    /** キラキラ1段階 */
    private static final int DARK_GREEN = 50;
    /** キラキラ2段階 */
    private static final int GREEN = 53;

    private Ships() {
    }

    /**
     * 艦娘が小破未満か判定します
     *
     * @param ship 艦娘
     * @return 小破未満の場合true
     */
    public static boolean isLessThanSlightDamage(Ship ship) {
        return Double.compare(hpPer(ship), SLIGHT_DAMAGE) > 0;
    }

    /**
     * 艦娘が小破状態か判定します
     *
     * @param ship 艦娘
     * @return 小破状態の場合true
     */
    public static boolean isSlightDamage(Ship ship) {
        double per = hpPer(ship);
        return Double.compare(per, SLIGHT_DAMAGE) <= 0 && Double.compare(per, HALF_DAMAGE) > 0;
    }

    /**
     * 艦娘が中破状態か判定します
     *
     * @param ship 艦娘
     * @return 中破状態の場合true
     */
    public static boolean isHalfDamage(Ship ship) {
        double per = hpPer(ship);
        return Double.compare(per, HALF_DAMAGE) <= 0 && Double.compare(per, BADLY_DAMAGE) > 0;
    }

    /**
     * 艦娘が大破状態か判定します
     *
     * @param ship 艦娘
     * @return 大破状態の場合true
     */
    public static boolean isBadlyDamage(Ship ship) {
        double per = hpPer(ship);
        return Double.compare(per, BADLY_DAMAGE) <= 0 && per > 0;
    }

    /**
     * 艦娘が撃沈状態か判定します
     *
     * @param ship 艦娘
     * @return 撃沈状態の場合true
     */
    public static boolean isLost(Ship ship) {
        return ship.getNowhp() == 0;
    }

    /**
     * 艦娘が退避状態か判定します
     *
     * @param ship 退避
     * @return 退避状態の場合true
     */
    public static boolean isEscape(Ship ship) {
        return AppCondition.get()
                .getEscape().contains(ship.getId());
    }

    /**
     * 艦娘がキラキラ2段階目(コンディション値 53以上)か判定します
     *
     * @param ship 艦娘
     * @return キラキラ2段階目の場合true
     */
    public static boolean isGreen(Ship ship) {
        return ship.getCond() >= GREEN;
    }

    /**
     * 艦娘がキラキラ1段階目(コンディション値 50以上53未満)か判定します
     *
     * @param ship 艦娘
     * @return キラキラ1段階目の場合true
     */
    public static boolean isDeepGreen(Ship ship) {
        int cond = ship.getCond();
        return cond >= DARK_GREEN && cond < GREEN;
    }

    /**
     * 艦娘が疲労か判定します
     *
     * @param ship 艦娘
     * @return 疲労の場合true
     */
    public static boolean isOrange(Ship ship) {
        int cond = ship.getCond();
        return cond <= ORANGE && cond > RED;
    }

    /**
     * 艦娘が赤疲労か判定します
     *
     * @param ship 艦娘
     * @return 赤疲労の場合true
     */
    public static boolean isRed(Ship ship) {
        return ship.getCond() <= RED;
    }

    /**
     * 艦娘の画像を取得します
     *
     * @param ship 艦娘
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipImage(Ship ship) throws IllegalStateException {
        return ShipImage.get(ship, false);
    }

    /**
     * 艦娘の画像を取得します
     *
     * @param ship 艦娘
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemImage(Ship ship) throws IllegalStateException {
        return ShipImage.get(ship, true);
    }

    /**
     * 艦娘に対応する艦船を取得します
     *
     * @param ship 艦娘
     * @return 艦船
     */
    public static Optional<ShipMst> shipMst(Ship ship) {
        ShipMst mst = ShipMstCollection.get()
                .getShipMap()
                .get(ship.getShipId());
        return Optional.ofNullable(mst);
    }

    /**
     * 艦娘に対応する艦種を取得します
     *
     * @param ship 艦娘
     * @return 艦種
     */
    public static Optional<Stype> stype(Ship ship) {
        Stype stype = null;
        Optional<ShipMst> mst = shipMst(ship);
        if (mst.isPresent()) {
            stype = StypeCollection.get()
                    .getStypeMap()
                    .get(mst.get().getStype());
        }
        return Optional.ofNullable(stype);
    }

    /**
     * 砲撃戦火力
     *
     * @param ship 艦娘
     * @return 砲撃戦火力
     */
    public static int hPower(Ship ship) {
        List<SlotitemMst> items = getSlotitemMst(ship);
        // 艦攻艦爆搭載艦
        boolean isPasedoCarrier = items.stream()
                .filter(e -> SlotItemType.艦上攻撃機.equals(e)
                        || SlotItemType.艦上爆撃機.equals(e))
                .findAny()
                .isPresent();
        // 艦種
        Integer stype = Ships.shipMst(ship)
                .map(ShipMst::getStype)
                .orElse(0);
        // 火力
        int karyoku = ship.getKaryoku().get(0);

        if (isPasedoCarrier
                || ShipType.正規空母.equals(stype)
                || ShipType.装甲空母.equals(stype)) {
            // 空母(または空母扱い)の場合
            // (火力 + 雷装) × 1.5 + 爆装 × 2 + 55
            int raig = items.stream()
                    .mapToInt(SlotitemMst::getRaig)
                    .sum();
            int baku = items.stream()
                    .mapToInt(SlotitemMst::getBaku)
                    .sum();
            return (int) Math.round(((karyoku + raig) * 1.5D) + (baku * 2) + 55);
        } else {
            return karyoku + 5;
        }
    }

    /**
     * 雷撃戦火力
     *
     * @param ship 艦娘
     * @return 雷撃戦火力
     */
    public static int rPower(Ship ship) {
        return ship.getRaisou().get(0) + 5;
    }

    /**
     * 対潜火力
     *
     * @param ship 艦娘
     * @return 対潜火力
     */
    public static int tPower(Ship ship) {
        List<SlotitemMst> items = getSlotitemMst(ship);

        // [ 艦船の対潜 ÷ 5 ] + 装備の対潜 × 2 + 25
        int tais = items.stream()
                .mapToInt(SlotitemMst::getTais)
                .sum();
        return (int) Math.round(Math.floor((ship.getTaisen().get(0) - tais) / 5D) + (tais * 2) + 25);
    }

    /**
     * 夜戦火力
     *
     * @param ship 艦娘
     * @return 夜戦火力
     */
    public static int yPower(Ship ship) {
        return ship.getKaryoku().get(0) + ship.getRaisou().get(0);
    }

    /**
     * 制空値(熟練度による上昇を考慮)
     *
     * @param ship 艦娘
     * @return 制空値
     */
    public static int airSuperiority(Ship ship) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        // 参考 http://wikiwiki.jp/kancolle/?%B4%CF%BA%DC%B5%A1%BD%CF%CE%FD%C5%D9
        // 内部熟練度
        ToIntFunction<Integer> skillLevel = e -> {
            switch (e) {
            case 1:
                return 10;
            case 2:
                return 25;
            case 3:
                return 40;
            case 4:
                return 55;
            case 5:
                return 70;
            case 6:
                return 85;
            case 7:
                return 100;
            }
            return 0;
        };
        // 制空ボーナス(艦戦)
        ToIntFunction<Integer> bonusF = e -> {
            switch (e) {
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 9;
            case 5:
                return 14;
            case 6:
                return 14;
            case 7:
                return 22;
            }
            return 0;
        };
        // 制空ボーナス(水爆)
        ToIntFunction<Integer> bonusS = e -> {
            switch (e) {
            case 2:
                return 1;
            case 3:
                return 1;
            case 4:
                return 1;
            case 5:
                return 3;
            case 6:
                return 3;
            case 7:
                return 6;
            }
            return 0;
        };

        // 艦娘制空値
        int value = 0;

        for (int i = 0; i < ship.getSlot().size(); i++) {
            // スロット内制空値
            double local = 0;

            int onslot = ship.getOnslot().get(i);
            SlotItem item = itemMap.get(ship.getSlot().get(i));
            if (item == null)
                continue;
            SlotitemMst itemMst = itemMstMap.get(item.getSlotitemId());

            // 制空状態に関係するのは対空値を持つ艦戦、艦攻、艦爆、水爆、水戦のみ
            if (SlotItemType.艦上戦闘機.equals(itemMst)
                    || SlotItemType.艦上攻撃機.equals(itemMst)
                    || SlotItemType.艦上爆撃機.equals(itemMst)
                    || SlotItemType.水上爆撃機.equals(itemMst)
                    || SlotItemType.水上戦闘機.equals(itemMst)) {
                // 制空値
                local += itemMst.getTyku() * Math.sqrt(onslot);

                if (item.getAlv() != null) {
                    // 上昇制空値＝内部熟練ボーナス＋制空ボーナス(艦戦/水爆)
                    // 内部熟練ボーナス＝√(内部熟練度/10)

                    // 熟練ボーナス
                    local += Math.sqrt(skillLevel.applyAsInt(item.getAlv()) / 10D);
                    // 制空ボーナス
                    if (SlotItemType.艦上戦闘機.equals(itemMst)) {
                        local += bonusF.applyAsInt(item.getAlv());
                    } else if (SlotItemType.水上爆撃機.equals(itemMst)) {
                        local += bonusS.applyAsInt(item.getAlv());
                    }
                }
            }
            value += local;
        }
        return value;
    }

    /**
     * 索敵値(2-5式秋)
     *
     * @param ships 艦娘達
     * @return 対潜火力
     */
    public static double viewRange(List<Ship> ships) {
        // 索敵スコア
        // = 艦上爆撃機 × (1.04)
        // + 艦上攻撃機 × (1.37)
        // + 艦上偵察機 × (1.66)
        // + 水上偵察機 × (2.00)
        // + 水上爆撃機 × (1.78)
        // + 小型電探 × (1.00)
        // + 大型電探 × (0.99)
        // + 探照灯 × (0.91)
        // + √(各艦毎の素索敵) × (1.69)
        // + (司令部レベルを5の倍数に切り上げ) × (-0.61)
        ToDoubleFunction<SlotitemMst> mapper = item -> {
            if (SlotItemType.艦上爆撃機.equals(item)) {
                return 1.04D;
            } else if (SlotItemType.艦上攻撃機.equals(item)) {
                return 1.37D;
            } else if (SlotItemType.艦上偵察機.equals(item) || SlotItemType.艦上偵察機II.equals(item)) {
                return 1.66D;
            } else if (SlotItemType.水上偵察機.equals(item)) {
                return 2.00D;
            } else if (SlotItemType.水上爆撃機.equals(item)) {
                return 1.78D;
            } else if (SlotItemType.小型電探.equals(item)) {
                return 1.00D;
            } else if (SlotItemType.大型電探.equals(item) || SlotItemType.大型電探II.equals(item)) {
                return 0.99D;
            } else if (SlotItemType.探照灯.equals(item)) {
                return 0.91D;
            }
            return 0D;
        };
        // 装備索敵スコア
        double itemScore = ships.stream()
                .flatMap(e -> getSlotitemMst(e).stream())
                .mapToDouble(mapper)
                .sum();
        // 艦娘索敵スコア
        double shipScore = ships.stream()
                .mapToDouble(e -> e.getSakuteki().get(0) - getSlotitemMst(e).stream()
                        .mapToInt(SlotitemMst::getSaku)
                        .sum())
                .map(Math::sqrt)
                .sum() * 1.69D;
        // レベルスコア
        double levelScore = ((Basic.get().getLevel() + 4) / 5) * 5 * -0.61D;

        return BigDecimal.valueOf(itemScore + shipScore + levelScore)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * 装備定義を取得します
     *
     * @param ship 艦娘
     * @return 装備定義のリスト
     */
    private static List<SlotitemMst> getSlotitemMst(Ship ship) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        return ship.getSlot()
                .stream()
                .map(itemMap::get)
                .filter(Objects::nonNull)
                .map(SlotItem::getSlotitemId)
                .map(itemMstMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 艦娘のHP割合
     * @param ship 艦娘
     * @return HP割合
     */
    private static double hpPer(Ship ship) {
        return (double) ship.getNowhp() / (double) ship.getMaxhp();
    }
}
