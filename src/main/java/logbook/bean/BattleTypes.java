package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

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
         * api_nowhpsを取得します。
         * @return api_nowhps
         */
        List<Integer> getNowhps();

        /**
         * api_maxhpsを取得します。
         * @return api_maxhps
         */
        List<Integer> getMaxhps();

        /**
         * api_eSlotを取得します。
         * @return api_eSlot
         */
        List<List<Integer>> getESlot();

        /**
         * api_eKyoukaを取得します。
         * @return api_eKyouka
         */
        List<List<Integer>> getEKyouka();

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
         * api_nowhps_combinedを取得します。
         * @return api_nowhps_combined
         */
        List<Integer> getNowhpsCombined();

        /**
         * api_maxhps_combinedを取得します。
         * @return api_maxhps_combined
         */
        List<Integer> getMaxhpsCombined();

        /**
         * api_fParam_combinedを取得します。
         * @return api_fParam_combined
         */
        List<List<Integer>> getFParamCombined();
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
        Boolean getSupportFlag();

        /**
         * api_support_infoを取得します。
         * @return api_support_info
         */
        BattleTypes.SupportInfo getSupportInfo();
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
    }

    /**
     * 基地航空隊
     */
    public interface IAirBaseAttack {

        /**
         * api_air_base_attackを取得します。
         * @return api_air_base_attack
         */
        List<AirBaseAttack> getAirBaseAttack();
    }

    /**
     * 航空戦
     */
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
        private Stage3Combined stage3Combined;

        /**
         * api_plane_fromを取得します。
         * @return api_plane_from
         */
        public List<List<Integer>> getPlaneFrom() {
            return this.planeFrom;
        }

        /**
         * api_plane_fromを設定します。
         * @param planeFrom api_plane_from
         */
        public void setPlaneFrom(List<List<Integer>> planeFrom) {
            this.planeFrom = planeFrom;
        }

        /**
         * api_stage1を取得します。
         * @return api_stage1
         */
        public Stage1 getStage1() {
            return this.stage1;
        }

        /**
         * api_stage1を設定します。
         * @param stage1 api_stage1
         */
        public void setStage1(Stage1 stage1) {
            this.stage1 = stage1;
        }

        /**
         * api_stage2を取得します。
         * @return api_stage2
         */
        public Stage2 getStage2() {
            return this.stage2;
        }

        /**
         * api_stage2を設定します。
         * @param stage2 api_stage2
         */
        public void setStage2(Stage2 stage2) {
            this.stage2 = stage2;
        }

        /**
         * api_stage3を取得します。
         * @return api_stage3
         */
        public Stage3 getStage3() {
            return this.stage3;
        }

        /**
         * api_stage3を設定します。
         * @param stage3 api_stage3
         */
        public void setStage3(Stage3 stage3) {
            this.stage3 = stage3;
        }

        /**
         * api_stage3_combinedを取得します。
         * @return api_stage3_combined
         */
        public Stage3Combined getStage3Combined() {
            return this.stage3Combined;
        }

        /**
         * api_stage3_combinedを設定します。
         * @param stage3Combined api_stage3_combined
         */
        public void setStage3Combined(Stage3Combined stage3Combined) {
            this.stage3Combined = stage3Combined;
        }

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
                    .set("api_stage3_combined", bean::setStage3Combined, Stage3Combined::toStage3Combined);
            return bean;
        }
    }

    /**
     * 航空戦 Stage1
     */
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
         * api_f_countを取得します。
         * @return api_f_count
         */
        public Integer getFCount() {
            return this.fCount;
        }

        /**
         * api_f_countを設定します。
         * @param fCount api_f_count
         */
        public void setFCount(Integer fCount) {
            this.fCount = fCount;
        }

        /**
         * api_f_lostcountを取得します。
         * @return api_f_lostcount
         */
        public Integer getFLostcount() {
            return this.fLostcount;
        }

        /**
         * api_f_lostcountを設定します。
         * @param fLostcount api_f_lostcount
         */
        public void setFLostcount(Integer fLostcount) {
            this.fLostcount = fLostcount;
        }

        /**
         * api_e_countを取得します。
         * @return api_e_count
         */
        public Integer getECount() {
            return this.eCount;
        }

        /**
         * api_e_countを設定します。
         * @param eCount api_e_count
         */
        public void setECount(Integer eCount) {
            this.eCount = eCount;
        }

        /**
         * api_e_lostcountを取得します。
         * @return api_e_lostcount
         */
        public Integer getELostcount() {
            return this.eLostcount;
        }

        /**
         * api_e_lostcountを設定します。
         * @param eLostcount api_e_lostcount
         */
        public void setELostcount(Integer eLostcount) {
            this.eLostcount = eLostcount;
        }

        /**
         * api_disp_seikuを取得します。
         * @return api_disp_seiku
         */
        public Integer getDispSeiku() {
            return this.dispSeiku;
        }

        /**
         * api_disp_seikuを設定します。
         * @param dispSeiku api_disp_seiku
         */
        public void setDispSeiku(Integer dispSeiku) {
            this.dispSeiku = dispSeiku;
        }

        /**
         * api_touch_planeを取得します。
         * @return api_touch_plane
         */
        public List<Integer> getTouchPlane() {
            return this.touchPlane;
        }

        /**
         * api_touch_planeを設定します。
         * @param touchPlane api_touch_plane
         */
        public void setTouchPlane(List<Integer> touchPlane) {
            this.touchPlane = touchPlane;
        }

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
         * api_f_countを取得します。
         * @return api_f_count
         */
        public Integer getFCount() {
            return this.fCount;
        }

        /**
         * api_f_countを設定します。
         * @param fCount api_f_count
         */
        public void setFCount(Integer fCount) {
            this.fCount = fCount;
        }

        /**
         * api_f_lostcountを取得します。
         * @return api_f_lostcount
         */
        public Integer getFLostcount() {
            return this.fLostcount;
        }

        /**
         * api_f_lostcountを設定します。
         * @param fLostcount api_f_lostcount
         */
        public void setFLostcount(Integer fLostcount) {
            this.fLostcount = fLostcount;
        }

        /**
         * api_e_countを取得します。
         * @return api_e_count
         */
        public Integer getECount() {
            return this.eCount;
        }

        /**
         * api_e_countを設定します。
         * @param eCount api_e_count
         */
        public void setECount(Integer eCount) {
            this.eCount = eCount;
        }

        /**
         * api_e_lostcountを取得します。
         * @return api_e_lostcount
         */
        public Integer getELostcount() {
            return this.eLostcount;
        }

        /**
         * api_e_lostcountを設定します。
         * @param eLostcount api_e_lostcount
         */
        public void setELostcount(Integer eLostcount) {
            this.eLostcount = eLostcount;
        }

        /**
         * api_air_fireを取得します。
         * @return api_air_fire
         */
        public AirFire getAirFire() {
            return this.airFire;
        }

        /**
         * api_air_fireを設定します。
         * @param airFire api_air_fire
         */
        public void setAirFire(AirFire airFire) {
            this.airFire = airFire;
        }

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
    public static class AirFire implements Serializable {

        private static final long serialVersionUID = 9184007312267069221L;

        /** api_idx */
        private Integer idx;

        /** api_kind */
        private Integer kind;

        /** api_use_items */
        private List<Integer> useItems;

        /**
         * api_idxを取得します。
         * @return api_idx
         */
        public Integer getIdx() {
            return this.idx;
        }

        /**
         * api_idxを設定します。
         * @param idx api_idx
         */
        public void setIdx(Integer idx) {
            this.idx = idx;
        }

        /**
         * api_kindを取得します。
         * @return api_kind
         */
        public Integer getKind() {
            return this.kind;
        }

        /**
         * api_kindを設定します。
         * @param kind api_kind
         */
        public void setKind(Integer kind) {
            this.kind = kind;
        }

        /**
         * api_use_itemsを取得します。
         * @return api_use_items
         */
        public List<Integer> getUseItems() {
            return this.useItems;
        }

        /**
         * api_use_itemsを設定します。
         * @param useItems api_use_items
         */
        public void setUseItems(List<Integer> useItems) {
            this.useItems = useItems;
        }

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
         * api_frai_flagを取得します。
         * @return api_frai_flag
         */
        public List<Integer> getFraiFlag() {
            return this.fraiFlag;
        }

        /**
         * api_frai_flagを設定します。
         * @param fraiFlag api_frai_flag
         */
        public void setFraiFlag(List<Integer> fraiFlag) {
            this.fraiFlag = fraiFlag;
        }

        /**
         * api_erai_flagを取得します。
         * @return api_erai_flag
         */
        public List<Integer> getEraiFlag() {
            return this.eraiFlag;
        }

        /**
         * api_erai_flagを設定します。
         * @param eraiFlag api_erai_flag
         */
        public void setEraiFlag(List<Integer> eraiFlag) {
            this.eraiFlag = eraiFlag;
        }

        /**
         * api_fbak_flagを取得します。
         * @return api_fbak_flag
         */
        public List<Integer> getFbakFlag() {
            return this.fbakFlag;
        }

        /**
         * api_fbak_flagを設定します。
         * @param fbakFlag api_fbak_flag
         */
        public void setFbakFlag(List<Integer> fbakFlag) {
            this.fbakFlag = fbakFlag;
        }

        /**
         * api_ebak_flagを取得します。
         * @return api_ebak_flag
         */
        public List<Integer> getEbakFlag() {
            return this.ebakFlag;
        }

        /**
         * api_ebak_flagを設定します。
         * @param ebakFlag api_ebak_flag
         */
        public void setEbakFlag(List<Integer> ebakFlag) {
            this.ebakFlag = ebakFlag;
        }

        /**
         * api_fcl_flagを取得します。
         * @return api_fcl_flag
         */
        public List<Integer> getFclFlag() {
            return this.fclFlag;
        }

        /**
         * api_fcl_flagを設定します。
         * @param fclFlag api_fcl_flag
         */
        public void setFclFlag(List<Integer> fclFlag) {
            this.fclFlag = fclFlag;
        }

        /**
         * api_ecl_flagを取得します。
         * @return api_ecl_flag
         */
        public List<Integer> getEclFlag() {
            return this.eclFlag;
        }

        /**
         * api_ecl_flagを設定します。
         * @param eclFlag api_ecl_flag
         */
        public void setEclFlag(List<Integer> eclFlag) {
            this.eclFlag = eclFlag;
        }

        /**
         * api_fdamを取得します。
         * @return api_fdam
         */
        public List<Double> getFdam() {
            return this.fdam;
        }

        /**
         * api_fdamを設定します。
         * @param fdam api_fdam
         */
        public void setFdam(List<Double> fdam) {
            this.fdam = fdam;
        }

        /**
         * api_edamを取得します。
         * @return api_edam
         */
        public List<Double> getEdam() {
            return this.edam;
        }

        /**
         * api_edamを設定します。
         * @param edam api_edam
         */
        public void setEdam(List<Double> edam) {
            this.edam = edam;
        }

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
     * 航空戦 Stage3Combined
     */
    public static class Stage3Combined implements Serializable {

        private static final long serialVersionUID = -628911942142343461L;

        /** api_frai_flag */
        private List<Integer> fraiFlag;

        /** api_fbak_flag */
        private List<Integer> fbakFlag;

        /** api_fcl_flag */
        private List<Integer> fclFlag;

        /** api_fdam */
        private List<Double> fdam;

        /**
         * api_frai_flagを取得します。
         * @return api_frai_flag
         */
        public List<Integer> getFraiFlag() {
            return this.fraiFlag;
        }

        /**
         * api_frai_flagを設定します。
         * @param fraiFlag api_frai_flag
         */
        public void setFraiFlag(List<Integer> fraiFlag) {
            this.fraiFlag = fraiFlag;
        }

        /**
         * api_fbak_flagを取得します。
         * @return api_fbak_flag
         */
        public List<Integer> getFbakFlag() {
            return this.fbakFlag;
        }

        /**
         * api_fbak_flagを設定します。
         * @param fbakFlag api_fbak_flag
         */
        public void setFbakFlag(List<Integer> fbakFlag) {
            this.fbakFlag = fbakFlag;
        }

        /**
         * api_fcl_flagを取得します。
         * @return api_fcl_flag
         */
        public List<Integer> getFclFlag() {
            return this.fclFlag;
        }

        /**
         * api_fcl_flagを設定します。
         * @param fclFlag api_fcl_flag
         */
        public void setFclFlag(List<Integer> fclFlag) {
            this.fclFlag = fclFlag;
        }

        /**
         * api_fdamを取得します。
         * @return api_fdam
         */
        public List<Double> getFdam() {
            return this.fdam;
        }

        /**
         * api_fdamを設定します。
         * @param fdam api_fdam
         */
        public void setFdam(List<Double> fdam) {
            this.fdam = fdam;
        }

        /**
         * JsonObjectから{@link Stage3Combined}を構築します
         *
         * @param json JsonObject
         * @return {@link Stage3Combined}
         */
        public static Stage3Combined toStage3Combined(JsonObject json) {
            Stage3Combined bean = new Stage3Combined();
            JsonHelper.bind(json)
                    .set("api_frai_flag", bean::setFraiFlag, JsonHelper::toIntegerList)
                    .set("api_fbak_flag", bean::setFbakFlag, JsonHelper::toIntegerList)
                    .set("api_fcl_flag", bean::setFclFlag, JsonHelper::toIntegerList)
                    .set("api_fdam", bean::setFdam, JsonHelper::toDoubleList);
            return bean;
        }
    }

    /**
     * 支援
     */
    public static class SupportInfo implements Serializable {

        private static final long serialVersionUID = 4584855806683009170L;

        /** api_support_airatack */
        private SupportAiratack supportAiratack;

        /** api_support_hourai */
        private SupportHourai supportHourai;

        /**
         * api_support_airatackを取得します。
         * @return api_support_airatack
         */
        public SupportAiratack getSupportAiratack() {
            return this.supportAiratack;
        }

        /**
         * api_support_airatackを設定します。
         * @param supportAiratack api_support_airatack
         */
        public void setSupportAiratack(SupportAiratack supportAiratack) {
            this.supportAiratack = supportAiratack;
        }

        /**
         * api_support_houraiを取得します。
         * @return api_support_hourai
         */
        public SupportHourai getSupportHourai() {
            return this.supportHourai;
        }

        /**
         * api_support_houraiを設定します。
         * @param supportHourai api_support_hourai
         */
        public void setSupportHourai(SupportHourai supportHourai) {
            this.supportHourai = supportHourai;
        }

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
         * api_deck_idを取得します。
         * @return api_deck_id
         */
        public Integer getDeckId() {
            return this.deckId;
        }

        /**
         * api_deck_idを設定します。
         * @param deckId api_deck_id
         */
        public void setDeckId(Integer deckId) {
            this.deckId = deckId;
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
         * api_undressing_flagを取得します。
         * @return api_undressing_flag
         */
        public List<Integer> getUndressingFlag() {
            return this.undressingFlag;
        }

        /**
         * api_undressing_flagを設定します。
         * @param undressingFlag api_undressing_flag
         */
        public void setUndressingFlag(List<Integer> undressingFlag) {
            this.undressingFlag = undressingFlag;
        }

        /**
         * api_stage_flagを取得します。
         * @return api_stage_flag
         */
        public List<Integer> getStageFlag() {
            return this.stageFlag;
        }

        /**
         * api_stage_flagを設定します。
         * @param stageFlag api_stage_flag
         */
        public void setStageFlag(List<Integer> stageFlag) {
            this.stageFlag = stageFlag;
        }

        /**
         * api_plane_fromを取得します。
         * @return api_plane_from
         */
        public List<List<Integer>> getPlaneFrom() {
            return this.planeFrom;
        }

        /**
         * api_plane_fromを設定します。
         * @param planeFrom api_plane_from
         */
        public void setPlaneFrom(List<List<Integer>> planeFrom) {
            this.planeFrom = planeFrom;
        }

        /**
         * api_stage1を取得します。
         * @return api_stage1
         */
        public Stage1 getStage1() {
            return this.stage1;
        }

        /**
         * api_stage1を設定します。
         * @param stage1 api_stage1
         */
        public void setStage1(Stage1 stage1) {
            this.stage1 = stage1;
        }

        /**
         * api_stage2を取得します。
         * @return api_stage2
         */
        public Stage2 getStage2() {
            return this.stage2;
        }

        /**
         * api_stage2を設定します。
         * @param stage2 api_stage2
         */
        public void setStage2(Stage2 stage2) {
            this.stage2 = stage2;
        }

        /**
         * api_stage3を取得します。
         * @return api_stage3
         */
        public Stage3 getStage3() {
            return this.stage3;
        }

        /**
         * api_stage3を設定します。
         * @param stage3 api_stage3
         */
        public void setStage3(Stage3 stage3) {
            this.stage3 = stage3;
        }

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
         * api_deck_idを取得します。
         * @return api_deck_id
         */
        public Integer getDeckId() {
            return this.deckId;
        }

        /**
         * api_deck_idを設定します。
         * @param deckId api_deck_id
         */
        public void setDeckId(Integer deckId) {
            this.deckId = deckId;
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
         * api_undressing_flagを取得します。
         * @return api_undressing_flag
         */
        public List<Integer> getUndressingFlag() {
            return this.undressingFlag;
        }

        /**
         * api_undressing_flagを設定します。
         * @param undressingFlag api_undressing_flag
         */
        public void setUndressingFlag(List<Integer> undressingFlag) {
            this.undressingFlag = undressingFlag;
        }

        /**
         * api_cl_listを取得します。
         * @return api_cl_list
         */
        public List<Integer> getClList() {
            return this.clList;
        }

        /**
         * api_cl_listを設定します。
         * @param clList api_cl_list
         */
        public void setClList(List<Integer> clList) {
            this.clList = clList;
        }

        /**
         * api_damageを取得します。
         * @return api_damage
         */
        public List<Double> getDamage() {
            return this.damage;
        }

        /**
         * api_damageを設定します。
         * @param damage api_damage
         */
        public void setDamage(List<Double> damage) {
            this.damage = damage;
        }

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
         * api_fraiを取得します。
         * @return api_frai
         */
        public List<Integer> getFrai() {
            return this.frai;
        }

        /**
         * api_fraiを設定します。
         * @param frai api_frai
         */
        public void setFrai(List<Integer> frai) {
            this.frai = frai;
        }

        /**
         * api_eraiを取得します。
         * @return api_erai
         */
        public List<Integer> getErai() {
            return this.erai;
        }

        /**
         * api_eraiを設定します。
         * @param erai api_erai
         */
        public void setErai(List<Integer> erai) {
            this.erai = erai;
        }

        /**
         * api_fdamを取得します。
         * @return api_fdam
         */
        public List<Double> getFdam() {
            return this.fdam;
        }

        /**
         * api_fdamを設定します。
         * @param fdam api_fdam
         */
        public void setFdam(List<Double> fdam) {
            this.fdam = fdam;
        }

        /**
         * api_edamを取得します。
         * @return api_edam
         */
        public List<Double> getEdam() {
            return this.edam;
        }

        /**
         * api_edamを設定します。
         * @param edam api_edam
         */
        public void setEdam(List<Double> edam) {
            this.edam = edam;
        }

        /**
         * api_fydamを取得します。
         * @return api_fydam
         */
        public List<Double> getFydam() {
            return this.fydam;
        }

        /**
         * api_fydamを設定します。
         * @param fydam api_fydam
         */
        public void setFydam(List<Double> fydam) {
            this.fydam = fydam;
        }

        /**
         * api_eydamを取得します。
         * @return api_eydam
         */
        public List<Double> getEydam() {
            return this.eydam;
        }

        /**
         * api_eydamを設定します。
         * @param eydam api_eydam
         */
        public void setEydam(List<Double> eydam) {
            this.eydam = eydam;
        }

        /**
         * api_fclを取得します。
         * @return api_fcl
         */
        public List<Integer> getFcl() {
            return this.fcl;
        }

        /**
         * api_fclを設定します。
         * @param fcl api_fcl
         */
        public void setFcl(List<Integer> fcl) {
            this.fcl = fcl;
        }

        /**
         * api_eclを取得します。
         * @return api_ecl
         */
        public List<Integer> getEcl() {
            return this.ecl;
        }

        /**
         * api_eclを設定します。
         * @param ecl api_ecl
         */
        public void setEcl(List<Integer> ecl) {
            this.ecl = ecl;
        }

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

        /**
         * api_at_listを取得します。
         * @return api_at_list
         */
        @Override
        public List<Integer> getAtList() {
            return this.atList;
        }

        /**
         * api_at_listを設定します。
         * @param atList api_at_list
         */
        public void setAtList(List<Integer> atList) {
            this.atList = atList;
        }

        /**
         * api_at_typeを取得します。
         * @return api_at_type
         */
        public List<Integer> getAtType() {
            return this.atType;
        }

        /**
         * api_at_typeを設定します。
         * @param atType api_at_type
         */
        public void setAtType(List<Integer> atType) {
            this.atType = atType;
        }

        /**
         * api_df_listを取得します。
         * @return api_df_list
         */
        @Override
        public List<List<Integer>> getDfList() {
            return this.dfList;
        }

        /**
         * api_df_listを設定します。
         * @param dfList api_df_list
         */
        public void setDfList(List<List<Integer>> dfList) {
            this.dfList = dfList;
        }

        /**
         * api_si_listを取得します。
         * @return api_si_list
         */
        @Override
        public List<List<Integer>> getSiList() {
            return this.siList;
        }

        /**
         * api_si_listを設定します。
         * @param siList api_si_list
         */
        public void setSiList(List<List<Integer>> siList) {
            this.siList = siList;
        }

        /**
         * api_cl_listを取得します。
         * @return api_cl_list
         */
        @Override
        public List<List<Integer>> getClList() {
            return this.clList;
        }

        /**
         * api_cl_listを設定します。
         * @param clList api_cl_list
         */
        public void setClList(List<List<Integer>> clList) {
            this.clList = clList;
        }

        /**
         * api_damageを取得します。
         * @return api_damage
         */
        @Override
        public List<List<Double>> getDamage() {
            return this.damage;
        }

        /**
         * api_damageを設定します。
         * @param damage api_damage
         */
        public void setDamage(List<List<Double>> damage) {
            this.damage = damage;
        }

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
                    .set("api_damage", bean::setDamage, JsonHelper.toList(JsonHelper::checkedToDoubleList));
            return bean;
        }
    }

    /**
     * 砲撃
     */
    public static class MidnightHougeki implements IHougeki, Serializable {

        private static final long serialVersionUID = 8897484398602178656L;

        /** api_at_list */
        private List<Integer> atList;

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

        /**
         * api_at_listを取得します。
         * @return api_at_list
         */
        @Override
        public List<Integer> getAtList() {
            return this.atList;
        }

        /**
         * api_at_listを設定します。
         * @param atList api_at_list
         */
        public void setAtList(List<Integer> atList) {
            this.atList = atList;
        }

        /**
         * api_df_listを取得します。
         * @return api_df_list
         */
        @Override
        public List<List<Integer>> getDfList() {
            return this.dfList;
        }

        /**
         * api_df_listを設定します。
         * @param dfList api_df_list
         */
        public void setDfList(List<List<Integer>> dfList) {
            this.dfList = dfList;
        }

        /**
         * api_si_listを取得します。
         * @return api_si_list
         */
        @Override
        public List<List<Integer>> getSiList() {
            return this.siList;
        }

        /**
         * api_si_listを設定します。
         * @param siList api_si_list
         */
        public void setSiList(List<List<Integer>> siList) {
            this.siList = siList;
        }

        /**
         * api_cl_listを取得します。
         * @return api_cl_list
         */
        @Override
        public List<List<Integer>> getClList() {
            return this.clList;
        }

        /**
         * api_cl_listを設定します。
         * @param clList api_cl_list
         */
        public void setClList(List<List<Integer>> clList) {
            this.clList = clList;
        }

        /**
         * api_sp_listを取得します。
         * @return api_sp_list
         */
        public List<Integer> getSpList() {
            return this.spList;
        }

        /**
         * api_sp_listを設定します。
         * @param spList api_sp_list
         */
        public void setSpList(List<Integer> spList) {
            this.spList = spList;
        }

        /**
         * api_damageを取得します。
         * @return api_damage
         */
        @Override
        public List<List<Double>> getDamage() {
            return this.damage;
        }

        /**
         * api_damageを設定します。
         * @param damage api_damage
         */
        public void setDamage(List<List<Double>> damage) {
            this.damage = damage;
        }

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
                    .set("api_df_list", bean::setDfList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_si_list", bean::setSiList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_cl_list", bean::setClList, JsonHelper.toList(JsonHelper::checkedToIntegerList))
                    .set("api_sp_list", bean::setSpList, JsonHelper::toIntegerList)
                    .set("api_damage", bean::setDamage, JsonHelper.toList(JsonHelper::checkedToDoubleList));
            return bean;
        }
    }

    /**
     * 基地航空戦
     */
    public static class AirBaseAttack implements Serializable {

        private static final long serialVersionUID = -7255540756859970824L;

        /** api_base_id */
        private Integer baseId;

        /** api_plane_from */
        private List<List<Integer>> planeFrom;

        /** api_squadron_plane */
        private List<SquadronPlane> squadronPlane;

        /** api_stage1 */
        private Stage1 stage1;

        /** api_stage2 */
        private Stage2 stage2;

        /** api_stage3 */
        private Stage3 stage3;

        /** api_stage_flag */
        private List<Integer> stageFlag;

        /**
         * api_base_idを取得します。
         * @return api_base_id
         */
        public Integer getBaseId() {
            return this.baseId;
        }

        /**
         * api_base_idを設定します。
         * @param baseId api_base_id
         */
        public void setBaseId(Integer baseId) {
            this.baseId = baseId;
        }

        /**
         * api_plane_fromを取得します。
         * @return api_plane_from
         */
        public List<List<Integer>> getPlaneFrom() {
            return this.planeFrom;
        }

        /**
         * api_plane_fromを設定します。
         * @param planeFrom api_plane_from
         */
        public void setPlaneFrom(List<List<Integer>> planeFrom) {
            this.planeFrom = planeFrom;
        }

        /**
         * api_squadron_planeを取得します。
         * @return api_squadron_plane
         */
        public List<SquadronPlane> getSquadronPlane() {
            return this.squadronPlane;
        }

        /**
         * api_squadron_planeを設定します。
         * @param squadronPlane api_squadron_plane
         */
        public void setSquadronPlane(List<SquadronPlane> squadronPlane) {
            this.squadronPlane = squadronPlane;
        }

        /**
         * api_stage1を取得します。
         * @return api_stage1
         */
        public Stage1 getStage1() {
            return this.stage1;
        }

        /**
         * api_stage1を設定します。
         * @param stage1 api_stage1
         */
        public void setStage1(Stage1 stage1) {
            this.stage1 = stage1;
        }

        /**
         * api_stage2を取得します。
         * @return api_stage2
         */
        public Stage2 getStage2() {
            return this.stage2;
        }

        /**
         * api_stage2を設定します。
         * @param stage2 api_stage2
         */
        public void setStage2(Stage2 stage2) {
            this.stage2 = stage2;
        }

        /**
         * api_stage3を取得します。
         * @return api_stage3
         */
        public Stage3 getStage3() {
            return this.stage3;
        }

        /**
         * api_stage3を設定します。
         * @param stage3 api_stage3
         */
        public void setStage3(Stage3 stage3) {
            this.stage3 = stage3;
        }

        /**
         * api_stage_flagを取得します。
         * @return api_stage_flag
         */
        public List<Integer> getStageFlag() {
            return this.stageFlag;
        }

        /**
         * api_stage_flagを設定します。
         * @param stageFlag api_stage_flag
         */
        public void setStageFlag(List<Integer> stageFlag) {
            this.stageFlag = stageFlag;
        }

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
                    .set("api_stage1", bean::setStage1, Stage1::toStage1)
                    .set("api_stage2", bean::setStage2, Stage2::toStage2)
                    .set("api_stage3", bean::setStage3, Stage3::toStage3)
                    .set("api_stage_flag", bean::setStageFlag, JsonHelper::toIntegerList);
            return bean;
        }
    }

    /**
     * api_squadron_plane
     */
    public static class SquadronPlane implements Serializable {

        private static final long serialVersionUID = -8553957904692166481L;

        /** api_count */
        private Integer count;

        /** api_mst_id */
        private Integer mstId;

        /**
         * api_countを取得します。
         * @return api_count
         */
        public Integer getCount() {
            return this.count;
        }

        /**
         * api_countを設定します。
         * @param count api_count
         */
        public void setCount(Integer count) {
            this.count = count;
        }

        /**
         * api_mst_idを取得します。
         * @return api_mst_id
         */
        public Integer getMstId() {
            return this.mstId;
        }

        /**
         * api_mst_idを設定します。
         * @param mstId api_mst_id
         */
        public void setMstId(Integer mstId) {
            this.mstId = mstId;
        }

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
        同航戦,
        反航戦,
        Ｔ字戦有利,
        Ｔ字戦不利;

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
        制空均衡,
        制空権確保,
        航空優勢,
        航空劣勢,
        制空権喪失;

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
        未結成,
        機動部隊,
        水上部隊,
        輸送部隊;

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
}
