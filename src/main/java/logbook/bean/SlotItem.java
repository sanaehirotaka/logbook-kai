package logbook.bean;

import java.io.Serializable;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 装備
 *
 */
@Data
public class SlotItem implements Serializable {

    private static final long serialVersionUID = -5902864924857205128L;

    /** api_id */
    private Integer id;

    /** api_level */
    private Integer level;

    /** api_alv */
    private Integer alv;

    /** api_locked */
    private Boolean locked;

    /** api_slotitem_id */
    private Integer slotitemId;

    /**
     * JsonObjectから{@link SlotItem}を構築します
     *
     * @param json JsonObject
     * @return {@link SlotItem}
     */
    public static SlotItem toSlotItem(JsonObject json) {
        SlotItem bean = new SlotItem();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_level", bean::setLevel)
                .setInteger("api_alv", bean::setAlv)
                .setBoolean("api_locked", bean::setLocked)
                .setInteger("api_slotitem_id", bean::setSlotitemId);
        return bean;
    }
}
