package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.ISortieBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.internal.JsonHelper;

/**
 * 機動部隊(水上部隊)
 *
 */
public class CombinedBattleBattleWater implements ICombinedBattle, ISortieBattle, ISortieHougeki, IFormation, IKouku,
        ISupport, IAirBaseAttack, Serializable {

    private static final long serialVersionUID = -856889126602983978L;

    /** api_air_base_attack */
    private List<BattleTypes.AirBaseAttack> airBaseAttack;

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

    /** api_midnight_flag */
    private Boolean midnightFlag;

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

    /** api_search */
    private List<Integer> search;

    /** api_formation */
    private List<Integer> formation;

    /** api_stage_flag */
    private List<Integer> stageFlag;

    /** api_kouku */
    private BattleTypes.Kouku kouku;

    /** api_support_flag */
    private Boolean supportFlag;

    /** api_support_info */
    private BattleTypes.SupportInfo supportInfo;

    /** api_opening_flag */
    private Boolean openingFlag;

    /** api_opening_atack */
    private BattleTypes.Raigeki openingAtack;

    /** api_hourai_flag */
    private List<Integer> houraiFlag;

    /** api_hougeki1 */
    private BattleTypes.Hougeki hougeki1;

    /** api_hougeki2 */
    private BattleTypes.Hougeki hougeki2;

    /** api_hougeki3 */
    private BattleTypes.Hougeki hougeki3;

    /** api_raigeki */
    private BattleTypes.Raigeki raigeki;

    /**
     * api_air_base_attackを取得します。
     * @return api_air_base_attack
     */
    @Override
    public List<BattleTypes.AirBaseAttack> getAirBaseAttack() {
        return this.airBaseAttack;
    }

    /**
     * api_air_base_attackを設定します。
     * @param airBaseAttack api_air_base_attack
     */
    public void setAirBaseAttack(List<BattleTypes.AirBaseAttack> airBaseAttack) {
        this.airBaseAttack = airBaseAttack;
    }

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
     * api_midnight_flagを取得します。
     * @return api_midnight_flag
     */
    @Override
    public Boolean getMidnightFlag() {
        return this.midnightFlag;
    }

    /**
     * api_midnight_flagを設定します。
     * @param midnightFlag api_midnight_flag
     */
    public void setMidnightFlag(Boolean midnightFlag) {
        this.midnightFlag = midnightFlag;
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
     * api_searchを取得します。
     * @return api_search
     */
    @Override
    public List<Integer> getSearch() {
        return this.search;
    }

    /**
     * api_searchを設定します。
     * @param search api_search
     */
    public void setSearch(List<Integer> search) {
        this.search = search;
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
     * api_stage_flagを取得します。
     * @return api_stage_flag
     */
    @Override
    public List<Integer> getStageFlag() {
        return this.stageFlag;
    }

    /**
     * api_stage_flagを設定します。
     * @param stageFlag api_stage_flag
     */
    public void setStageFlag(List<Integer> stageFlag) {
        this.stageFlag = stageFlag;
    }

    /**
     * api_koukuを取得します。
     * @return api_kouku
     */
    @Override
    public BattleTypes.Kouku getKouku() {
        return this.kouku;
    }

    /**
     * api_koukuを設定します。
     * @param kouku api_kouku
     */
    public void setKouku(BattleTypes.Kouku kouku) {
        this.kouku = kouku;
    }

    /**
     * api_support_flagを取得します。
     * @return api_support_flag
     */
    @Override
    public Boolean getSupportFlag() {
        return this.supportFlag;
    }

    /**
     * api_support_flagを設定します。
     * @param supportFlag api_support_flag
     */
    public void setSupportFlag(Boolean supportFlag) {
        this.supportFlag = supportFlag;
    }

    /**
     * api_support_infoを取得します。
     * @return api_support_info
     */
    @Override
    public BattleTypes.SupportInfo getSupportInfo() {
        return this.supportInfo;
    }

    /**
     * api_support_infoを設定します。
     * @param supportInfo api_support_info
     */
    public void setSupportInfo(BattleTypes.SupportInfo supportInfo) {
        this.supportInfo = supportInfo;
    }

    /**
     * api_opening_flagを取得します。
     * @return api_opening_flag
     */
    @Override
    public Boolean getOpeningFlag() {
        return this.openingFlag;
    }

    /**
     * api_opening_flagを設定します。
     * @param openingFlag api_opening_flag
     */
    public void setOpeningFlag(Boolean openingFlag) {
        this.openingFlag = openingFlag;
    }

    /**
     * api_opening_atackを取得します。
     * @return api_opening_atack
     */
    @Override
    public BattleTypes.Raigeki getOpeningAtack() {
        return this.openingAtack;
    }

    /**
     * api_opening_atackを設定します。
     * @param openingAtack api_opening_atack
     */
    public void setOpeningAtack(BattleTypes.Raigeki openingAtack) {
        this.openingAtack = openingAtack;
    }

    /**
     * api_hourai_flagを取得します。
     * @return api_hourai_flag
     */
    @Override
    public List<Integer> getHouraiFlag() {
        return this.houraiFlag;
    }

    /**
     * api_hourai_flagを設定します。
     * @param houraiFlag api_hourai_flag
     */
    public void setHouraiFlag(List<Integer> houraiFlag) {
        this.houraiFlag = houraiFlag;
    }

    /**
     * api_hougeki1を取得します。
     * @return api_hougeki1
     */
    @Override
    public BattleTypes.Hougeki getHougeki1() {
        return this.hougeki1;
    }

    /**
     * api_hougeki1を設定します。
     * @param hougeki1 api_hougeki1
     */
    public void setHougeki1(BattleTypes.Hougeki hougeki1) {
        this.hougeki1 = hougeki1;
    }

    /**
     * api_hougeki2を取得します。
     * @return api_hougeki2
     */
    @Override
    public BattleTypes.Hougeki getHougeki2() {
        return this.hougeki2;
    }

    /**
     * api_hougeki2を設定します。
     * @param hougeki2 api_hougeki2
     */
    public void setHougeki2(BattleTypes.Hougeki hougeki2) {
        this.hougeki2 = hougeki2;
    }

    /**
     * api_hougeki3を取得します。
     * @return api_hougeki3
     */
    @Override
    public BattleTypes.Hougeki getHougeki3() {
        return this.hougeki3;
    }

    /**
     * api_hougeki3を設定します。
     * @param hougeki3 api_hougeki3
     */
    public void setHougeki3(BattleTypes.Hougeki hougeki3) {
        this.hougeki3 = hougeki3;
    }

    /**
     * api_raigekiを取得します。
     * @return api_raigeki
     */
    @Override
    public BattleTypes.Raigeki getRaigeki() {
        return this.raigeki;
    }

    /**
     * api_raigekiを設定します。
     * @param raigeki api_raigeki
     */
    public void setRaigeki(BattleTypes.Raigeki raigeki) {
        this.raigeki = raigeki;
    }

    /**
     * JsonObjectから{@link CombinedBattleBattleWater}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleBattleWater}
     */
    public static CombinedBattleBattleWater toBattle(JsonObject json) {
        CombinedBattleBattleWater bean = new CombinedBattleBattleWater();
        JsonHelper.bind(json)
                .set("api_air_base_attack", bean::setAirBaseAttack,
                        JsonHelper.toList(BattleTypes.AirBaseAttack::toAirBaseAttack))
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .set("api_ship_ke", bean::setShipKe, JsonHelper::toIntegerList)
                .set("api_ship_lv", bean::setShipLv, JsonHelper::toIntegerList)
                .set("api_nowhps", bean::setNowhps, JsonHelper::toIntegerList)
                .set("api_maxhps", bean::setMaxhps, JsonHelper::toIntegerList)
                .set("api_nowhps_combined", bean::setNowhpsCombined, JsonHelper::toIntegerList)
                .set("api_maxhps_combined", bean::setMaxhpsCombined, JsonHelper::toIntegerList)
                .setBoolean("api_midnight_flag", bean::setMidnightFlag)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eKyouka", bean::setEKyouka, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam_combined", bean::setFParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_search", bean::setSearch, JsonHelper::toIntegerList)
                .set("api_formation", bean::setFormation, JsonHelper::toIntegerList)
                .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList)
                .set("api_kouku", bean::setKouku, BattleTypes.Kouku::toKouku)
                .setBoolean("api_support_flag", bean::setSupportFlag)
                .set("api_support_info", bean::setSupportInfo, BattleTypes.SupportInfo::toSupportInfo)
                .setBoolean("api_opening_flag", bean::setOpeningFlag)
                .set("api_opening_atack", bean::setOpeningAtack, BattleTypes.Raigeki::toRaigeki)
                .set("api_hourai_flag", bean::setHouraiFlag, JsonHelper::toIntegerList)
                .set("api_hougeki1", bean::setHougeki1, BattleTypes.Hougeki::toHougeki)
                .set("api_hougeki2", bean::setHougeki2, BattleTypes.Hougeki::toHougeki)
                .set("api_hougeki3", bean::setHougeki3, BattleTypes.Hougeki::toHougeki)
                .set("api_raigeki", bean::setRaigeki, BattleTypes.Raigeki::toRaigeki);
        return bean;
    }
}
