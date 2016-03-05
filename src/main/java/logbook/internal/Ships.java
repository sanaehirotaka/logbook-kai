package logbook.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import javafx.scene.image.Image;
import logbook.bean.Ship;
import logbook.bean.ShipDescription;
import logbook.bean.ShipDescriptionCollection;

/**
 * 艦娘に関するメソッドを集めたクラス
 *
 */
public class Ships {

    /** 小破(75%) */
    private static final BigDecimal SLIGHT_DAMAGE = BigDecimal.valueOf(0.75D);

    /** 中破(50%) */
    private static final BigDecimal HALF_DAMAGE = BigDecimal.valueOf(0.5D);

    /** 大破(25%) */
    private static final BigDecimal BADLY_DAMAGE = BigDecimal.valueOf(0.25D);

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
        return hpPer(ship).compareTo(SLIGHT_DAMAGE) > 0;
    }

    /**
     * 艦娘が小破状態か判定します
     *
     * @param ship 艦娘
     * @return 小破状態の場合true
     */
    public static boolean isSlightDamage(Ship ship) {
        BigDecimal per = hpPer(ship);
        return per.compareTo(SLIGHT_DAMAGE) <= 0 && per.compareTo(HALF_DAMAGE) > 0;
    }

    /**
     * 艦娘が中破状態か判定します
     *
     * @param ship 艦娘
     * @return 中破状態の場合true
     */
    public static boolean isHalfDamage(Ship ship) {
        BigDecimal per = hpPer(ship);
        return per.compareTo(HALF_DAMAGE) <= 0 && per.compareTo(BADLY_DAMAGE) > 0;
    }

    /**
     * 艦娘が大破状態か判定します
     *
     * @param ship 艦娘
     * @return 大破状態の場合true
     */
    public static boolean isBadlyDamage(Ship ship) {
        return hpPer(ship).compareTo(BADLY_DAMAGE) <= 0;
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
    public static Optional<ShipDescription> shipDescription(Ship ship) {
        ShipDescription desc = ShipDescriptionCollection.get()
                .getShipMap()
                .get(ship.getShipId());
        return Optional.ofNullable(desc);
    }

    /**
     * 艦娘のHP割合
     * @param ship 艦娘
     * @return HP割合
     */
    private static BigDecimal hpPer(Ship ship) {
        return BigDecimal.valueOf(ship.getNowhp())
                .divide(BigDecimal.valueOf(ship.getMaxhp()), 3, RoundingMode.HALF_EVEN);
    }
}
