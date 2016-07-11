package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_mission
 *
 */
@Data
public class Mission implements Serializable {

    private static final long serialVersionUID = 7503112116568598849L;

    /** api_id */
    private Integer id;

    /** api_name */
    private String name;

    /** api_details */
    private String details;

    /** api_time */
    private Integer time;

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * JsonObjectから{@link Mission}を構築します
     *
     * @param json JsonObject
     * @return {@link Mission}
     */
    public static Mission toMission(JsonObject json) {
        Mission bean = new Mission();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setString("api_name", bean::setName)
                .setString("api_details", bean::setDetails)
                .setInteger("api_time", bean::setTime);
        return bean;
    }
}
