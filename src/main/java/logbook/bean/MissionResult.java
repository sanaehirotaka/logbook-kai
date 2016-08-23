package logbook.bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * api_req_mission/result
 *
 */
@Data
public class MissionResult implements Serializable {

    private static final long serialVersionUID = -1817304978875414123L;

    /** api_clear_result */
    private Integer clearResult;

    /** api_detail */
    private String detail;

    /** api_get_exp */
    private Integer getExp;

    /** api_get_exp_lvup */
    private List<List<Integer>> getExpLvup;

    /** api_get_material */
    private List<Integer> getMaterial;

    /** api_get_ship_exp */
    private List<Integer> getShipExp;

    /** api_maparea_name */
    private String mapareaName;

    /** api_member_exp */
    private Integer memberExp;

    /** api_member_lv */
    private String memberLv;

    /** api_quest_level */
    private Integer questLevel;

    /** api_quest_name */
    private String questName;

    /** api_ship_id */
    private List<Integer> shipId;

    /** api_useitem_flag */
    private List<Integer> useitemFlag;

    /** api_get_item1 */
    private GetItem getItem1;

    /** api_get_item2 */
    private GetItem getItem2;

    /**
     * api_get_item[n]
     *
     */
    @Data
    public static final class GetItem implements Serializable {

        private static final long serialVersionUID = 4356438738303170646L;

        /** api_useitem_id */
        private Integer useitemId;

        /** api_useitem_name */
        private String useitemName;

        /** api_useitem_count */
        private Integer useitemCount;

        /**
         * JsonObjectから{@link GetItem}を構築します
         *
         * @param json JsonObject
         * @return {@link GetItem}
         */
        public static GetItem toGetItem(JsonObject json) {
            GetItem bean = new GetItem();
            JsonHelper.bind(json)
                    .setInteger("api_useitem_id", bean::setUseitemId)
                    .setString("api_useitem_name", bean::setUseitemName)
                    .setInteger("api_useitem_count", bean::setUseitemCount);
            return bean;
        }
    }

    /**
     * JsonObjectから{@link MissionResult}を構築します
     *
     * @param json JsonObject
     * @return {@link MissionResult}
     */
    public static MissionResult toMissionResult(JsonObject json) {
        MissionResult bean = new MissionResult();
        JsonHelper.bind(json)
                .setInteger("api_clear_result", bean::setClearResult)
                .setString("api_detail", bean::setDetail)
                .setInteger("api_get_exp", bean::setGetExp)
                .set("api_get_exp_lvup", bean::setGetExpLvup, JsonHelper.toList(JsonHelper::toIntegerList))
                .set("api_get_material", bean::setGetMaterial, val -> {
                    if (val instanceof JsonArray) {
                        return JsonHelper.toIntegerList((JsonArray) val);
                    } else {
                        return Arrays.asList(0, 0, 0, 0);
                    }
                })
                .set("api_get_ship_exp", bean::setGetShipExp, JsonHelper::toIntegerList)
                .setString("api_maparea_name", bean::setMapareaName)
                .setInteger("api_member_exp", bean::setMemberExp)
                .setString("api_member_lv", bean::setMemberLv)
                .setInteger("api_quest_level", bean::setQuestLevel)
                .setString("api_quest_name", bean::setQuestName)
                .set("api_ship_id", bean::setShipId, JsonHelper::toIntegerList)
                .set("api_useitem_flag", bean::setUseitemFlag, JsonHelper::toIntegerList)
                .set("api_get_item1", bean::setGetItem1, GetItem::toGetItem)
                .set("api_get_item2", bean::setGetItem2, GetItem::toGetItem);
        return bean;
    }
}
