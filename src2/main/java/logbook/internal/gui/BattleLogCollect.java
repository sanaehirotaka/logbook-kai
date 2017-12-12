package logbook.internal.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.BattleLogs.Unit;

/**
 * 集計行
 *
 */
public class BattleLogCollect {

    /** 集計  */
    private StringProperty unit;

    /** 出撃  */
    private StringProperty start;

    /** 勝利  */
    private StringProperty win;

    /** S勝利 */
    private StringProperty s;

    /** A勝利 */
    private StringProperty a;

    /** B勝利 */
    private StringProperty b;

    /** C敗北 */
    private StringProperty c;

    /** D敗北 */
    private StringProperty d;

    /** 集計単位 */
    private Unit collectUnit;

    /** 海域 */
    private String area;

    /** ボス */
    private boolean boss;

    /**
     * 集計を取得します。
     * @return 集計
     */
    public String getUnit() {
        return this.unit.get();
    }

    /**
     * 集計を設定します。
     * @param unit 集計
     */
    public void setUnit(String unit) {
        this.unit = new SimpleStringProperty(unit);
    }

    /**
     * 集計を取得します。
     * @return 集計
     */
    public StringProperty unitProperty() {
        return this.unit;
    }

    /**
     * 出撃を取得します。
     * @return 出撃
     */
    public String getStart() {
        return this.start.get();
    }

    /**
     * 出撃を設定します。
     * @param start 出撃
     */
    public void setStart(String start) {
        this.start = new SimpleStringProperty(start);
    }

    /**
     * 出撃を取得します。
     * @return 出撃
     */
    public StringProperty startProperty() {
        return this.start;
    }

    /**
     * 勝利を取得します。
     * @return 勝利
     */
    public String getWin() {
        return this.win.get();
    }

    /**
     * 勝利を設定します。
     * @param win 勝利
     */
    public void setWin(String win) {
        this.win = new SimpleStringProperty(win);
    }

    /**
     * 勝利を取得します。
     * @return 勝利
     */
    public StringProperty winProperty() {
        return this.win;
    }

    /**
     * S勝利を取得します。
     * @return S勝利
     */
    public String getS() {
        return this.s.get();
    }

    /**
     * S勝利を設定します。
     * @param s S勝利
     */
    public void setS(String s) {
        this.s = new SimpleStringProperty(s);
    }

    /**
     * S勝利を取得します。
     * @return S勝利
     */
    public StringProperty sProperty() {
        return this.s;
    }

    /**
     * A勝利を取得します。
     * @return A勝利
     */
    public String getA() {
        return this.a.get();
    }

    /**
     * A勝利を設定します。
     * @param a A勝利
     */
    public void setA(String a) {
        this.a = new SimpleStringProperty(a);
    }

    /**
     * A勝利を取得します。
     * @return A勝利
     */
    public StringProperty aProperty() {
        return this.a;
    }

    /**
     * B勝利を取得します。
     * @return B勝利
     */
    public String getB() {
        return this.b.get();
    }

    /**
     * B勝利を設定します。
     * @param b B勝利
     */
    public void setB(String b) {
        this.b = new SimpleStringProperty(b);
    }

    /**
     * B勝利を取得します。
     * @return B勝利
     */
    public StringProperty bProperty() {
        return this.b;
    }

    /**
     * C敗北を取得します。
     * @return C敗北
     */
    public String getC() {
        return this.c.get();
    }

    /**
     * C敗北を設定します。
     * @param c C敗北
     */
    public void setC(String c) {
        this.c = new SimpleStringProperty(c);
    }

    /**
     * C敗北を取得します。
     * @return C敗北
     */
    public StringProperty cProperty() {
        return this.c;
    }

    /**
     * D敗北を取得します。
     * @return D敗北
     */
    public String getD() {
        return this.d.get();
    }

    /**
     * D敗北を設定します。
     * @param d D敗北
     */
    public void setD(String d) {
        this.d = new SimpleStringProperty(d);
    }

    /**
     * D敗北を取得します。
     * @return D敗北
     */
    public StringProperty dProperty() {
        return this.d;
    }

    /**
     * 集計単位を取得します。
     * @return 集計単位
     */
    public Unit getCollectUnit() {
        return this.collectUnit;
    }

    /**
     * 集計単位を設定します。
     * @param collectUnit 集計単位
     */
    public void setCollectUnit(Unit collectUnit) {
        this.collectUnit = collectUnit;
    }

    /**
     * 海域を取得します。
     * @return 海域
     */
    public String getArea() {
        return this.area;
    }

    /**
     * 海域を設定します。
     * @param area 海域
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * ボスを取得します。
     * @return ボス
     */
    public boolean isBoss() {
        return this.boss;
    }

    /**
     * ボスを設定します。
     * @param boss ボス
     */
    public void setBoss(boolean boss) {
        this.boss = boss;
    }
}
