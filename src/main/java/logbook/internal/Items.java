package logbook.internal;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import logbook.Messages;
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
    private static final ReferenceCache<String, Image> CACHE = new ReferenceCache<>(50);

    /** 装備アイコンサイズ */
    private static final int ITEM_ICON_SIZE = 45;

    private Items() {
    }

    /**
     * 装備の名前を取得します
     *
     * @param item 装備
     * @return 装備の名前
     */
    public static String name(SlotItem item) {
        return Items.slotitemMst(item)
                .map(mst -> {

                    StringBuilder text = new StringBuilder(mst.getName());

                    text.append(Optional.ofNullable(item.getAlv())
                            .map(alv -> Messages.getString("item.alv", alv)) //$NON-NLS-1$
                            .orElse(""));
                    text.append(Optional.ofNullable(item.getLevel())
                            .filter(lv -> lv > 0)
                            .map(lv -> Messages.getString("item.level", lv)) //$NON-NLS-1$
                            .orElse(""));
                    return text.toString();
                }).orElse("");
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
        if (item != null) {
            int key = item.getType().get(3);
            return itemIcon(key, true);
        }
        return defaultItemIcon();
    }

    /**
     * 装備の画像を取得します
     *
     * @param item 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image borderedItemImageByType(int type3) throws IllegalStateException {
        return itemIcon(type3, true);
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
        if (item != null) {
            int type3 = item.getType().get(3);
            return itemIcon(type3, false);
        }
        return defaultItemIcon();
    }

    /**
     * 装備の画像を取得します
     *
     * @param type3 装備定義
     * @return 装備の画像(36x36)
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    public static Image itemImageByType(int type3) throws IllegalStateException {
        return itemIcon(type3, false);
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
     * 装備アイコンを返します
     *
     * @param type 装備
     * @param slot スロット背景
     * @return 装備アイコン
     * @throws IllegalStateException このメソッドがJavaFXアプリケーション・スレッド以外のスレッドで呼び出された場合
     */
    private static Image itemIcon(int type, boolean slot) {
        Image image = optimizeItemIcon(type);
        if (slot) {
            return CACHE.get("slotItemIcon#" + type, key -> {
                double width = image.getWidth();
                double height = image.getHeight();
                Canvas canvas = new Canvas(width, height);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.rgb(44, 58, 59));
                gc.fillOval(2, 2, width - 2, height - 2);
                gc.drawImage(image, 0, 0);
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
                return canvas.snapshot(sp, null);
            });
        }
        return image;
    }

    /**
     * 装備アイコンを調節します
     *
     * @param p 装備アイコンへのパス
     * @return 装備アイコン
     */
    private static Image optimizeItemIcon(int type) {
        Path dir = Paths.get(AppConfig.get().getResourcesDir());
        Path p = dir.resolve(Paths.get("common", "common_icon_weapon/common_icon_weapon_id_" + type + ".png"));

        return CACHE.get(p.toUri().toString(), url -> {
            Image image = new Image(url);
            if (image.isError()) {
                return defaultItemIcon();
            }
            double width = image.getWidth();
            double height = image.getHeight();

            if (width != ITEM_ICON_SIZE) {
                double x = (int) ((ITEM_ICON_SIZE - width) / 2);
                double y = (int) ((ITEM_ICON_SIZE - height) / 2);

                Canvas canvas = new Canvas(ITEM_ICON_SIZE, ITEM_ICON_SIZE);
                GraphicsContext gc = canvas.getGraphicsContext2D();

                gc.drawImage(image, x, y);
                SnapshotParameters sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);

                return canvas.snapshot(sp, null);
            }
            return image;
        });
    }

    /**
     * デフォルト装備アイコン
     *
     * @return デフォルト装備アイコン
     */
    private static Image defaultItemIcon() {
        return CACHE.get("defaultItemIcon", key -> {
            return new WritableImage(ITEM_ICON_SIZE, ITEM_ICON_SIZE);
        });
    }

    /**
     * 航空機かどうかを調べます
     *
     * @param item 装備
     * @return 航空機かどうか
     */
    public static boolean isAircraft(SlotItem item) {
        return Items.slotitemMst(item)
                .map(Items::isAircraft)
                .orElse(false);
    }

    /**
     * 航空機かどうかを調べます
     *
     * @param item 装備
     * @return 航空機かどうか
     */
    public static boolean isAircraft(SlotitemMst item) {
        switch (SlotItemType.toSlotItemType(item)) {
        case 艦上戦闘機:
        case 艦上爆撃機:
        case 艦上攻撃機:
        case 水上爆撃機:
        case オートジャイロ:
        case 対潜哨戒機:
        case 水上戦闘機:
        case 陸上攻撃機:
        case 局地戦闘機:
        case 噴式戦闘機:
        case 噴式戦闘爆撃機:
        case 噴式攻撃機:

        case 艦上偵察機:
        case 艦上偵察機II:
        case 水上偵察機:
        case 大型飛行艇:
        case 噴式偵察機:
        case 陸上偵察機:
            return true;
        default:
            return false;
        }
    }

    /**
     * 戦闘に参加する航空機かどうかを調べます
     *
     * @param item 装備
     * @return 戦闘に参加する航空機かどうか
     */
    public static boolean isCombatAircraft(SlotItem item) {
        return Items.slotitemMst(item)
                .map(Items::isCombatAircraft)
                .orElse(false);
    }

    /**
     * 戦闘に参加する航空機かどうかを調べます
     *
     * @param item 装備
     * @return 戦闘に参加する航空機かどうか
     */
    public static boolean isCombatAircraft(SlotitemMst item) {
        switch (SlotItemType.toSlotItemType(item)) {
        case 艦上戦闘機:
        case 艦上爆撃機:
        case 艦上攻撃機:
        case 水上爆撃機:
        case オートジャイロ:
        case 対潜哨戒機:
        case 水上戦闘機:
        case 陸上攻撃機:
        case 局地戦闘機:
        case 噴式戦闘機:
        case 噴式戦闘爆撃機:
        case 噴式攻撃機:
            return true;
        default:
            return false;
        }
    }

    /**
     * 偵察機かどうかを調べます
     *
     * @param item 装備
     * @return 偵察機かどうか
     */
    public static boolean isReconAircraft(SlotItem item) {
        return Items.slotitemMst(item)
                .map(Items::isReconAircraft)
                .orElse(false);
    }

    /**
     * 偵察機かどうかを調べます
     *
     * @param item 装備
     * @return 偵察機かどうか
     */
    public static boolean isReconAircraft(SlotitemMst item) {
        switch (SlotItemType.toSlotItemType(item)) {
        case 艦上偵察機:
        case 艦上偵察機II:
        case 水上偵察機:
        case 大型飛行艇:
        case 噴式偵察機:
        case 陸上偵察機:
            return true;
        default:
            return false;
        }
    }
}
