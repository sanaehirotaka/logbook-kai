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

    /** api_taik */
    private Integer taik;

    /** api_souk */
    private Integer souk;

    /** api_houg */
    private Integer houg;

    /** api_raig */
    private Integer raig;

    /** api_soku */
    private Integer soku;

    /** api_baku */
    private Integer baku;

    /** api_tyku */
    private Integer tyku;

    /** api_tais */
    private Integer tais;

    /** api_atap */
    private Integer atap;

    /** api_houm */
    private Integer houm;

    /** api_raim */
    private Integer raim;

    /** api_houk */
    private Integer houk;

    /** api_raik */
    private Integer raik;

    /** api_bakk */
    private Integer bakk;

    /** api_saku */
    private Integer saku;

    /** api_sakb */
    private Integer sakb;

    /** api_luck */
    private Integer luck;

    /** api_leng */
    private Integer leng;

    /** api_rare */
    private Integer rare;

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
                .setInteger("api_rare", bean::setRare);
        return bean;
    }
}
