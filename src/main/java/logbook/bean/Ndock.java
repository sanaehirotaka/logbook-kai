package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_ndock
 *
 */
@Data
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
