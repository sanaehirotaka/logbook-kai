package logbook.bean;

import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 戦闘
 *
 */
public class SortieAirbattle {

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

    /** api_stage_flag2 */
    private List<Integer> stageFlag2;

    /** api_kouku2 */
    private BattleTypes.Kouku kouku2;

    /**
     * api_dock_id/api_deck_idを取得します。
     * @return api_dock_id/api_deck_id
     */
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
     * api_midnight_flagを取得します。
     * @return api_midnight_flag
     */
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
    public List<List<Integer>> geteSlot() {
        return this.eSlot;
    }

    /**
     * api_eSlotを設定します。
     * @param eSlot api_eSlot
     */
    public void seteSlot(List<List<Integer>> eSlot) {
        this.eSlot = eSlot;
    }

    /**
     * api_eKyoukaを取得します。
     * @return api_eKyouka
     */
    public List<List<Integer>> geteKyouka() {
        return this.eKyouka;
    }

    /**
     * api_eKyoukaを設定します。
     * @param eKyouka api_eKyouka
     */
    public void seteKyouka(List<List<Integer>> eKyouka) {
        this.eKyouka = eKyouka;
    }

    /**
     * api_fParamを取得します。
     * @return api_fParam
     */
    public List<List<Integer>> getfParam() {
        return this.fParam;
    }

    /**
     * api_fParamを設定します。
     * @param fParam api_fParam
     */
    public void setfParam(List<List<Integer>> fParam) {
        this.fParam = fParam;
    }

    /**
     * api_eParamを取得します。
     * @return api_eParam
     */
    public List<List<Integer>> geteParam() {
        return this.eParam;
    }

    /**
     * api_eParamを設定します。
     * @param eParam api_eParam
     */
    public void seteParam(List<List<Integer>> eParam) {
        this.eParam = eParam;
    }

    /**
     * api_searchを取得します。
     * @return api_search
     */
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
     * api_stage_flag2を取得します。
     * @return api_stage_flag2
     */
    public List<Integer> getStageFlag2() {
        return this.stageFlag2;
    }

    /**
     * api_stage_flag2を設定します。
     * @param stageFlag2 api_stage_flag2
     */
    public void setStageFlag2(List<Integer> stageFlag2) {
        this.stageFlag2 = stageFlag2;
    }

    /**
     * api_kouku2を取得します。
     * @return api_kouku2
     */
    public BattleTypes.Kouku getKouku2() {
        return this.kouku2;
    }

    /**
     * api_kouku2を設定します。
     * @param kouku2 api_kouku2
     */
    public void setKouku2(BattleTypes.Kouku kouku2) {
        this.kouku2 = kouku2;
    }

    /**
     * JsonObjectから{@link SortieAirbattle}を構築します
     *
     * @param json JsonObject
     * @return {@link SortieAirbattle}
     */
    public static SortieAirbattle toAirbattle(JsonObject json) {
        SortieAirbattle bean = new SortieAirbattle();
        JsonHelper.bind(json)
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .set("api_ship_ke", bean::setShipKe, JsonHelper::toIntegerList)
                .set("api_ship_lv", bean::setShipLv, JsonHelper::toIntegerList)
                .set("api_nowhps", bean::setNowhps, JsonHelper::toIntegerList)
                .set("api_maxhps", bean::setMaxhps, JsonHelper::toIntegerList)
                .setBoolean("api_midnight_flag", bean::setMidnightFlag)
                .set("api_eSlot", bean::seteSlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eKyouka", bean::seteKyouka, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setfParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::seteParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_search", bean::setSearch, JsonHelper::toIntegerList)
                .set("api_formation", bean::setFormation, JsonHelper::toIntegerList)
                .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList)
                .set("api_kouku", bean::setKouku, BattleTypes.Kouku::toKouku)
                .setBoolean("api_support_flag", bean::setSupportFlag)
                .set("api_support_info", bean::setSupportInfo, BattleTypes.SupportInfo::toSupportInfo)
                .set("api_stage_flag2", bean::setStageFlag2, JsonHelper::toIntegerList)
                .set("api_kouku2", bean::setKouku2, BattleTypes.Kouku::toKouku);
        return bean;
    }
}
