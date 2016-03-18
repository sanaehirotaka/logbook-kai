package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 戦闘結果
 *
 */
public class BattleResult implements Serializable {

    private static final long serialVersionUID = 2163846602385545034L;

    /** api_ship_id */
    private List<Integer> shipId;

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

    /**
     * 敵艦隊情報
     */
    public static class EnemyInfo implements Serializable {

        private static final long serialVersionUID = -1884647867712526970L;

        /** api_level */
        private String level;

        /** api_rank */
        private String rank;

        /** api_deck_name */
        private String deckName;

        /**
         * api_levelを取得します。
         * @return api_level
         */
        public String getLevel() {
            return this.level;
        }

        /**
         * api_levelを設定します。
         * @param level api_level
         */
        public void setLevel(String level) {
            this.level = level;
        }

        /**
         * api_rankを取得します。
         * @return api_rank
         */
        public String getRank() {
            return this.rank;
        }

        /**
         * api_rankを設定します。
         * @param rank api_rank
         */
        public void setRank(String rank) {
            this.rank = rank;
        }

        /**
         * api_deck_nameを取得します。
         * @return api_deck_name
         */
        public String getDeckName() {
            return this.deckName;
        }

        /**
         * api_deck_nameを設定します。
         * @param deckName api_deck_name
         */
        public void setDeckName(String deckName) {
            this.deckName = deckName;
        }

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
         * api_ship_idを設定します。
         * @return api_ship_id
         */
        public Integer getShipId() {
            return this.shipId;
        }

        /**
         * api_ship_idを設定します。
         * @param shipId api_ship_id
         */
        public void setShipId(Integer shipId) {
            this.shipId = shipId;
        }

        /**
         * api_ship_typeを取得します。
         * @return api_ship_type
         */
        public String getShipType() {
            return this.shipType;
        }

        /**
         * api_ship_typeを設定します。
         * @param shipType api_ship_type
         */
        public void setShipType(String shipType) {
            this.shipType = shipType;
        }

        /**
         * api_ship_nameを取得します。
         * @return api_ship_name
         */
        public String getShipName() {
            return this.shipName;
        }

        /**
         * api_ship_nameを設定します。
         * @param shipName api_ship_name
         */
        public void setShipName(String shipName) {
            this.shipName = shipName;
        }

        /**
         * api_ship_getmesを取得します。
         * @return api_ship_getmes
         */
        public String getShipGetmes() {
            return this.shipGetmes;
        }

        /**
         * api_ship_getmesを設定します。
         * @param shipGetmes api_ship_getmes
         */
        public void setShipGetmes(String shipGetmes) {
            this.shipGetmes = shipGetmes;
        }

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
    public static class GetEventitem implements Serializable {

        private static final long serialVersionUID = 3067459493591329375L;

        /** api_type */
        private Integer type;

        /** api_id */
        private Integer id;

        /** api_value */
        private Integer value;

        /**
         * api_typeを取得します。
         * @return api_type
         */
        public Integer getType() {
            return this.type;
        }

        /**
         * api_typeを設定します。
         * @param type api_type
         */
        public void setType(Integer type) {
            this.type = type;
        }

        /**
         * api_idを取得します。
         * @return api_id
         */
        public Integer getId() {
            return this.id;
        }

        /**
         * api_idを設定します。
         * @param id api_id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * api_valueを取得します。
         * @return api_value
         */
        public Integer getValue() {
            return this.value;
        }

        /**
         * api_valueを設定します。
         * @param value api_value
         */
        public void setValue(Integer value) {
            this.value = value;
        }

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
    public static class Escape implements Serializable {

        private static final long serialVersionUID = 6425584217371037280L;

        /** api_escape_idx */
        private List<Integer> escapeIdx;

        /** api_tow_idx */
        private List<Integer> towIdx;

        /**
         * api_escape_idxを取得します。
         * @return api_escape_idx
         */
        public List<Integer> getEscapeIdx() {
            return this.escapeIdx;
        }

        /**
         * api_escape_idxを設定します。
         * @param escapeIdx api_escape_idx
         */
        public void setEscapeIdx(List<Integer> escapeIdx) {
            this.escapeIdx = escapeIdx;
        }

