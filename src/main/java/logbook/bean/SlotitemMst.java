package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 装備定義
 *
 */
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
     * api_typeを取得します。
     * @return api_type
     */
    public List<Integer> getType() {
        return this.type;
    }

    /**
     * api_typeを設定します。
     * @param type api_type
     */
    public void setType(List<Integer> type) {
        this.type = type;
    }

    /**
     * api_taikを取得します。
     * @return api_taik
     */
    public Integer getTaik() {
        return this.taik;
    }

    /**
     * api_taikを設定します。
     * @param taik api_taik
     */
    public void setTaik(Integer taik) {
        this.taik = taik;
    }

    /**
     * api_soukを取得します。
     * @return api_souk
     */
    public Integer getSouk() {
        return this.souk;
    }

    /**
     * api_soukを設定します。
     * @param souk api_souk
     */
    public void setSouk(Integer souk) {
        this.souk = souk;
    }

    /**
     * api_hougを取得します。
     * @return api_houg
     */
    public Integer getHoug() {
        return this.houg;
    }

    /**
     * api_hougを設定します。
     * @param houg api_houg
     */
    public void setHoug(Integer houg) {
        this.houg = houg;
    }

    /**
     * api_raigを取得します。
     * @return api_raig
     */
    public Integer getRaig() {
        return this.raig;
    }

    /**
     * api_raigを設定します。
     * @param raig api_raig
     */
    public void setRaig(Integer raig) {
        this.raig = raig;
    }

    /**
     * api_sokuを取得します。
     * @return api_soku
     */
    public Integer getSoku() {
        return this.soku;
    }

    /**
     * api_sokuを設定します。
     * @param soku api_soku
     */
    public void setSoku(Integer soku) {
        this.soku = soku;
    }

    /**
     * api_bakuを取得します。
     * @return api_baku
     */
    public Integer getBaku() {
        return this.baku;
    }

    /**
     * api_bakuを設定します。
     * @param baku api_baku
     */
    public void setBaku(Integer baku) {
        this.baku = baku;
    }

    /**
     * api_tykuを取得します。
     * @return api_tyku
     */
    public Integer getTyku() {
        return this.tyku;
    }

    /**
     * api_tykuを設定します。
     * @param tyku api_tyku
     */
    public void setTyku(Integer tyku) {
        this.tyku = tyku;
    }

    /**
     * api_taisを取得します。
     * @return api_tais
     */
    public Integer getTais() {
        return this.tais;
    }

    /**
     * api_taisを設定します。
     * @param tais api_tais
     */
    public void setTais(Integer tais) {
        this.tais = tais;
    }

    /**
     * api_atapを取得します。
     * @return api_atap
     */
    public Integer getAtap() {
        return this.atap;
    }

    /**
     * api_atapを設定します。
     * @param atap api_atap
     */
    public void setAtap(Integer atap) {
        this.atap = atap;
    }

    /**
     * api_houmを取得します。
     * @return api_houm
     */
    public Integer getHoum() {
        return this.houm;
    }

    /**
     * api_houmを設定します。
     * @param houm api_houm
     */
    public void setHoum(Integer houm) {
        this.houm = houm;
    }

    /**
     * api_raimを取得します。
     * @return api_raim
     */
    public Integer getRaim() {
        return this.raim;
    }

    /**
     * api_raimを設定します。
     * @param raim api_raim
     */
    public void setRaim(Integer raim) {
        this.raim = raim;
    }

    /**
     * api_houkを取得します。
     * @return api_houk
     */
    public Integer getHouk() {
        return this.houk;
    }

    /**
     * api_houkを設定します。
     * @param houk api_houk
     */
    public void setHouk(Integer houk) {
        this.houk = houk;
    }

    /**
     * api_raikを取得します。
     * @return api_raik
     */
    public Integer getRaik() {
        return this.raik;
    }

    /**
     * api_raikを設定します。
     * @param raik api_raik
     */
    public void setRaik(Integer raik) {
        this.raik = raik;
    }

    /**
     * api_bakkを取得します。
     * @return api_bakk
     */
    public Integer getBakk() {
        return this.bakk;
    }

    /**
     * api_bakkを設定します。
     * @param bakk api_bakk
     */
    public void setBakk(Integer bakk) {
        this.bakk = bakk;
    }

    /**
     * api_sakuを取得します。
     * @return api_saku
     */
    public Integer getSaku() {
        return this.saku;
    }

    /**
     * api_sakuを設定します。
     * @param saku api_saku
     */
    public void setSaku(Integer saku) {
        this.saku = saku;
    }

    /**
     * api_sakbを取得します。
     * @return api_sakb
     */
    public Integer getSakb() {
        return this.sakb;
    }

    /**
     * api_sakbを設定します。
     * @param sakb api_sakb
     */
    public void setSakb(Integer sakb) {
        this.sakb = sakb;
    }

    /**
     * api_luckを取得します。
     * @return api_luck
     */
    public Integer getLuck() {
        return this.luck;
    }

    /**
     * api_luckを設定します。
     * @param luck api_luck
     */
    public void setLuck(Integer luck) {
        this.luck = luck;
    }

    /**
     * api_lengを取得します。
     * @return api_leng
     */
    public Integer getLeng() {
        return this.leng;
    }

    /**
     * api_lengを設定します。
     * @param leng api_leng
     */
    public void setLeng(Integer leng) {
        this.leng = leng;
    }

    /**
     * api_rareを取得します。
     * @return api_rare
     */
    public Integer getRare() {
        return this.rare;
    }

    /**
     * api_rareを設定します。
     * @param rare api_rare
     */
    public void setRare(Integer rare) {
        this.rare = rare;
    }

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
