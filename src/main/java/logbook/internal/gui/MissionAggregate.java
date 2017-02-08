package logbook.internal.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 右下ペインの集計行
 *
 */
public class MissionAggregate {

    /** 資材 */
    private StringProperty resource;

    /** 個数 */
    private IntegerProperty count;

    /** 平均 */
    private DoubleProperty average;

    /**
     * 資材を設定します。
     * @param resource 資材
     */
    public void setResource(String resource) {
        this.resource = new SimpleStringProperty(resource);
    }

    /**
     * 資材を取得します。
     * @return 資材
     */
    public StringProperty resourceProperty() {
        return this.resource;
    }

    /**
     * 個数を設定します。
     * @param count 個数
     */
    public void setCount(Integer count) {
        this.count = new SimpleIntegerProperty(count);
    }

    /**
     * 個数を取得します。
     * @return 個数
     */
    public IntegerProperty countProperty() {
        return this.count;
    }

    /**
     * 平均を設定します。
     * @param average 平均
     */
    public void setAverage(Double average) {
        this.average = new SimpleDoubleProperty(average);
    }

    /**
     * 平均を取得します。
     * @return 平均
     */
    public DoubleProperty averageProperty() {
        return this.average;
    }
}