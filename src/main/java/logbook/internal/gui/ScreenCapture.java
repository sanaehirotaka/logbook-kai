package logbook.internal.gui;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

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
}
