package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_material
 *
 */
@Data
public class Material implements Serializable {

    private static final long serialVersionUID = -6919096591550530580L;

    /** api_id */
    private Integer id;

    /** api_value */
    private Integer value;

    /**
     * JsonObjectから{@link Material}を構築します
     *
     * @param json JsonObject
     * @return {@link Material}
     */
    public static Material toMaterial(JsonObject json) {
        Material bean = new Material();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_value", bean::setValue);
        return bean;
    }
}
