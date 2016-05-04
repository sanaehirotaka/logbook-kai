package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.internal.JsonHelper;

/**
 * 夜戦(連合艦隊)
 *
 */
public class CombinedBattleSpMidnight implements ICombinedBattle, IMidnightBattle, IFormation, Serializable {

    private static final long serialVersionUID = -364877629377359534L;

    /** api_dock_id/api_deck_id */
    private Integer dockId;

    /** api_ship_ke */
    private List<Integer> shipKe;

    /** api_ship_lv */
    private List<Integer> shipLv;

    /** api_nowhps */
    private List<Integer> nowhps;

    /** api_maxhps */
    private List<Integer> maxhps;

    /** api_nowhps_combined */
    private List<Integer> nowhpsCombined;

    /** api_maxhps_combined */
    private List<Integer> maxhpsCombined;

    /** api_eSlot */
    private List<List<Integer>> eSlot;

    /** api_eKyouka */
    private List<List<Integer>> eKyouka;

    /** api_fParam */
    private List<List<Integer>> fParam;

    /** api_eParam */
    private List<List<Integer>> eParam;

    /** api_fParam_combined */
    private List<List<Integer>> fParamCombined;

    /** api_formation */
    private List<Integer> formation;

    /** api_touch_plane */
    private List<Integer> touchPlane;

    /** api_flare_pos */
    private List<Integer> flarePos;

    /** api_hougeki */
    private BattleTypes.MidnightHougeki hougeki;

    /**
     * api_dock_id/api_deck_idを取得します。
     * @return api_dock_id/api_deck_id
     */
    @Override
    public Integer getDockId() {
        return this.dockId;
    }

    /**
     * api_dock_id/api_deck_idを設定します。
     * @param dockId api_dock_id/api_deck_id
     */
    public void setDockId(Integer dockId) {
        this.dockId = dockId;
    }

    /**
     * api_ship_keを取得します。
     * @return api_ship_ke
     */
    @Override
    public List<Integer> getShipKe() {
        return this.shipKe;
    }

    /**
     * api_ship_keを設定します。
     * @param shipKe api_ship_ke
     */
    public void setShipKe(List<Integer> shipKe) {
        this.shipKe = shipKe;
    }

    /**
     * api_ship_lvを取得します。
     * @return api_ship_lv
     */
    @Override
    public List<Integer> getShipLv() {
        return this.shipLv;
    }

    /**
     * api_ship_lvを設定します。
     * @param shipLv api_ship_lv
     */
    public void setShipLv(List<Integer> shipLv) {
        this.shipLv = shipLv;
    }

    /**
     * api_nowhpsを取得します。
     * @return api_nowhps
     */
    @Override
    public List<Integer> getNowhps() {
        return this.nowhps;
    }

    /**
     * api_nowhpsを設定します。
     * @param nowhps api_nowhps
     */
    public void setNowhps(List<Integer> nowhps) {
        this.nowhps = nowhps;
    }

    /**
     * api_maxhpsを取得します。
     * @return api_maxhps
     */
    @Override
    public List<Integer> getMaxhps() {
        return this.maxhps;
    }

    /**
     * api_maxhpsを設定します。
     * @param maxhps api_maxhps
     */
    public void setMaxhps(List<Integer> maxhps) {
        this.maxhps = maxhps;
    }

    /**
     * api_nowhps_combinedを取得します。
     * @return api_nowhps_combined
     */
    @Override
    public List<Integer> getNowhpsCombined() {
        return this.nowhpsCombined;
    }

    /**
     * api_nowhps_combinedを設定します。
     * @param nowhpsCombined api_nowhps_combined
     */
    public void setNowhpsCombined(List<Integer> nowhpsCombined) {
        this.nowhpsCombined = nowhpsCombined;
    }

    /**
     * api_maxhps_combinedを取得します。
     * @return api_maxhps_combined
     */
    @Override
    public List<Integer> getMaxhpsCombined() {
        return this.maxhpsCombined;
    }

    /**
     * api_maxhps_combinedを設定します。
     * @param maxhpsCombined api_maxhps_combined
     */
    public void setMaxhpsCombined(List<Integer> maxhpsCombined) {
        this.maxhpsCombined = maxhpsCombined;
    }

    /**
     * api_eSlotを取得します。
     * @return api_eSlot
     */
    @Override
    public List<List<Integer>> getESlot() {
        return this.eSlot;
    }

