package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_mst_mission
 *
 */
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
     * api_detailsを取得します。
     * @return api_details
     */
    public String getDetails() {
        return this.details;
    }

    /**
     * api_detailsを設定します。
     * @param details api_details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * api_timeを取得します。
     * @return api_time
     */
    public Integer getTime() {
        return this.time;
    }

    /**
     * api_timeを設定します。
     * @param time api_time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

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
