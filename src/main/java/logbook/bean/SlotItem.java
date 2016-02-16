package logbook.bean;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * slot_item
 *
 */
public class SlotItem {

    /** api_id */
    private Integer id;

    /** api_level */
    private Integer level;

    /** api_locked */
    private Boolean locked;

    /** api_slotitem_id */
    private Integer slotitemId;

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
     * api_levelを取得します。
     * @return api_level
     */
    public Integer getLevel() {
        return this.level;
    }

    /**
     * api_levelを設定します。
     * @param level api_level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * api_lockedを取得します。
     * @return api_locked
     */
    public Boolean getLocked() {
        return this.locked;
    }

    /**
     * api_lockedを設定します。
     * @param locked api_locked
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * api_slotitem_idを取得します。
     * @return api_slotitem_id
     */
    public Integer getSlotitemId() {
        return this.slotitemId;
    }

    /**
     * api_slotitem_idを設定します。
     * @param slotitemId api_slotitem_id
     */
    public void setSlotitemId(Integer slotitemId) {
        this.slotitemId = slotitemId;
    }

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
                .setBoolean("api_locked", bean::setLocked)
                .setInteger("api_slotitem_id", bean::setSlotitemId);
        return bean;
    }
}
