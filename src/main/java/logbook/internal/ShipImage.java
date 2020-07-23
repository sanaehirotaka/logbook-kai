package logbook.internal;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;

class ShipImage {

    /** 画像キャッシュ(艦) */
    private static final ReferenceCache<String, Image> BASE_CACHE = new ReferenceCache<>(120);

    /** 画像キャッシュ(アイコン類) */
    private static final ReferenceCache<String, Image> COMMON_CACHE = new ReferenceCache<>(32);

    /** 画像キャッシュ(HPゲージ) */
    private static final ReferenceCache<Double, Image> HPGAUGE_CACHE = new ReferenceCache<>(50);

    /** 画像キャッシュ(経験値ゲージ) */
    private static final ReferenceCache<Double, Image> EXPGAUGE_CACHE = new ReferenceCache<>(50);

    /** 艦娘画像ファイル名(健在・小破) */
    private static final String[] NORMAL = { "1.png", "1.jpg" };

    /** 艦娘画像ファイル名(中破・大破) */
    private static final String[] DAMAGED = { "3.png", "3.jpg", "1.png", "1.jpg" };

    /** 艦娘画像ファイル名(健在・小破) */
    private static final String[] STANDING_NORMAL = { "17.png", "17.jpg" };

    /** 艦娘画像ファイル名(中破・大破) */
    private static final String[] STANDING_DAMAGED = { "19.png", "19.jpg" };

    /** 小破バナーアイコン */
    private static final String MC_BANNER_ICON0 = "common_misc/common_misc_105.png";

    /** 中破バナーアイコン */
    private static final String MC_BANNER_ICON1 = "common_misc/common_misc_99.png";

    /** 大破バナーアイコン */
    private static final String MC_BANNER_ICON2 = "common_misc/common_misc_109.png";

    /** 撃沈バナーアイコン */
    private static final String MC_BANNER_ICON3 = "common_misc/common_misc_102.png";

    /** 修復バナーアイコン */
    private static final String MC_BANNER_ICON4 = "common_misc/common_misc_108.png";

    /** 遠征バナーアイコン */
    private static final String MC_BANNER_ICON5 = "common_misc/common_misc_100.png";

    /** 退避バナーアイコン */
    private static final String MC_BANNER_ICON10 = "common_misc/common_misc_110.png";

    /** 小破汚れ */
    private static final String MC_BANNER_SMOKE_IMG0 = "common_misc/common_misc_96.png";

    /** 中破汚れ */
    private static final String MC_BANNER_SMOKE_IMG1 = "common_misc/common_misc_97.png";

    /** 大破汚れ */
    private static final String MC_BANNER_SMOKE_IMG2 = "common_misc/common_misc_98.png";

    /** 疲労オレンジ背景 */
    private static final String COMMON_MISC_35 = "common_misc/common_misc_35.png";

    /** 疲労オレンジ顔 */
    private static final String COMMON_MISC_112 = "common_misc/common_misc_112.png";

    /** 疲労赤背景 */
    private static final String COMMON_MISC_36 = "common_misc/common_misc_36.png";

    /** 疲労赤顔 */
    private static final String COMMON_MISC_113 = "common_misc/common_misc_113.png";

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
    private static final Layer ORANGE_BACKGROUND = new Layer(150, 0, Paths.get("common", COMMON_MISC_35));

    /** 疲労オレンジ顔 */
    private static final Layer ORANGE_FACE = new Layer(214, 18, Paths.get("common", COMMON_MISC_112));

    /** 疲労赤背景 */
    private static final Layer RED_BACKGROUND = new Layer(150, 0, Paths.get("common", COMMON_MISC_36));

    /** 疲労赤顔 */
    private static final Layer RED_FACE = new Layer(214, 18, Paths.get("common", COMMON_MISC_113));

    /** 出撃札 */
    private static final String JOIN_BANNER = "common_event/common_event_{0}.png";

