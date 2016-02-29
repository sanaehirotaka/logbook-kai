package logbook.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_mst_stype
 *
 */
public class Stype implements Serializable {

    private static final long serialVersionUID = -8213373063199467493L;

    /** api_id */
    private Integer id;

    /** api_sortno */
    private Integer sortno;

    /** api_name */
    private String name;

    /** api_scnt */
    private Integer scnt;

    /** api_kcnt */
    private Integer kcnt;

    /** api_equip_type */
    private Map<Integer, Integer> equipType;

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
     * api_sortnoを取得します。
     * @return api_sortno
     */
    public Integer getSortno() {
        return this.sortno;
    }

    /**
     * api_sortnoを設定します。
     * @param sortno api_sortno
     */
    public void setSortno(Integer sortno) {
        this.sortno = sortno;
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
     * api_scntを取得します。
     * @return api_scnt
     */
    public Integer getScnt() {
        return this.scnt;
    }

    /**
     * api_scntを設定します。
     * @param scnt api_scnt
     */
    public void setScnt(Integer scnt) {
        this.scnt = scnt;
    }

    /**
     * api_kcntを取得します。
     * @return api_kcnt
     */
    public Integer getKcnt() {
        return this.kcnt;
    }

    /**
     * api_kcntを設定します。
     * @param kcnt api_kcnt
     */
    public void setKcnt(Integer kcnt) {
        this.kcnt = kcnt;
    }

    /**
     * api_equip_typeを取得します。
     * @return api_equip_type
     */
    public Map<Integer, Integer> getEquipType() {
        return this.equipType;
    }

    /**
     * api_equip_typeを設定します。
     * @param equipType api_equip_type
     */
    public void setEquipType(Map<Integer, Integer> equipType) {
        this.equipType = equipType;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * JsonObjectから{@link Stype}を構築します
     *
     * @param json JsonObject
     * @return {@link Stype}
     */
    public static Stype toStype(JsonObject json) {
        // api_equip_type変換関数
        Function<JsonObject, Map<Integer, Integer>> equipTypeFunc = t -> JsonHelper
                .toMap(t, Integer::valueOf, JsonHelper::toInteger);

        Stype bean = new Stype();
        JsonHelper.bind(json)
                .set("api_id", bean::setId, JsonHelper::toInteger)
                .set("api_sortno", bean::setSortno, JsonHelper::toInteger)
                .set("api_name", bean::setName, JsonHelper::toString)
                .set("api_scnt", bean::setScnt, JsonHelper::toInteger)
                .set("api_kcnt", bean::setKcnt, JsonHelper::toInteger)
                .set("api_equip_type", bean::setEquipType, equipTypeFunc);
        return bean;
    }
}
