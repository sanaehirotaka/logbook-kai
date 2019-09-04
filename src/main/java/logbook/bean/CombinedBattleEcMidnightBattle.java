package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.ICombinedEcMidnightBattle;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 夜戦(連合艦隊)
 *
 */
@Data
public class CombinedBattleEcMidnightBattle implements ICombinedEcMidnightBattle, IMidnightBattle, Serializable {

    private static final long serialVersionUID = 8584847683187523584L;

    /** api_active_deck */
    private List<Integer> activeDeck;

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

    /** api_eSlot */
    private List<List<Integer>> eSlot;

    /** api_eSlot_combined */
    private List<List<Integer>> eSlotCombined;

    /** api_fParam */
    private List<List<Integer>> fParam;

    /** api_fParam_combined */
    private List<List<Integer>> fParamCombined;

    /** api_eParam */
    private List<List<Integer>> eParam;

    /** api_eParam_combined */
    private List<List<Integer>> eParamCombined;

    /** api_friendly_info */
    private BattleTypes.FriendlyInfo friendlyInfo;

    /** api_friendly_battle */
    private BattleTypes.FriendlyBattle friendlyBattle;

    /** api_touch_plane */
    private List<Integer> touchPlane;

    /** api_flare_pos */
    private List<Integer> flarePos;

    /** api_hougeki */
    private BattleTypes.MidnightHougeki hougeki;

    /**
     * JsonObjectから{@link CombinedBattleEcMidnightBattle}を構築します
     *
     * @param json JsonObject
     * @return {@link CombinedBattleEcMidnightBattle}
     */
    public static CombinedBattleEcMidnightBattle toBattle(JsonObject json) {
        CombinedBattleEcMidnightBattle bean = new CombinedBattleEcMidnightBattle();
        JsonHelper.bind(json)
                .setIntegerList("api_active_deck", bean::setActiveDeck)
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .setIntegerList("api_ship_ke", bean::setShipKe)
                .setIntegerList("api_ship_ke_combined", bean::setShipKeCombined)
                .setIntegerList("api_ship_lv", bean::setShipLv)
                .setIntegerList("api_ship_lv_combined", bean::setShipLvCombined)
                .setIntegerList("api_f_nowhps", bean::setFNowhps)
                .setIntegerList("api_f_maxhps", bean::setFMaxhps)
                .setIntegerList("api_e_nowhps", bean::setENowhps)
                .setIntegerList("api_e_maxhps", bean::setEMaxhps)
                .setIntegerList("api_e_nowhps_combined", bean::setENowhpsCombined)
                .setIntegerList("api_e_maxhps_combined", bean::setEMaxhpsCombined)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eSlot_combined", bean::setESlotCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam_combined", bean::setEParamCombined, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_friendly_info", bean::setFriendlyInfo, BattleTypes.FriendlyInfo::toFriendlyInfo)
                .set("api_friendly_battle", bean::setFriendlyBattle, BattleTypes.FriendlyBattle::toFriendlyBattle)
                .setIntegerList("api_touch_plane", bean::setTouchPlane)
                .setIntegerList("api_flare_pos", bean::setFlarePos)
                .set("api_hougeki", bean::setHougeki, BattleTypes.MidnightHougeki::toMidnightHougeki);
        return bean;
    }
}
