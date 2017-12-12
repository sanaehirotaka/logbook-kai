package logbook.internal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.bean.AppConfig;
import logbook.bean.SlotItem;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;

/**
 * 装備に関するメソッドを集めたクラス
 *
 */
public class Items {

    /** 画像キャッシュ */
    private static final ReferenceCache<String, Image> CACHE = new ReferenceCache<>(100);

    /** 装備アイコンサイズ */
    private static final int ITEM_ICON_SIZE = 36;

    /** 装備アイコンファイル名 */
    private static final Map<Integer, String> ITEM_MAP = new HashMap<Integer, String>();

    /** 装備アイコンファイル名 */
    private static final Map<Integer, String> BORDERED_ITEM_MAP = new HashMap<Integer, String>();

    static {
        ResourceBundle itemBundle = ResourceBundle.getBundle("logbook.itemName");
        Collections.list(itemBundle.getKeys()).stream()
                .forEach(k -> ITEM_MAP.put(Integer.valueOf(k), itemBundle.getString(k)));

        ResourceBundle borderedItemBundle = ResourceBundle.getBundle("logbook.borderedItemName");
        Collections.list(borderedItemBundle.getKeys()).stream()
                .forEach(k -> BORDERED_ITEM_MAP.put(Integer.valueOf(k), borderedItemBundle.getString(k)));
    }

    private Items() {
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImage(SlotItem item) throws IllegalStateException {
        return borderedItemImage(slotitemMst(item).orElse(null));
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImage(SlotitemMst item) throws IllegalStateException {
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
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image itemImage(SlotItem item) throws IllegalStateException {
        return itemImage(slotitemMst(item).orElse(null));
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image itemImage(SlotitemMst item) throws IllegalStateException {
        Path path = null;
        if (item != null) {
            path = getItemImagePath(item);
        }
        return optimizeItemIcon(path);
    }

    /**
     * 装備に対応する装備定義を取得します
     *
     * @param item 装備
     * @return 装備定義
     */
    public static Optional<SlotitemMst> slotitemMst(SlotItem item) {
        SlotitemMst mst = null;
        if (item != null) {
            mst = SlotitemMstCollection.get()
                    .getSlotitemMap()
                    .get(item.getSlotitemId());
        }
        return Optional.ofNullable(mst);
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
    private static Path getItemImagePath(SlotitemMst item) {
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
    private static Path getBorderedItemImagePath(SlotitemMst item) {
        String name = getBorderedItemImageName(item);
        if (name != null) {
            return getItemResourcePathDir().resolve(name);
        } else {
            return null;
        }
    }

    private static String getItemImageName(SlotitemMst item) {
        int key = item.getType().get(3);
        return ITEM_MAP.get(key);
    }

    private static String getBorderedItemImageName(SlotitemMst item) {
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
                Image image = CACHE.get(p.toUri().toString(), Image::new);

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
