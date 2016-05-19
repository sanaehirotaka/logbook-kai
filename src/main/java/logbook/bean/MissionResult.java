package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * api_req_mission/result
 *
 */
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
     * api_clear_resultを取得します。
     * @return api_clear_result
     */
    public Integer getClearResult() {
        return this.clearResult;
    }

    /**
     * api_clear_resultを設定します。
     * @param clearResult api_clear_result
     */
    public void setClearResult(Integer clearResult) {
        this.clearResult = clearResult;
    }

    /**
     * api_detailを取得します。
     * @return api_detail
     */
    public String getDetail() {
        return this.detail;
    }

    /**
     * api_detailを設定します。
     * @param detail api_detail
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * api_get_expを取得します。
     * @return api_get_exp
     */
    public Integer getGetExp() {
        return this.getExp;
    }

    /**
     * api_get_expを設定します。
     * @param getExp api_get_exp
     */
    public void setGetExp(Integer getExp) {
        this.getExp = getExp;
    }

    /**
     * api_get_exp_lvupを取得します。
     * @return api_get_exp_lvup
     */
    public List<List<Integer>> getGetExpLvup() {
        return this.getExpLvup;
    }

    /**
     * api_get_exp_lvupを設定します。
     * @param getExpLvup api_get_exp_lvup
     */
    public void setGetExpLvup(List<List<Integer>> getExpLvup) {
        this.getExpLvup = getExpLvup;
    }

    /**
     * api_get_materialを取得します。
     * @return api_get_material
     */
    public List<Integer> getGetMaterial() {
        return this.getMaterial;
    }

    /**
     * api_get_materialを設定します。
     * @param getMaterial api_get_material
     */
    public void setGetMaterial(List<Integer> getMaterial) {
        this.getMaterial = getMaterial;
    }

    /**
     * api_get_ship_expを取得します。
     * @return api_get_ship_exp
     */
    public List<Integer> getGetShipExp() {
        return this.getShipExp;
    }

    /**
     * api_get_ship_expを設定します。
     * @param getShipExp api_get_ship_exp
     */
    public void setGetShipExp(List<Integer> getShipExp) {
        this.getShipExp = getShipExp;
    }

    /**
     * api_maparea_nameを取得します。
     * @return api_maparea_name
     */
    public String getMapareaName() {
        return this.mapareaName;
    }

    /**
     * api_maparea_nameを設定します。
     * @param mapareaName api_maparea_name
     */
    public void setMapareaName(String mapareaName) {
        this.mapareaName = mapareaName;
    }

    /**
     * api_member_expを取得します。
     * @return api_member_exp
     */
    public Integer getMemberExp() {
        return this.memberExp;
    }

    /**
     * api_member_expを設定します。
     * @param memberExp api_member_exp
     */
    public void setMemberExp(Integer memberExp) {
        this.memberExp = memberExp;
    }

    /**
     * api_member_lvを取得します。
     * @return api_member_lv
     */
    public String getMemberLv() {
        return this.memberLv;
    }

    /**
     * api_member_lvを設定します。
     * @param memberLv api_member_lv
     */
    public void setMemberLv(String memberLv) {
        this.memberLv = memberLv;
    }

    /**
     * api_quest_levelを取得します。
     * @return api_quest_level
     */
    public Integer getQuestLevel() {
        return this.questLevel;
    }

    /**
     * api_quest_levelを設定します。
     * @param questLevel api_quest_level
     */
    public void setQuestLevel(Integer questLevel) {
        this.questLevel = questLevel;
    }

    /**
     * api_quest_nameを取得します。
     * @return api_quest_name
     */
    public String getQuestName() {
        return this.questName;
    }

    /**
     * api_quest_nameを設定します。
     * @param questName api_quest_name
     */
    public void setQuestName(String questName) {
        this.questName = questName;
    }

    /**
     * api_ship_idを取得します。
     * @return api_ship_id
     */
    public List<Integer> getShipId() {
        return this.shipId;
    }

    /**
     * api_ship_idを設定します。
     * @param shipId api_ship_id
     */
    public void setShipId(List<Integer> shipId) {
        this.shipId = shipId;
    }

    /**
     * api_useitem_flagを取得します。
     * @return api_useitem_flag
     */
    public List<Integer> getUseitemFlag() {
        return this.useitemFlag;
    }

    /**
     * api_useitem_flagを設定します。
     * @param useitemFlag api_useitem_flag
     */
    public void setUseitemFlag(List<Integer> useitemFlag) {
        this.useitemFlag = useitemFlag;
    }

    /**
     * api_get_item1を取得します。
     * @return api_get_item1
     */
    public GetItem getGetItem1() {
        return this.getItem1;
    }

    /**
     * api_get_item1を設定します。
     * @param getItem1 api_get_item1
     */
    public void setGetItem1(GetItem getItem1) {
        this.getItem1 = getItem1;
    }

    /**
     * api_get_item2を取得します。
     * @return api_get_item2
     */
    public GetItem getGetItem2() {
        return this.getItem2;
    }

    /**
     * api_get_item2を設定します。
     * @param getItem2 api_get_item2
     */
    public void setGetItem2(GetItem getItem2) {
        this.getItem2 = getItem2;
    }

    /**
     * api_get_item[n]
     *
     */
    public static final class GetItem implements Serializable {

        private static final long serialVersionUID = 4356438738303170646L;

        /** api_useitem_id */
        private Integer useitemId;

        /** api_useitem_name */
        private String useitemName;

        /** api_useitem_count */
        private Integer useitemCount;

        /**
         * api_useitem_idを取得します。
         * @return api_useitem_id
         */
        public Integer getUseitemId() {
            return this.useitemId;
        }

        /**
         * api_useitem_idを設定します。
         * @param useitemId api_useitem_id
         */
        public void setUseitemId(Integer useitemId) {
            this.useitemId = useitemId;
        }

        /**
         * api_useitem_nameを取得します。
         * @return api_useitem_name
         */
        public String getUseitemName() {
            return this.useitemName;
        }

        /**
         * api_useitem_nameを設定します。
         * @param useitemName api_useitem_name
         */
        public void setUseitemName(String useitemName) {
            this.useitemName = useitemName;
        }

        /**
         * api_useitem_countを取得します。
         * @return api_useitem_count
         */
        public Integer getUseitemCount() {
            return this.useitemCount;
        }

        /**
         * api_useitem_countを設定します。
         * @param useitemCount api_useitem_count
         */
        public void setUseitemCount(Integer useitemCount) {
            this.useitemCount = useitemCount;
        }

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
            .set("api_get_material", bean::setGetMaterial, JsonHelper::toIntegerList)
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
