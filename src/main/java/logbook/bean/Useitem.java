package logbook.bean;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_mst_useitem
 */
public class Useitem {

    /** api_id */
    private Integer id;

    /** api_name */
    private String name;

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
     * api_nameを取得します。
     * @return api_name
     */
    public String getName() {
        return this.name;
    }

    /**
     * api_nameを設定します。
     * @param name api_name
     */
    public void setName(String name) {
        this.name = name;
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
                .set("api_id", bean::setId, JsonHelper::toInteger)
                .set("api_name", bean::setName, JsonHelper::toString);
        return bean;
    }
}
