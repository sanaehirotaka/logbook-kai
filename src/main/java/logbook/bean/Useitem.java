package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_useitem
 */
@Data
public class Useitem implements Serializable {

    private static final long serialVersionUID = -3290324243327123224L;

    /** api_id */
    private Integer id;

    /** api_name */
    private String name;

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * JsonObjectから{@link Useitem}を構築します
     *
     * @param json JsonObject
     * @return {@link Useitem}
     */
    public static Useitem toMission(JsonObject json) {
        Useitem bean = new Useitem();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setString("api_name", bean::setName);
        return bean;
    }
}
