package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 戦闘で使用される型
 *
 */
public class BattleTypes {

    private BattleTypes() {
    }

    /**
     * 戦闘を表します
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public interface IBattle extends Serializable {

        /**
         * api_dock_id/api_deck_idを取得します。
         * @return api_dock_id/api_deck_id
         */
        Integer getDockId();

        /**
         * api_ship_keを取得します。
         * @return api_ship_ke
         */
        List<Integer> getShipKe();

        /**
         * api_ship_lvを取得します。
         * @return api_ship_lv
         */
        List<Integer> getShipLv();

        /**
         * api_f_nowhpsを取得します。
         * @return api_f_nowhps
         */
        List<Integer> getFNowhps();

        /**
         * api_f_maxhpsを取得します。
         * @return api_f_maxhps
         */
        List<Integer> getFMaxhps();

        /**
         * api_e_nowhpsを取得します。
         * @return api_e_nowhps
         */
        List<Integer> getENowhps();

        /**
         * api_e_maxhpsを取得します。
         * @return api_e_maxhps
         */
        List<Integer> getEMaxhps();

        /**
         * api_eSlotを取得します。
         * @return api_eSlot
         */
        List<List<Integer>> getESlot();

        /**
         * api_fParamを取得します。
         * @return api_fParam
         */
        List<List<Integer>> getFParam();

