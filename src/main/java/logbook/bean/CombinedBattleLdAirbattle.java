package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.ISortieBattle;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 長距離空襲戦(連合艦隊)
 *
 */
@Data
public class CombinedBattleLdAirbattle
        implements ICombinedBattle, ISortieBattle, IFormation, IKouku, IAirBaseAttack, Serializable {

    private static final long serialVersionUID = 6539186077635769896L;

    /** api_air_base_injection */
    private BattleTypes.AirBaseAttack airBaseInjection;

    /** api_air_base_attack */
    private List<BattleTypes.AirBaseAttack> airBaseAttack;

    /** api_dock_id/api_deck_id */
    private Integer dockId;

    /** api_ship_ke */
    private List<Integer> shipKe;

    /** api_ship_lv */
    private List<Integer> shipLv;

    /** api_f_nowhps */
    private List<Integer> fNowhps;

    /** api_f_maxhps */
    private List<Integer> fMaxhps;

    /** api_e_nowhps */
    private List<Integer> eNowhps;

    /** api_e_maxhps */
    private List<Integer> eMaxhps;

    /** api_f_nowhps_combined */
    private List<Integer> fNowhpsCombined;

    /** api_f_maxhps_combined */
    private List<Integer> fMaxhpsCombined;

    /** api_midnight_flag */
    private Boolean midnightFlag;

    /** api_eSlot */
    private List<List<Integer>> eSlot;

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

    /** api_injection_kouku */
    private BattleTypes.Kouku injectionKouku;

    /** api_kouku */
    private BattleTypes.Kouku kouku;

    /**
     * JsonObjectから{@link CombinedBattleLdAirbattle}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleLdAirbattle}
     */
    public static CombinedBattleLdAirbattle toBattle(JsonObject json) {
        CombinedBattleLdAirbattle bean = new CombinedBattleLdAirbattle();
        JsonHelper.bind(json)
                .set("api_air_base_injection", bean::setAirBaseInjection,
                        BattleTypes.AirBaseAttack::toAirBaseAttack)
                .set("api_air_base_attack", bean::setAirBaseAttack,
                        JsonHelper.toList(BattleTypes.AirBaseAttack::toAirBaseAttack))
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .set("api_ship_ke", bean::setShipKe, JsonHelper::toIntegerList)
                .set("api_ship_lv", bean::setShipLv, JsonHelper::toIntegerList)
                .set("api_f_nowhps", bean::setFNowhps, JsonHelper::toIntegerList)
                .set("api_f_maxhps", bean::setFMaxhps, JsonHelper::toIntegerList)
                .set("api_e_nowhps", bean::setENowhps, JsonHelper::toIntegerList)
                .set("api_e_maxhps", bean::setEMaxhps, JsonHelper::toIntegerList)
                .set("api_f_nowhps_combined", bean::setFNowhpsCombined, JsonHelper::toIntegerList)
                .set("api_f_maxhps_combined", bean::setFMaxhpsCombined, JsonHelper::toIntegerList)
                .setBoolean("api_midnight_flag", bean::setMidnightFlag)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam_combined", bean::setFParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_search", bean::setSearch, JsonHelper::toIntegerList)
                .set("api_formation", bean::setFormation, JsonHelper::toIntegerList)
                .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList)
                .set("api_injection_kouku", bean::setInjectionKouku, BattleTypes.Kouku::toKouku)
                .set("api_kouku", bean::setKouku, BattleTypes.Kouku::toKouku);
        return bean;
    }
}
