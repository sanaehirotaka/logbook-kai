package logbook.internal.gui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import logbook.internal.BattleLogs.Unit;
import lombok.Getter;
import lombok.Setter;

/**
 * 集計行
 *
 */
public class MissionLogCollect {

    /** 集計  */
    private StringProperty unit;

    /** 大成功 */
    private IntegerProperty successGood;

    /** 成功 */
    private IntegerProperty success;

    /** 失敗 */
    private IntegerProperty fail;

    /** 集計単位 */
    @Getter
    @Setter
    private Unit collectUnit;

    /** 遠征名 */
    @Getter
    @Setter
    private String name;

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
     * 大成功を設定します。
     * @param successGood 大成功
     */
    public void setSuccessGood(Integer successGood) {
        this.successGood = new SimpleIntegerProperty(successGood);
    }

    /**
     * 大成功を取得します。
     * @return 大成功
     */
    public IntegerProperty successGoodProperty() {
        return this.successGood;
    }

    /**
     * 成功を設定します。
     * @param success 成功
     */
    public void setSuccess(Integer success) {
        this.success = new SimpleIntegerProperty(success);
    }

    /**
     * 成功を取得します。
     * @return 成功
     */
    public IntegerProperty successProperty() {
        return this.success;
    }

    /**
     * 失敗を設定します。
     * @param fail 失敗
     */
    public void setFail(Integer fail) {
        this.fail = new SimpleIntegerProperty(fail);
    }

    /**
     * 失敗を取得します。
     * @return 失敗
     */
    public IntegerProperty failProperty() {
        return this.fail;
    }
}