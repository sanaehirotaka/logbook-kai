package logbook.bean;

import java.io.Serializable;

/**
 * api_get_member/kdock
 *
 */
public class Kdock implements Serializable {

    private static final long serialVersionUID = -7774442020876701833L;

    /** api_id */
    private Integer id;

    /** api_state */
    private Integer state;

    /** api_created_ship_id */
    private Integer createdShipId;

    /** api_complete_time */
    private Integer completeTime;

    /** api_complete_time_str */
    private String completeTimeStr;

    /** api_item1 */
    private Integer item1;

    /** api_item2 */
    private Integer item2;

    /** api_item3 */
    private Integer item3;

    /** api_item4 */
    private Integer item4;

    /** api_item5 */
    private Integer item5;

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
     * api_created_ship_idを取得します。
     * @return api_created_ship_id
     */
    public Integer getCreatedShipId() {
        return this.createdShipId;
    }

    /**
     * api_created_ship_idを設定します。
     * @param createdShipId api_created_ship_id
     */
    public void setCreatedShipId(Integer createdShipId) {
        this.createdShipId = createdShipId;
    }

    /**
     * api_complete_timeを取得します。
     * @return api_complete_time
     */
    public Integer getCompleteTime() {
        return this.completeTime;
    }

    /**
     * api_complete_timeを設定します。
     * @param completeTime api_complete_time
     */
    public void setCompleteTime(Integer completeTime) {
        this.completeTime = completeTime;
    }

    /**
     * api_complete_time_strを取得します。
     * @return api_complete_time_str
     */
    public String getCompleteTimeStr() {
        return this.completeTimeStr;
    }

    /**
     * api_complete_time_strを設定します。
     * @param completeTimeStr api_complete_time_str
     */
    public void setCompleteTimeStr(String completeTimeStr) {
        this.completeTimeStr = completeTimeStr;
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
     * api_item5を取得します。
     * @return api_item5
     */
    public Integer getItem5() {
        return this.item5;
    }

    /**
     * api_item5を設定します。
     * @param item5 api_item5
     */
    public void setItem5(Integer item5) {
        this.item5 = item5;
    }
}
