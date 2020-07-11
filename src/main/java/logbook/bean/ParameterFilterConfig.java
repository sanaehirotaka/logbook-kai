package logbook.bean;

import logbook.internal.Operator;
import lombok.Data;

/**
 * パラメータフィルターの設定
 */
@Data
public class ParameterFilterConfig {
    /** 有効 */
    private boolean enabled;
    /** 選ばれていたパラメータ */
    private String name;
    /** フィールドの値 */
    private Integer value;
    /** 選択のインデックス */
    private Integer valueChoice;
    /** 比較タイプ */
    private Operator type;
}