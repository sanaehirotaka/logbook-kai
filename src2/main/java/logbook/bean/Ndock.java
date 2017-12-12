package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_ndock
 *
 */
public class Ndock implements Serializable {

    private static final long serialVersionUID = 1739445888580394927L;

    /** api_id */
    private Integer id;

    /** api_complete_time */
    private Long completeTime;

    /** api_item1 */
    private Integer item1;

    /** api_item2 */
    private Integer item2;

    /** api_item3 */
    private Integer item3;

    /** api_item4 */
    private Integer item4;

    /** api_ship_id */
    private Integer shipId;

    /** api_state */
    private Integer state;

    /**
     * api_idを取得します。
     * @return api_id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * api_idを設定します。
     * @param id api_id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * api_complete_timeを取得します。
     * @return api_complete_time
     */
    public Long getCompleteTime() {
        return this.completeTime;
    }

    /**
     * api_complete_timeを設定します。
     * @param completeTime api_complete_time
     */
    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    /**
     * api_item1を取得します。
     * @return api_item1
     */
    public Integer getItem1() {
        return this.item1;
    }

    /**
     * api_item1を設定します。
     * @param item1 api_item1
     */
    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    /**
     * api_item2を取得します。
     * @return api_item2
     */
    public Integer getItem2() {
        return this.item2;
    }

    /**
     * api_item2を設定します。
     * @param item2 api_item2
     */
    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    /**
     * api_item3を取得します。
     * @return api_item3
     */
    public Integer getItem3() {
        return this.item3;
    }

    /**
     * api_item3を設定します。
     * @param item3 api_item3
     */
    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    /**
     * api_item4を取得します。
     * @return api_item4
     */
    public Integer getItem4() {
        return this.item4;
    }

    /**
     * api_item4を設定します。
     * @param item4 api_item4
     */
    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    /**
     * api_ship_idを取得します。
     * @return api_ship_id
     */
    public Integer getShipId() {
        return this.shipId;
    }

    /**
     * api_ship_idを設定します。
     * @param shipId api_ship_id
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    /**
     * api_stateを取得します。
     * @return api_state
     */
    public Integer getState() {
        return this.state;
    }

    /**
     * api_stateを設定します。
     * @param state api_state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * JsonObjectから{@link Ndock}を構築します
     *
     * @param json JsonObject
     * @return {@link Ndock}
     */
    public static Ndock toNdock(JsonObject json) {
        Ndock bean = new Ndock();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setLong("api_complete_time", bean::setCompleteTime)
                .setInteger("api_item1", bean::setItem1)
                .setInteger("api_item2", bean::setItem2)
                .setInteger("api_item3", bean::setItem3)
                .setInteger("api_item4", bean::setItem4)
                .setInteger("api_ship_id", bean::setShipId)
                .setInteger("api_state", bean::setState);
        return bean;
    }
}
