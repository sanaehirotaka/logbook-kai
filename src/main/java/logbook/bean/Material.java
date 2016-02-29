package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_material
 *
 */
public class Material implements Serializable {

    private static final long serialVersionUID = -6919096591550530580L;

    /** api_id */
    private Integer id;

    /** api_value */
    private Integer value;

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
     * api_valueを取得します。
     * @return api_value
     */
    public Integer getValue() {
        return this.value;
    }

    /**
     * api_valueを設定します。
     * @param value api_value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

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
