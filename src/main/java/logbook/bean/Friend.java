package logbook.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 友軍
 *
 */
@Data
public class Friend implements Chara, Serializable, Cloneable {

    private static final long serialVersionUID = -4222845234889789870L;

    /** 艦船ID */
    private Integer shipId;

    /** Lv */
    private Integer lv;

    /** HP */
    private Integer nowhp;

    /** 最大HP */
    private Integer maxhp;

    /** 装備 */
    private List<Integer> slot;

    @Override
    public Friend clone() {
        try {
            return (Friend) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