        /**
         * api_tow_idxを取得します。
         * @return api_tow_idx
         */
        public List<Integer> getTowIdx() {
            return this.towIdx;
        }

        /**
         * api_tow_idxを設定します。
         * @param towIdx api_tow_idx
         */
        public void setTowIdx(List<Integer> towIdx) {
            this.towIdx = towIdx;
        }

        /**
         * JsonObjectから{@link Escape}を構築します
         *
         * @param json JsonObject
         * @return {@link Escape}
         */
        public static Escape toEscape(JsonObject json) {
            Escape bean = new Escape();
            JsonHelper.bind(json)
                    .set("api_escape_idx", bean::setEscapeIdx, JsonHelper::toIntegerList)
                    .set("api_tow_idx", bean::setTowIdx, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 輸送作戦
     */
    public static class LandingHp implements Serializable {

        private static final long serialVersionUID = 126264574756473409L;

        /** api_now_hp */
        private Integer nowHp;

        /** api_max_hp */
        private Integer maxHp;

        /** api_sub_value */
        private Integer subValue;

        /**
         * api_now_hpを取得します。
         * @return api_now_hp
         */
        public Integer getNowHp() {
            return this.nowHp;
        }

        /**
         * api_now_hpを設定します。
         * @param nowHp api_now_hp
         */
        public void setNowHp(Integer nowHp) {
            this.nowHp = nowHp;
        }

        /**
         * api_max_hpを取得します。
         * @return api_max_hp
         */
        public Integer getMaxHp() {
            return this.maxHp;
        }

        /**
         * api_max_hpを設定します。
         * @param maxHp api_max_hp
         */
        public void setMaxHp(Integer maxHp) {
            this.maxHp = maxHp;
        }

        /**
         * api_sub_valueを取得します。
         * @return api_sub_value
         */
        public Integer getSubValue() {
            return this.subValue;
        }

        /**
         * api_sub_valueを設定します。
         * @param subValue api_sub_value
         */
        public void setSubValue(Integer subValue) {
            this.subValue = subValue;
        }

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
     * api_win_rankを取得します。
     * @return api_win_rank
     */
    public String getWinRank() {
        return this.winRank;
    }

    /**
     * api_win_rankを設定します。
     * @param winRank api_win_rank
     */
    public void setWinRank(String winRank) {
        this.winRank = winRank;
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
     * api_mvpを取得します。
     * @return api_mvp
     */
    public Integer getMvp() {
        return this.mvp;
    }

    /**
     * api_mvpを設定します。
     * @param mvp api_mvp
     */
    public void setMvp(Integer mvp) {
        this.mvp = mvp;
    }

    /**
     * api_member_lvを取得します。
     * @return api_member_lv
     */
    public Integer getMemberLv() {
        return this.memberLv;
    }

    /**
     * api_member_lvを設定します。
     * @param memberLv api_member_lv
     */
    public void setMemberLv(Integer memberLv) {
        this.memberLv = memberLv;
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
     * api_get_base_expを取得します。
     * @return api_get_base_exp
     */
    public Integer getGetBaseExp() {
        return this.getBaseExp;
    }

    /**
     * api_get_base_expを設定します。
     * @param getBaseExp api_get_base_exp
     */
    public void setGetBaseExp(Integer getBaseExp) {
        this.getBaseExp = getBaseExp;
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
     * api_destsを取得します。
     * @return api_dests
     */
    public Integer getDests() {
        return this.dests;
    }

    /**
     * api_destsを設定します。
     * @param dests api_dests
     */
    public void setDests(Integer dests) {
        this.dests = dests;
    }

    /**
     * api_destsfを取得します。
     * @return api_destsf
     */
    public Integer getDestsf() {
        return this.destsf;
    }

    /**
     * api_destsfを設定します。
     * @param destsf api_destsf
     */
    public void setDestsf(Integer destsf) {
        this.destsf = destsf;
    }

    /**
     * api_lost_flagを取得します。
     * @return api_lost_flag
     */
    public List<Integer> getLostFlag() {
        return this.lostFlag;
    }

    /**
     * api_lost_flagを設定します。
     * @param lostFlag api_lost_flag
     */
    public void setLostFlag(List<Integer> lostFlag) {
        this.lostFlag = lostFlag;
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
     * api_enemy_infoを取得します。
     * @return api_enemy_info
     */
    public BattleResult.EnemyInfo getEnemyInfo() {
        return this.enemyInfo;
    }

    /**
     * api_enemy_infoを設定します。
     * @param enemyInfo api_enemy_info
     */
    public void setEnemyInfo(BattleResult.EnemyInfo enemyInfo) {
        this.enemyInfo = enemyInfo;
    }

    /**
     * api_first_clearを取得します。
     * @return api_first_clear
     */
    public Integer getFirstClear() {
        return this.firstClear;
    }

    /**
     * api_first_clearを設定します。
     * @param firstClear api_first_clear
     */
    public void setFirstClear(Integer firstClear) {
        this.firstClear = firstClear;
    }

    /**
     * api_mapcell_incentiveを取得します。
     * @return api_mapcell_incentive
     */
    public Boolean getMapcellIncentive() {
        return this.mapcellIncentive;
    }

    /**
     * api_mapcell_incentiveを設定します。
     * @param mapcellIncentive api_mapcell_incentive
     */
    public void setMapcellIncentive(Boolean mapcellIncentive) {
        this.mapcellIncentive = mapcellIncentive;
    }

    /**
     * api_get_flagを取得します。
     * @return api_get_flag
     */
    public List<Integer> getGetFlag() {
        return this.getFlag;
    }

    /**
     * api_get_flagを設定します。
     * @param getFlag api_get_flag
     */
    public void setGetFlag(List<Integer> getFlag) {
        this.getFlag = getFlag;
    }

    /**
     * api_get_shipを取得します。
     * @return api_get_ship
     */
    public BattleResult.GetShip getGetShip() {
        return this.getShip;
    }

    /**
     * api_get_shipを設定します。
     * @param getShip api_get_ship
     */
    public void setGetShip(BattleResult.GetShip getShip) {
        this.getShip = getShip;
    }

    /**
     * api_get_eventitemを取得します。
     * @return api_get_eventitem
     */
    public List<BattleResult.GetEventitem> getGetEventitem() {
        return this.getEventitem;
    }

    /**
     * api_get_eventitemを設定します。
     * @param getEventitem api_get_eventitem
     */
    public void setGetEventitem(List<BattleResult.GetEventitem> getEventitem) {
        this.getEventitem = getEventitem;
    }

    /**
     * api_get_exmap_rateを取得します。
     * @return api_get_exmap_rate
     */
    public Integer getGetExmapRate() {
        return this.getExmapRate;
    }

    /**
     * api_get_exmap_rateを設定します。
     * @param getExmapRate api_get_exmap_rate
     */
    public void setGetExmapRate(Integer getExmapRate) {
        this.getExmapRate = getExmapRate;
    }

    /**
     * api_get_exmap_useitem_idを取得します。
     * @return api_get_exmap_useitem_id
     */
    public Integer getGetExmapUseitemId() {
        return this.getExmapUseitemId;
    }

    /**
     * api_get_exmap_useitem_idを設定します。
     * @param getExmapUseitemId api_get_exmap_useitem_id
     */
    public void setGetExmapUseitemId(Integer getExmapUseitemId) {
        this.getExmapUseitemId = getExmapUseitemId;
    }

    /**
     * api_mvp_combinedを取得します。
     * @return api_mvp_combined
     */
    public Integer getMvpCombined() {
        return this.mvpCombined;
    }

    /**
     * api_mvp_combinedを設定します。
     * @param mvpCombined api_mvp_combined
     */
    public void setMvpCombined(Integer mvpCombined) {
        this.mvpCombined = mvpCombined;
    }

    /**
     * api_get_ship_exp_combinedを取得します。
     * @return api_get_ship_exp_combined
     */
    public List<Integer> getGetShipExpCombined() {
        return this.getShipExpCombined;
    }

    /**
     * api_get_ship_exp_combinedを設定します。
     * @param getShipExpCombined api_get_ship_exp_combined
     */
    public void setGetShipExpCombined(List<Integer> getShipExpCombined) {
        this.getShipExpCombined = getShipExpCombined;
    }

    /**
     * api_get_exp_lvup_combinedを取得します。
     * @return api_get_exp_lvup_combined
     */
    public List<List<Integer>> getGetExpLvupCombined() {
        return this.getExpLvupCombined;
    }

    /**
     * api_get_exp_lvup_combinedを設定します。
     * @param getExpLvupCombined api_get_exp_lvup_combined
     */
    public void setGetExpLvupCombined(List<List<Integer>> getExpLvupCombined) {
        this.getExpLvupCombined = getExpLvupCombined;
    }

    /**
     * api_get_eventflagを取得します。
     * @return api_get_eventflag
     */
    public Integer getGetEventflag() {
        return this.getEventflag;
    }

    /**
     * api_get_eventflagを設定します。
     * @param getEventflag api_get_eventflag
     */
    public void setGetEventflag(Integer getEventflag) {
        this.getEventflag = getEventflag;
    }

    /**
     * api_escape_flagを取得します。
     * @return api_escape_flag
     */
    public Boolean getEscapeFlag() {
        return this.escapeFlag;
    }

    /**
     * api_escape_flagを設定します。
     * @param escapeFlag api_escape_flag
     */
    public void setEscapeFlag(Boolean escapeFlag) {
        this.escapeFlag = escapeFlag;
    }

    /**
     * api_escapeを取得します。
     * @return api_escape
     */
    public BattleResult.Escape getEscape() {
        return this.escape;
    }

    /**
     * api_escapeを設定します。
     * @param escape api_escape
     */
    public void setEscape(BattleResult.Escape escape) {
        this.escape = escape;
    }

    /**
     * api_landing_hpを取得します。
     * @return api_landing_hp
     */
    public BattleResult.LandingHp getLandingHp() {
        return this.landingHp;
    }

    /**
     * api_landing_hpを設定します。
     * @param landingHp api_landing_hp
     */
    public void setLandingHp(BattleResult.LandingHp landingHp) {
        this.landingHp = landingHp;
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
                .set("api_ship_id", bean::setShipId, JsonHelper::toIntegerList)
                .setString("api_win_rank", bean::setWinRank)
                .setInteger("api_get_exp", bean::setGetExp)
                .setInteger("api_mvp", bean::setMvp)
                .setInteger("api_member_lv", bean::setMemberLv)
                .setInteger("api_member_exp", bean::setMemberExp)
                .setInteger("api_get_base_exp", bean::setGetBaseExp)
                .set("api_get_ship_exp", bean::setGetShipExp, JsonHelper::toIntegerList)
                .set("api_get_exp_lvup", bean::setGetExpLvup, JsonHelper.toList(JsonHelper::toIntegerList))
                .setInteger("api_dests", bean::setDests)
                .setInteger("api_destsf", bean::setDestsf)
                .setString("api_quest_name", bean::setQuestName)
                .setInteger("api_quest_level", bean::setQuestLevel)
                .set("api_enemy_info", bean::setEnemyInfo, EnemyInfo::toEnemyInfo)
                .setInteger("api_first_clear", bean::setFirstClear)
                .setBoolean("api_mapcell_incentive", bean::setMapcellIncentive)
                .set("api_get_flag", bean::setGetFlag, JsonHelper::toIntegerList)
                .set("api_get_ship", bean::setGetShip, GetShip::toGetShip)
                .set("api_get_eventitem", bean::setGetEventitem, JsonHelper.toList(GetEventitem::toGetEventitem))
                .setInteger("api_get_exmap_rate", bean::setGetExmapRate)
                .setInteger("api_get_exmap_useitem_id", bean::setGetExmapUseitemId)
                .setInteger("api_mvp_combined", bean::setMvpCombined)
                .set("api_get_ship_exp_combined", bean::setGetShipExpCombined, JsonHelper::toIntegerList)
                .set("api_get_exp_lvup_combined", bean::setGetExpLvupCombined,
                        JsonHelper.toList(JsonHelper::toIntegerList))
                .setInteger("api_get_eventflag", bean::setGetEventflag)
                .setBoolean("api_escape_flag", bean::setEscapeFlag)
                .set("api_escape", bean::setEscape, Escape::toEscape)
                .set("api_landing_hp", bean::setLandingHp, LandingHp::toLandingHp);
        return bean;
    }
}
