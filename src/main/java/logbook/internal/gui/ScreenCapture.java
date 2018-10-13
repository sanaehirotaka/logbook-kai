package logbook.internal.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import logbook.internal.BiImage;
import logbook.internal.LoggerHolder;
import logbook.internal.ThreadManager;
import lombok.Getter;
import lombok.Setter;

/**
 * スクリーンショットに関係するメソッドを集めたクラス
 *
 */
class ScreenCapture {

    private static final int SCREEN_WIDTH = 1200;

    private static final int SCREEN_HEIGHT = 720;

    /** Jpeg品質 */
    private static final float QUALITY = 0.9f;

    /** ゲーム画面サイズ */
    private static final Dimension[] sizes = IntStream.rangeClosed(600, 1500)
            .mapToObj(w -> new Dimension(w, (int) (((float) w) / SCREEN_WIDTH * SCREEN_HEIGHT)))
            .toArray(Dimension[]::new);

    @Setter
    @Getter
    private Robot robot;

    /** キャプチャ範囲 */
    @Setter
    @Getter
    private Rectangle rectangle;

    /** 切り取り範囲 */
    @Setter
    @Getter
    private Rectangle cutRect;

    /** 形式 */
    @Setter
    @Getter
    private String type = "jpg";

    private int size = 200;

    private ObservableList<ImageData> list;

    private ObjectProperty<ImageData> current;

    /** 切り取り範囲 */
    enum CutType {
        /** 切り取らない */
        NONE(null),
        /** 改装一覧の範囲(艦娘除く) */
        UNIT_WITHOUT_SHIP(new Rectangle(490, 154, 345, 547)),
        /** 改装一覧の範囲 */
        UNIT(new Rectangle(490, 154, 690, 547));

        private Rectangle angle;

        private CutType(Rectangle angle) {
            this.angle = angle;
        }

        Rectangle getAngle() {
            return this.angle;
        }
    }

    /**
     * スクリーンショット
     *
     * @param robot Robot
     * @param rectangle ゲーム画面の座標
     */
    ScreenCapture(Robot robot, Rectangle rectangle) {
        this.robot = robot;
        this.rectangle = rectangle;
    }

    void setItems(ObservableList<ImageData> list) {
        this.list = list;
    }

    void setCurrent(ObjectProperty<ImageData> current) {
        this.current = current;
    }

    void setSize(int size) {
        this.size = size;
    }

    void capture() throws IOException {
        ThreadManager.getExecutorService()
                .execute(this::execute);
    }

    void captureDirect(Path dir) {
        ThreadManager.getExecutorService()
                .execute(() -> this.executeDirect(dir));
    }

    private void execute() {
        try {
            ImageData image = new ImageData();
            image.setDateTime(ZonedDateTime.now());
            image.setFormat(this.type);

            byte[] data;
            if (this.cutRect != null) {
                data = encode(cut(this.robot.createScreenCapture(this.rectangle), this.cutRect), image.getFormat());
            } else {
                data = encode(this.robot.createScreenCapture(this.rectangle), image.getFormat());
            }
            image.setImage(data);

            Platform.runLater(() -> {
                this.current.set(image);
                this.list.add(image);
                while (this.list.size() > this.size) {
                    this.list.remove(0);
                }
            });
        } catch (IOException e) {
            LoggerHolder.get().warn("キャプチャ処理で例外", e);
        }
    }

    private void executeDirect(Path dir) {
        try {
            ImageData image = new ImageData();
            image.setDateTime(ZonedDateTime.now());
            image.setFormat(this.type);

            byte[] data;
            if (this.cutRect != null) {
                data = encode(cut(this.robot.createScreenCapture(this.rectangle), this.cutRect), image.getFormat());
            } else {
                data = encode(this.robot.createScreenCapture(this.rectangle), image.getFormat());
            }
            image.setImage(data);

            if (data != null) {
                String fname = CaptureSaveController.DATE_FORMAT.format(image.getDateTime()) + "." + image.getFormat();
                Path to = dir.resolve(fname);
                try (OutputStream out = Files.newOutputStream(to)) {
                    out.write(data);
                }
            }

            Platform.runLater(() -> {
                this.current.set(image);
            });
        } catch (IOException e) {
            LoggerHolder.get().warn("キャプチャ処理で例外", e);
        }
    }

