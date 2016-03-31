package logbook.internal.gui;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

/**
 * スクリーンショットに関係するメソッドを集めたクラス
 *
 */
public class ScreenCapture {

    private static final int WHITE = Color.WHITE.getRGB();

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
                    iwp.setCompressionQuality(0.9F);
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
}
