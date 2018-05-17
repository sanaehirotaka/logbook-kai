package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 艦娘
 *
 */
@Data
public class Ship implements Chara, Serializable, Cloneable {

    private static final long serialVersionUID = 3598977177423429679L;

    /** ID */
    private Integer id;

    /** 図鑑番号 */
    private Integer sortno;

    /** 艦船ID */
    private Integer shipId;

    /** Lv */
    private Integer lv;

    /** 経験値 */
    private List<Integer> exp;

    /** HP */
    private Integer nowhp;

    /** 最大HP */
    private Integer maxhp;

    /** 速力 */
    private Integer soku;

    /** 射程 */
    private Integer leng;

    /** 装備 */
    private List<Integer> slot;

    /** 機数 */
    private List<Integer> onslot;

    /** 補強増設 */
    private Integer slotEx;

    /** 改修 */
    private List<Integer> kyouka;

    /** レア度 */
    private Integer backs;

    /** 燃料 */
    private Integer fuel;

    /** 弾薬 */
    private Integer bull;

    /** スロット数 */
    private Integer slotnum;

    /** 入渠時間 */
    private Integer ndockTime;

    /** 入渠消費資材 */
    private List<Integer> ndockItem;

    /** api_srate */
    private Integer srate;

    /** コンディション */
    private Integer cond;

    /** 火力 */
    private List<Integer> karyoku;

    /** 雷装 */
    private List<Integer> raisou;

    /** 対空 */
    private List<Integer> taiku;

    /** 装甲 */
    private List<Integer> soukou;

    /** 回避 */
    private List<Integer> kaihi;

    /** 対潜 */
    private List<Integer> taisen;

    /** 索敵 */
    private List<Integer> sakuteki;

    /** 運 */
    private List<Integer> lucky;

    /** ロック */
    private Boolean locked;

    /** ロック(装備) */
    private Boolean lockedEquip;

    /** 出撃海域 */
    private Integer sallyArea = 0;

    @Override
    public Ship clone() {
        try {
            return (Ship) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * JsonObjectから{@link Ship}を構築します
     *
     * @param json JsonObject
     * @return {@link Ship}
     */
    public static Ship toShip(JsonObject json) {
        Ship bean = new Ship();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_sortno", bean::setSortno)
                .setInteger("api_ship_id", bean::setShipId)
                .setInteger("api_lv", bean::setLv)
                .set("api_exp", bean::setExp, JsonHelper::toIntegerList)
                .setInteger("api_nowhp", bean::setNowhp)
                .setInteger("api_maxhp", bean::setMaxhp)
                .setInteger("api_soku", bean::setSoku)
                .setInteger("api_leng", bean::setLeng)
                .set("api_slot", bean::setSlot, JsonHelper::toIntegerList)
                .set("api_onslot", bean::setOnslot, JsonHelper::toIntegerList)
                .setInteger("api_slot_ex", bean::setSlotEx)
                .set("api_kyouka", bean::setKyouka, JsonHelper::toIntegerList)
                .setInteger("api_backs", bean::setBacks)
                .setInteger("api_fuel", bean::setFuel)
                .setInteger("api_bull", bean::setBull)
                .setInteger("api_slotnum", bean::setSlotnum)
                .setInteger("api_ndock_time", bean::setNdockTime)
                .set("api_ndock_item", bean::setNdockItem, JsonHelper::toIntegerList)
                .setInteger("api_srate", bean::setSrate)
                .setInteger("api_cond", bean::setCond)
                .set("api_karyoku", bean::setKaryoku, JsonHelper::toIntegerList)
                .set("api_raisou", bean::setRaisou, JsonHelper::toIntegerList)
                .set("api_taiku", bean::setTaiku, JsonHelper::toIntegerList)
                .set("api_soukou", bean::setSoukou, JsonHelper::toIntegerList)
                .set("api_kaihi", bean::setKaihi, JsonHelper::toIntegerList)
                .set("api_taisen", bean::setTaisen, JsonHelper::toIntegerList)
                .set("api_sakuteki", bean::setSakuteki, JsonHelper::toIntegerList)
                .set("api_lucky", bean::setLucky, JsonHelper::toIntegerList)
                .setBoolean("api_locked", bean::setLocked)
                .setBoolean("api_locked_equip", bean::setLockedEquip)
                .setInteger("api_sally_area", bean::setSallyArea);
        return bean;
    }
}
