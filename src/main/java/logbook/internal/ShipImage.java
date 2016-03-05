package logbook.internal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import logbook.bean.AppConfig;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;

class ShipImage {

    /** 艦娘画像ファイル名(健在・小破) */
    private static final String[] NORMAL = { "1.jpg", "1.png" };

    /** 艦娘画像ファイル名(中破・大破) */
    private static final String[] DAMAGED = { "3.jpg", "3.png" };

    /** 小破バッチ */
    private static final Layer LAYER_1 = new Layer(0, 0, Paths.get("common", "388.png"));

    /** 中破バッチ */
    private static final Layer LAYER_2 = new Layer(0, 0, Paths.get("common", "390.png"));

    /** 大破バッチ */
    private static final Layer LAYER_3 = new Layer(0, 0, Paths.get("common", "392.png"));

    /** 撃沈バッチ */
    private static final Layer LAYER_4 = new Layer(0, 0, Paths.get("common", "394.png"));

    /** 修復バッチ */
    private static final Layer LAYER_5 = new Layer(0, 0, Paths.get("common", "396.png"));

    /** 小破汚れ */
    private static final Layer LAYER_6 = new Layer(0, 0, Paths.get("common", "411.png"));

    /** 中破汚れ */
    private static final Layer LAYER_7 = new Layer(0, 0, Paths.get("common", "413.png"));

    /** 大破汚れ */
    private static final Layer LAYER_8 = new Layer(0, 0, Paths.get("common", "415.png"));

    /** 疲労オレンジ背景 */
    private static final Layer LAYER_9 = new Layer(100, 0, Paths.get("common", "522.png"));

    /** 疲労オレンジ顔 */
    private static final Layer LAYER_10 = new Layer(143, 12, Paths.get("common", "523.png"));

    /** 疲労赤背景 */
    private static final Layer LAYER_11 = new Layer(100, 0, Paths.get("common", "525.png"));

    /** 疲労赤顔 */
    private static final Layer LAYER_12 = new Layer(143, 12, Paths.get("common", "526.png"));

    /** 装備アイコンのサイズ */
    private static final int ITEM_ICON_SIZE = 24;

    /**
     * 艦娘の画像を作成します
     *
     * @param ship 艦娘
     * @param addItem 装備画像を追加します
     * @return 艦娘の画像
     */
    static Image get(Ship ship, boolean addItem) {
        Canvas canvas = new Canvas(160, 40);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        if (ship != null) {
            Path base = getBaseImagePath(ship);
            if (base != null) {
                Image img = new Image(base.toUri().toString());
                gc.drawImage(img, 0, 0);
            }
            List<Layer> layers = new ArrayList<>();

            // 入渠中
            boolean isOnNdock = NdockCollection.get()
                    .getNdockSet()
                    .contains(ship.getId());
            // バッチ
            if (isOnNdock) {
                layers.add(LAYER_5);
            } else if (Ships.isSlightDamage(ship)) {
                layers.add(LAYER_1);
                layers.add(LAYER_6);
            } else if (Ships.isHalfDamage(ship)) {
                layers.add(LAYER_2);
                layers.add(LAYER_7);
            } else if (Ships.isBadlyDamage(ship)) {
                layers.add(LAYER_3);
                layers.add(LAYER_8);
            } else if (Ships.isLost(ship)) {
                layers.add(LAYER_4);
            }
            // 疲労
            if (Ships.isOrange(ship)) {
                layers.add(LAYER_9);
                layers.add(LAYER_10);
            } else if (Ships.isRed(ship)) {
                layers.add(LAYER_11);
                layers.add(LAYER_12);
            }
            // 装備画像
            if (addItem) {
                int x = 16;
                int y = 16;
                for (Integer itemId : ship.getSlot()) {
                    // 装備アイコン
                    layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(itemId)));
                    x += ITEM_ICON_SIZE;
                }
                if (ship.getSlotEx() != 0) {
                    // 補強増設は0(未開放)以外の場合
                    layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(ship.getSlotEx())));
                }
            }
            applyLayers(gc, layers);
        }
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);

        return canvas.snapshot(sp, null);
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
                    img = new Image(p.toUri().toString());
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
     * 艦娘のベースとなる画像を取得します。
     *
     * @param ship 艦娘
     * @return 艦娘のベースとなる画像
     */
    private static Path getBaseImagePath(Ship ship) {
        Optional<ShipMst> mst = Ships.shipMst(ship);
        if (mst.isPresent()) {
            Path dir = ShipMst.getResourcePathDir(mst.get());
            String[] names = (Ships.isHalfDamage(ship) || Ships.isBadlyDamage(ship)) ? DAMAGED : NORMAL;
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
