package logbook.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 艦娘・敵などのキャラクターを表す
 */
public interface Chara extends Cloneable {

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
     * HPを設定します。
     * @param nowhp HP
     */
    void setNowhp(Integer nowhp);

    /**
     * 最大HPを取得します。
     * @return 最大HP
     */
    Integer getMaxhp();

    /**
     * 最大HPを設定します。
     * @param maxhp 最大HP
     */
    void setMaxhp(Integer maxhp);

    /**
     * 装備を取得します。
     * @return 装備
     */
    List<Integer> getSlot();

    /**
     * このオブジェクトの複製を返します
     * @return キャラクター
     */
    Chara clone();

    /**
     * このオブジェクトが{@link Ship}のインスタンスである場合trueを返します。
     * @return このオブジェクトが{@link Ship}のインスタンスである場合true
     */
    default boolean isShip() {
        return false;
    }

    /**
     * このオブジェクトを{@link Ship}のインスタンスとして返します。
     * @return {@link Ship}
     */
    @JsonIgnore
    default Ship asShip() {
        throw new IllegalStateException(this + " is not an Ship");
    }

    /**
     * このオブジェクトが{@link Friend}のインスタンスである場合trueを返します。
     * @return このオブジェクトが{@link Friend}のインスタンスである場合true
     */
    @JsonIgnore
    default boolean isFriend() {
        return false;
    }

    /**
     * このオブジェクトを{@link Friend}のインスタンスとして返します。
     * @return {@link Friend}
     */
    @JsonIgnore
    default Friend asFriend() {
        throw new IllegalStateException(this + " is not an Friend");
    }

    /**
     * このオブジェクトが{@link Enemy}のインスタンスである場合trueを返します。
     * @return このオブジェクトが{@link Enemy}のインスタンスである場合true
     */
    @JsonIgnore
    default boolean isEnemy() {
        return false;
    }

    /**
     * このオブジェクトを{@link Enemy}のインスタンスとして返します。
     * @return {@link Enemy}
     */
    @JsonIgnore
    default Enemy asEnemy() {
        throw new IllegalStateException(this + " is not an Enemy");
    }

}
