package logbook.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 敵艦
 *
 */
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

    /** 強化 */
    private List<Integer> kyouka;

    /**
     * 艦船IDを取得します。
     * @return 艦船ID
     */
    @Override
    public Integer getShipId() {
        return this.shipId;
    }

    /**
     * 艦船IDを設定します。
     * @param shipId 艦船ID
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    @Override
    public Integer getLv() {
        return this.lv;
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(Integer lv) {
        this.lv = lv;
    }

    /**
     * HPを取得します。
     * @return HP
     */
    @Override
    public Integer getNowhp() {
        return this.nowhp;
    }

    /**
     * HPを設定します。
     * @param nowhp HP
     */
    public void setNowhp(Integer nowhp) {
        this.nowhp = nowhp;
    }

    /**
     * 最大HPを取得します。
     * @return 最大HP
     */
    @Override
    public Integer getMaxhp() {
        return this.maxhp;
    }

    /**
     * 最大HPを設定します。
     * @param maxhp 最大HP
     */
    public void setMaxhp(Integer maxhp) {
        this.maxhp = maxhp;
    }

    /**
     * 装備を取得します。
     * @return 装備
     */
    @Override
    public List<Integer> getSlot() {
        return this.slot;
    }

    /**
     * 装備を設定します。
     * @param slot 装備
     */
    public void setSlot(List<Integer> slot) {
        this.slot = slot;
    }

    /**
     * 強化を取得します。
     * @return 強化
     */
    public List<Integer> getKyouka() {
        return this.kyouka;
    }

    /**
     * 強化を設定します。
     * @param kyouka 強化
     */
    public void setKyouka(List<Integer> kyouka) {
        this.kyouka = kyouka;
    }

    @Override
    public Enemy clone() {
        try {
            return (Enemy) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
