/**
 * 
 */
package logbook.internal.gui;

import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.bean.Quest;

/**
 * 任務一覧
 * @author turane_gaku
 *
 */
public final class QuestItem {

    /** No */
    private IntegerProperty no = new SimpleIntegerProperty();

    /** カテゴリ ex. 編成任務 or 出撃任務 */
    private IntegerProperty category = new SimpleIntegerProperty();

    /** 期限 */
    private StringProperty type = new SimpleStringProperty();

    /** 遂行 */
    private StringProperty state = new SimpleStringProperty();

    /** タイトル */
    private StringProperty title = new SimpleStringProperty();

    /** 詳細 */
    private StringProperty detail = new SimpleStringProperty();

    /** 燃料 */
    private IntegerProperty fuel = new SimpleIntegerProperty();

    /** 弾薬 */
    private IntegerProperty ammo = new SimpleIntegerProperty();

    /** 鋼材 */
    private IntegerProperty metal = new SimpleIntegerProperty();

    /** ボーキサイト */
    private IntegerProperty boux = new SimpleIntegerProperty();

    /** 4資源以外の報酬があるか? */
    private IntegerProperty bonusFlag = new SimpleIntegerProperty();

    /** 進行度 */
    private IntegerProperty progressFlag = new SimpleIntegerProperty();

    /**
     * QuestItemのコンストラクタ
     * @param quest
     */
    public QuestItem(Quest quest) {
        List<Integer> material = quest.getGetMaterial();
        this.setNo(quest.getNo());
        this.setCategory(quest.getCategory());
        this.setType(quest.getTypeString());
        this.setState(quest.getStateString());
        this.setTitle(quest.getTitle());
        this.setDetail(quest.getDetail());
        this.setFuel(material.get(0));
        this.setAmmo(material.get(1));
        this.setMetal(material.get(2));
        this.setBoux(material.get(3));
        this.setBonusFlag(quest.getBonusFlag());
        this.setProgressFlag(quest.getProgressFlag());
    }

    /**
     * Noを取得します。
     * @return No
     */
    public IntegerProperty noPropertya() {
        return this.no;
    }

    /**
     * Noを取得します。
     * @return No
     */
    public int getNo() {
        return this.no.get();
    }

    /**
     * Noを設定します。
     * @param No
     */
    public void setNo(int no) {
        this.no.set(no);
    }

    /**
     * カテゴリを取得します。
     * @return カテゴリ
     */
    public IntegerProperty categoryProperty() {
        return this.category;
    }

    /**
     * カテゴリを取得します。
     * @return カテゴリ
     */
    public int getCategory() {
        return this.category.get();
    }

    /**
     * カテゴリを設定します。
     * @param カテゴリ
     */
    public void setCategory(int category) {
        this.category.set(category);
    }

    /**
     * タイプを取得します。
     * @return タイプ
     */
    public StringProperty typeProperty() {
        return this.type;
    }

    /**
     * タイプを取得します。
     * @return タイプ
     */
    public String getType() {
        return this.type.get();
    }

    /**
     * タイプを設定します。
     * @param タイプ
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * 遂行を取得します。
     * @return 遂行
     */
    public StringProperty stateProperty() {
        return this.state;
    }

    /**
     * 遂行を取得します。
     * @return 遂行
     */
    public String getState() {
        return this.state.get();
    }

    /**
     * 遂行を設定します。
     * @param 遂行
     */
    public void setState(String state) {
        this.state.set(state);
    }

    /**
     * タイトルを取得します。
     * @return タイトル
     */
    public StringProperty titleProperty() {
        return this.title;
    }

    /**api_detail
     * タイトルを取得します。
     * @return タイトル
     */
    public String getTitle() {
        return this.title.get();
    }

    /**
     * タイトルを設定します。
     * @param タイトル
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * 詳細を取得します。
     * @return 詳細
     */
    public StringProperty detailProperty() {
        return this.detail;
    }

    /**
     * 詳細を取得します。
     * @return 詳細
     */
    public String getDetail() {
        return this.detail.get();
    }

    /**
     * 詳細を設定します。
     * @param 詳細
     */
    public void setDetail(String detail) {
        this.detail.set(detail);
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public IntegerProperty fuelProperty() {
        return this.fuel;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public int getFuel() {
        return this.fuel.get();
    }

    /**
     * 燃料を設定します。
     * @param 燃料
     */
    public void setFuel(int mat) {
        this.fuel.set(mat);
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public IntegerProperty ammoProperty() {
        return this.ammo;
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public int getAmmo() {
        return this.ammo.get();
    }

    /**
     * 弾薬を設定します。
     * @param 弾薬
     */
    public void setAmmo(int mat) {
        this.ammo.set(mat);
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public IntegerProperty metalProperty() {
        return this.metal;
    }

    /**
     * 鋼材を取得します。
     * @return 鋼材
     */
    public int getMetal() {
        return this.metal.get();
    }

    /**
     * 鋼材を設定します。
     * @param 鋼材
     */
    public void setMetal(int mat) {
        this.metal.set(mat);
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public IntegerProperty bouxProperty() {
        return this.boux;
    }

    /**
     * ボーキサイトを取得します。
     * @return ボーキサイト
     */
    public int getBoux() {
        return this.boux.get();
    }

    /**
     * ボーキサイトを設定します。
     * @param ボーキサイト
     */
    public void setBoux(int bonusFlag) {
        this.boux.set(bonusFlag);
    }

    /**
     * ボーナスを取得します。
     * @return ボーナス
     */
    public IntegerProperty bonusProperty() {
        return this.bonusFlag;
    }

    /**
     * ボーナスを取得します。
     * @return ボーナス
     */
    public int getBonusFlag() {
        return this.bonusFlag.get();
    }

    /**
     * ボーナスを設定します。
     * @param ボーナス
     */
    public void setBonusFlag(int bonusFlag) {
        this.bonusFlag.set(bonusFlag);
    }

    /**
     * 進行度を取得します。
     * @return 進行度
     */
    public IntegerProperty progressProperty() {
        return this.progressFlag;
    }

    /**
     * 進行度を取得します。
     * @return 進行度
     */
    public int getProgressFlag() {
        return this.progressFlag.get();
    }

    /**
     * 進行度を設定します。
     * @param 進行度
     */
    public void setProgressFlag(int progressFlag) {
        this.progressFlag.set(progressFlag);
    }
}