    /**
     * 座標からスクリーンを取得します
     *
     * @param x X
     * @param y Y
     * @return スクリーン
     */
    static GraphicsConfiguration detectScreenDevice(int x, int y) {
        GraphicsDevice[] gds = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getScreenDevices();

        for (GraphicsDevice gd : gds) {
            for (GraphicsConfiguration gc : gd.getConfigurations()) {
                Rectangle r = gc.getBounds();
                if (r.contains(x, y)) {
                    return gc;
                }
            }
        }
        return null;
    }

    /**
     * イメージからゲーム画面を検索します
     *
     * @param image イメージ
     * @return 画面の座標
     */
    static Rectangle detectGameScreen(BufferedImage image) {
        BiImage biImage = new BiImage(image, Color.WHITE) {
            @Override
            protected boolean test(int a, int b) {
                return (a & 0xf0f0f0) == (b & 0xf0f0f0);
            }
        };
        int height = image.getHeight() - sizes[0].height;
        int width = image.getWidth() - sizes[0].width;
        int sizelen = sizes.length;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // 最初の1パターンをテスト
                // x,yから縦方向(height+2)が全て白かテスト(「全てが白」ではない場合スキップ)
                if (!biImage.allH(x, y, sizes[0].height + 2))
                    continue;
                // x+1,y+1から縦方向(height)が全て白かテスト(「全てが白」の場合スキップ)
                if (biImage.allH(x + 1, y + 1, sizes[0].height))
                    continue;

                for (int i = 0; i < sizelen; i++) {
                    Dimension size = sizes[i];
                    // x,yから縦方向(height+2)が全て白かテスト(「全てが白」ではない場合break) (左辺)
                    if (!biImage.allH(x, y, size.height + 2))
                        break;
                    // x,yから横方向(width+2)が全て白かテスト(「全てが白」ではない場合break) (上辺)
                    if (!biImage.allW(x, y, size.width + 2))
                        break;
                    // x+width+1,yから縦方向(height+2)が全て白かテスト(「全てが白」ではない場合別の矩形をテストする) (右辺)
                    if (!biImage.allH(x + size.width + 1, y, size.height + 2))
                        continue;
                    // x,y+height+1から横方向(width+2)が全て白かテスト(「全てが白」ではない場合別の矩形をテストする) (下辺)
                    if (!biImage.allW(x, y + size.height + 1, size.width + 2))
                        continue;
                    // x+1,y+1から縦方向(height)が全て白かテスト(「全てが白」の場合別の矩形をテストする)
                    if (biImage.allH(x + 1, y + 1, size.height))
                        continue;
                    // x+width,y+1から縦方向(height)が全て白かテスト(「全てが白」の場合別の矩形をテストする)
                    if (biImage.allH(x + size.width, y + 1, size.height))
                        continue;
                    // x+1,y+1から縦方向(height)の白の数を数える(白の数が50%より多ければ別の矩形をテストする)
                    if (biImage.countH(x + 1, y + 1, size.height) > size.height / 2)
                        continue;
                    // x+width,y+1から縦方向(height)の白の数を数える(白の数が50%より多ければ別の矩形をテストする)
                    if (biImage.countH(x + size.width, y + 1, size.height) > size.height / 2)
                        continue;
                    return new Rectangle(x + 1, y + 1, size.width, size.height);
                }
            }
        }
        return null;
    }

    /**
     * BufferedImageをエンコードします
     *
     * @param image BufferedImage
     * @param format 画像形式
     * @return エンコードされた画像
     * @throws IOException 入出力例外
     */
    static byte[] encode(BufferedImage image, String format) throws IOException {
        if ("jpg".equals(format))
            return encodeJpeg(image);
        return encodeOther(image, format);
    }

    /**
     * BufferedImageをJPEG形式にエンコードします
     *
     * @param image BufferedImage
     * @return JPEG形式の画像
     * @throws IOException 入出力例外
     */
    static byte[] encodeJpeg(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            try {
                ImageWriteParam iwp = writer.getDefaultWriteParam();
                if (iwp.canWriteCompressed()) {
                    iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    iwp.setCompressionQuality(QUALITY);
                }
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), iwp);
            } finally {
                writer.dispose();
            }
        }
        return out.toByteArray();
    }

    /**
     * BufferedImageを指定された形式にエンコードします
     *
     * @param image BufferedImage
     * @param format 画像形式
     * @return 指定された形式の画像
     * @throws IOException 入出力例外
     */
    static byte[] encodeOther(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(out)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName(format).next();
            try {
                writer.setOutput(ios);
                writer.write(null, new IIOImage(image, null, null), null);
            } finally {
                writer.dispose();
            }
        }
        return out.toByteArray();
    }

    /**
     * BufferedImageを{@code rect}で指定された範囲で切り取ります
     *
     * @param image BufferedImage
     * @param rect 画像の範囲
     * @return BufferedImage
     */
    static BufferedImage cut(BufferedImage image, Rectangle rect) {
        int x;
        int y;
        int w;
        int h;
        if (image.getWidth() == SCREEN_WIDTH && image.getHeight() == SCREEN_HEIGHT) {
            x = rect.x;
            y = rect.y;
            w = rect.width;
            h = rect.height;
        } else {
            x = (int) (rect.x * ((double) image.getWidth() / SCREEN_WIDTH));
            y = (int) (rect.y * ((double) image.getHeight() / SCREEN_HEIGHT));
            w = (int) (rect.width * ((double) image.getWidth() / SCREEN_WIDTH));
            h = (int) (rect.height * ((double) image.getHeight() / SCREEN_HEIGHT));
        }
        return image.getSubimage(x, y, w, h);
    }

    /**
     * 複数の画像を横に並べた画像を返します
     *
     * @param bytes JPEG形式などにエンコード済みの画像ファイルのバイト配列
     * @param column 列数
     * @return 横に並べた画像
     */
    static BufferedImage tileImage(List<byte[]> bytes, int column) throws IOException {
        if (bytes.isEmpty()) {
            return null;
        }
        BufferedImage base = ImageIO.read(new ByteArrayInputStream(bytes.get(0)));

        int baseWidth = base.getWidth();
        int baseHeight = base.getHeight();

        int width = baseWidth * Math.min(bytes.size(), column);
        int height = (int) (baseHeight * Math.ceil((float) bytes.size() / column));

        BufferedImage canvas = new BufferedImage(width, height, ColorSpace.TYPE_RGB);

        Graphics gc = canvas.createGraphics();
        gc.setColor(Color.WHITE);
        gc.fillRect(0, 0, width, height);
        for (int i = 0; i < bytes.size(); i++) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes.get(i)));
            int c = i % column;
            int r = (int) Math.ceil((float) (i + 1) / column) - 1;
            int x = baseWidth * c;
            int y = baseHeight * r;
            gc.drawImage(image, x, y, null);
        }
        return canvas;
    }

    /**
     * 画像データ
     *
     */
    static final class ImageData {

        /** 日付書式 */
        private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        /** 画像フォーマット */
        private String format;

        /** 日付 */
        private ZonedDateTime dateTime;

        /** 画像データ */
        private Reference<byte[]> image;

        /**
         * 画像フォーマットを取得します。
         * @return 画像フォーマット
         */
        String getFormat() {
            return this.format;
        }

        /**
         * 画像フォーマットを設定します。
         * @param format 画像フォーマット
         */
        void setFormat(String format) {
            this.format = format;
        }

        /**
         * 日付を取得します。
         * @return 日付
         */
        ZonedDateTime getDateTime() {
            return this.dateTime;
        }

        /**
         * 日付を設定します。
         * @param dateTime 日付
         */
        void setDateTime(ZonedDateTime dateTime) {
            this.dateTime = dateTime;
        }

        /**
         * 画像データを取得します。
         * @return 画像データ
         */
        byte[] getImage() {
            return this.image.get();
        }

        /**
         * 画像データを設定します。
         * @param image 画像データ
         */
        void setImage(byte[] image) {
            this.image = new SoftReference<>(image);
        }

        @Override
        public String toString() {
            return DATE_FORMAT.format(this.dateTime);
        }
    }
}
