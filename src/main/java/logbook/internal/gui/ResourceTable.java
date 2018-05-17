package logbook.internal.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 資材ログのテーブル行
 *
 */
public class ResourceTable {

    /** 日付 */
    private StringProperty date = new SimpleStringProperty();

    /** 燃料 */
    private StringProperty fuel = new SimpleStringProperty();

    /** 弾薬 */
    private StringProperty ammo = new SimpleStringProperty();

    /** 鋼材 */
    private StringProperty metal = new SimpleStringProperty();

    /** ボーキサイト */
    private StringProperty bauxite = new SimpleStringProperty();

    /** 高速修復材 */
    private StringProperty bucket = new SimpleStringProperty();

    /** 高速建造材 */
    private StringProperty burner = new SimpleStringProperty();

    /** 開発資材 */
    private StringProperty research = new SimpleStringProperty();

    /** 改修資材 */
    private StringProperty improve = new SimpleStringProperty();

    /**日付を取得します。
     * @return 日付
     */
    public StringProperty dateProperty() {
        return this.date;
    }

    /**
     * 日付を取得します。
     * @return 日付
     */
    public String getDate() {
        return this.date.get();
    }

    /**
     * 日付を設定します。
     * @param date 日付
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public StringProperty fuelProperty() {
        return this.fuel;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public String getFuel() {
        return this.fuel.get();
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(String fuel) {
        this.fuel.set(fuel);
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public StringProperty ammoProperty() {
        return this.ammo;
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public String getAmmo() {
        return this.ammo.get();
    }

    /**
     * 弾薬を設定します。
     * @param ammo 弾薬
     */
    public void setAmmo(String ammo) {
        this.ammo.set(ammo);
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public StringProperty metalProperty() {
        return this.metal;
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public String getMetal() {
        return this.metal.get();
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(String metal) {
        this.metal.set(metal);
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public StringProperty bauxiteProperty() {
        return this.bauxite;
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public String getBauxite() {
        return this.bauxite.get();
    }

    /**
     * ボーキサイトを設定します。
     * @param bauxite ボーキサイト
     */
    public void setBauxite(String bauxite) {
        this.bauxite.set(bauxite);
    }

    /**
     * 高速修復材を取得します。
     * @return 高速修復材
     */
    public StringProperty bucketProperty() {
        return this.bucket;
    }

    /**
     * 高速修復材を取得します。
     * @return 高速修復材
     */
    public String getBucket() {
        return this.bucket.get();
    }

    /**
     * 高速修復材を設定します。
     * @param bucket 高速修復材
     */
    public void setBucket(String bucket) {
        this.bucket.set(bucket);
    }

    /**
     * 高速建造材を取得します。
     * @return 高速建造材
     */
    public StringProperty burnerProperty() {
        return this.burner;
    }

    /**
     * 高速建造材を取得します。
     * @return 高速建造材
     */
    public String getBurner() {
        return this.burner.get();
    }

    /**
     * 高速建造材を設定します。
     * @param burner 高速建造材
     */
    public void setBurner(String burner) {
        this.burner.set(burner);
    }

    /**
     * 開発資材を取得します。
     * @return 開発資材
     */
    public StringProperty researchProperty() {
        return this.research;
    }

    /**
     * 開発資材を取得します。
     * @return 開発資材
     */
    public String getResearch() {
        return this.research.get();
    }

    /**
     * 開発資材を設定します。
     * @param research 開発資材
     */
    public void setResearch(String research) {
        this.research.set(research);
    }

    /**
     * 改修資材を取得します。
     * @return 改修資材
     */
    public StringProperty improveProperty() {
        return this.improve;
    }

    /**
     * 改修資材を取得します。
     * @return 改修資材
     */
    public String getImprove() {
        return this.improve.get();
    }

    /**
     * 改修資材を設定します。
     * @param improve 改修資材
     */
    public void setImprove(String improve) {
        this.improve.set(improve);
    }
}
