package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_mst_mission
 *
 */
@Data
public class Mission implements Serializable {

    private static final long serialVersionUID = 7503112116568598849L;

    /** api_damage_type */
    private Integer damageType;

    /** api_deck_num */
    private Integer deckNum;

    /** api_details */
    private String details;

    /** api_difficulty */
    private Integer difficulty;

    /** api_disp_no */
    private String dispNo;

    /** api_id */
    private Integer id;

    /** api_maparea_id */
    private Integer mapareaId;

    /** api_name */
    private String name;

    /** api_reset_type */
    private Integer resetType;

    /** api_return_flag */
    private Integer returnFlag;

    /** api_sample_fleet */
    private List<Integer> sampleFleet;

    /** api_time */
    private Integer time;

    /** api_use_bull */
    private Double useBull;

    /** api_use_fuel */
    private Double useFuel;

    /** api_win_item1 */
    private List<Integer> winItem1;

    /** api_win_item2 */
    private List<Integer> winItem2;

    /** api_win_mat_level */
    private List<Integer> winMatLevel;

    @Override
    public String toString() {
        if (this.dispNo != null)
            return this.dispNo + " " + this.name;
        return this.name;
    }

    /**
     * JsonObjectから{@link Mission}を構築します
     *
     * @param json JsonObject
     * @return {@link Mission}
     */
    public static Mission toMission(JsonObject json) {
        Mission bean = new Mission();
        JsonHelper.bind(json)
                .setInteger("api_damage_type", bean::setDamageType)
                .setInteger("api_deck_num", bean::setDeckNum)
                .setString("api_details", bean::setDetails)
                .setInteger("api_difficulty", bean::setDifficulty)
                .setString("api_disp_no", bean::setDispNo)
                .setInteger("api_id", bean::setId)
                .setInteger("api_maparea_id", bean::setMapareaId)
                .setString("api_name", bean::setName)
                .setInteger("api_reset_type", bean::setResetType)
                .setInteger("api_return_flag", bean::setReturnFlag)
                .set("api_sample_fleet", bean::setSampleFleet, JsonHelper::toIntegerList)
                .setDouble("api_use_bull", bean::setUseBull)
                .setDouble("api_use_fuel", bean::setUseFuel)
                .setInteger("api_time", bean::setTime)
                .set("api_win_item1", bean::setWinItem1, JsonHelper::toIntegerList)
                .set("api_win_item2", bean::setWinItem2, JsonHelper::toIntegerList)
                .set("api_win_mat_level", bean::setWinMatLevel, JsonHelper::toIntegerList);
        return bean;
    }
}
