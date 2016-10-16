package logbook.bean;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 敵艦
 *
 */
@Data
public class Enemy implements Chara, Serializable, Cloneable {

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
    public Enemy clone() {
        try {
            return (Enemy) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
