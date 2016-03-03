package logbook.internal;

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
import logbook.bean.ShipDescription;

class ShipImage {

    /** 艦娘画像ファイル名(健在・小破) */
    private static final String[] NORMAL = { "1.jpg", "1.png" };

    /** 艦娘画像ファイル名(中破・大破) */
    private static final String[] DAMAGED = { "3.jpg", "3.png" };

    /** 小破バッチ */
    private static final Layer LAYER_1 = new Layer(0, 0, "388.png");

    /** 中破バッチ */
    private static final Layer LAYER_2 = new Layer(0, 0, "390.png");

    /** 大破バッチ */
    private static final Layer LAYER_3 = new Layer(0, 0, "392.png");

    ///** 撃沈バッチ */
    //private static final Layer LAYER_4 = new Layer(0, 0, "394.png");

    /** 修復バッチ */
    private static final Layer LAYER_5 = new Layer(0, 0, "396.png");

    /** 小破汚れ */
    private static final Layer LAYER_6 = new Layer(0, 0, "411.png");

    /** 中破汚れ */
    private static final Layer LAYER_7 = new Layer(0, 0, "413.png");

    /** 大破汚れ */
    private static final Layer LAYER_8 = new Layer(0, 0, "415.png");

    /** 疲労オレンジ背景 */
    private static final Layer LAYER_9 = new Layer(100, 0, "522.png");

    /** 疲労オレンジ顔 */
    private static final Layer LAYER_10 = new Layer(143, 12, "523.png");

    /** 疲労赤背景 */
    private static final Layer LAYER_11 = new Layer(100, 0, "525.png");

    /** 疲労赤顔 */
    private static final Layer LAYER_12 = new Layer(143, 12, "526.png");

    /**
     * コモンリソースファイルのディレクトリを取得します。
     * @return コモンリソースファイルのディレクトリ
     */
    static Path getResourcePathDir() {
        return Paths.get(AppConfig.get().getResourcesDir(), "common");
    }

    /**
     * 艦娘の画像を作成します
     *
     * @param ship 艦娘
     * @return 艦娘の画像
     */
    static Image get(Ship ship) {
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
            }
            // 疲労
            if (Ships.isOrange(ship)) {
                layers.add(LAYER_9);
                layers.add(LAYER_10);
            } else if (Ships.isRed(ship)) {
                layers.add(LAYER_11);
                layers.add(LAYER_12);
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
        Path dir = getResourcePathDir();
        for (Layer layer : layers) {
            Path p = dir.resolve(layer.name);
            if (Files.isReadable(p)) {
                Image img = new Image(p.toUri().toString());
                gc.drawImage(img, layer.x, layer.y);
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
        Optional<ShipDescription> desc =  Ships.shipDescription(ship);
        if (desc.isPresent()) {
            Path dir = ShipDescription.getResourcePathDir(desc.get());
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
     * レイヤー
     *
     */
    private static class Layer {

        /** X座標 */
        private final double x;

        /** Y座標 */
        private final double y;

        /** 画像ファイル名 */
        private final String name;

        private Layer(double x, double y, String name) {
            this.x = x;
            this.y = y;
            this.name = name;
        }
    }
}
