package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.internal.JsonHelper;
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

    /**
     * JsonObjectから{@link Kdock}を構築します
     *
     * @param json JsonObject
     * @return {@link Kdock}
     */
    public static Kdock toKdock(JsonValue json) {
        Kdock bean = new Kdock();
        JsonHelper.bind((JsonObject) json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_state", bean::setState)
                .setInteger("api_created_ship_id", bean::setCreatedShipId)
                .setInteger("api_complete_time", bean::setCompleteTime)
                .setString("api_complete_time_str", bean::setCompleteTimeStr)
                .setInteger("api_item1", bean::setItem1)
                .setInteger("api_item2", bean::setItem2)
                .setInteger("api_item3", bean::setItem3)
                .setInteger("api_item4", bean::setItem4)
                .setInteger("api_item5", bean::setItem5);
        return bean;
    }
}
