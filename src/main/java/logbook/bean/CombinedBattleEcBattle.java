/**
 *
 */
package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.ICombinedEcBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.ISortieBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 連合艦隊(6-5)
 *
 */
@Data
public class CombinedBattleEcBattle implements ICombinedEcBattle, ISortieBattle, ISortieHougeki,
        IFormation, IKouku, ISupport, IAirBaseAttack, Serializable {

    private static final long serialVersionUID = 9219457270531289491L;

    /** api_air_base_injection */
    private BattleTypes.AirBaseAttack airBaseInjection;

    /** api_air_base_attack */
    private List<BattleTypes.AirBaseAttack> airBaseAttack;

    /** api_dock_id/api_deck_id */
    private Integer dockId;

    /** api_ship_ke */
    private List<Integer> shipKe;

    /** api_ship_ke_combined */
    private List<Integer> shipKeCombined;

    /** api_ship_lv */
    private List<Integer> shipLv;

    /** api_ship_lv_combined */
    private List<Integer> shipLvCombined;

    /** api_f_nowhps */
    private List<Integer> fNowhps;

    /** api_f_maxhps */
    private List<Integer> fMaxhps;

    /** api_e_nowhps */
    private List<Integer> eNowhps;

    /** api_e_maxhps */
    private List<Integer> eMaxhps;

    /** api_e_nowhps_combined */
    private List<Integer> eNowhpsCombined;

    /** api_e_maxhps_combined */
    private List<Integer> eMaxhpsCombined;

    /** api_midnight_flag */
    private Boolean midnightFlag;

    /** api_eSlot */
    private List<List<Integer>> eSlot;

    /** api_eSlot_combined */
    private List<List<Integer>> eSlotCombined;

    /** api_eKyouka */
    private List<List<Integer>> eKyouka;

    /** api_fParam */
    private List<List<Integer>> fParam;

    /** api_eParam */
    private List<List<Integer>> eParam;

    /** api_eParam_combined */
    private List<List<Integer>> eParamCombined;

    /** api_search */
    private List<Integer> search;

    /** api_formation */
    private List<Integer> formation;

    /** api_stage_flag */
    private List<Integer> stageFlag;

    /** api_injection_kouku */
    private BattleTypes.Kouku injectionKouku;

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

    /** api_opening_taisen_flag */
    private Boolean openingTaisenFlag;

    /** api_opening_taisen */
    private BattleTypes.Hougeki openingTaisen;

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
     * JsonObjectから{@link CombinedBattleEcBattle}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleEcBattle}
     */
    public static CombinedBattleEcBattle toBattle(JsonObject json) {
        CombinedBattleEcBattle bean = new CombinedBattleEcBattle();
        JsonHelper.bind(json)
                .set("api_air_base_injection", bean::setAirBaseInjection,
                        BattleTypes.AirBaseAttack::toAirBaseAttack)
                .set("api_air_base_attack", bean::setAirBaseAttack,
                        JsonHelper.toList(BattleTypes.AirBaseAttack::toAirBaseAttack))
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .set("api_ship_ke", bean::setShipKe, JsonHelper::toIntegerList)
                .set("api_ship_ke_combined", bean::setShipKeCombined, JsonHelper::toIntegerList)
                .set("api_ship_lv", bean::setShipLv, JsonHelper::toIntegerList)
                .set("api_ship_lv_combined", bean::setShipLvCombined, JsonHelper::toIntegerList)
                .set("api_f_nowhps", bean::setFNowhps, JsonHelper::toIntegerList)
                .set("api_f_maxhps", bean::setFMaxhps, JsonHelper::toIntegerList)
                .set("api_e_nowhps", bean::setENowhps, JsonHelper::toIntegerList)
                .set("api_e_maxhps", bean::setEMaxhps, JsonHelper::toIntegerList)
                .set("api_e_nowhps_combined", bean::setENowhpsCombined, JsonHelper::toIntegerList)
                .set("api_e_maxhps_combined", bean::setEMaxhpsCombined, JsonHelper::toIntegerList)
                .setBoolean("api_midnight_flag", bean::setMidnightFlag)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eSlot_combined", bean::setESlotCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eKyouka", bean::setEKyouka, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam_combined", bean::setEParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_search", bean::setSearch, JsonHelper::toIntegerList)
                .set("api_formation", bean::setFormation, JsonHelper::toIntegerList)
                .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList)
                .set("api_injection_kouku", bean::setInjectionKouku, BattleTypes.Kouku::toKouku)
                .set("api_kouku", bean::setKouku, BattleTypes.Kouku::toKouku)
                .setBoolean("api_support_flag", bean::setSupportFlag)
                .set("api_support_info", bean::setSupportInfo, BattleTypes.SupportInfo::toSupportInfo)
                .setBoolean("api_opening_flag", bean::setOpeningFlag)
                .set("api_opening_atack", bean::setOpeningAtack, BattleTypes.Raigeki::toRaigeki)
                .setBoolean("api_opening_taisen_flag", bean::setOpeningTaisenFlag)
                .set("api_opening_taisen", bean::setOpeningTaisen, BattleTypes.Hougeki::toHougeki)
                .set("api_hourai_flag", bean::setHouraiFlag, JsonHelper::toIntegerList)
                .set("api_hougeki1", bean::setHougeki1, BattleTypes.Hougeki::toHougeki)
                .set("api_hougeki2", bean::setHougeki2, BattleTypes.Hougeki::toHougeki)
                .set("api_hougeki3", bean::setHougeki3, BattleTypes.Hougeki::toHougeki)
                .set("api_raigeki", bean::setRaigeki, BattleTypes.Raigeki::toRaigeki);
        return bean;
    }
}
