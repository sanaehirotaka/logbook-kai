package logbook.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.image.Image;
import logbook.bean.AppCondition;
import logbook.bean.Basic;
import logbook.bean.Chara;
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

    /** 索敵係数 */
    private static final Map<SlotItemType, Double> VIEW_COEFFICIENT = new EnumMap<>(SlotItemType.class);
    /** 改修係数 */
    private static final Map<SlotItemType, Double> LV_COEFFICIENT = new EnumMap<>(SlotItemType.class);

    static {
        // 索敵係数
        // 艦上戦闘機：0.6
        VIEW_COEFFICIENT.put(SlotItemType.艦上戦闘機, 0.6D);
        // 艦上爆撃機：0.6
        VIEW_COEFFICIENT.put(SlotItemType.艦上爆撃機, 0.6D);
        // 艦上攻撃機：0.8
        VIEW_COEFFICIENT.put(SlotItemType.艦上攻撃機, 0.8D);
        // 艦上偵察機：1
        VIEW_COEFFICIENT.put(SlotItemType.艦上偵察機, 1.0D);
        // 水上偵察機：1.2
        VIEW_COEFFICIENT.put(SlotItemType.水上偵察機, 1.2D);
        // 水上爆撃機：1.1
        VIEW_COEFFICIENT.put(SlotItemType.水上爆撃機, 1.1D);
        // 小型電探：0.6
        VIEW_COEFFICIENT.put(SlotItemType.小型電探, 0.6D);
        // 大型電探：0.6
        VIEW_COEFFICIENT.put(SlotItemType.大型電探, 0.6D);
        // 対潜哨戒機：0.6
        VIEW_COEFFICIENT.put(SlotItemType.対潜哨戒機, 0.6D);
        // 探照灯：0.6
        VIEW_COEFFICIENT.put(SlotItemType.探照灯, 0.6D);
        // 司令部施設：0.6
        VIEW_COEFFICIENT.put(SlotItemType.司令部施設, 0.6D);
        // 航空要員(熟練艦載機整備員)：0.6
        VIEW_COEFFICIENT.put(SlotItemType.航空要員, 0.6D);
        // 水上艦要員(熟練見張員)：0.6
        VIEW_COEFFICIENT.put(SlotItemType.水上艦要員, 0.6D);
        // 大型ソナー：0.6
        VIEW_COEFFICIENT.put(SlotItemType.大型ソナー, 0.6D);
        // 大型飛行艇：0.6
        VIEW_COEFFICIENT.put(SlotItemType.大型飛行艇, 0.6D);
        // 大型探照灯：0.6
        VIEW_COEFFICIENT.put(SlotItemType.大型探照灯, 0.6D);
        // 水上戦闘機：0.6
        VIEW_COEFFICIENT.put(SlotItemType.水上戦闘機, 0.6D);
        // 艦上偵察機(II)：1
        VIEW_COEFFICIENT.put(SlotItemType.艦上偵察機II, 1.0D);

        // 改修係数
        // 小型電探：1.25
        LV_COEFFICIENT.put(SlotItemType.小型電探, 1.25D);
        // 大型電探：1.25
        LV_COEFFICIENT.put(SlotItemType.大型電探, 1.25D);
        // 水上偵察機：1.2
        LV_COEFFICIENT.put(SlotItemType.水上偵察機, 1.2D);
    }

    private Ships() {
    }

    /**
     * キャラクターが小破未満か判定します
     *
     * @param chara キャラクター
     * @return 小破未満の場合true
     */
    public static boolean isLessThanSlightDamage(Chara chara) {
        return Double.compare(hpPer(chara), SLIGHT_DAMAGE) > 0;
    }

    /**
     * キャラクターが小破状態か判定します
     *
     * @param chara キャラクター
     * @return 小破状態の場合true
     */
    public static boolean isSlightDamage(Chara chara) {
        double per = hpPer(chara);
        return Double.compare(per, SLIGHT_DAMAGE) <= 0 && Double.compare(per, HALF_DAMAGE) > 0;
    }

    /**
     * キャラクターが中破状態か判定します
     *
     * @param chara キャラクター
     * @return 中破状態の場合true
     */
    public static boolean isHalfDamage(Chara chara) {
        double per = hpPer(chara);
        return Double.compare(per, HALF_DAMAGE) <= 0 && Double.compare(per, BADLY_DAMAGE) > 0;
    }

    /**
     * キャラクターが大破状態か判定します
     *
     * @param chara キャラクター
     * @return 大破状態の場合true
     */
    public static boolean isBadlyDamage(Chara chara) {
        double per = hpPer(chara);
        return Double.compare(per, BADLY_DAMAGE) <= 0 && per > 0;
    }

    /**
     * キャラクターが撃沈状態か判定します
     *
     * @param chara キャラクター
     * @return 撃沈状態の場合true
     */
    public static boolean isLost(Chara chara) {
        return chara.getNowhp() <= 0;
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
     * キャラクターの画像を取得します
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipImage(Chara chara) throws IllegalStateException {
        return ShipImage.get(chara, false, true);
    }

    /**
     * キャラクターの画像を取得します(装備画像付き)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemImage(Chara chara) throws IllegalStateException {
        return ShipImage.get(chara, true, true);
    }

    /**
     * キャラクターの画像を取得します(装備画像付き、状態バナー無し)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemWithoutStateBannerImage(Chara chara) throws IllegalStateException {
        return ShipImage.get(chara, true, false);
    }

    /**
     * キャラクターに対応する艦船を取得します
     *
     * @param chara キャラクター
     * @return 艦船
     */
    public static Optional<ShipMst> shipMst(Chara chara) {
        ShipMst mst = ShipMstCollection.get()
                .getShipMap()
                .get(chara.getShipId());
        return Optional.ofNullable(mst);
    }

    /**
     * キャラクターに対応する艦種を取得します
     *
     * @param chara キャラクター
     * @return 艦種
     */
    public static Optional<Stype> stype(Chara chara) {
        Stype stype = null;
        Optional<ShipMst> mst = shipMst(chara);
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
        List<SlotitemMst> items = getSlotitemMst(ship).collect(Collectors.toList());
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
        // [ 艦船の対潜 ÷ 5 ] + 装備の対潜 × 2 + 25
        int tais = getSlotitemMst(ship)
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
     * @return 索敵値(2-5式秋)
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
            double itemScore = item.getSaku();
            if (SlotItemType.艦上爆撃機.equals(item)) {
                itemScore *= 1.04D;
            } else if (SlotItemType.艦上攻撃機.equals(item)) {
                itemScore *= 1.37D;
            } else if (SlotItemType.艦上偵察機.equals(item) || SlotItemType.艦上偵察機II.equals(item)) {
                itemScore *= 1.66D;
            } else if (SlotItemType.水上偵察機.equals(item)) {
                itemScore *= 2.00D;
            } else if (SlotItemType.水上爆撃機.equals(item)) {
                itemScore *= 1.78D;
            } else if (SlotItemType.小型電探.equals(item)) {
                itemScore *= 1.00D;
            } else if (SlotItemType.大型電探.equals(item) || SlotItemType.大型電探II.equals(item)) {
                itemScore *= 0.99D;
            } else if (SlotItemType.探照灯.equals(item)) {
                itemScore *= 0.91D;
            } else {
                itemScore *= 0;
            }
            return itemScore;
        };
        // 装備索敵スコア
        double itemScore = ships.stream()
                .flatMap(e -> getSlotitemMst(e))
                .mapToDouble(mapper)
                .sum();
        // 艦娘索敵スコア
        double shipScore = ships.stream()
                .mapToDouble(e -> e.getSakuteki().get(0) - getSlotitemMst(e)
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
     * 判定式(33)
     *
     * @param ships 艦娘達
     * @return 判定式(33)
     */
    public static double decision33(List<Ship> ships) {
        // 装備マスタ
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        // 装備
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        // 艦娘から装備を取り出すFunction
        Function<Ship, Stream<SlotItem>> toSlotItem = ship -> {
            return ship.getSlot()
                    .stream()
                    .map(itemMap::get)
                    .filter(Objects::nonNull);
        };

        //  索敵スコア＝(装備倍率×装備索敵値)の和＋√(各艦娘の素索敵)の和－[0.4×司令部レベル(端数切り上げ)]＋2×(6－出撃艦数)

        // 裝備係数(索敵)×(裝備索敵値+改修係数(索敵)×√★)の和
        double itemView = ships.stream()
                .flatMap(toSlotItem)
                .mapToDouble(e -> {
                    SlotitemMst mst = itemMstMap.get(e.getSlotitemId());
                    if (mst != null) {
                        SlotItemType type = SlotItemType.toSlotItemType(mst);
                        // 装備係数
                        double ic = VIEW_COEFFICIENT.getOrDefault(type, 0D);
                        // (裝備索敵値+改修係数(索敵)×√★)
                        double v = mst.getSaku() + LV_COEFFICIENT.getOrDefault(type, 0D) * Math.sqrt(e.getLevel());

                        return ic * v;
                    }
                    return 0;
                })
                .sum();
        // √(各艦娘の素索敵)の和
        double shipView = ships.stream()
                .mapToDouble(e -> e.getSakuteki().get(0) - getSlotitemMst(e)
                        .mapToInt(SlotitemMst::getSaku)
                        .sum())
                .map(Math::sqrt)
                .sum();
        // [0.4×司令部レベル(端数切り上げ)]
        double levelScore = Math.ceil(Basic.get().getLevel() * 0.4D);
        // 2×(6－出撃艦数)
        double fleetScore = 2 * (6 - ships.size());

        return itemView + shipView - levelScore + fleetScore;
    }

    /**
     * 触接開始率
     * 参考 http://wikiwiki.jp/kancolle/?%B9%D2%B6%F5%C0%EF
     * @param ships 艦娘達
     * @return 触接開始率
     */
    public static double touchPlaneStartProbability(List<Ship> ships) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        // 艦娘からスロット毎に装備を取り出す
        Function<Ship, Stream<Pair<SlotitemMst, Integer>>> onSlotItem = ship -> {
            List<Integer> slot = ship.getSlot();
            List<Integer> onslot = ship.getOnslot();
            List<Pair<SlotitemMst, Integer>> pair = new ArrayList<>();

            for (int i = 0, l = slot.size(); i < l; i++) {
                SlotItem item = itemMap.get(slot.get(i));
                if (item != null) {
                    SlotitemMst mst = itemMstMap.get(item.getSlotitemId());
                    if (mst != null) {
                        pair.add(new Pair<>(mst, onslot.get(i)));
                    }
                }
            }
            return pair.stream();
        };
        // 対象となる艦載機は水上偵察機(水偵)・艦上偵察機(艦偵)・大型飛行艇(二式大艇)の3種
        Predicate<Pair<SlotitemMst, Integer>> filter = pair -> {
            SlotitemMst mst = pair.getKey();
            return SlotItemType.艦上偵察機.equals(mst)
                    || SlotItemType.艦上偵察機II.equals(mst)
                    || SlotItemType.水上偵察機.equals(mst)
                    || SlotItemType.大型飛行艇.equals(mst);
        };
        // 各スロットごとの{0.04 × 艦載機の索敵値 × √(搭載数)}を計算
        ToDoubleFunction<Pair<SlotitemMst, Integer>> calc = pair -> 0.04D * pair.getKey().getSaku()
                * Math.sqrt(pair.getValue());

        return ships.stream()
                .flatMap(onSlotItem)
                .filter(filter)
                .mapToDouble(calc)
                .sum();
    }

    /**
     * 装備定義を取得します
     *
     * @param chara キャラクター
     * @return 装備定義のリスト
     */
    private static Stream<SlotitemMst> getSlotitemMst(Chara chara) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        return chara.getSlot()
                .stream()
                .map(itemMap::get)
                .filter(Objects::nonNull)
                .map(SlotItem::getSlotitemId)
                .map(itemMstMap::get)
                .filter(Objects::nonNull);
    }

    /**
     * キャラクターのHP割合
     * @param chara キャラクター
     * @return HP割合
     */
    private static double hpPer(Chara chara) {
        return (double) chara.getNowhp() / (double) chara.getMaxhp();
    }

    /**
     * Key-Value Pair
     *
     * @param <K> Key Type
     * @param <V> Value Type
     */
    private static final class Pair<K, V> {

        private K key;

        private V value;

        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        private K getKey() {
            return this.key;
        }

        private V getValue() {
            return this.value;
        }
    }
}
