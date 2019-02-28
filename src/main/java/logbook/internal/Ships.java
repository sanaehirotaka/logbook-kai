package logbook.internal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.Messages;
import logbook.bean.AppCondition;
import logbook.bean.Basic;
import logbook.bean.Chara;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.bean.Stype;
import logbook.bean.StypeCollection;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 艦娘に関するメソッドを集めたクラス
 *
 */
public class Ships {

    /** 小破(75%) */
    public static final double SLIGHT_DAMAGE = 0.75D;
    /** 中破(50%) */
    public static final double HALF_DAMAGE = 0.5D;
    /** 大破(25%) */
    public static final double BADLY_DAMAGE = 0.25D;
    /** 疲労赤色 */
    public static final int RED = 19;
    /** 疲労オレンジ色 */
    public static final int ORANGE = 29;
    /** キラキラ1段階 */
    public static final int DARK_GREEN = 50;
    /** キラキラ2段階 */
    public static final int GREEN = 53;

    /** 索敵係数 */
    private static final Map<SlotItemType, Double> VIEW_COEFFICIENT = new EnumMap<>(SlotItemType.class);
    /** 改修係数 */
    private static final Map<SlotItemType, Double> LV_COEFFICIENT = new EnumMap<>(SlotItemType.class);
    /** 対空係数 */
    private static final Map<SlotItemType, Double> AA_COEFFICIENT = new EnumMap<>(SlotItemType.class);
    /** 対空改修係数 */
    private static final Map<SlotItemType, Double> AALV_COEFFICIENT = new EnumMap<>(SlotItemType.class);

    static {
        // 索敵係数
        // 小口径主砲：0.6
        VIEW_COEFFICIENT.put(SlotItemType.小口径主砲, 0.6D);
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
        // 噴式戦闘爆撃機：0.6
        VIEW_COEFFICIENT.put(SlotItemType.噴式戦闘爆撃機, 0.6D);
        // 潜水艦装備：0.6
        VIEW_COEFFICIENT.put(SlotItemType.潜水艦装備, 0.6D);

        // 改修係数
        // 小型電探：1.25
        LV_COEFFICIENT.put(SlotItemType.小型電探, 1.25D);
        // 大型電探：1.25
        LV_COEFFICIENT.put(SlotItemType.大型電探, 1.25D);
        // 水上偵察機：1.2
        LV_COEFFICIENT.put(SlotItemType.水上偵察機, 1.2D);

        // 対空係数
        // 対空機銃：6
        AA_COEFFICIENT.put(SlotItemType.対空機銃, 6D);
        // 高角砲・高射装置：4
        AA_COEFFICIENT.put(SlotItemType.小口径主砲, 4D);
        AA_COEFFICIENT.put(SlotItemType.副砲, 4D);
        AA_COEFFICIENT.put(SlotItemType.高射装置, 4D);
        // 対空電探：3
        AA_COEFFICIENT.put(SlotItemType.小型電探, 3D);
        AA_COEFFICIENT.put(SlotItemType.大型電探, 3D);
        AA_COEFFICIENT.put(SlotItemType.大型電探II, 3D);

        // 対空改修係数
        // 機銃：4
        AALV_COEFFICIENT.put(SlotItemType.対空機銃, 4D);
        // 高角砲・高射装置：2
        AALV_COEFFICIENT.put(SlotItemType.小口径主砲, 2D);
        AALV_COEFFICIENT.put(SlotItemType.副砲, 2D);
        AALV_COEFFICIENT.put(SlotItemType.高射装置, 2D);
    }

    private Ships() {
    }

