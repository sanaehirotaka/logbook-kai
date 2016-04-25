package logbook.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.bean.AppConfig;
import logbook.bean.Chara;
import logbook.bean.DeckPortCollection;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;

class ShipImage {

    /** 画像キャッシュ */
    private static final ReferenceCache<String, Image> CACHE = new ReferenceCache<>(200);

    /** 艦娘画像ファイル名(健在・小破) */
    private static final String[] NORMAL = { "1.jpg", "1.png" };

    /** 艦娘画像ファイル名(中破・大破) */
    private static final String[] DAMAGED = { "3.jpg", "3.png" };

    /** 小破バナーアイコン */
    private static final String MC_BANNER_ICON0 = "res.common.MCBannerIcon_0.png";

    /** 中破バナーアイコン */
    private static final String MC_BANNER_ICON1 = "res.common.MCBannerIcon_1.png";

    /** 大破バナーアイコン */
    private static final String MC_BANNER_ICON2 = "res.common.MCBannerIcon_2.png";

    /** 撃沈バナーアイコン */
    private static final String MC_BANNER_ICON3 = "res.common.MCBannerIcon_3.png";

    /** 修復バナーアイコン */
    private static final String MC_BANNER_ICON4 = "res.common.MCBannerIcon_4.png";

    /** 遠征バナーアイコン */
    private static final String MC_BANNER_ICON5 = "res.common.MCBannerIcon_5.png";

    /** 遠征バナーアイコン */
    private static final String MC_BANNER_ICON10 = "res.common.MCBannerIcon_10.png";

    /** 小破汚れ */
    private static final String MC_BANNER_SMOKE_IMG0 = "res.common.MCBannerSmokeImg_0.png";

    /** 中破汚れ */
    private static final String MC_BANNER_SMOKE_IMG1 = "res.common.MCBannerSmokeImg_1.png";

    /** 大破汚れ */
    private static final String MC_BANNER_SMOKE_IMG2 = "res.common.MCBannerSmokeImg_2.png";

    /** 疲労オレンジ背景 */
    private static final String MC_BANNER_VITAL_MASK0 = "res.common.MCBannerVitalMask_0.png";

    /** 疲労オレンジ顔 */
    private static final String MC_BANNER_VITAL_MASK1 = "res.common.MCBannerVitalMask_1.png";

    /** 疲労赤背景 */
    private static final String MC_BANNER_VITAL_MASK2 = "res.common.MCBannerVitalMask_2.png";

    /** 疲労赤顔 */
    private static final String MC_BANNER_VITAL_MASK3 = "res.common.MCBannerVitalMask_3.png";

