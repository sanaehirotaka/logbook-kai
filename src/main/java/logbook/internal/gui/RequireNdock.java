package logbook.internal.gui;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.StringJoiner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.internal.Logs;
import logbook.internal.Ships;

/**
 * お風呂に入りたい艦娘
 *
 */
public class RequireNdock {

    /** ID */
    private IntegerProperty id;

    /** 艦娘 */
    private ObjectProperty<Ship> ship;

    /** Lv */
    private IntegerProperty lv;

    /** 時間 */
    private StringProperty time;

    /** 今から */
    private StringProperty end;

    /** 燃料 */
    private IntegerProperty fuel;

    /** 鋼材 */
    private IntegerProperty metal;

    /**
     * IDを取得します。
     * @return ID
     */
    public IntegerProperty idProperty() {
        return this.id;
    }

    /**
     * IDを取得します。
     * @return ID
     */
    public int getId() {
        return this.id.get();
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public ObjectProperty<Ship> shipProperty() {
        return this.ship;
    }

    /**
     * 艦娘を取得します。
     * @return 艦娘
     */
    public Ship getShip() {
        return this.ship.get();
    }

    /**
     * 艦娘を設定します。
     * @param ship 艦娘
     */
    public void setShip(Ship ship) {
        this.ship = new SimpleObjectProperty<>(ship);
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public IntegerProperty lvProperty() {
        return this.lv;
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    public int getLv() {
        return this.lv.get();
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(int lv) {
        this.lv = new SimpleIntegerProperty(lv);
    }

    /**
     * 時間を取得します。
     * @return time
     */
    public StringProperty timeProperty() {
        return this.time;
    }

    /**
     * 時間を取得します。
     * @return 時間
     */
    public String getTime() {
        return this.time.get();
    }

    /**
     * 時間を設定します。
     * @param time 時間
     */
    public void setTime(String time) {
        this.time = new SimpleStringProperty(time);
    }

    /**
     * 今からを取得します。
     * @return end
     */
    public StringProperty endProperty() {
        return this.end;
    }

    /**
     * 今からを取得します。
     * @return 今から
     */
    public String getEnd() {
        return this.end.get();
    }

    /**
     * 今からを設定します。
     * @param end 今から
     */
    public void setEnd(String end) {
        this.end = new SimpleStringProperty(end);
    }

    /**
     * 燃料を取得します。
     * @return fuel
     */
    public IntegerProperty fuelProperty() {
        return this.fuel;
    }

    /**
     * 燃料を取得します。
     * @return fuel
     */
    public int getFuel() {
        return this.fuel.get();
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(int fuel) {
        this.fuel = new SimpleIntegerProperty(fuel);
    }

    /**
     * 鋼材を取得します。
     * @return metal
     */
    public IntegerProperty metalProperty() {
        return this.metal;
    }

    /**
     * 鋼材を取得します。
     * @return metal
     */
    public int getMetal() {
        return this.metal.get();
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(int metal) {
        this.metal = new SimpleIntegerProperty(metal);
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(Integer.toString(this.id.get()))
                .add(Optional.ofNullable(this.ship.get())
                        .map(s -> Ships.shipMst(s).map(ShipMst::getName).orElse(""))
                        .orElse(""))
                .add(Integer.toString(this.lv.get()))
                .add(this.time.get())
                .add(this.end.get())
                .add(Integer.toString(this.fuel.get()))
                .add(Integer.toString(this.metal.get()))
                .toString();
    }

    /**
     * 艦娘からお風呂に入りたい艦娘を生成します
     *
     * @param ship 艦娘
     * @return お風呂に入りたい艦娘
     */
    public static RequireNdock toRequireNdock(Ship ship) {
        Duration d = Duration.ofMillis(ship.getNdockTime());

        RequireNdock ndock = new RequireNdock();
        ndock.setId(ship.getId());
        ndock.setShip(ship);
        ndock.setLv(ship.getLv());
        ndock.setTime(timeText(d));
        ndock.setEnd(endText(d));
        ndock.setFuel(ship.getNdockItem().get(0));
        ndock.setMetal(ship.getNdockItem().get(1));

        return ndock;
    }

    /**
     * 入渠時間のテキスト表現
     *
     * @param d 期間
     * @return 入渠時間のテキスト表現
     */
    private static String timeText(Duration d) {
        long days = d.toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        long seconds = d.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days + "日");
        }
        if (hours > 0) {
            sb.append(hours + "時間");
        }
        if (minutes > 0) {
            sb.append(minutes + "分");
        }
        if (seconds > 0 && days == 0 && hours == 0) {
            sb.append(seconds + "秒");
        }
        if (d.isZero() || d.isNegative()) {
            sb.append("修復完了");
        }
        return sb.toString();
    }

    /**
     * 今からのテキスト表現
     *
     * @param d 期間
     * @return 今からのテキスト表現
     */
    private static String endText(Duration d) {
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(Logs.now().plus(d));
    }
}
