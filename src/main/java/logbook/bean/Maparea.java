package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_maparea
 *
 */
@Data
public class Maparea implements Serializable {

    private static final long serialVersionUID = 3065470938786532030L;

    /** api_id */
    private Integer id;

    /** api_name */
    private String name;

    /** api_type */
    private Integer type;

    /**
     * JsonObjectから{@link Maparea}を構築します
     *
     * @param json JsonObject
     * @return {@link Maparea}
     */
    public static Maparea toMaparea(JsonObject json) {
        Maparea bean = new Maparea();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setString("api_name", bean::setName)
                .setInteger("api_type", bean::setType);
        return bean;
    }
}
