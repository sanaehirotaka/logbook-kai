package logbook.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_stype
 *
 */
@Data
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
                .setInteger("api_id", bean::setId)
                .setInteger("api_sortno", bean::setSortno)
                .setString("api_name", bean::setName)
                .setInteger("api_scnt", bean::setScnt)
                .setInteger("api_kcnt", bean::setKcnt)
                .set("api_equip_type", bean::setEquipType, equipTypeFunc);
        return bean;
    }
}