    /** 装備アイコンのサイズ */
    private static final int ITEM_ICON_SIZE = 32;

    /**
     * キャラクターの画像を作成します
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     */
    static Image get(Chara chara) {
        if (chara != null) {
            Path base = getPath(chara);
            if (base != null) {
                return BASE_CACHE.get(base.toUri().toString(), (url, status) -> {
                    Image image = new Image(url);
                    status.setDoCache(!image.isError());
                    return image;
                });
            }
        }
        return null;
    }

    /**
     * キャラクターの画像を作成します(バックグラウンドでのロード)
     *
     * @param chara キャラクター
     * @return 艦娘の画像
     */
    static Image getBackgroundLoading(Chara chara) {
        if (chara != null) {
            Path base = getPath(chara);
            if (base != null) {
                return new Image(base.toUri().toString(), true);
            }
        }
        return null;
    }

    /**
     * キャラクターの画像を作成します
     *
     * @param chara キャラクター
     * @param addItem 装備画像を追加します
     * @param applyState 遠征や入渠、退避のバナーアイコンを追加する
     * @param itemMap 装備Map
     * @param escape 退避艦ID
     * @return 艦娘の画像
     */
    static Image get(Chara chara, boolean addItem, boolean applyState,
            Map<Integer, SlotItem> itemMap, Set<Integer> escape) {
        boolean visibleExpGauge = AppConfig.get().isVisibleExpGauge();
        return get(chara, addItem, applyState, true, true, true, visibleExpGauge, itemMap, escape);
    }

