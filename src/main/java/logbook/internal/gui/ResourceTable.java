package logbook.internal.gui;

import java.text.MessageFormat;
import java.util.StringJoiner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.Tuple.Pair;

/**
 * 資材ログのテーブル行
 *
 */
public class ResourceTable {

    /** 日付 */
    private StringProperty date = new SimpleStringProperty();

    /** 燃料 */
    private ObjectProperty<Pair<Integer, Integer>> fuel = new SimpleObjectProperty<>();

    /** 弾薬 */
    private ObjectProperty<Pair<Integer, Integer>> ammo = new SimpleObjectProperty<>();

    /** 鋼材 */
    private ObjectProperty<Pair<Integer, Integer>> metal = new SimpleObjectProperty<>();

    /** ボーキサイト */
    private ObjectProperty<Pair<Integer, Integer>> bauxite = new SimpleObjectProperty<>();

    /** 高速修復材 */
    private ObjectProperty<Pair<Integer, Integer>> bucket = new SimpleObjectProperty<>();

    /** 高速建造材 */
    private ObjectProperty<Pair<Integer, Integer>> burner = new SimpleObjectProperty<>();

    /** 開発資材 */
    private ObjectProperty<Pair<Integer, Integer>> research = new SimpleObjectProperty<>();

    /** 改修資材 */
    private ObjectProperty<Pair<Integer, Integer>> improve = new SimpleObjectProperty<>();

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
    public ObjectProperty<Pair<Integer, Integer>> fuelProperty() {
        return this.fuel;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public Pair<Integer, Integer> getFuel() {
        return this.fuel.get();
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(Integer fuel, Integer diff) {
        this.fuel.set(new Part(fuel, diff));
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public ObjectProperty<Pair<Integer, Integer>> ammoProperty() {
        return this.ammo;
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public Pair<Integer, Integer> getAmmo() {
        return this.ammo.get();
    }

    /**
     * 弾薬を設定します。
     * @param ammo 弾薬
     */
    public void setAmmo(Integer ammo, Integer diff) {
        this.ammo.set(new Part(ammo, diff));
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public ObjectProperty<Pair<Integer, Integer>> metalProperty() {
        return this.metal;
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public Pair<Integer, Integer> getMetal() {
        return this.metal.get();
    }

    /**
     * 鋼材を設定します。
     * @param metal 鋼材
     */
    public void setMetal(Integer metal, Integer diff) {
        this.metal.set(new Part(metal, diff));
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public ObjectProperty<Pair<Integer, Integer>> bauxiteProperty() {
        return this.bauxite;
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public Pair<Integer, Integer> getBauxite() {
        return this.bauxite.get();
    }

    /**
     * ボーキサイトを設定します。
     * @param bauxite ボーキサイト
     */
    public void setBauxite(Integer bauxite, Integer diff) {
        this.bauxite.set(new Part(bauxite, diff));
    }

    /**
     * 高速修復材を取得します。
     * @return 高速修復材
     */
    public ObjectProperty<Pair<Integer, Integer>> bucketProperty() {
        return this.bucket;
    }

    /**
     * 高速修復材を取得します。
     * @return 高速修復材
     */
    public Pair<Integer, Integer> getBucket() {
        return this.bucket.get();
    }

    /**
     * 高速修復材を設定します。
     * @param bucket 高速修復材
     */
    public void setBucket(Integer bauxite, Integer diff) {
        this.bucket.set(new Part(bauxite, diff));
    }

    /**
     * 高速建造材を取得します。
     * @return 高速建造材
     */
    public ObjectProperty<Pair<Integer, Integer>> burnerProperty() {
        return this.burner;
    }

    /**
     * 高速建造材を取得します。
     * @return 高速建造材
     */
    public Pair<Integer, Integer> getBurner() {
        return this.burner.get();
    }

    /**
     * 高速建造材を設定します。
     * @param burner 高速建造材
     */
    public void setBurner(Integer burner, Integer diff) {
        this.burner.set(new Part(burner, diff));
    }

    /**
     * 開発資材を取得します。
     * @return 開発資材
     */
    public ObjectProperty<Pair<Integer, Integer>> researchProperty() {
        return this.research;
    }

    /**
     * 開発資材を取得します。
     * @return 開発資材
     */
    public Pair<Integer, Integer> getResearch() {
        return this.research.get();
    }

    /**
     * 開発資材を設定します。
     * @param research 開発資材
     */
    public void setResearch(Integer research, Integer diff) {
        this.research.set(new Part(research, diff));
    }

    /**
     * 改修資材を取得します。
     * @return 改修資材
     */
    public ObjectProperty<Pair<Integer, Integer>> improveProperty() {
        return this.improve;
    }

    /**
     * 改修資材を取得します。
     * @return 改修資材
     */
    public Pair<Integer, Integer> getImprove() {
        return this.improve.get();
    }

    /**
     * 改修資材を設定します。
     * @param improve 改修資材
     */
    public void setImprove(Integer improve, Integer diff) {
        this.improve.set(new Part(improve, diff));
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(this.date.get())
                .add(this.fuel.get().toString())
                .add(this.ammo.get().toString())
                .add(this.metal.get().toString())
                .add(this.bauxite.get().toString())
                .add(this.bucket.get().toString())
                .add(this.burner.get().toString())
                .add(this.research.get().toString())
                .add(this.improve.get().toString())
                .toString();
    }

    static final class Part extends Pair<Integer, Integer> {

        /** 資材テーブルに表示する資材のフォーマット */
        private static final MessageFormat COMPARE_FORMAT = new MessageFormat("{0,number,0}({1,number,+0;-0})");

        Part(Integer _1, Integer _2) {
            super(_1, _2);
        }

        @Override
        public String toString() {
            return COMPARE_FORMAT.format(new Integer[] {
                    this.get1(),
                    this.get2()
            });
        }
    }
}
