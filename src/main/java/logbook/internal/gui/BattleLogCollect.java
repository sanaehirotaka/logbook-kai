package logbook.internal.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.BattleLogs.IUnit;

/**
 * 集計行
 *
 */
public class BattleLogCollect {

    /** 集計  */
    private final StringProperty unit = new SimpleStringProperty();

    /** 出撃 （ボスカウントを"-"にするため String のままにする） */
    private final SimpleStringProperty start = new SimpleStringProperty();

    /** 勝利  */
    private final IntegerProperty win = new SimpleIntegerProperty();

    /** S勝利 */
    private final IntegerProperty s = new SimpleIntegerProperty();

    /** A勝利 */
    private final IntegerProperty a = new SimpleIntegerProperty();

    /** B勝利 */
    private final IntegerProperty b = new SimpleIntegerProperty();

    /** C敗北 */
    private final IntegerProperty c = new SimpleIntegerProperty();

    /** D敗北 */
    private final IntegerProperty d = new SimpleIntegerProperty();

    /** 集計単位 */
    private IUnit collectUnit;

    /** 海域 */
    private String area;

    /** 海域短縮名 */
    private String areaShortName;

    /** ボス */
    private boolean boss;

    /** 海域ソート順 */
    private int areaSortOrder;
    
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
        this.unit.set(unit);
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
        this.start .set(start);
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
    public int getWin() {
        return this.win.get();
    }

    /**
     * 勝利を設定します。
     * @param win 勝利
     */
    public void setWin(int win) {
        this.win.set(win);
    }

    /**
     * 勝利を取得します。
     * @return 勝利
     */
    public IntegerProperty winProperty() {
        return this.win;
    }

    /**
     * S勝利を取得します。
     * @return S勝利
     */
    public int getS() {
        return this.s.get();
    }

    /**
     * S勝利を設定します。
     * @param s S勝利
     */
    public void setS(int s) {
        this.s.set(s);
    }

    /**
     * S勝利を取得します。
     * @return S勝利
     */
    public IntegerProperty sProperty() {
        return this.s;
    }

    /**
     * A勝利を取得します。
     * @return A勝利
     */
    public int getA() {
        return this.a.get();
    }

    /**
     * A勝利を設定します。
     * @param a A勝利
     */
    public void setA(int a) {
        this.a.set(a);
    }

    /**
     * A勝利を取得します。
     * @return A勝利
     */
    public IntegerProperty aProperty() {
        return this.a;
    }

    /**
     * B勝利を取得します。
     * @return B勝利
     */
    public int getB() {
        return this.b.get();
    }

    /**
     * B勝利を設定します。
     * @param b B勝利
     */
    public void setB(int b) {
        this.b.set(b);
    }

    /**
     * B勝利を取得します。
     * @return B勝利
     */
    public IntegerProperty bProperty() {
        return this.b;
    }

    /**
     * C敗北を取得します。
     * @return C敗北
     */
    public int getC() {
        return this.c.get();
    }

    /**
     * C敗北を設定します。
     * @param c C敗北
     */
    public void setC(int c) {
        this.c.set(c);
    }

    /**
     * C敗北を取得します。
     * @return C敗北
     */
    public IntegerProperty cProperty() {
        return this.c;
    }

    /**
     * D敗北を取得します。
     * @return D敗北
     */
    public int getD() {
        return this.d.get();
    }

    /**
     * D敗北を設定します。
     * @param d D敗北
     */
    public void setD(int d) {
        this.d.set(d);
    }

    /**
     * D敗北を取得します。
     * @return D敗北
     */
    public IntegerProperty dProperty() {
        return this.d;
    }

    /**
     * 集計単位を取得します。
     * @return 集計単位
     */
    public IUnit getCollectUnit() {
        return this.collectUnit;
    }

    /**
     * 集計単位を設定します。
     * @param collectUnit 集計単位
     */
    public void setCollectUnit(IUnit collectUnit) {
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
     * 海域短縮名を取得します。
     * @return 海域短縮名
     */
    public String getAreaShortName() {
        return this.areaShortName;
    }

    /**
     * 海域短縮名を設定します。
     * @param areaShortName 海域短縮名
     */
    public void setAreaShortName(String areaShortName) {
        this.areaShortName = areaShortName;
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

    /**
     * 海域ソート順を取得します。
     * @return ソート順
     */
    public int getAreaSortOrder() {
        return this.areaSortOrder;
    }

    /**
     * 海域ソート順を設定します。
     * @param areaSortOrder ソート順
     */
    public void setAreaSortOrder(int areaSortOrder) {
        this.areaSortOrder = areaSortOrder;
    }
}