    /**
     * キャラクターの画像を作成します
     *
     * @param chara キャラクター
     * @param addItem 装備画像を追加します
     * @param applyState 遠征や入渠、退避のバナーアイコンを追加する
     * @param banner バナーアイコンを追加する
     * @param cond コンディションを反映する
     * @param hpGauge HPゲージを反映する
     * @param expGauge 経験値ゲージを反映する
     * @param itemMap 装備Map
     * @param escape 退避艦ID
     * @return 艦娘の画像
     */
    static Image get(Chara chara, boolean addItem, boolean applyState, boolean banner, boolean cond, boolean hpGauge,
            boolean expGauge,
            Map<Integer, SlotItem> itemMap, Set<Integer> escape) {
        Canvas canvas = new Canvas(240, 60);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.drawImage(get(chara), 0, 0, canvas.getWidth(), canvas.getHeight());

        if (chara != null) {
            List<Layer> layers = new ArrayList<>();

            // 艦娘
            boolean isShip = chara.isShip();

            // 入渠中
            boolean isOnNdock = applyState && isShip && NdockCollection.get()
                    .getNdockSet()
                    .contains(chara.asShip().getId());
            // 遠征中
            boolean isMission = applyState && isShip && DeckPortCollection.get()
                    .getMissionShips()
                    .contains(chara.asShip().getId());
            // 退避
            boolean isEscape = isShip && Ships.isEscape(chara.asShip(), escape);

            // バッチ
            if (banner) {
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
                } else if (Ships.isBadlyDamage(chara) || (Ships.isLost(chara) && chara.isPractice())) {
                    layers.add(BADLY_DAMAGE_BADGE);
                    layers.add(BADLY_DAMAGE_BACKGROUND);
                } else if (Ships.isLost(chara)) {
                    layers.add(LOST_BADGE);
                    gc.applyEffect(new ColorAdjust(0, -1, 0, 0));
                }
            }
            // 疲労
            if (cond) {
                if (isShip && Ships.isOrange(chara.asShip())) {
                    layers.add(ORANGE_BACKGROUND);
                    layers.add(ORANGE_FACE);
                } else if (isShip && Ships.isRed(chara.asShip())) {
                    layers.add(RED_BACKGROUND);
                    layers.add(RED_FACE);
                }
            }
            // 出撃札
            if (isShip) {
                Ship ship = chara.asShip();
                Integer sallyArea = ship.getSallyArea();
                if (sallyArea != null && sallyArea.intValue() != 0) {
                    Path p = Paths.get("common", JOIN_BANNER.replace("{0}", Integer.toString(sallyArea + 3)));
                    layers.add(new Layer(50, -3, p));
                }
            }
            // 装備画像
            if (addItem) {
                int x = 17;
                int y = 24;
                if (isShip) {
                    Ship ship = chara.asShip();
                    int slotnum = ship.getSlotnum();
                    for (int i = 0; i < 5; i++) {
                        if (slotnum > i) {
                            Integer itemId = ship.getSlot().get(i);
                            // 装備アイコン
                            layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(itemId, itemMap)));
                        }
                        x += ITEM_ICON_SIZE + 2;
                    }
                    if (ship.getSlotEx() != 0) {
                        // 補強増設は0(未開放)以外の場合
                        layers.add(
                                new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, itemIcon(ship.getSlotEx(), itemMap)));
                    }
                } else {
                    Map<Integer, SlotitemMst> map = SlotitemMstCollection.get()
                            .getSlotitemMap();
                    for (Integer itemId : chara.getSlot()) {
                        Image icon = Items.borderedItemImage(map.get(itemId));
                        // 装備アイコン
                        layers.add(new Layer(x, y, ITEM_ICON_SIZE, ITEM_ICON_SIZE, icon));
                        x += ITEM_ICON_SIZE + 2;
                    }
                }
            }

            applyLayers(gc, layers);

            if (expGauge && chara.isShip()) {
                writeExpGauge(chara.asShip(), canvas, gc);
            }
            if (hpGauge) {
                writeHpGauge(chara, canvas, gc);
            }
        }
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);

        return canvas.snapshot(sp, null);
    }

    /**
     * 補給ゲージ(燃料・弾薬)を取得します
     *
     * @param ship 艦娘
     * @return 補給ゲージ
     */
    static Image getSupplyGauge(Ship ship) {
        Canvas canvas = new Canvas(36, 12);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Optional<ShipMst> mstOpt = Ships.shipMst(ship);
        if (mstOpt.isPresent()) {
            double width = canvas.getWidth();

            ShipMst mst = mstOpt.get();
            double fuelPer = (double) ship.getFuel() / (double) mst.getFuelMax();
            double ammoPer = (double) ship.getBull() / (double) mst.getBullMax();

            gc.setFill(Color.GRAY);
            gc.fillRect(0, 3, width, 2);

            gc.setFill(Color.GRAY);
            gc.fillRect(0, 10, width, 2);

            Color fuelColor = fuelPer >= 0.5D ? Color.GREEN : fuelPer >= 0.4D ? Color.ORANGE : Color.RED;
            Color ammoColor = ammoPer >= 0.5D ? Color.SADDLEBROWN : ammoPer >= 0.4D ? Color.ORANGE : Color.RED;

            gc.setFill(fuelColor);
            gc.fillRect(0, 0, width * fuelPer, 4);

            gc.setFill(ammoColor);
            gc.fillRect(0, 7, width * ammoPer, 4);
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
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        double gaugeWidth = 7;
        double hpPer = (double) chara.getNowhp() / (double) chara.getMaxhp();
        gc.drawImage(createGauge(gaugeWidth, h, hpPer, ShipImage::hpGaugeColor, HPGAUGE_CACHE), w - gaugeWidth, 0);
    }

    /**
     * 経験値ゲージ
     * @param chara キャラクター
     * @param canvas Canvas
     * @param gc GraphicsContext
     */
    private static void writeExpGauge(Ship ship, Canvas canvas, GraphicsContext gc) {
        double w = canvas.getWidth() - 7;
        double h = canvas.getHeight();
        double gaugeHeight = 6;
        double exp = ship.getExp().get(0);
        double next = ship.getExp().get(1);
        double expPer;
        if (next > 0) {
            Integer nowLvExp = ExpTable.get().get(ship.getLv());
            Integer nextLvExp = ExpTable.get().get(ship.getLv() + 1);
            if (nowLvExp != null && nextLvExp != null) {
                expPer = (exp - nowLvExp.doubleValue()) / (nextLvExp.doubleValue() - nowLvExp.doubleValue());
            } else {
                expPer = 0;
            }
        } else {
            // 既に max の場合は表示しない
            return;
        }
        Color color = Color.TRANSPARENT.interpolate(Color.DEEPSKYBLUE, 0.9);
        gc.drawImage(createGauge(w, gaugeHeight, expPer, k -> color, EXPGAUGE_CACHE), 0, h - gaugeHeight);
    }

    /**
     * ゲージを作成する
     * @param width ゲージの幅
     * @param height ゲージの高さ
     * @param per 割合
     * @param colorFunc 色
     * @param cache キャッシュ
     * @return ゲージのImage
     */
    private static Image createGauge(double width, double height, double per,
            Function<Double, Color> colorFunc,
            ReferenceCache<Double, Image> cache) {
        double size = (int) Math.max((Math.max(width, height) * per), 0);
        return cache.get(size, key -> {
            Canvas canvas = new Canvas(width, height);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.TRANSPARENT.interpolate(Color.ALICEBLUE, 0.9));
            if (width < height) {
                gc.fillRect(0, 0, width, height - size);
            } else {
                gc.fillRect(size, 0, width - size, height);
            }
            gc.setFill(colorFunc.apply(size / Math.max(width, height)));
            if (width < height) {
                gc.fillRect(0, height - size, width, size);
            } else {
                gc.fillRect(0, 0, size, height);
            }

            SnapshotParameters sp = new SnapshotParameters();
            sp.setFill(Color.TRANSPARENT);
            return canvas.snapshot(sp, null);
        });
    }

    /**
     * 画像レイヤーを適用します
     *
     * @param gc GraphicsContext
     * @param layers 画像レイヤー
     */
    private static void applyLayers(GraphicsContext gc, List<Layer> layers) {
        for (Layer layer : layers) {
            Image img = null;
            if (layer.path != null) {
                Path p = Paths.get(AppConfig.get().getResourcesDir()).resolve(layer.path);
                img = COMMON_CACHE.get(p.toUri().toString(), (url, status) -> {
                    Image image = new Image(url);
                    status.setDoCache(!image.isError());
                    return image;
                });
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
     * キャラクター画像へのパスを返します。
     * @param chara キャラクター
     * @return キャラクター画像へのパス
     */
    static Path getPath(Chara chara) {
        return getBaseImagePath(chara, NORMAL, DAMAGED);
    }

    /**
     * キャラクター画像へのパスを返します(立ち絵)。
     * @param chara キャラクター
     * @return キャラクター画像へのパス(立ち絵)
     */
    static Path getStandingPosePath(Chara chara) {
        return getBaseImagePath(chara, STANDING_NORMAL, STANDING_DAMAGED);
    }

    /**
     * キャラクターのベースとなる画像を取得します。
     *
     * @param chara キャラクター
     * @return 艦娘のベースとなる画像
     */
    private static Path getBaseImagePath(Chara chara, String[] normal, String[] damaged) {
        Optional<ShipMst> mst = Ships.shipMst(chara);
        if (mst.isPresent()) {
            Path dir = ShipMst.getResourcePathDir(mst.get());
            String[] names;
            if ((chara.isShip() || chara.isFriend() || chara.isPractice())
                    && (Ships.isHalfDamage(chara) || Ships.isBadlyDamage(chara) || Ships.isLost(chara))) {
                names = damaged;
            } else {
                names = normal;
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
     * @param itemMap 装備Map
     */
    private static Image itemIcon(Integer itemId, Map<Integer, SlotItem> itemMap) {
        return Items.borderedItemImage(itemMap.get(itemId));
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
