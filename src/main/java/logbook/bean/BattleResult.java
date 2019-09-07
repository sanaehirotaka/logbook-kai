package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 戦闘結果
 *
 */
@Data
public class BattleResult implements Serializable {

    private static final long serialVersionUID = 2163846602385545034L;

    /** api_win_rank */
    private String winRank;

    /** api_get_exp */
    private Integer getExp;

    /** api_mvp */
    private Integer mvp;

    /** api_member_lv */
    private Integer memberLv;

    /** api_member_exp */
    private Integer memberExp;

    /** api_get_base_exp */
    private Integer getBaseExp;

    /** api_get_ship_exp */
    private List<Integer> getShipExp;

    /** api_get_exp_lvup */
    private List<List<Integer>> getExpLvup;

    /** api_dests */
    private Integer dests;

    /** api_destsf */
    private Integer destsf;

    /** api_lost_flag */
    private List<Integer> lostFlag;

    /** api_quest_name */
    private String questName;

    /** api_quest_level */
    private Integer questLevel;

    /** api_enemy_info */
    private BattleResult.EnemyInfo enemyInfo;

    /** api_first_clear */
    private Integer firstClear;

    /** api_mapcell_incentive */
    private Boolean mapcellIncentive;

    /** api_get_flag */
    private List<Integer> getFlag;

    /** api_get_useitem */
    private Useitem getUseitem;

    /** api_get_ship */
    private BattleResult.GetShip getShip;

    /** api_get_eventitem */
    private List<BattleResult.GetEventitem> getEventitem;

    /** api_get_exmap_rate */
    private Integer getExmapRate;

    /** api_get_exmap_useitem_id */
    private Integer getExmapUseitemId;

    /** api_mvp_combined */
    private Integer mvpCombined;

    /** api_get_ship_exp_combined */
    private List<Integer> getShipExpCombined;

    /** api_get_exp_lvup_combined */
    private List<List<Integer>> getExpLvupCombined;

    /** api_get_eventflag */
    private Integer getEventflag;

    /** api_escape_flag */
    private Boolean escapeFlag;

    /** api_escape */
    private BattleResult.Escape escape;

    /** api_landing_hp */
    private BattleResult.LandingHp landingHp;

    /** api_m1 */
    private Integer m1;

    /** api_m2 */
    private Integer m2;

    /**
     * ギミック1が達成されたかを返します
     * @return
     */
    @JsonIgnore
    public boolean achievementGimmick1() {
        return (this.m1 != null && this.m1 > 0);
    }

    /**
     * ギミック2が達成されたかを返します
     * @return
     */
    @JsonIgnore
    public boolean achievementGimmick2() {
        return (this.m2 != null && this.m2 > 0);
    }

    /**
     * 敵艦隊情報
     */
    @Data
    public static class EnemyInfo implements Serializable {

        private static final long serialVersionUID = -1884647867712526970L;

        /** api_level */
        private String level;

        /** api_rank */
        private String rank;

        /** api_deck_name */
        private String deckName;

        /**
         * JsonObjectから{@link EnemyInfo}を構築します
         *
         * @param json JsonObject
         * @return {@link EnemyInfo}
         */
        public static EnemyInfo toEnemyInfo(JsonObject json) {
            EnemyInfo bean = new EnemyInfo();
            JsonHelper.bind(json)
                    .setString("api_level", bean::setLevel)
                    .setString("api_rank", bean::setRank)
                    .setString("api_deck_name", bean::setDeckName);
            return bean;
        }
    }

    /**
     * ドロップ艦情報
     */
    @Data
    public static class GetShip implements Serializable {

        private static final long serialVersionUID = 95363524139607534L;

        /** api_ship_id */
        private Integer shipId;

        /** api_ship_type */
        private String shipType;

        /** api_ship_name */
        private String shipName;

        /** api_ship_getmes */
        private String shipGetmes;

        /**
         * JsonObjectから{@link GetShip}を構築します
         *
         * @param json JsonObject
         * @return {@link GetShip}
         */
        public static GetShip toGetShip(JsonObject json) {
            GetShip bean = new GetShip();
            JsonHelper.bind(json)
                    .setInteger("api_ship_id", bean::setShipId)
                    .setString("api_ship_type", bean::setShipType)
                    .setString("api_ship_name", bean::setShipName)
                    .setString("api_ship_getmes", bean::setShipGetmes);
            return bean;
        }
    }

    /**
     * 海域攻略報酬
     */
    @Data
    public static class GetEventitem implements Serializable {

        private static final long serialVersionUID = 3067459493591329375L;

        /** api_type */
        private Integer type;

        /** api_id */
        private Integer id;

        /** api_value */
        private Integer value;

        /**
         * JsonObjectから{@link GetEventitem}を構築します
         *
         * @param json JsonObject
         * @return {@link GetEventitem}
         */
        public static GetEventitem toGetEventitem(JsonObject json) {
            GetEventitem bean = new GetEventitem();
            JsonHelper.bind(json)
                    .setInteger("api_type", bean::setType)
                    .setInteger("api_id", bean::setId)
                    .setInteger("api_value", bean::setValue);
            return bean;
        }
    }

