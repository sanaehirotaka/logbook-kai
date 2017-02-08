package logbook.internal.gui;

import java.time.ZoneId;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.Logs;
import logbook.internal.gui.MissionLogController.SimpleMissionLog;

/**
 * 遠征ログの詳細行
 *
 */
public class MissionLogDetail {

    /** 日付 */
    private StringProperty date;

    /** 遠征名 */
    private StringProperty name;

    /** 結果 */
    private StringProperty result;

    /** 燃料 */
    private IntegerProperty fuel;

    /** 弾薬 */
    private IntegerProperty ammo;

    /** 鋼材 */
    private IntegerProperty metal;

    /** ボーキ */
    private IntegerProperty bauxite;

    /** アイテム1 */
    private StringProperty item1name;

    /** アイテム1 */
    private StringProperty item1count;

    /** アイテム2 */
    private StringProperty item2name;

    /** アイテム2 */
    private StringProperty item2count;

    /**
     * 日付を設定します。
     * @param date 日付
     */
    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    /**
     * 日付を取得します。
     * @return 日付
     */
    public StringProperty dateProperty() {
        return this.date;
    }

    /**
     * 遠征名を設定します。
     * @param name 遠征名
     */
    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    /**
     * 遠征名を取得します。
     * @return 遠征名
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * 結果を設定します。
     * @param result 結果
     */
    public void setResult(String result) {
        this.result = new SimpleStringProperty(result);
    }

    /**
     * 結果を取得します。
     * @return 結果
     */
    public StringProperty resultProperty() {
        return this.result;
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(Integer fuel) {
        this.fuel = new SimpleIntegerProperty(fuel);
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public IntegerProperty fuelProperty() {
        return this.fuel;
    }

    /**
     * 弾薬を設定します。
     * @param ammo 弾薬
     */
    public void setAmmo(Integer ammo) {
        this.ammo = new SimpleIntegerProperty(ammo);
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public IntegerProperty ammoProperty() {
        return this.ammo;
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(Integer metal) {
        this.metal = new SimpleIntegerProperty(metal);
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public IntegerProperty metalProperty() {
        return this.metal;
    }

    /**
     * ボーキを設定します。
     * @param bauxite ボーキ
     */
    public void setBauxite(Integer bauxite) {
        this.bauxite = new SimpleIntegerProperty(bauxite);
    }

    /**
     * ボーキを取得します。
     * @return ボーキ
     */
    public IntegerProperty bauxiteProperty() {
        return this.bauxite;
    }

    /**
     * アイテム1を設定します。
     * @param item1name アイテム1
     */
    public void setItem1name(String item1name) {
        this.item1name = new SimpleStringProperty(item1name);
    }

    /**
     * アイテム1を取得します。
     * @return アイテム1
     */
    public StringProperty item1nameProperty() {
        return this.item1name;
    }

    /**
     * アイテム1を設定します。
     * @param item1count アイテム1
     */
    public void setItem1count(String item1count) {
        this.item1count = new SimpleStringProperty(item1count);
    }

    /**
     * アイテム1を取得します。
     * @return アイテム1
     */
    public StringProperty item1countProperty() {
        return this.item1count;
    }

    /**
     * アイテム2を設定します。
     * @param item2name アイテム2
     */
    public void setItem2name(String item2name) {
        this.item2name = new SimpleStringProperty(item2name);
    }

    /**
     * アイテム2を取得します。
     * @return アイテム2
     */
    public StringProperty item2nameProperty() {
        return this.item2name;
    }

    /**
     * アイテム2を設定します。
     * @param item2count アイテム2
     */
    public void setItem2count(String item2count) {
        this.item2count = new SimpleStringProperty(item2count);
    }

    /**
     * アイテム2を取得します。
     * @return アイテム2
     */
    public StringProperty item2countProperty() {
        return this.item2count;
    }

    /**
     * 遠征統計のベースから遠征ログの詳細行を作成します
     *
     * @param log 遠征統計のベース
     * @return 遠征ログの詳細行
     */
    public static MissionLogDetail toMissionLogDetail(SimpleMissionLog log) {
        MissionLogDetail detail = new MissionLogDetail();
        // GMT+04:00のタイムゾーンになっているので日本時間に戻す
        String date = Logs.DATE_FORMAT.format(log.getDate().withZoneSameInstant(ZoneId.of("Asia/Tokyo")));
        detail.setDate(date);
        detail.setName(log.getName());
        detail.setResult(log.getResult());
        detail.setFuel(log.getFuel());
        detail.setAmmo(log.getAmmo());
        detail.setMetal(log.getMetal());
        detail.setBauxite(log.getBauxite());
        detail.setItem1name(log.getItem1name());
        detail.setItem1count(log.getItem1count() != 0 ? Integer.toString(log.getItem1count()) : "");
        detail.setItem2name(log.getItem2name());
        detail.setItem2count(log.getItem2count() != 0 ? Integer.toString(log.getItem2count()) : "");
        return detail;
    }
}