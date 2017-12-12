package logbook.internal.gui;

import java.awt.Color;
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
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import logbook.internal.ThreadManager;

/**
 * スクリーンショットに関係するメソッドを集めたクラス
 *
 */
class ScreenCapture {

    /** Jpeg品質 */
    private static final float QUALITY = 0.9f;

    private static final int WHITE = Color.WHITE.getRGB();

    /** 改装一覧の範囲(艦娘除く) */
    static final Rectangle UNIT_WITHOUT_SHIP_RECT = new Rectangle(327, 103, 230, 365);

    /** 改装一覧の範囲 */
    static final Rectangle UNIT_RECT = new Rectangle(327, 103, 460, 365);

    private Robot robot;

    /** キャプチャ範囲 */
    private Rectangle rectangle;

    /** 切り取り範囲 */
    private Rectangle cutRect;

    private int size = 200;

    private ObservableList<ImageData> list;

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

    /**
     * 切り取り範囲を取得します。
     * @return 切り取り範囲
     */
    Rectangle getCutRect() {
        return this.cutRect;
    }

    /**
     * 切り取り範囲を設定します。
     * @param cutRect 切り取り範囲
     */
    void setCutRect(Rectangle cutRect) {
        this.cutRect = cutRect;
    }

    void setSize(int size) {
        this.size = size;
    }

    void capture() throws IOException {
        ThreadManager.getExecutorService()
                .execute(this::execute);
    }

    private void execute() {
        try {
            ImageData data = new ImageData();
            data.setDateTime(ZonedDateTime.now(ZoneId.of("Asia/Tokyo")));

            byte[] image;
            if (this.cutRect != null) {
                image = encodeJpeg(this.robot.createScreenCapture(this.rectangle), this.cutRect);
            } else {
                image = encodeJpeg(this.robot.createScreenCapture(this.rectangle));
            }
            data.setImage(image);

            Platform.runLater(() -> {
                this.list.add(data);
                while (this.list.size() > this.size) {
                    this.list.remove(0);
                }
            });
        } catch (IOException e) {
            LoggerHolder.LOG.warn("キャプチャ処理で例外", e);
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
     * @param width 画面の幅
     * @param height 画面の高さ
     * @return 画面の座標
     */
    static Rectangle detectGameScreen(BufferedImage image, int width, int height) {
        int searchX = image.getWidth() - width - 2;
        int searchY = image.getHeight() - height - 2;

        for (int x = 0; x <= searchX; x++) {
            for (int y = 0; y <= searchY; y++) {
                // 左上
                if ((image.getRGB(x, y) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + 1, y) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x, y + 1) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + 1, y + 1) & WHITE) == WHITE)
                    continue;
                // 右上
                if ((image.getRGB(x + width, y) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + width + 1, y) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + width, y + 1) & WHITE) == WHITE)
                    continue;
                if ((image.getRGB(x + width + 1, y + 1) & WHITE) != WHITE)
                    continue;
                // 左下
                if ((image.getRGB(x, y + height) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + 1, y + height) & WHITE) == WHITE)
                    continue;
                if ((image.getRGB(x, y + height + 1) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + 1, y + height + 1) & WHITE) != WHITE)
                    continue;
                // 右下
                if ((image.getRGB(x + width, y + height) & WHITE) == WHITE)
                    continue;
                if ((image.getRGB(x + width + 1, y + height) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + width, y + height + 1) & WHITE) != WHITE)
                    continue;
                if ((image.getRGB(x + width + 1, y + height + 1) & WHITE) != WHITE)
                    continue;

                return new Rectangle(x + 1, y + 1, width, height);
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
                int x = rect.x;
                int y = rect.y;
                int w = rect.width;
                int h = rect.height;
                writer.write(null, new IIOImage(image.getSubimage(x, y, w, h), null, null), iwp);
            } finally {
                writer.dispose();
            }
        }
        return out.toByteArray();
    }

    /**
     * アニメーションGIFを作成します
     *
     * @param out 出力先
     * @param images JPEG形式などにエンコード済みの画像ファイルのバイト配列
     * @param delay 表示する際の遅延時間
     * @throws IOException 入出力例外
     */
    static void createAnimetedGIF(OutputStream out, List<byte[]> images, Duration delay) throws IOException {
        try (ImageOutputStream iout = ImageIO.createImageOutputStream(out)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("gif").next();
            try {
                ImageWriteParam param = writer.getDefaultWriteParam();

                writer.setOutput(iout);
                writer.prepareWriteSequence(writer.getDefaultStreamMetadata(param));

                for (byte[] image : images) {
                    BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));

                    // Delay timeを設定する
                    // 参考: https://docs.oracle.com/javase/jp/8/docs/api/javax/imageio/metadata/doc-files/gif_metadata.html
                    IIOMetadata metadata = writer.getDefaultImageMetadata(new ImageTypeSpecifier(bufferedImage), param);
                    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_gif_image_1.0");
                    IIOMetadataNode gce = new IIOMetadataNode("GraphicControlExtension");
                    gce.setAttribute("disposalMethod", "none");
                    gce.setAttribute("userInputFlag", "FALSE");
                    gce.setAttribute("transparentColorFlag", "FALSE");
                    gce.setAttribute("transparentColorIndex", "0");
                    // 100分の1秒単位なので10で割る
                    gce.setAttribute("delayTime", Long.toString(delay.toMillis() / 10));
                    root.appendChild(gce);
                    metadata.mergeTree("javax_imageio_gif_image_1.0", root);

                    writer.writeToSequence(new IIOImage(bufferedImage, null, metadata), param);
                }
                writer.endWriteSequence();
            } finally {
                writer.dispose();
            }
        }
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
            this.image = new SoftReference<byte[]>(image);
        }

        @Override
        public String toString() {
            return DATE_FORMAT.format(this.dateTime);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ScreenCapture.class);
    }
}
