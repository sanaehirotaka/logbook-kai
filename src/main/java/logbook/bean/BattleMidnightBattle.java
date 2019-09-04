package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 夜戦
 *
 */
@Data
public class BattleMidnightBattle implements IMidnightBattle, Serializable {

    private static final long serialVersionUID = 1993839270894519690L;

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

    /** api_eSlot */
    private List<List<Integer>> eSlot;

    /** api_fParam */
    private List<List<Integer>> fParam;

    /** api_eParam */
    private List<List<Integer>> eParam;

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
     * JsonObjectから{@link BattleMidnightBattle}を構築します
     *
     * @param json JsonObject
     * @return {@link BattleMidnightBattle}
     */
    public static BattleMidnightBattle toBattle(JsonObject json) {
        BattleMidnightBattle bean = new BattleMidnightBattle();
        JsonHelper.bind(json)
                .setInteger("api_dock_id", bean::setDockId)
                .setInteger("api_deck_id", bean::setDockId)
                .setIntegerList("api_ship_ke", bean::setShipKe)
                .setIntegerList("api_ship_lv", bean::setShipLv)
                .setIntegerList("api_f_nowhps", bean::setFNowhps)
                .setIntegerList("api_f_maxhps", bean::setFMaxhps)
                .setIntegerList("api_e_nowhps", bean::setENowhps)
                .setIntegerList("api_e_maxhps", bean::setEMaxhps)
                .set("api_eSlot", bean::setESlot, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_fParam", bean::setFParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_eParam", bean::setEParam, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_friendly_info", bean::setFriendlyInfo, BattleTypes.FriendlyInfo::toFriendlyInfo)
                .set("api_friendly_battle", bean::setFriendlyBattle, BattleTypes.FriendlyBattle::toFriendlyBattle)
                .setIntegerList("api_touch_plane", bean::setTouchPlane)
                .setIntegerList("api_flare_pos", bean::setFlarePos)
                .set("api_hougeki", bean::setHougeki, BattleTypes.MidnightHougeki::toMidnightHougeki);
        return bean;
    }
}
