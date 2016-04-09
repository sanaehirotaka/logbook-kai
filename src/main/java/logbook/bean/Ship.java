package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 艦娘
 *
 */
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

    /**
     * IDを取得します。
     * @return ID
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 図鑑番号を取得します。
     * @return 図鑑番号
     */
    public Integer getSortno() {
        return this.sortno;
    }

    /**
     * 図鑑番号を設定します。
     * @param sortno 図鑑番号
     */
    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    /**
     * 艦船IDを取得します。
     * @return 艦船ID
     */
    @Override
    public Integer getShipId() {
        return this.shipId;
    }

    /**
     * 艦船IDを設定します。
     * @param shipId 艦船ID
     */
    public void setShipId(Integer shipId) {
        this.shipId = shipId;
    }

    /**
     * Lvを取得します。
     * @return Lv
     */
    @Override
    public Integer getLv() {
        return this.lv;
    }

    /**
     * Lvを設定します。
     * @param lv Lv
     */
    public void setLv(Integer lv) {
        this.lv = lv;
    }

    /**
     * 経験値を取得します。
     * @return 経験値
     */
    public List<Integer> getExp() {
        return this.exp;
    }

    /**
     * 経験値を設定します。
     * @param exp 経験値
     */
    public void setExp(List<Integer> exp) {
        this.exp = exp;
    }

    /**
     * HPを取得します。
     * @return HP
     */
    @Override
    public Integer getNowhp() {
        return this.nowhp;
    }

    /**
     * HPを設定します。
     * @param nowhp HP
     */
    public void setNowhp(Integer nowhp) {
        this.nowhp = nowhp;
    }

    /**
     * 最大HPを取得します。
     * @return 最大HP
     */
    @Override
    public Integer getMaxhp() {
        return this.maxhp;
    }

    /**
     * 最大HPを設定します。
     * @param maxhp 最大HP
     */
    public void setMaxhp(Integer maxhp) {
        this.maxhp = maxhp;
    }

    /**
     * 射程を取得します。
     * @return 射程
     */
    public Integer getLeng() {
        return this.leng;
    }

    /**
     * 射程を設定します。
     * @param leng 射程
     */
    public void setLeng(Integer leng) {
        this.leng = leng;
    }

    /**
     * 装備を取得します。
     * @return 装備
     */
    @Override
    public List<Integer> getSlot() {
        return this.slot;
    }

    /**
     * 装備を設定します。
     * @param slot 装備
     */
    public void setSlot(List<Integer> slot) {
        this.slot = slot;
    }

    /**
     * 機数を取得します。
     * @return 機数
     */
    public List<Integer> getOnslot() {
        return this.onslot;
    }

    /**
     * 機数を設定します。
     * @param onslot 機数
     */
    public void setOnslot(List<Integer> onslot) {
        this.onslot = onslot;
    }

    /**
     * 補強増設を取得します。
     * @return 補強増設
     */
    public Integer getSlotEx() {
        return this.slotEx;
    }

    /**
     * 補強増設を設定します。
     * @param slotEx 補強増設
     */
    public void setSlotEx(Integer slotEx) {
        this.slotEx = slotEx;
    }

    /**
     * 改修を取得します。
     * @return 改修
     */
    public List<Integer> getKyouka() {
        return this.kyouka;
    }

    /**
     * 改修を設定します。
     * @param kyouka 改修
     */
    public void setKyouka(List<Integer> kyouka) {
        this.kyouka = kyouka;
    }

    /**
     * レア度を取得します。
     * @return レア度
     */
    public Integer getBacks() {
        return this.backs;
    }

    /**
     * レア度を設定します。
     * @param backs レア度
     */
    public void setBacks(Integer backs) {
        this.backs = backs;
    }

    /**
     * 燃料を取得します。
     * @return 燃料
     */
    public Integer getFuel() {
        return this.fuel;
    }

    /**
     * 燃料を設定します。
     * @param fuel 燃料
     */
    public void setFuel(Integer fuel) {
        this.fuel = fuel;
    }

    /**
     * 弾薬を取得します。
     * @return 弾薬
     */
    public Integer getBull() {
        return this.bull;
    }

    /**
     * 弾薬を設定します。
     * @param bull 弾薬
     */
    public void setBull(Integer bull) {
        this.bull = bull;
    }

    /**
     * スロット数を取得します。
     * @return スロット数
     */
    public Integer getSlotnum() {
        return this.slotnum;
    }

    /**
     * スロット数を設定します。
     * @param slotnum スロット数
     */
    public void setSlotnum(Integer slotnum) {
        this.slotnum = slotnum;
    }

    /**
     * 入渠時間を取得します。
     * @return 入渠時間
     */
    public Integer getNdockTime() {
        return this.ndockTime;
    }

    /**
     * 入渠時間を設定します。
     * @param ndockTime 入渠時間
     */
    public void setNdockTime(Integer ndockTime) {
        this.ndockTime = ndockTime;
    }

    /**
     * 入渠消費資材を取得します。
     * @return 入渠消費資材
     */
    public List<Integer> getNdockItem() {
        return this.ndockItem;
    }

    /**
     * 入渠消費資材を設定します。
     * @param ndockItem 入渠消費資材
     */
    public void setNdockItem(List<Integer> ndockItem) {
        this.ndockItem = ndockItem;
    }

    /**
     * api_srateを取得します。
     * @return api_srate
     */
    public Integer getSrate() {
        return this.srate;
    }

    /**
     * api_srateを設定します。
     * @param srate api_srate
     */
    public void setSrate(Integer srate) {
        this.srate = srate;
    }

    /**
     * コンディションを取得します。
     * @return コンディション
     */
    public Integer getCond() {
        return this.cond;
    }

    /**
     * コンディションを設定します。
     * @param cond コンディション
     */
    public void setCond(Integer cond) {
        this.cond = cond;
    }

    /**
     * 火力を取得します。
     * @return 火力
     */
    public List<Integer> getKaryoku() {
        return this.karyoku;
    }

    /**
     * 火力を設定します。
     * @param karyoku 火力
     */
    public void setKaryoku(List<Integer> karyoku) {
        this.karyoku = karyoku;
    }

    /**
     * 雷装を取得します。
     * @return 雷装
     */
    public List<Integer> getRaisou() {
        return this.raisou;
    }

    /**
     * 雷装を設定します。
     * @param raisou 雷装
     */
    public void setRaisou(List<Integer> raisou) {
        this.raisou = raisou;
    }

    /**
     * 対空を取得します。
     * @return 対空
     */
    public List<Integer> getTaiku() {
        return this.taiku;
    }

    /**
     * 対空を設定します。
     * @param taiku 対空
     */
    public void setTaiku(List<Integer> taiku) {
        this.taiku = taiku;
    }

    /**
     * 装甲を取得します。
     * @return 装甲
     */
    public List<Integer> getSoukou() {
        return this.soukou;
    }

    /**
     * 装甲を設定します。
     * @param soukou 装甲
     */
    public void setSoukou(List<Integer> soukou) {
        this.soukou = soukou;
    }

    /**
     * 回避を取得します。
     * @return 回避
     */
    public List<Integer> getKaihi() {
        return this.kaihi;
    }

    /**
     * 回避を設定します。
     * @param kaihi 回避
     */
    public void setKaihi(List<Integer> kaihi) {
        this.kaihi = kaihi;
    }

    /**
     * 対潜を取得します。
     * @return 対潜
     */
    public List<Integer> getTaisen() {
        return this.taisen;
    }

    /**
     * 対潜を設定します。
     * @param taisen 対潜
     */
    public void setTaisen(List<Integer> taisen) {
        this.taisen = taisen;
    }

    /**
     * 索敵を取得します。
     * @return 索敵
     */
    public List<Integer> getSakuteki() {
        return this.sakuteki;
    }

    /**
     * 索敵を設定します。
     * @param sakuteki 索敵
     */
    public void setSakuteki(List<Integer> sakuteki) {
        this.sakuteki = sakuteki;
    }

    /**
     * 運を取得します。
     * @return 運
     */
    public List<Integer> getLucky() {
        return this.lucky;
    }

    /**
     * 運を設定します。
     * @param lucky 運
     */
    public void setLucky(List<Integer> lucky) {
        this.lucky = lucky;
    }

    /**
     * ロックを取得します。
     * @return ロック
     */
    public Boolean getLocked() {
        return this.locked;
    }

    /**
     * ロックを設定します。
     * @param locked ロック
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * ロック(装備)を取得します。
     * @return ロック(装備)
     */
    public Boolean getLockedEquip() {
        return this.lockedEquip;
    }

    /**
     * ロック(装備)を設定します。
     * @param lockedEquip ロック(装備)
     */
    public void setLockedEquip(Boolean lockedEquip) {
        this.lockedEquip = lockedEquip;
    }

    /**
     * 出撃海域を取得します。
     * @return 出撃海域
     */
    public Integer getSallyArea() {
        return this.sallyArea;
    }

    /**
     * 出撃海域を設定します。
     * @param sallyArea 出撃海域
     */
    public void setSallyArea(Integer sallyArea) {
        this.sallyArea = sallyArea;
    }

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