    /**
     * api_eSlotを設定します。
     * @param eSlot api_eSlot
     */
    public void setESlot(List<List<Integer>> eSlot) {
        this.eSlot = eSlot;
    }

    /**
     * api_eKyoukaを取得します。
     * @return api_eKyouka
     */
    @Override
    public List<List<Integer>> getEKyouka() {
        return this.eKyouka;
    }

    /**
     * api_eKyoukaを設定します。
     * @param eKyouka api_eKyouka
     */
    public void setEKyouka(List<List<Integer>> eKyouka) {
        this.eKyouka = eKyouka;
    }

    /**
     * api_fParamを取得します。
     * @return api_fParam
     */
    @Override
    public List<List<Integer>> getFParam() {
        return this.fParam;
    }

    /**
     * api_fParamを設定します。
     * @param fParam api_fParam
     */
    public void setFParam(List<List<Integer>> fParam) {
        this.fParam = fParam;
    }

    /**
     * api_eParamを取得します。
     * @return api_eParam
     */
    @Override
    public List<List<Integer>> getEParam() {
        return this.eParam;
    }

    /**
     * api_eParamを設定します。
     * @param eParam api_eParam
     */
    public void setEParam(List<List<Integer>> eParam) {
        this.eParam = eParam;
    }

    /**
     * api_fParam_combinedを取得します。
     * @return api_fParam_combined
     */
    @Override
    public List<List<Integer>> getFParamCombined() {
        return this.fParamCombined;
    }

    /**
     * api_fParam_combinedを設定します。
     * @param fParamCombined api_fParam_combined
     */
    public void setFParamCombined(List<List<Integer>> fParamCombined) {
        this.fParamCombined = fParamCombined;
    }

    /**
     * api_formationを取得します。
     * @return api_formation
     */
    @Override
    public List<Integer> getFormation() {
        return this.formation;
    }

    /**
     * api_formationを設定します。
     * @param formation api_formation
     */
    public void setFormation(List<Integer> formation) {
        this.formation = formation;
    }

    /**
     * api_touch_planeを取得します。
     * @return api_touch_plane
     */
    @Override
    public List<Integer> getTouchPlane() {
        return this.touchPlane;
    }

    /**
     * api_touch_planeを設定します。
     * @param touchPlane api_touch_plane
     */
    public void setTouchPlane(List<Integer> touchPlane) {
        this.touchPlane = touchPlane;
    }

    /**
     * api_flare_posを取得します。
     * @return api_flare_pos
     */
    @Override
    public List<Integer> getFlarePos() {
        return this.flarePos;
    }

    /**
     * api_flare_posを設定します。
     * @param flarePos api_flare_pos
     */
    public void setFlarePos(List<Integer> flarePos) {
        this.flarePos = flarePos;
    }

    /**
     * api_hougekiを取得します。
     * @return api_hougeki
     */
    @Override
    public BattleTypes.MidnightHougeki getHougeki() {
        return this.hougeki;
    }

    /**
     * api_hougekiを設定します。
     * @param hougeki api_hougeki
     */
    public void setHougeki(BattleTypes.MidnightHougeki hougeki) {
        this.hougeki = hougeki;
    }

    /**
     * JsonObjectから{@link CombinedBattleSpMidnight}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleSpMidnight}
     */
    public static CombinedBattleSpMidnight toBattle(JsonObject json) {
        CombinedBattleSpMidnight bean = new CombinedBattleSpMidnight();
        JsonHelper.bind(json)
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .set("api_ship_ke", bean::setShipKe, JsonHelper::toIntegerList)
                .set("api_ship_lv", bean::setShipLv, JsonHelper::toIntegerList)
                .set("api_nowhps", bean::setNowhps, JsonHelper::toIntegerList)
                .set("api_maxhps", bean::setMaxhps, JsonHelper::toIntegerList)
                .set("api_nowhps_combined", bean::setNowhpsCombined, JsonHelper::toIntegerList)
                .set("api_maxhps_combined", bean::setMaxhpsCombined, JsonHelper::toIntegerList)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eKyouka", bean::setEKyouka, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam_combined", bean::setFParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_formation", bean::setFormation, JsonHelper::toIntegerList)
                .set("api_touch_plane", bean::setTouchPlane, JsonHelper::toIntegerList)
                .set("api_flare_pos", bean::setFlarePos, JsonHelper::toIntegerList)
                .set("api_hougeki", bean::setHougeki, BattleTypes.MidnightHougeki::toMidnightHougeki);
        return bean;
    }
}