    /**
     * 所属している艦隊を取得します。
     *
     * @param ship 艦娘
     * @return 所属している艦隊
     */
    public static Optional<DeckPort> deckPort(Ship ship) {
        for (Entry<Integer, DeckPort> entry : DeckPortCollection.get().getDeckPortMap().entrySet()) {
            if (entry.getValue().getShip().contains(ship.getId())) {
                return Optional.of(entry.getValue());
            }
        }
        return Optional.empty();
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
     * @param escape 退避艦ID
     * @return 退避状態の場合true
     */
    public static boolean isEscape(Ship ship, Set<Integer> escape) {
        if (escape != null) {
            return escape.contains(ship.getId());
        }
        return false;
    }

    /**
     * 艦娘が退避状態か判定します
     *
     * @param ship 退避
     * @return 退避状態の場合true
     */
    public static boolean isEscape(Ship ship) {
        return isEscape(ship, AppCondition.get().getEscape());
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
        return ShipImage.get(chara, false, true, null, null);
    }

    /**
     * キャラクターの画像を取得します(装備画像付き)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemImage(Chara chara) throws IllegalStateException {
        return shipWithItemImage(chara, SlotItemCollection.get().getSlotitemMap(), AppCondition.get().getEscape());
    }

    /**
     * キャラクターの画像を取得します(装備画像付き)
     *
     * @param chara キャラクター
     * @param itemMap 装備Map
     * @param escape 退避艦ID
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemImage(Chara chara,
            Map<Integer, SlotItem> itemMap, Set<Integer> escape) throws IllegalStateException {
        return ShipImage.get(chara, true, true, itemMap, escape);
    }

    /**
     * キャラクターの画像を取得します(装備画像付き、状態バナー無し)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemWithoutStateBannerImage(Chara chara) throws IllegalStateException {
        return shipWithItemWithoutStateBannerImage(chara,
                SlotItemCollection.get().getSlotitemMap(), AppCondition.get().getEscape());
    }

    /**
     * キャラクターの画像を取得します(装備画像付き、状態バナー無し)
     *
     * @param chara キャラクター
     * @param itemMap 装備Map
     * @param escape 退避艦ID
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image shipWithItemWithoutStateBannerImage(Chara chara,
            Map<Integer, SlotItem> itemMap, Set<Integer> escape) throws IllegalStateException {
        return ShipImage.get(chara, true, false, itemMap, escape);
    }

    /**
     * キャラクターの画像を取得します(装飾無し、バックグラウンドでのロード)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image getBackgroundLoading(Chara chara) throws IllegalStateException {
        Image img = ShipImage.getBackgroundLoading(chara);
        if (img != null) {
            return img;
        }
        Canvas canvas = new Canvas(160, 40);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        return canvas.snapshot(sp, null);
    }

    /**
     * 艦娘の補給ゲージを取得します
     *
     * @param ship 艦娘
     * @return 補給ゲージ
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image supplyGaugeImage(Ship ship) throws IllegalStateException {
        return ShipImage.getSupplyGauge(ship);
    }

    /**
     * キャラクターに対応する艦船を取得します
     *
     * @param chara キャラクター
     * @return 艦船
     */
    public static Optional<ShipMst> shipMst(Chara chara) {
        ShipMst mst = null;
        if (chara != null) {
            mst = ShipMstCollection.get()
                    .getShipMap()
                    .get(chara.getShipId());
        }
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
                .filter(i -> i.is(SlotItemType.艦上攻撃機, SlotItemType.艦上爆撃機))
                .findAny()
                .isPresent();
        // 火力
        int karyoku = ship.getKaryoku().get(0);

        if (isPasedoCarrier || ship.is(ShipType.軽空母, ShipType.正規空母, ShipType.装甲空母)) {
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

        // 艦娘制空値
        int value = 0;

        for (int i = 0; i < ship.getSlot().size(); i++) {
            // スロット内制空値
            double local = 0;

            int onslot = ship.getOnslot().get(i);
            if (onslot <= 0)
                continue;
            SlotItem item = itemMap.get(ship.getSlot().get(i));
            if (item == null)
                continue;
            SlotitemMst itemMst = itemMstMap.get(item.getSlotitemId());

            // 制空状態に関係するのは対空値を持つ艦戦、艦攻、艦爆、水爆、水戦のみ
            if (itemMst.is(SlotItemType.艦上戦闘機, SlotItemType.艦上攻撃機, SlotItemType.艦上爆撃機, SlotItemType.水上爆撃機,
                    SlotItemType.水上戦闘機, SlotItemType.噴式戦闘爆撃機)) {
                // 対空値
                double tyku = itemMst.getTyku();
                tyku += airSuperiorityTykuAdditional(itemMst, item);

                // 制空値
                local += tyku * Math.sqrt(onslot);
                // 制空ボーナス値
                local += airSuperiorityBonus(itemMst, item);
            }
            value += local;
        }
        return value;
    }

    /**
     * 制空加算(改修効果)
     *
     * @param itemMst 装備定義
     * @param item 装備
     * @return 加算される制空値
     */
    public static double airSuperiorityTykuAdditional(SlotitemMst itemMst, SlotItem item) {
        if (itemMst.is(SlotItemType.艦上戦闘機, SlotItemType.水上戦闘機, SlotItemType.局地戦闘機)) {
            return Optional.ofNullable(item.getLevel())
                    .map(level -> 0.2D * level)
                    .orElse(0D);
        }
        if (itemMst.is(SlotItemType.艦上爆撃機)) {
            return Optional.ofNullable(item.getLevel())
                    .map(level -> 0.25D * level)
                    .orElse(0D);
        }
        return 0D;
    }

    /**
     * 制空ボーナス値
     *
     * @param itemMst 装備定義
     * @param item 装備
     * @return 制空ボーナス値
     */
    public static double airSuperiorityBonus(SlotitemMst itemMst, SlotItem item) {
        if (item.getAlv() != null) {
            // 上昇制空値＝内部熟練ボーナス＋制空ボーナス(艦戦/水爆)
            // 内部熟練ボーナス＝√(内部熟練度/10)

            // 熟練ボーナス
            double bonus = Math.sqrt(skillLevel(item.getAlv()) / 10D);
            // 制空ボーナス
            if (itemMst.is(SlotItemType.艦上戦闘機, SlotItemType.水上戦闘機, SlotItemType.局地戦闘機)) {
                bonus += skillBonus1(item.getAlv());
            } else if (itemMst.is(SlotItemType.水上爆撃機)) {
                bonus += skillBonus2(item.getAlv());
            }
            return bonus;
        }
        return 0D;
    }

    /**
     * 加重対空値
     *
     * @param ship 艦娘
     * @return 加重対空値
     */
    public static double weightAntiAircraft(Ship ship) {
        // 装備マスタ
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get()
                .getSlotitemMap();
        // 装備
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();

        // 装備から加重対空値を取り出すFunction
        ToDoubleFunction<SlotItem> weightAA = item -> {
            SlotitemMst mst = itemMstMap.get(item.getSlotitemId());
            if (mst == null)
                return 0D;

            boolean isGun = Optional.ofNullable(mst)
                    .map(type -> type.is(SlotItemType.小口径主砲, SlotItemType.副砲))
                    .orElse(false);
            if (mst.getType().get(3) != 16 && isGun)
                return 0D;

            SlotItemType type = SlotItemType.toSlotItemType(mst);
            int taikuu = mst.getTyku();

            // 装備係数× 装備対空値
            double ic = AA_COEFFICIENT.getOrDefault(type, 0D) * taikuu;
            // 改修係数(索敵)
            double aalvCoefficient = AALV_COEFFICIENT.getOrDefault(type, 0D);
            if (aalvCoefficient == 2D && taikuu > 7) {
                aalvCoefficient = 3D;
            }
            // 改修係数(索敵)×√★
            double v = aalvCoefficient * Math.sqrt(item.getLevel());

            return ic + v;
        };

        // 装備加重対空値＝装備係数(対空)×装備対空値+改修係数(対空)×√★
        double itemWeightAA = Stream.concat(ship.getSlot().stream(), Stream.of(ship.getSlotEx()))
                .map(itemMap::get)
                .filter(Objects::nonNull)
                .mapToDouble(weightAA)
                .sum();

        int itemTyku = getSlotitemMst(ship).mapToInt(SlotitemMst::getTyku).sum();

        int shipAA = ship.getTaiku().get(0) - itemTyku;
        return itemWeightAA + (double) shipAA;
    }

    /**
     * 対空噴進弾幕発動率
     *
     * @param ship 艦娘
     * @return 噴進弾幕発動率
     */
    public static double rocketBarrageActivationRate(Ship ship) {
        List<SlotitemMst> items = getSlotitemMst(ship).collect(Collectors.toList());
        long rocketCount = items.stream()
                .filter(e -> e.getId() == 274) // 噴進砲改二
                .count();
        if (rocketCount == 0)
            return 0D;

        boolean canRocketBarrage = ship.is(ShipType.水上機母艦,
                ShipType.航空巡洋艦,
                ShipType.航空戦艦,
                ShipType.軽空母,
                ShipType.正規空母,
                ShipType.装甲空母);
        if (!canRocketBarrage)
            return 0D;

        double bonus = 0D;

        if (rocketCount > 1)
            bonus += 15D;

        Integer shipMstId = shipMst(ship)
                .map(ShipMst::getId)
                .orElse(0);
        List<Integer> iseClass = new ArrayList<Integer>();
        iseClass.add(82); // 伊勢改
        iseClass.add(553); // 伊勢改二
        iseClass.add(88); // 日向改

        if (iseClass.contains(shipMstId))
            bonus += 25D;

        int antiAircraft = (int) Math.floor(weightAntiAircraft(ship));
        if (antiAircraft % 2 != 0)
            antiAircraft -= 1;

        double baseActivationRate = (antiAircraft + ship.getLucky().get(0)) / 282D;
        double activationRate = Math.floor(baseActivationRate * 1000) / 10;

        return activationRate + bonus;
    }

    /**
     * 判定式(33)
     *
     * @param ships 艦娘達
     * @param branchCoefficient 分岐点係数
     * @return 判定式(33)
     */
    public static Decision33 decision33(List<Ship> ships, double branchCoefficient) {
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

        // (装備係数(索敵)×(装備索敵値+改修係数(索敵)×√★))の和
        double itemView = ships.stream()
                .flatMap(toSlotItem)
                .mapToDouble(e -> {
                    SlotitemMst mst = itemMstMap.get(e.getSlotitemId());
                    if (mst != null) {
                        SlotItemType type = SlotItemType.toSlotItemType(mst);
                        // 装備係数
                        double ic = VIEW_COEFFICIENT.getOrDefault(type, 0D);
                        // (装備索敵値+改修係数(索敵)×√★)
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

        return new Decision33(branchCoefficient, itemView, shipView, levelScore, fleetScore);
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
            return mst.is(SlotItemType.艦上偵察機, SlotItemType.艦上偵察機II, SlotItemType.水上偵察機, SlotItemType.大型飛行艇);
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
     * 触接攻撃力補正
     *
     * @param item 装備(偵察機/艦攻)
     * @return 0.0以上の数値
     */
    public static double touchPlaneAttackCompensation(SlotitemMst item) {
        if (item != null) {
            Integer houm = item.getHoum();
            if (houm != null) {
                switch (houm) {
                case 0:
                case 1:
                    return 1.12D;
                case 2:
                    return 1.17D;
                default:
                    return 1.2D;
                }
            }
        }
        return 0;
    }

    /**
     * キャラクターの名前を取得します
     *
     * @param chara キャラクター
     * @return 名前
     */
    public static String toName(Chara chara) {
        if (chara == null) {
            return "";
        }
        if (chara.isShip() || chara.isFriend()) {
            // 艦娘
            return Messages.getString("ship.name", shipMst(chara)
                    .map(ShipMst::getName)
                    .orElse(""), chara.getLv());
        } else {
            // 敵艦
            return Messages.getString("ship.name", shipMst(chara)
                    .map(mst -> {
                        String yomi = mst.getYomi();
                        if (yomi == null || "-".equals(yomi) || yomi.isEmpty()) {
                            return mst.getName();
                        } else {
                            return mst.getName() + yomi;
                        }
                    })
                    .orElse(""), chara.getLv());
        }
    }

    /**
     * 速力のテキスト表記
     *
     * @param soku soku
     * @return 速力
     */
    public static String sokuText(Integer soku) {
        if (soku == null)
            return "";
        switch (soku) {
        case 0:
            return "基地";
        case 5:
            return "低速";
        case 10:
            return "高速";
        case 15:
            return "高速+";
        case 20:
            return "最速";
        }
        return "";
    }

    /**
     * 射程のテキスト表記
     *
     * @param leng leng
     * @return
     */
    public static String lengText(Integer leng) {
        if (leng == null)
            return "";
        switch (leng) {
        case 1:
            return "短";
        case 2:
            return "中";
        case 3:
            return "長";
        case 4:
            return "超長";
        }
        return "";
    }

    /**
     * 内部熟練度を取得します
     *
     * @param level 熟練度
     * @return 内部熟練度
     */
    private static int skillLevel(int level) {
        switch (level) {
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
    }

    /**
     * 熟練度ボーナス(艦戦)
     *
     * @param skill 熟練度
     * @return 熟練度ボーナス
     */
    private static int skillBonus1(int skill) {
        switch (skill) {
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
    }

    /**
     * 熟練度ボーナス(水爆)
     *
     * @param skill 熟練度
     * @return 熟練度ボーナス
     */
    private static int skillBonus2(int skill) {
        switch (skill) {
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
        Stream<Integer> stream = chara.getSlot().stream();
        if (chara.isShip()) {
            stream = Stream.concat(stream, Stream.of(chara.asShip().getSlotEx()));
        }
        return stream.map(itemMap::get)
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

    /**
     * 判定式(33)
     */
    @Data
    @AllArgsConstructor
    public static class Decision33 {

        /** 分岐点係数 */
        private double branchCoefficient;

        /** 装備索敵 */
        private double itemView;

        /** 艦娘索敵 */
        private double shipView;

        /** 司令部スコア */
        private double levelScore;

        /** 艦隊スコア */
        private double fleetScore;

        public double get() {
            return (this.branchCoefficient * this.itemView) + this.shipView - this.levelScore + this.fleetScore;
        }
    }
}
