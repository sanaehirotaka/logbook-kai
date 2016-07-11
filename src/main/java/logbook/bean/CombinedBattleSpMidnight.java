package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.ICombinedBattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 夜戦(連合艦隊)
 *
 */
@Data
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
