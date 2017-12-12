package logbook.internal.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 資材ログのテーブル行
 *
 */
public class ResourceTable {

    /** 日付 */
    private StringProperty date;

    /** 燃料 */
    private StringProperty fuel;

    /** 弾薬 */
    private StringProperty ammo;

    /** 鋼材 */
    private StringProperty metal;

    /** ボーキサイト */
    private StringProperty bauxite;

    /** 高速修復材 */
    private StringProperty bucket;

    /** 高速建造材 */
    private StringProperty burner;

    /** 開発資材 */
    private StringProperty research;

    /** 改修資材 */
    private StringProperty improve;

    /**
     * 日付を取得します。
     * @return 日付
     */
    public StringProperty dateProperty() {
        return this.date;
    }

    /**
     * 日付を設定します。
     * @param date 日付
     */
    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public StringProperty fuelProperty() {
        return this.fuel;
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(String fuel) {
        this.fuel = new SimpleStringProperty(fuel);
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public StringProperty ammoProperty() {
        return this.ammo;
    }

    /**
     * 弾薬を設定します。
     * @param ammo 弾薬
     */
    public void setAmmo(String ammo) {
        this.ammo = new SimpleStringProperty(ammo);
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public StringProperty metalProperty() {
        return this.metal;
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(String metal) {
        this.metal = new SimpleStringProperty(metal);
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public StringProperty bauxiteProperty() {
        return this.bauxite;
    }

    /**
     * ボーキサイトを設定します。
     * @param bauxite ボーキサイト
     */
    public void setBauxite(String bauxite) {
        this.bauxite = new SimpleStringProperty(bauxite);
    }

    /**
     * 高速修復材を取得します。
     * @return 高速修復材
     */
    public StringProperty bucketProperty() {
        return this.bucket;
    }

    /**
     * 高速修復材を設定します。
     * @param bucket 高速修復材
     */
    public void setBucket(String bucket) {
        this.bucket = new SimpleStringProperty(bucket);
    }

    /**
     * 高速建造材を取得します。
     * @return 高速建造材
     */
    public StringProperty burnerProperty() {
        return this.burner;
    }

    /**
     * 高速建造材を設定します。
     * @param burner 高速建造材
     */
    public void setBurner(String burner) {
        this.burner = new SimpleStringProperty(burner);
    }

    /**
     * 開発資材を取得します。
     * @return 開発資材
     */
    public StringProperty researchProperty() {
        return this.research;
    }

    /**
     * 開発資材を設定します。
     * @param research 開発資材
     */
    public void setResearch(String research) {
        this.research = new SimpleStringProperty(research);
    }

    /**
     * 改修資材を取得します。
     * @return 改修資材
     */
    public StringProperty improveProperty() {
        return this.improve;
    }

    /**
     * 改修資材を設定します。
     * @param improve 改修資材
     */
    public void setImprove(String improve) {
        this.improve = new SimpleStringProperty(improve);
    }
}
