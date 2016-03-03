package logbook.internal;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.bean.AppConfig;
import logbook.bean.Ship;
import logbook.bean.ShipDescription;
import logbook.bean.ShipDescriptionCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotitemDescription;
import logbook.bean.SlotitemDescriptionCollection;

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

    /** 装備アイコンファイル名 */
    private static final Map<Integer, String> ITEM_MAP = new HashMap<Integer, String>();

    /** 装備アイコンファイル名 */
    private static final Map<Integer, String> BORDERED_ITEM_MAP = new HashMap<Integer, String>();

    static {
        ITEM_MAP.put(1, "74.png");
        ITEM_MAP.put(2, "76.png");
        ITEM_MAP.put(3, "78.png");
        ITEM_MAP.put(4, "80.png");
        ITEM_MAP.put(5, "82.png");
        ITEM_MAP.put(6, "84.png");
        ITEM_MAP.put(7, "86.png");
        ITEM_MAP.put(8, "88.png");
        ITEM_MAP.put(9, "90.png");
        ITEM_MAP.put(10, "92.png");
        ITEM_MAP.put(11, "94.png");
        ITEM_MAP.put(12, "96.png");
        ITEM_MAP.put(13, "98.png");
        ITEM_MAP.put(14, "100.png");
        ITEM_MAP.put(15, "102.png");
        ITEM_MAP.put(16, "104.png");
        ITEM_MAP.put(17, "106.png");
        ITEM_MAP.put(18, "108.png");
        ITEM_MAP.put(19, "110.png");
        ITEM_MAP.put(20, "112.png");
        ITEM_MAP.put(21, "114.png");
        ITEM_MAP.put(22, "116.png");
        ITEM_MAP.put(23, "118.png");
        ITEM_MAP.put(24, "120.png");
        ITEM_MAP.put(25, "122.png");
        ITEM_MAP.put(26, "124.png");
        ITEM_MAP.put(27, "126.png");
        ITEM_MAP.put(28, "128.png");
        ITEM_MAP.put(29, "130.png");
        ITEM_MAP.put(30, "132.png");
        ITEM_MAP.put(31, "134.png");
        ITEM_MAP.put(32, "136.png");
        ITEM_MAP.put(33, "138.png");
        ITEM_MAP.put(34, "140.png");
        ITEM_MAP.put(35, "142.png");

        BORDERED_ITEM_MAP.put(1, "3.png");
        BORDERED_ITEM_MAP.put(2, "5.png");
        BORDERED_ITEM_MAP.put(3, "7.png");
        BORDERED_ITEM_MAP.put(4, "9.png");
        BORDERED_ITEM_MAP.put(5, "11.png");
        BORDERED_ITEM_MAP.put(6, "13.png");
        BORDERED_ITEM_MAP.put(7, "15.png");
        BORDERED_ITEM_MAP.put(8, "17.png");
        BORDERED_ITEM_MAP.put(9, "19.png");
        BORDERED_ITEM_MAP.put(10, "21.png");
        BORDERED_ITEM_MAP.put(11, "23.png");
        BORDERED_ITEM_MAP.put(12, "25.png");
        BORDERED_ITEM_MAP.put(13, "27.png");
        BORDERED_ITEM_MAP.put(14, "29.png");
        BORDERED_ITEM_MAP.put(15, "31.png");
        BORDERED_ITEM_MAP.put(16, "33.png");
        BORDERED_ITEM_MAP.put(17, "35.png");
        BORDERED_ITEM_MAP.put(18, "37.png");
        BORDERED_ITEM_MAP.put(19, "39.png");
        BORDERED_ITEM_MAP.put(20, "41.png");
        BORDERED_ITEM_MAP.put(21, "43.png");
        BORDERED_ITEM_MAP.put(22, "45.png");
        BORDERED_ITEM_MAP.put(23, "47.png");
        BORDERED_ITEM_MAP.put(24, "49.png");
        BORDERED_ITEM_MAP.put(25, "51.png");
        BORDERED_ITEM_MAP.put(26, "53.png");
        BORDERED_ITEM_MAP.put(27, "55.png");
        BORDERED_ITEM_MAP.put(28, "57.png");
        BORDERED_ITEM_MAP.put(29, "59.png");
        BORDERED_ITEM_MAP.put(30, "61.png");
        BORDERED_ITEM_MAP.put(31, "63.png");
        BORDERED_ITEM_MAP.put(32, "65.png");
        BORDERED_ITEM_MAP.put(33, "67.png");
        BORDERED_ITEM_MAP.put(34, "69.png");
        BORDERED_ITEM_MAP.put(35, "71.png");
    }

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
        return borderedItemImage(slotitemDescription(item).orElse(null));
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImage(SlotitemDescription item) {
        Path path = null;
        if (item != null) {
            path = getBorderedItemImagePath(item);
        }
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
        return itemImage(slotitemDescription(item).orElse(null));
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
        Path path = null;
        if (item != null) {
            path = getItemImagePath(item);
        }
        return optimizeItemIcon(path);
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
     * 装備に対応する装備定義を取得します
     *
     * @param item 装備
     * @return 装備定義
     */
    public static Optional<SlotitemDescription> slotitemDescription(SlotItem item) {
        SlotitemDescription desc = SlotitemDescriptionCollection.get()
                .getSlotitemMap()
                .get(item.getSlotitemId());
        return Optional.ofNullable(desc);
    }

    /**
     * 装備リソースファイルのディレクトリを取得します。
     * @return 装備リソースファイルのディレクトリ
     */
    static Path getItemResourcePathDir() {
        return Paths.get(AppConfig.get().getResourcesDir(), "icons");
    }

    /**
     * 装備アイコン(背景無し)を取得します。
     * @param item 装備
     * @return 装備アイコン(背景無し)
     */
    private static Path getItemImagePath(SlotitemDescription item) {
        String name = getItemImageName(item);
        if (name != null) {
            return getItemResourcePathDir().resolve(name);
        } else {
            return null;
        }
    }

    /**
     * 装備アイコン(背景有り)を取得します。
     * @param item 装備
     * @return 装備アイコン(背景有り)
     */
    private static Path getBorderedItemImagePath(SlotitemDescription item) {
        String name = getBorderedItemImageName(item);
        if (name != null) {
            return getItemResourcePathDir().resolve(name);
        } else {
            return null;
        }
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

    private static String getItemImageName(SlotitemDescription item) {
        int key = item.getType().get(3);
        return ITEM_MAP.get(key);
    }

    private static String getBorderedItemImageName(SlotitemDescription item) {
        int key = item.getType().get(3);
        return BORDERED_ITEM_MAP.get(key);
    }

    /**
     * 装備アイコンを調節します
     *
     * @param p 装備アイコンへのパス
     * @return 装備アイコン
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    private static Image optimizeItemIcon(Path p) {
        if (p != null && Files.isReadable(p)) {
            try {
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
            } catch (Exception e) {
                return defaultItemIcon();
            }
        } else {
            return defaultItemIcon();
        }
    }

    /**
     * デフォルト装備アイコン
     *
     * @return デフォルト装備アイコン
     */
    private static Image defaultItemIcon() {
        Canvas canvas = new Canvas(ITEM_ICON_SIZE, ITEM_ICON_SIZE);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);

        return canvas.snapshot(sp, null);
    }
}
