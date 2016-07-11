package logbook.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * api_get_member/kdock
 *
 */
@Data
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
}