    /** 小破バッチ */
    private static final Layer SLIGHT_DAMAGE_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON0));

    /** 中破バッチ */
    private static final Layer HALF_DAMAGE_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON1));

    /** 大破バッチ */
    private static final Layer BADLY_DAMAGE_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON2));

    /** 撃沈バッチ */
    private static final Layer LOST_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON3));

    /** 修復バッチ */
    private static final Layer NDOCK_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON4));

    /** 遠征バッチ */
    private static final Layer MISSION_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON5));

    /** 退避バッチ */
    private static final Layer ESCAPE_BADGE = new Layer(0, 0, Paths.get("common", MC_BANNER_ICON10));

    /** 小破汚れ */
    private static final Layer SLIGHT_DAMAGE_BACKGROUND = new Layer(0, 0, Paths.get("common", MC_BANNER_SMOKE_IMG0));

    /** 中破汚れ */
    private static final Layer HALF_DAMAGE_BACKGROUND = new Layer(0, 0, Paths.get("common", MC_BANNER_SMOKE_IMG1));

    /** 大破汚れ */
    private static final Layer BADLY_DAMAGE_BACKGROUND = new Layer(0, 0, Paths.get("common", MC_BANNER_SMOKE_IMG2));

    /** 疲労オレンジ背景 */
    private static final Layer ORANGE_BACKGROUND = new Layer(100, 0, Paths.get("common", MC_BANNER_VITAL_MASK0));

    /** 疲労オレンジ顔 */
    private static final Layer ORANGE_FACE = new Layer(143, 12, Paths.get("common", MC_BANNER_VITAL_MASK1));

    /** 疲労赤背景 */
    private static final Layer RED_BACKGROUND = new Layer(100, 0, Paths.get("common", MC_BANNER_VITAL_MASK2));

    /** 疲労赤顔 */
    private static final Layer RED_FACE = new Layer(143, 12, Paths.get("common", MC_BANNER_VITAL_MASK3));

    /** 装備アイコンのサイズ */
    private static final int ITEM_ICON_SIZE = 24;

    /**
     * キャラクターの画像を作成します
     *
     * @param chara キャラクター
     * @param addItem 装備画像を追加します
     * @param applyState 遠征や入渠、退避のバナーアイコンを追加する
     * @return 艦娘の画像
     */
    static Image get(Chara chara, boolean addItem, boolean applyState) {
        Canvas canvas = new Canvas(160, 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (chara != null) {
            Path base = getBaseImagePath(chara);
            if (base != null) {
                Image img = CACHE.get(base.toUri().toString(), Image::new);
                gc.drawImage(img, 0, 0);
            }
            List<Layer> layers = new ArrayList<>();

            // 艦娘
            boolean isShip = chara instanceof Ship;

            // 入渠中
            boolean isOnNdock = applyState && isShip && NdockCollection.get()
                    .getNdockSet()
                    .contains(((Ship) chara).getId());
            // 遠征中
            boolean isMission = applyState && isShip && DeckPortCollection.get()
                    .getMissionShips()
                    .contains(((Ship) chara).getId());
            // 退避
            boolean isEscape = applyState && isShip && Ships.isEscape((Ship) chara);

            // バッチ
            if (isOnNdock) {
                layers.add(NDOCK_BADGE);
            } else if (isMission) {
                layers.add(MISSION_BADGE);
            } else if (isEscape) {
                layers.add(ESCAPE_BADGE);
                gc.applyEffect(new ColorAdjust(0, -1, 0, 0));
            } else if (Ships.isSlightDamage(chara)) {
                layers.add(SLIGHT_DAMAGE_BADGE);
                layers.add(SLIGHT_DAMAGE_BACKGROUND);
            } else if (Ships.isHalfDamage(chara)) {
                layers.add(HALF_DAMAGE_BADGE);
                layers.add(HALF_DAMAGE_BACKGROUND);
            } else if (Ships.isBadlyDamage(chara)) {
                layers.add(BADLY_DAMAGE_BADGE);
                layers.add(BADLY_DAMAGE_BACKGROUND);
            } else if (Ships.isLost(chara)) {
                layers.add(LOST_BADGE);
                gc.applyEffect(new ColorAdjust(0, -1, 0, 0));
            }
            // 疲労
            if (isShip && Ships.isOrange((Ship) chara)) {
                layers.add(ORANGE_BACKGROUND);
                layers.add(ORANGE_FACE);
            } else if (isShip && Ships.isRed((Ship) chara)) {
                layers.add(RED_BACKGROUND);
                layers.add(RED_FACE);
            }
            // 装備画像
            if (addItem) {
                int x = 11;
                int y = 16;
                if (isShip) {
                    Ship ship = (Ship) chara;
                    for (Integer itemId : ship.getSlot()) {
                        // 装備アイコン
                        layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(itemId)));
                        x += ITEM_ICON_SIZE;
                    }
                    if (((Ship) chara).getSlotEx() != 0) {
                        // 補強増設は0(未開放)以外の場合
                        layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(ship.getSlotEx())));
                    }
                } else {
                    Map<Integer, SlotitemMst> map = SlotitemMstCollection.get()
                            .getSlotitemMap();
                    for (Integer itemId : chara.getSlot()) {
                        Image icon = Items.borderedItemImage(map.get(itemId));
                        // 装備アイコン
                        layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, icon));
                        x += ITEM_ICON_SIZE;
                    }
                }
            }

            applyLayers(gc, layers);

            writeHpGauge(chara, canvas, gc);
        }
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);

        return canvas.snapshot(sp, null);
    }

    /**
     * HPゲージ
     * @param chara キャラクター
     * @param canvas Canvas
     * @param gc GraphicsContext
     */
    private static void writeHpGauge(Chara chara, Canvas canvas, GraphicsContext gc) {
        double width = canvas.getWidth();
        double height = canvas.getHeight();

        double hpPer = (double) chara.getNowhp() / (double) chara.getMaxhp();
        gc.setFill(Color.TRANSPARENT.interpolate(Color.WHITE, 0.6));
        gc.fillRect(width - 5, 0, 5, height - (height * hpPer));
        gc.setFill(hpGaugeColor(hpPer));
        gc.fillRect(width - 5, height - (height * hpPer), 5, height * hpPer);
    }

    /**
     * 画像レイヤーを適用します
     *
     * @param gc GraphicsContext
     * @param layers 画像レイヤー
     */
    private static void applyLayers(GraphicsContext gc, List<Layer> layers) {
        Path dir = Paths.get(AppConfig.get().getResourcesDir());
        for (Layer layer : layers) {
            Image img = null;
            if (layer.path != null) {
                Path p = dir.resolve(layer.path);
                if (Files.isReadable(p)) {
                    img = CACHE.get(p.toUri().toString(), Image::new);
                }
            }
            if (layer.img != null) {
                img = layer.img;
            }
            if (img != null) {
                if (layer.w > 0) {
                    gc.drawImage(img, layer.x, layer.y, layer.w, layer.h);
                } else {
                    gc.drawImage(img, layer.x, layer.y);
                }
            }
        }
    }

    /**
     * キャラクターのベースとなる画像を取得します。
     *
     * @param chara キャラクター
     * @return 艦娘のベースとなる画像
     */
    private static Path getBaseImagePath(Chara chara) {
        Optional<ShipMst> mst = Ships.shipMst(chara);
        if (mst.isPresent()) {
            Path dir = ShipMst.getResourcePathDir(mst.get());
            String[] names;
            if (chara instanceof Ship
                    && (Ships.isHalfDamage(chara) || Ships.isBadlyDamage(chara) || Ships.isLost(chara))) {
                names = DAMAGED;
            } else {
                names = NORMAL;
            }
            for (String name : names) {
                Path p = dir.resolve(name);
                if (Files.isReadable(p)) {
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * 装備アイコンを返します
     *
     * @param itemId 装備ID
     * @throws IOException
     */
    private static Image itemIcon(Integer itemId) {
        SlotItem item = SlotItemCollection.get()
                .getSlotitemMap()
                .get(itemId);
        return Items.borderedItemImage(item);
    }

    /**
     * HPゲージの色を返します
     *
     * @param per HP割合
     * @return HPゲージの色
     */
    private static Color hpGaugeColor(double per) {
        if (per > 0.5) {
            return Color.TRANSPARENT.interpolate(Color.ORANGE.interpolate(Color.LIME, (per - 0.5) * 2), 0.9);
        } else {
            return Color.TRANSPARENT.interpolate(Color.RED.interpolate(Color.ORANGE, per * 2), 0.9);
        }
    }

    /**
     * レイヤー
     *
     */
    private static class Layer {

        /** X座標 */
        private final double x;

        /** Y座標 */
        private final double y;

        /** width */
        private final double w;

        /** height */
        private final double h;

        /** 画像ファイル名 */
        private final Path path;

        /** 画像 */
        private final Image img;

        private Layer(double x, double y, Path path) {
            this.x = x;
            this.y = y;
            this.w = -1;
            this.h = -1;
            this.path = path;
            this.img = null;
        }

        private Layer(double x, double y, double w, double h, Image img) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.path = null;
            this.img = img;
        }
    }
}
