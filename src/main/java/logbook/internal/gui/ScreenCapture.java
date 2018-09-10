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

    /** Jpeg品質 */
    private static final float QUALITY = 0.9f;

    /** ゲーム画面サイズ */
    private static final Dimension[] sizes = IntStream.rangeClosed(600, 1500)
            .mapToObj(w -> new Dimension(w, (int) (w / 1200f * 720)))
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

            byte[] data;
            if (this.cutRect != null) {
                data = encodeJpeg(this.robot.createScreenCapture(this.rectangle), this.cutRect);
            } else {
                data = encodeJpeg(this.robot.createScreenCapture(this.rectangle));
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

            byte[] data;
            if (this.cutRect != null) {
                data = encodeJpeg(this.robot.createScreenCapture(this.rectangle), this.cutRect);
            } else {
                data = encodeJpeg(this.robot.createScreenCapture(this.rectangle));
            }
            image.setImage(data);

            if (data != null) {
                Path to = dir.resolve(CaptureSaveController.DATE_FORMAT.format(ZonedDateTime.now()) + ".jpg");
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
        for (int y = 0, height = image.getHeight() - sizes[0].height; y < height; y++) {
            for (int x = 0, width = image.getWidth() - sizes[0].width; x < width; x++) {
                for (int i = 0; i < sizes.length; i++) {
                    Dimension size = sizes[i];
                    if (!biImage.allW(x, y, size.width + 2))
                        break;
                    if (!biImage.allH(x, y, size.height + 2))
                        break;
                    if (!biImage.allW(x, y + size.height + 1, size.width + 2))
                        break;
                    if (!biImage.allH(x + size.width + 1, y, size.height + 2))
                        break;
                    if (!biImage.anyH(x + 1, y + 1, size.height))
                        continue;
                    if (!biImage.anyH(x + size.width, y + 1, size.height))
                        continue;
                    return new Rectangle(x + 1, y + 1, size.width, size.height);
                }
            }
        }
        return null;
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
     * BufferedImageをJPEG形式にエンコードします
     *
     * @param image BufferedImage
     * @param rect 画像の範囲
     * @return JPEG形式の画像
     * @throws IOException 入出力例外
     */
    static byte[] encodeJpeg(BufferedImage image, Rectangle rect) throws IOException {
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
                int x;
                int y;
                int w;
                int h;
                if (image.getWidth() == 1200 && image.getHeight() == 720) {
                    x = rect.x;
                    y = rect.y;
                    w = rect.width;
                    h = rect.height;
                } else {
                    x = (int) (rect.x * ((double) image.getWidth() / 1200));
                    y = (int) (rect.y * ((double) image.getHeight() / 720));
                    w = (int) (rect.width * ((double) image.getWidth() / 1200));
                    h = (int) (rect.height * ((double) image.getHeight() / 720));
                }
                writer.write(null, new IIOImage(image.getSubimage(x, y, w, h), null, null), iwp);
            } finally {
                writer.dispose();
            }
        }
        return out.toByteArray();
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

        /** 日付 */
        private ZonedDateTime dateTime;

        /** 画像データ */
        private Reference<byte[]> image;

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