        /**
         * api_eParamを取得します。
         * @return api_eParam
         */
        List<List<Integer>> getEParam();
    }

    /**
     * 連合艦隊での戦闘を表します
     */
    public interface ICombinedBattle extends IBattle {

        /**
         * api_f_nowhps_combinedを取得します。
         * @return api_f_nowhps_combined
         */
        List<Integer> getFNowhpsCombined();

        /**
         * api_f_maxhps_combinedを取得します。
         * @return api_f_maxhps_combined
         */
        List<Integer> getFMaxhpsCombined();

        /**
         * api_fParam_combinedを取得します。
         * @return api_fParam_combined
         */
        List<List<Integer>> getFParamCombined();
    }

    /**
     * 連合艦隊での戦闘を表します
     */
    public interface ICombinedEcBattle extends IBattle {

        /**
         * api_e_nowhps_combinedを取得します。
         * @return api_e_nowhps_combined
         */
        List<Integer> getENowhpsCombined();

        /**
         * api_e_maxhps_combinedを取得します。
         * @return api_e_maxhps_combined
         */
        List<Integer> getEMaxhpsCombined();

        /**
         * api_ship_ke_combinedを取得します。
         * @return api_ship_ke_combined
         */
        List<Integer> getShipKeCombined();

        /**
         * api_ship_lv_combinedを取得します。
         * @return api_ship_lv_combined
         */
        List<Integer> getShipLvCombined();

        /**
         * api_eSlot_combinedを取得します。
         * @return api_eSlot_combined
         */
        List<List<Integer>> getESlotCombined();

        /**
         * api_eParam_combinedを取得します。
         * @return api_eParam_combined
         */
        List<List<Integer>> getEParamCombined();
    }

    /**
     * 連合艦隊での夜戦を表します
     */
    public interface ICombinedEcMidnightBattle extends ICombinedEcBattle {

        /**
         * api_active_deckを取得します。
         * @return api_active_deck
         */
        List<Integer> getActiveDeck();
    }

    /**
     * 昼戦を表します
     */
    public interface ISortieBattle extends IBattle {

        /**
         * api_midnight_flagを取得します。
         * @return api_midnight_flag
         */
        Boolean getMidnightFlag();

        /**
         * api_searchを取得します。
         * @return api_search
         */
        List<Integer> getSearch();

        /**
         * api_stage_flagを取得します。
         * @return api_stage_flag
         */
        List<Integer> getStageFlag();
    }

    /**
     * 昼戦砲撃を表します
     */
    public interface ISortieHougeki extends ISortieBattle {

        /**
         * api_opening_flagを取得します。
         * @return api_opening_flag
         */
        Boolean getOpeningFlag();

        /**
         * api_opening_atackを取得します。
         * @return api_opening_atack
         */
        BattleTypes.Raigeki getOpeningAtack();

        /**
         * api_opening_taisen_flagを取得します。
         * @return api_opening_taisen_flag
         */
        Boolean getOpeningTaisenFlag();

        /**
         * api_opening_taisenを取得します。
         * @return api_opening_taisen
         */
        BattleTypes.Hougeki getOpeningTaisen();

        /**
         * api_hourai_flagを取得します。
         * @return api_hourai_flag
         */
        List<Integer> getHouraiFlag();

        /**
         * api_hougeki1を取得します。
         * @return api_hougeki1
         */
        BattleTypes.Hougeki getHougeki1();

        /**
         * api_hougeki2を取得します。
         * @return api_hougeki2
         */
        BattleTypes.Hougeki getHougeki2();

        /**
         * api_hougeki3を取得します。
         * @return api_hougeki3
         */
        BattleTypes.Hougeki getHougeki3();

        /**
         * api_raigekiを取得します。
         * @return api_raigeki
         */
        BattleTypes.Raigeki getRaigeki();
    }

    /**
     * 夜戦を表します
     */
    public interface IMidnightBattle extends IBattle {

        /**
         * api_touch_planeを取得します。
         * @return api_touch_plane
         */
        List<Integer> getTouchPlane();

        /**
         * api_flare_posを取得します。
         * @return api_flare_pos
         */
        List<Integer> getFlarePos();

        /**
         * api_hougekiを取得します。
         * @return api_hougeki
         */
        BattleTypes.MidnightHougeki getHougeki();
    }

    /**
     * 陣形を表します
     */
    public interface IFormation extends IBattle {

        /**
         * api_formationを取得します。
         * @return api_formation
         */
        List<Integer> getFormation();
    }

    /**
     * 航空戦フェイズ
     */
    public interface IKouku extends IBattle {

        /**
         * api_injection_koukuを取得します。
         * @return api_injection_kouku
         */
        BattleTypes.Kouku getInjectionKouku();

        /**
         * api_koukuを取得します。
         * @return api_kouku
         */
        BattleTypes.Kouku getKouku();
    }

    /**
     * 航空戦
     */
    public interface IAirbattle extends IKouku {

        /**
         * api_kouku2を取得します。
         * @return api_kouku2
         */
        BattleTypes.Kouku getKouku2();
    }

    /**
     * 支援フェイズ
     */
    public interface ISupport extends IBattle {

        /**
         * api_support_flagを取得します。
         * @return api_support_flag
         */
        Integer getSupportFlag();

        /**
         * api_support_infoを取得します。
         * @return api_support_info
         */
        BattleTypes.SupportInfo getSupportInfo();
    }

    /**
     * 支援フェイズ(夜戦)
     */
    public interface INSupport extends IBattle {

        /**
         * api_n_support_flagを取得します。
         * @return api_n_support_flag
         */
        Integer getNSupportFlag();

        /**
         * api_n_support_infoを取得します。
         * @return api_n_support_info
         */
        BattleTypes.SupportInfo getNSupportInfo();
    }

    /**
     * 砲撃フェイズ
     */
    public interface IHougeki {

        /**
         * api_at_listを取得します。
         * @return api_at_list
         */
        List<Integer> getAtList();

        /**
         * api_at_typeを取得します。
         * @return api_at_type
         */
        List<Integer> getAtType();

        /**
         * api_df_listを取得します。
         * @return api_df_list
         */
        List<List<Integer>> getDfList();

        /**
         * api_si_listを取得します。
         * @return api_si_list
         */
        List<List<Integer>> getSiList();

        /**
         * api_cl_listを取得します。
         * @return api_cl_list
         */
        List<List<Integer>> getClList();

        /**
         * api_damageを取得します。
         * @return api_damage
         */
        List<List<Double>> getDamage();

        /**
         * api_at_eflagを取得します。
         */
        List<Integer> getAtEflag();
    }

    /**
     * 基地航空隊
     */
    public interface IAirBaseAttack {

        /**
         * api_air_base_injectionを取得します。
         * @return api_air_base_injection
         */
        AirBaseAttack getAirBaseInjection();

        /**
         * api_air_base_attackを取得します。
         * @return api_air_base_attack
         */
        List<AirBaseAttack> getAirBaseAttack();
    }

    /**
     * 航空戦
     */
    @Data
    public static class Kouku implements Serializable {

        private static final long serialVersionUID = -4772699490686134759L;

        /** api_plane_from */
        private List<List<Integer>> planeFrom;

        /** api_stage1 */
        private Stage1 stage1;

        /** api_stage2 */
        private Stage2 stage2;

        /** api_stage3 */
        private Stage3 stage3;

        /** api_stage3_combined */
        private Stage3 stage3Combined;

        /**
         * JsonObjectから{@link Kouku}を構築します
         *
         * @param json JsonObject
         * @return {@link Kouku}
         */
        public static Kouku toKouku(JsonObject json) {
            Kouku bean = new Kouku();
            JsonHelper.bind(json)
                    .set("api_plane_from", bean::setPlaneFrom, JsonHelper.toList(JsonHelper::toIntegerList))
                    .set("api_stage1", bean::setStage1, Stage1::toStage1)
                    .set("api_stage2", bean::setStage2, Stage2::toStage2)
                    .set("api_stage3", bean::setStage3, Stage3::toStage3)
                    .set("api_stage3_combined", bean::setStage3Combined, Stage3::toStage3);
            return bean;
        }
    }

    /**
     * 航空戦 Stage1
     */
    @Data
    public static class Stage1 implements Serializable {

        private static final long serialVersionUID = 4452384504283266181L;

        /** api_f_count */
        private Integer fCount;

        /** api_f_lostcount */
        private Integer fLostcount;

        /** api_e_count */
        private Integer eCount;

        /** api_e_lostcount */
        private Integer eLostcount;

        /** api_disp_seiku */
        private Integer dispSeiku;

        /** api_touch_plane */
        private List<Integer> touchPlane;

        /**
         * JsonObjectから{@link Stage1}を構築します
         *
         * @param json JsonObject
         * @return {@link Stage1}
         */
        public static Stage1 toStage1(JsonObject json) {
            Stage1 bean = new Stage1();
            JsonHelper.bind(json)
                    .setInteger("api_f_count", bean::setFCount)
                    .setInteger("api_f_lostcount", bean::setFLostcount)
                    .setInteger("api_e_count", bean::setECount)
                    .setInteger("api_e_lostcount", bean::setELostcount)
                    .setInteger("api_disp_seiku", bean::setDispSeiku)
                    .set("api_touch_plane", bean::setTouchPlane, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 航空戦 Stage2
     */
    @Data
    public static class Stage2 implements Serializable {

        private static final long serialVersionUID = -4886136119036663720L;

        /** api_f_count */
        private Integer fCount;

        /** api_f_lostcount */
        private Integer fLostcount;

        /** api_e_count */
        private Integer eCount;

        /** api_e_lostcount */
        private Integer eLostcount;

        /** api_air_fire */
        private AirFire airFire;

        /**
         * JsonObjectから{@link Stage2}を構築します
         *
         * @param json JsonObject
         * @return {@link Stage2}
         */
        public static Stage2 toStage2(JsonObject json) {
            Stage2 bean = new Stage2();
            JsonHelper.bind(json)
                    .setInteger("api_f_count", bean::setFCount)
                    .setInteger("api_f_lostcount", bean::setFLostcount)
                    .setInteger("api_e_count", bean::setECount)
                    .setInteger("api_e_lostcount", bean::setELostcount)
                    .set("api_air_fire", bean::setAirFire, AirFire::toAirFire);
            return bean;
        }
    }

    /**
     * 航空戦 AirFire
     */
    @Data
    public static class AirFire implements Serializable {

        private static final long serialVersionUID = 9184007312267069221L;

        /** api_idx */
        private Integer idx;

        /** api_kind */
        private Integer kind;

        /** api_use_items */
        private List<Integer> useItems;

        /**
         * JsonObjectから{@link AirFire}を構築します
         *
         * @param json JsonObject
         * @return {@link AirFire}
         */
        public static AirFire toAirFire(JsonObject json) {
            AirFire bean = new AirFire();
            JsonHelper.bind(json)
                    .setInteger("api_idx", bean::setIdx)
                    .setInteger("api_kind", bean::setKind)
                    .set("api_use_items", bean::setUseItems, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 航空戦 Stage3
     */
    @Data
    public static class Stage3 implements Serializable {

        private static final long serialVersionUID = -3123663766612134315L;

        /** api_frai_flag */
        private List<Integer> fraiFlag;

        /** api_erai_flag */
        private List<Integer> eraiFlag;

        /** api_fbak_flag */
        private List<Integer> fbakFlag;

        /** api_ebak_flag */
        private List<Integer> ebakFlag;

        /** api_fcl_flag */
        private List<Integer> fclFlag;

        /** api_ecl_flag */
        private List<Integer> eclFlag;

        /** api_fdam */
        private List<Double> fdam;

        /** api_edam */
        private List<Double> edam;

        /**
         * JsonObjectから{@link Stage3}を構築します
         *
         * @param json JsonObject
         * @return {@link Stage3}
         */
        public static Stage3 toStage3(JsonObject json) {
            Stage3 bean = new Stage3();
            JsonHelper.bind(json)
                    .set("api_frai_flag", bean::setFraiFlag, JsonHelper::toIntegerList)
                    .set("api_erai_flag", bean::setEraiFlag, JsonHelper::toIntegerList)
                    .set("api_fbak_flag", bean::setFbakFlag, JsonHelper::toIntegerList)
                    .set("api_ebak_flag", bean::setEbakFlag, JsonHelper::toIntegerList)
                    .set("api_fcl_flag", bean::setFclFlag, JsonHelper::toIntegerList)
                    .set("api_ecl_flag", bean::setEclFlag, JsonHelper::toIntegerList)
                    .set("api_fdam", bean::setFdam, JsonHelper::toDoubleList)
                    .set("api_edam", bean::setEdam, JsonHelper::toDoubleList);
            return bean;
        }
    }

    /**
     * 支援
     */
    @Data
    public static class SupportInfo implements Serializable {

        private static final long serialVersionUID = 4584855806683009170L;

        /** api_support_airatack */
        private SupportAiratack supportAiratack;

        /** api_support_hourai */
        private SupportHourai supportHourai;

        /**
         * JsonObjectから{@link SupportInfo}を構築します
         *
         * @param json JsonObject
         * @return {@link SupportInfo}
         */
        public static SupportInfo toSupportInfo(JsonObject json) {
            SupportInfo bean = new SupportInfo();
            JsonHelper.bind(json)
                    .set("api_support_airatack", bean::setSupportAiratack, SupportAiratack::toSupportAiratack)
                    .set("api_support_hourai", bean::setSupportHourai, SupportHourai::toSupportHourai);
            return bean;
        }
    }

    /**
     * 支援 Airatack
     */
    @Data
    public static class SupportAiratack implements Serializable {

        private static final long serialVersionUID = -152573445471984643L;

        /** api_deck_id */
        private Integer deckId;

        /** api_ship_id */
        private List<Integer> shipId;

        /** api_undressing_flag */
        private List<Integer> undressingFlag;

        /** api_stage_flag */
        private List<Integer> stageFlag;

        /** api_plane_from */
        private List<List<Integer>> planeFrom;

        /** api_stage1 */
        private Stage1 stage1;

        /** api_stage2 */
        private Stage2 stage2;

        /** api_stage3 */
        private Stage3 stage3;

        /**
         * JsonObjectから{@link SupportAiratack}を構築します
         *
         * @param json JsonObject
         * @return {@link SupportAiratack}
         */
        public static SupportAiratack toSupportAiratack(JsonObject json) {
            SupportAiratack bean = new SupportAiratack();
            JsonHelper.bind(json)
                    .setInteger("api_deck_id", bean::setDeckId)
                    .set("api_ship_id", bean::setShipId, JsonHelper::toIntegerList)
                    .set("api_undressing_flag", bean::setUndressingFlag, JsonHelper::toIntegerList)
                    .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList)
                    .set("api_plane_from", bean::setPlaneFrom, JsonHelper.toList(JsonHelper::toIntegerList))
                    .set("api_stage1", bean::setStage1, Stage1::toStage1)
                    .set("api_stage2", bean::setStage2, Stage2::toStage2)
                    .set("api_stage3", bean::setStage3, Stage3::toStage3);
            return bean;
        }
    }

    /**
     * 支援 Hourai
     */
    @Data
    public static class SupportHourai implements Serializable {

        private static final long serialVersionUID = 3060460027587015362L;

        /** api_deck_id */
        private Integer deckId;

        /** api_ship_id */
        private List<Integer> shipId;

        /** api_undressing_flag */
        private List<Integer> undressingFlag;

        /** api_cl_list */
        private List<Integer> clList;

        /** api_damage */
        private List<Double> damage;

        /**
         * JsonObjectから{@link SupportHourai}を構築します
         *
         * @param json JsonObject
         * @return {@link SupportHourai}
         */
        public static SupportHourai toSupportHourai(JsonObject json) {
            SupportHourai bean = new SupportHourai();
            JsonHelper.bind(json)
                    .setInteger("api_deck_id", bean::setDeckId)
                    .set("api_ship_id", bean::setShipId, JsonHelper::toIntegerList)
                    .set("api_undressing_flag", bean::setUndressingFlag, JsonHelper::toIntegerList)
                    .set("api_cl_list", bean::setClList, JsonHelper::toIntegerList)
                    .set("api_damage", bean::setDamage, JsonHelper::toDoubleList);
            return bean;
        }
    }

    /**
     * 雷撃
     */
    @Data
    public static class Raigeki implements Serializable {

        private static final long serialVersionUID = 4769524848250854584L;

        /** api_frai */
        private List<Integer> frai;

        /** api_erai */
        private List<Integer> erai;

        /** api_fdam */
        private List<Double> fdam;

        /** api_edam */
        private List<Double> edam;

        /** api_fydam */
        private List<Double> fydam;

        /** api_eydam */
        private List<Double> eydam;

        /** api_fcl */
        private List<Integer> fcl;

        /** api_ecl */
        private List<Integer> ecl;

        /**
         * JsonObjectから{@link Raigeki}を構築します
         *
         * @param json JsonObject
         * @return {@link Raigeki}
         */
        public static Raigeki toRaigeki(JsonObject json) {
            Raigeki bean = new Raigeki();
            JsonHelper.bind(json)
                    .set("api_frai", bean::setFrai, JsonHelper::toIntegerList)
                    .set("api_erai", bean::setErai, JsonHelper::toIntegerList)
                    .set("api_fdam", bean::setFdam, JsonHelper::toDoubleList)
                    .set("api_edam", bean::setEdam, JsonHelper::toDoubleList)
                    .set("api_fydam", bean::setFydam, JsonHelper::toDoubleList)
                    .set("api_eydam", bean::setEydam, JsonHelper::toDoubleList)
                    .set("api_fcl", bean::setFcl, JsonHelper::toIntegerList)
                    .set("api_ecl", bean::setEcl, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 砲撃
     */
    @Data
    public static class Hougeki implements IHougeki, Serializable {

        private static final long serialVersionUID = -5339612671179791906L;

        /** api_at_list */
        public List<Integer> atList;

        /** api_at_type */
        public List<Integer> atType;

        /** api_df_list */
        public List<List<Integer>> dfList;

        /** api_si_list */
        public List<List<Integer>> siList;

        /** api_cl_list */
        public List<List<Integer>> clList;

        /** api_damage */
        public List<List<Double>> damage;

        /** api_at_eflag */
        public List<Integer> atEflag;

        /**
         * JsonObjectから{@link Hougeki}を構築します
         *
         * @param json JsonObject
         * @return {@link Hougeki}
         */
        public static Hougeki toHougeki(JsonObject json) {
            Hougeki bean = new Hougeki();
            JsonHelper.bind(json)
                    .set("api_at_list", bean::setAtList, JsonHelper::toIntegerList)
                    .set("api_at_type", bean::setAtType, JsonHelper::toIntegerList)
                    .set("api_df_list", bean::setDfList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_si_list", bean::setSiList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_cl_list", bean::setClList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_damage", bean::setDamage, JsonHelper.toList(JsonHelper::checkedToDoubleList))
                    .set("api_at_eflag", bean::setAtEflag, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 砲撃
     */
    @Data
    public static class MidnightHougeki implements IHougeki, Serializable {

        private static final long serialVersionUID = 8897484398602178656L;

        /** api_at_list */
        private List<Integer> atList;

        /** api_at_type */
        private List<Integer> atType;

        /** api_df_list */
        private List<List<Integer>> dfList;

        /** api_si_list */
        private List<List<Integer>> siList;

        /** api_cl_list */
        private List<List<Integer>> clList;

        /** api_sp_list */
        private List<Integer> spList;

        /** api_damage */
        private List<List<Double>> damage;

        /** api_at_eflag */
        private List<Integer> atEflag;

        /**
         * JsonObjectから{@link MidnightHougeki}を構築します
         *
         * @param json JsonObject
         * @return {@link MidnightHougeki}
         */
        public static MidnightHougeki toMidnightHougeki(JsonObject json) {
            MidnightHougeki bean = new MidnightHougeki();
            JsonHelper.bind(json)
                    .set("api_at_list", bean::setAtList, JsonHelper::toIntegerList)
                    .set("api_at_type", bean::setAtType, JsonHelper::toIntegerList)
                    .set("api_df_list", bean::setDfList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_si_list", bean::setSiList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_cl_list", bean::setClList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_sp_list", bean::setSpList, JsonHelper::toIntegerList)
                    .set("api_damage", bean::setDamage, JsonHelper.toList(JsonHelper::checkedToDoubleList))
                    .set("api_at_eflag", bean::setAtEflag, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * 基地航空戦
     */
    @Data
    public static class AirBaseAttack implements Serializable {

        private static final long serialVersionUID = -7255540756859970824L;

        /** api_base_id */
        private Integer baseId;

        /** api_plane_from */
        private List<List<Integer>> planeFrom;

        /** api_squadron_plane または api_air_base_data */
        private List<SquadronPlane> squadronPlane;

        /** api_stage1 */
        private Stage1 stage1;

        /** api_stage2 */
        private Stage2 stage2;

        /** api_stage3 */
        private Stage3 stage3;

        /** api_stage3_combined */
        private Stage3 stage3Combined;

        /** api_stage_flag */
        private List<Integer> stageFlag;

        /**
         * JsonObjectから{@link AirBaseAttack}を構築します
         *
         * @param json JsonObject
         * @return {@link AirBaseAttack}
         */
        public static AirBaseAttack toAirBaseAttack(JsonObject json) {
            AirBaseAttack bean = new AirBaseAttack();
            JsonHelper.bind(json)
                    .setInteger("api_base_id", bean::setBaseId)
                    .set("api_plane_from", bean::setPlaneFrom, JsonHelper.toList(JsonHelper::toIntegerList))
                    .set("api_squadron_plane", bean::setSquadronPlane,
                            JsonHelper.toList(SquadronPlane::toSquadronPlane))
                    .set("api_air_base_data", bean::setSquadronPlane,
                            JsonHelper.toList(SquadronPlane::toSquadronPlane))
                    .set("api_stage1", bean::setStage1, Stage1::toStage1)
                    .set("api_stage2", bean::setStage2, Stage2::toStage2)
                    .set("api_stage3", bean::setStage3, Stage3::toStage3)
                    .set("api_stage3_combined", bean::setStage3Combined, Stage3::toStage3)
                    .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * api_squadron_plane
     */
    @Data
    public static class SquadronPlane implements Serializable {

        private static final long serialVersionUID = -8553957904692166481L;

        /** api_count */
        private Integer count;

        /** api_mst_id */
        private Integer mstId;

        /**
         * JsonObjectから{@link SquadronPlane}を構築します
         *
         * @param json JsonObject
         * @return {@link SquadronPlane}
         */
        public static SquadronPlane toSquadronPlane(JsonObject json) {
            SquadronPlane bean = new SquadronPlane();
            JsonHelper.bind(json)
                    .setInteger("api_count", bean::setCount)
                    .setInteger("api_mst_id", bean::setMstId);
            return bean;
        }
    }

    /**
     * 陣形
     */
    public enum Formation {
        単縦陣,
        複縦陣,
        輪形陣,
        梯形陣,
        単横陣,
        警戒陣,
        第一警戒航行序列,
        第二警戒航行序列,
        第三警戒航行序列,
        第四警戒航行序列;

        /**
         * 陣形を取得します
         * @param f api_formation[0],api_formation[1]
         * @return 陣形
         */
        public static Formation toFormation(int f) {
            switch (f) {
            case 1:
                return 単縦陣;
            case 2:
                return 複縦陣;
            case 3:
                return 輪形陣;
            case 4:
                return 梯形陣;
            case 5:
                return 単横陣;
            case 6:
                return 警戒陣;
            case 11:
                return 第一警戒航行序列;
            case 12:
                return 第二警戒航行序列;
            case 13:
                return 第三警戒航行序列;
            case 14:
                return 第四警戒航行序列;
            default:
                return 単縦陣;
            }
        }
    }

    /**
     * 艦隊行動
     */
    public enum Intercept {
        同航戦, 反航戦, Ｔ字戦有利, Ｔ字戦不利;

        /**
         * 艦隊行動を取得します
         *
         * @param i api_formation[2]
         * @return 艦隊行動
         */
        public static Intercept toIntercept(int i) {
            switch (i) {
            case 1:
                return 同航戦;
            case 2:
                return 反航戦;
            case 3:
                return Ｔ字戦有利;
            case 4:
                return Ｔ字戦不利;
            default:
                return 同航戦;
            }
        }
    }

    /**
     * 制空権表示
     */
    public enum DispSeiku {
        制空均衡, 制空権確保, 航空優勢, 航空劣勢, 制空権喪失;

        /**
         * 制空権表示を取得します
         *
         * @param i api_disp_seiku
         * @return 制空権表示
         */
        public static DispSeiku toDispSeiku(int i) {
            switch (i) {
            case 0:
                return 制空均衡;
            case 1:
                return 制空権確保;
            case 2:
                return 航空優勢;
            case 3:
                return 航空劣勢;
            case 4:
                return 制空権喪失;
            default:
                return 制空均衡;
            }
        }
    }

    /**
     * 連合艦隊
     * 0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊
     */
    public enum CombinedType {
        未結成, 機動部隊, 水上部隊, 輸送部隊;

        public static CombinedType toCombinedType(int i) {
            switch (i) {
            case 0:
                return 未結成;
            case 1:
                return 機動部隊;
            case 2:
                return 水上部隊;
            case 3:
                return 輸送部隊;
            default:
                return 未結成;
            }
        }
    }

    /**
     * 攻撃種別
     *
     */
    public interface AtType {
    }

    /**
     * 攻撃種別
     *
     */
    public enum SortieAtType implements AtType {
        通常攻撃,
        レーザー攻撃,
        連撃,
        主砲副砲CI,
        主砲電探CI,
        主砲徹甲弾CI,
        主砲主砲CI,
        戦爆連合CI;

        public static SortieAtType toSortieAtType(int i) {
            switch (i) {
            case 0:
                return 通常攻撃;
            case 1:
                return レーザー攻撃;
            case 2:
                return 連撃;
            case 3:
                return 主砲副砲CI;
            case 4:
                return 主砲電探CI;
            case 5:
                return 主砲徹甲弾CI;
            case 6:
                return 主砲主砲CI;
            case 7:
                return 戦爆連合CI;
            default:
                return 通常攻撃;
            }
        }
    }

    /**
     * 攻撃種別
     *
     */
    public enum SortieAtTypeRaigeki implements AtType {
        通常雷撃;
    }

    /**
     * 攻撃種別
     *
     */
    public enum MidnightSpList implements AtType {
        通常攻撃,
        連撃,
        主砲魚雷CI,
        魚雷魚雷CI,
        主砲副砲CI,
        主砲主砲CI,
        戦爆連合CI;

        public static MidnightSpList toMidnightSpList(int i) {
            switch (i) {
            case 0:
                return 通常攻撃;
            case 1:
                return 連撃;
            case 2:
                return 主砲魚雷CI;
            case 3:
                return 魚雷魚雷CI;
            case 4:
                return 主砲副砲CI;
            case 5:
                return 主砲主砲CI;
            case 6:
                return 戦爆連合CI;
            default:
                return 通常攻撃;
            }
        }
    }
}
