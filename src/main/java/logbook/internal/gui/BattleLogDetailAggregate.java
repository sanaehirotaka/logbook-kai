package logbook.internal.gui;

import java.util.StringJoiner;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ログの集計
 */
public class BattleLogDetailAggregate {

    /** 種類 */
    private StringProperty name = new SimpleStringProperty();

    /** 合計 */
    private LongProperty count = new SimpleLongProperty();

    /** 割合 */
    private DoubleProperty ratio = new SimpleDoubleProperty();

    /**
     * 種類を取得します。
     * @return 種類
     */
    public StringProperty nameProperty() {
        return this.name;
    }

    /**
     * 種類を取得します。
     * @return 種類
     */
    public String getName() {
        return this.name.get();
    }

    /**
     * 種類を設定します。
     * @param name 種類
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * 合計を取得します。
     * @return 合計
     */
    public LongProperty countProperty() {
        return this.count;
    }

    /**
     * 合計を取得します。
     * @return 合計
     */
    public Long getCount() {
        return this.count.get();
    }

    /**
     * 合計を設定します。
     * @param count 合計
     */
    public void setCount(Long count) {
        this.count.set(count);
    }

    /**
     * 割合を取得します。
     * @return 割合
     */
    public DoubleProperty ratioProperty() {
        return this.ratio;
    }

    /**
     * 割合を取得します。
     * @return 割合
     */
    public Double getRatio() {
        return this.ratio.get();
    }

    /**
     * 割合を設定します。
     * @param ratio 割合
     */
    public void setRatio(Double ratio) {
        this.ratio.set(ratio);
    }

    @Override
    public String toString() {
        return new StringJoiner("\t")
                .add(this.name.get())
                .add(String.valueOf(this.count.get()))
                .add(String.valueOf(this.ratio.get()))
                .toString();
    }
}
