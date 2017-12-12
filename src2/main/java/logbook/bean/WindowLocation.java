package logbook.bean;

import java.io.Serializable;

/**
 * ウインドウの位置とサイズ
 *
 */
public class WindowLocation implements Serializable {

    private static final long serialVersionUID = 3548270453031293618L;

    /** X */
    private double x;

    /** Y */
    private double y;

    /** Width */
    private double width;

    /** Height */
    private double height;

    /**
     * Xを取得します。
     * @return X
     */
    public double getX() {
        return this.x;
    }

    /**
     * Xを設定します。
     * @param x X
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Yを取得します。
     * @return Y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Yを設定します。
     * @param y Y
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Widthを取得します。
     * @return Width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Widthを設定します。
     * @param width Width
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Heightを取得します。
     * @return Height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Heightを設定します。
     * @param height Height
     */
    public void setHeight(double height) {
        this.height = height;
    }

}