    /**
     * 退避艦
     */
    @Data
    public static class Escape implements Serializable {

        private static final long serialVersionUID = 6425584217371037280L;

        /** api_escape_idx */
        private List<Integer> escapeIdx;

        /** api_tow_idx */
        private List<Integer> towIdx;

        /**
         * JsonObjectから{@link Escape}を構築します
         *
         * @param json JsonObject
         * @return {@link Escape}
         */
        public static Escape toEscape(JsonObject json) {
            Escape bean = new Escape();
            JsonHelper.bind(json)
                    .setIntegerList("api_escape_idx", bean::setEscapeIdx)
                    .setIntegerList("api_tow_idx", bean::setTowIdx);
            return bean;
        }
    }

    /**
     * 輸送作戦
     */
    @Data
    public static class LandingHp implements Serializable {

        private static final long serialVersionUID = 126264574756473409L;

        /** api_now_hp */
        private Integer nowHp;

        /** api_max_hp */
        private Integer maxHp;

        /** api_sub_value */
        private Integer subValue;

        /**
         * JsonObjectから{@link LandingHp}を構築します
         *
         * @param json JsonObject
         * @return {@link LandingHp}
         */
        public static LandingHp toLandingHp(JsonObject json) {
            LandingHp bean = new LandingHp();
            JsonHelper.bind(json)
                    .setInteger("api_now_hp", bean::setNowHp)
                    .setInteger("api_max_hp", bean::setMaxHp)
                    .setInteger("api_sub_value", bean::setSubValue);
            return bean;
        }
    }

    /**
     * ドロップアイテム
     */
    @Data
    public static class Useitem implements Serializable {

        private static final long serialVersionUID = 3453865997555376359L;

        /** api_useitem_id*/
        private Integer useitemId;

        /** api_useitem_name */
        private String useitemName;

        /**
         * JsonObjectから{@link Useitem}を構築します
         *
         * @param json JsonObject
         * @return {@link Useitem}
         */
        public static Useitem toUseitem(JsonObject json) {
            Useitem bean = new Useitem();
            JsonHelper.bind(json)
                    .setInteger("api_useitem_id", bean::setUseitemId)
                    .setString("api_useitem_name", bean::setUseitemName);
            return bean;
        }
    }

    /**
     * JsonObjectから{@link BattleResult}を構築します
     *
     * @param json JsonObject
     * @return {@link BattleResult}
     */
    public static BattleResult toBattleResult(JsonObject json) {
        BattleResult bean = new BattleResult();
        JsonHelper.bind(json)
                .setString("api_win_rank", bean::setWinRank)
                .setInteger("api_get_exp", bean::setGetExp)
                .setInteger("api_mvp", bean::setMvp)
                .setInteger("api_member_lv", bean::setMemberLv)
                .setInteger("api_member_exp", bean::setMemberExp)
                .setInteger("api_get_base_exp", bean::setGetBaseExp)
                .setIntegerList("api_get_ship_exp", bean::setGetShipExp)
                .set("api_get_exp_lvup", bean::setGetExpLvup, JsonHelper.toList(JsonHelper::toIntegerList))
                .setInteger("api_dests", bean::setDests)
                .setInteger("api_destsf", bean::setDestsf)
                .setString("api_quest_name", bean::setQuestName)
                .setInteger("api_quest_level", bean::setQuestLevel)
                .set("api_enemy_info", bean::setEnemyInfo, EnemyInfo::toEnemyInfo)
                .setInteger("api_first_clear", bean::setFirstClear)
                .setBoolean("api_mapcell_incentive", bean::setMapcellIncentive)
                .setIntegerList("api_get_flag", bean::setGetFlag)
                .set("api_get_useitem", bean::setGetUseitem, Useitem::toUseitem)
                .set("api_get_ship", bean::setGetShip, GetShip::toGetShip)
                .set("api_get_eventitem", bean::setGetEventitem, JsonHelper.toList(GetEventitem::toGetEventitem))
                .setInteger("api_get_exmap_rate", bean::setGetExmapRate)
                .setInteger("api_get_exmap_useitem_id", bean::setGetExmapUseitemId)
                .setInteger("api_mvp_combined", bean::setMvpCombined)
                .setIntegerList("api_get_ship_exp_combined", bean::setGetShipExpCombined)
                .set("api_get_exp_lvup_combined", bean::setGetExpLvupCombined,
                        JsonHelper.toList(JsonHelper::toIntegerList))
                .setInteger("api_get_eventflag", bean::setGetEventflag)
                .setBoolean("api_escape_flag", bean::setEscapeFlag)
                .set("api_escape", bean::setEscape, Escape::toEscape)
                .set("api_landing_hp", bean::setLandingHp, LandingHp::toLandingHp)
                .setInteger("api_m1", bean::setM1);
        return bean;
    }
}
