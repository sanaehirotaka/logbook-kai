package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 装備定義
 *
 */
@Data
public class SlotitemMst implements Serializable {

    private static final long serialVersionUID = 383813721548687786L;

    /** api_id */
    private Integer id;

    /** api_sortno */
    private Integer sortno;

    /** api_name */
    private String name;

    /** api_type */
    private List<Integer> type;

    /** api_taik(耐久) */
    private Integer taik;

    /** api_souk(装甲) */
    private Integer souk;

    /** api_houg(火力) */
    private Integer houg;

    /** api_raig(雷装) */
    private Integer raig;

    /** api_soku(速力) */
    private Integer soku;

    /** api_baku(爆装) */
    private Integer baku;

    /** api_tyku(対空) */
    private Integer tyku;

    /** api_tais(対潜) */
    private Integer tais;

    /** api_atap(?) */
    private Integer atap;

    /** api_houm(命中) */
    private Integer houm;

    /** api_raim(雷撃命中) */
    private Integer raim;

    /** api_houk(回避) */
    private Integer houk;

    /** api_raik(雷撃回避) */
    private Integer raik;

    /** api_bakk(爆撃回避) */
    private Integer bakk;

    /** api_saku(索敵) */
    private Integer saku;

    /** api_sakb(索敵妨害) */
    private Integer sakb;

    /** api_luck(運) */
    private Integer luck;

    /** api_leng(射程) */
    private Integer leng;

    /** api_rare(レアリティ) */
    private Integer rare;

    /** api_distance(航続距離) */
    private Integer distance;

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * JsonObjectから{@link SlotitemMst}を構築します
     *
     * @param json JsonObject
     * @return {@link SlotitemMst}
     */
    public static SlotitemMst toSlotitem(JsonObject json) {
        SlotitemMst bean = new SlotitemMst();
        JsonHelper.bind(json)
                .setInteger("api_id", bean::setId)
                .setInteger("api_sortno", bean::setSortno)
                .setString("api_name", bean::setName)
                .set("api_type", bean::setType, JsonHelper::toIntegerList)
                .setInteger("api_taik", bean::setTaik)
                .setInteger("api_souk", bean::setSouk)
                .setInteger("api_houg", bean::setHoug)
                .setInteger("api_raig", bean::setRaig)
                .setInteger("api_soku", bean::setSoku)
                .setInteger("api_baku", bean::setBaku)
                .setInteger("api_tyku", bean::setTyku)
                .setInteger("api_tais", bean::setTais)
                .setInteger("api_atap", bean::setAtap)
                .setInteger("api_houm", bean::setHoum)
                .setInteger("api_raim", bean::setRaim)
                .setInteger("api_houk", bean::setHouk)
                .setInteger("api_raik", bean::setRaik)
                .setInteger("api_bakk", bean::setBakk)
                .setInteger("api_saku", bean::setSaku)
                .setInteger("api_sakb", bean::setSakb)
                .setInteger("api_luck", bean::setLuck)
                .setInteger("api_leng", bean::setLeng)
                .setInteger("api_rare", bean::setRare)
                .setInteger("api_distance", bean::setDistance);
        return bean;
    }
}
