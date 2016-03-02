package logbook.internal;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotitemDescription;

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

    /** 装備アイコンサイズ */
    private static final int ITEM_ICON_SIZE = 36;

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
        return ShipImage.get(ship);
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImage(SlotItem item) {
        return borderedItemImage(item.slotitemDescription());
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImage(SlotitemDescription item) {
        Path path = SlotitemDescription.getBorderedImagePath(item);

        return optimizeItemIcon(path);
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備
     * @return 装備の画像(36x36)
     * @throws IOException 装備アイコンの調節に失敗した場合
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image itemImage(SlotItem item) {
        return itemImage(item.slotitemDescription());
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IOException 装備アイコンの調節に失敗した場合
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image itemImage(SlotitemDescription item) {
        Path path = SlotitemDescription.getImagePath(item);

        return optimizeItemIcon(path);
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

    /**
     * 装備アイコンを調節します
     *
     * @param p 装備アイコンへのパス
     * @return 装備アイコン
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    private static Image optimizeItemIcon(Path p) {
        if (Files.isReadable(p)) {
            Image image = new Image(p.toUri().toString());

            double width = image.getWidth();
            double height = image.getHeight();

            if (width != ITEM_ICON_SIZE) {
                double x = (ITEM_ICON_SIZE - width) / 2;
                double y = (ITEM_ICON_SIZE - height) / 2;

                Canvas canvas = new Canvas(ITEM_ICON_SIZE, ITEM_ICON_SIZE);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                gc.drawImage(image, x, y);
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);

                return canvas.snapshot(sp, null);
            }
            return image;
        } else {
            Canvas canvas = new Canvas(ITEM_ICON_SIZE, ITEM_ICON_SIZE);
            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);

            return canvas.snapshot(sp, null);
        }
    }
}
