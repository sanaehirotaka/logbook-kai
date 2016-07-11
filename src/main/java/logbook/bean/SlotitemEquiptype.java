package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_slotitem_equiptype
 *
 */
@Data
public class SlotitemEquiptype implements Serializable {

    private static final long serialVersionUID = 6987412391631651270L;

    /** api_id */
    private Integer id;

    /** api_name */
    private String name;

    /** api_show_flg */
    private Integer showFlg;

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * JsonObjectから{@link SlotitemEquiptype}を構築します
     *
     * @param json JsonObject
     * @return {@link SlotitemEquiptype}
     */
    public static SlotitemEquiptype toSlotitemEquiptype(JsonObject json) {
        SlotitemEquiptype bean = new SlotitemEquiptype();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setString("api_name", bean::setName)
                .setInteger("api_show_flg", bean::setShowFlg);
        return bean;
    }
}
