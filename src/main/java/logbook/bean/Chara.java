package logbook.bean;

import java.util.List;

/**
 * 艦娘・敵などのキャラクターを表す
 */
public interface Chara {

    /**
     * 艦船IDを取得します。
     * @return 艦船ID
     */
    Integer getShipId();

    /**
     * Lvを取得します。
     * @return Lv
     */
    Integer getLv();

    /**
     * HPを取得します。
     * @return HP
     */
    Integer getNowhp();

    /**
     * 最大HPを取得します。
     * @return 最大HP
     */
    Integer getMaxhp();

    /**
     * 装備を取得します。
     * @return 装備
     */
    List<Integer> getSlot();
}
