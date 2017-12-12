package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 出撃/進撃で使用される型
 *
 */
public class MapTypes {

    private MapTypes() {
    }

    /**
     * ゲージ
     *
     */
    public static class Eventmap implements Serializable {

        private static final long serialVersionUID = -4400495600829555494L;

        /** api_max_maphp */
        private Integer maxMaphp;

        /** api_now_maphp */
        private Integer nowMaphp;

        /** api_dmg */
        private Integer dmg;

        /**
         * api_max_maphpを取得します。
         * @return api_max_maphp
         */
        public Integer getMaxMaphp() {
            return this.maxMaphp;
        }

        /**
         * api_max_maphpを設定します。
         * @param maxMaphp api_max_maphp
         */
        public void setMaxMaphp(Integer maxMaphp) {
            this.maxMaphp = maxMaphp;
        }

        /**
         * api_now_maphpを取得します。
         * @return api_now_maphp
         */
        public Integer getNowMaphp() {
            return this.nowMaphp;
        }

        /**
         * api_now_maphpを設定します。
         * @param nowMaphp api_now_maphp
         */
        public void setNowMaphp(Integer nowMaphp) {
            this.nowMaphp = nowMaphp;
        }

        /**
         * api_dmgを取得します。
         * @return api_dmg
         */
        public Integer getDmg() {
            return this.dmg;
        }

        /**
         * api_dmgを設定します。
         * @param dmg api_dmg
         */
        public void setDmg(Integer dmg) {
            this.dmg = dmg;
        }

        /**
         * JsonObjectから{@link Eventmap}を構築します
         *
         * @param json JsonObject
         * @return {@link Eventmap}
         */
        public static Eventmap toEventmap(JsonObject json) {
            Eventmap bean = new Eventmap();
            JsonHelper.bind(json)
                    .setInteger("api_max_maphp", bean::setMaxMaphp)
                    .setInteger("api_now_maphp", bean::setNowMaphp)
                    .setInteger("api_dmg", bean::setDmg);
            return bean;
        }
    }

    /**
     * 接敵
     *
     */
    public static class Enemy implements Serializable {

        private static final long serialVersionUID = 4031791674782777895L;

        /** api_result */
        private Integer result;

        /** api_result_str */
        private String resultStr;

        /**
         * api_resultを取得します。
         * @return api_result
         */
        public Integer getResult() {
            return this.result;
        }

        /**
         * api_resultを設定します。
         * @param result api_result
         */
        public void setResult(Integer result) {
            this.result = result;
        }

        /**
         * api_result_strを取得します。
         * @return api_result_str
         */
        public String getResultStr() {
            return this.resultStr;
        }

        /**
         * api_result_strを設定します。
         * @param resultStr api_result_str
         */
        public void setResultStr(String resultStr) {
            this.resultStr = resultStr;
        }

        /**
         * JsonObjectから{@link Enemy}を構築します
         *
         * @param json JsonObject
         * @return {@link Enemy}
         */
        public static Enemy toEnemy(JsonObject json) {
            Enemy bean = new Enemy();
            JsonHelper.bind(json)
                    .setInteger("api_result", bean::setResult)
                    .setString("api_result_str", bean::setResultStr);
            return bean;
        }
    }

    /**
     * うずしお
     *
     */
    public static class Happening implements Serializable {

        private static final long serialVersionUID = 1064277392880384826L;

        /** api_type */
        private Integer type;

        /** api_count */
        private Integer count;

        /** api_usemst */
        private Integer usemst;

        /** api_mst_id */
        private Integer mstId;

        /** api_icon_id */
        private Integer iconId;

        /** api_dentan */
        private Integer dentan;

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
         * api_usemstを取得します。
         * @return api_usemst
         */
        public Integer getUsemst() {
            return this.usemst;
        }

        /**
         * api_usemstを設定します。
         * @param usemst api_usemst
         */
        public void setUsemst(Integer usemst) {
            this.usemst = usemst;
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
         * api_icon_idを取得します。
         * @return api_icon_id
         */
        public Integer getIconId() {
            return this.iconId;
        }

        /**
         * api_icon_idを設定します。
         * @param iconId api_icon_id
         */
        public void setIconId(Integer iconId) {
            this.iconId = iconId;
        }

        /**
         * api_dentanを取得します。
         * @return api_dentan
         */
        public Integer getDentan() {
            return this.dentan;
        }

        /**
         * api_dentanを設定します。
         * @param dentan api_dentan
         */
        public void setDentan(Integer dentan) {
            this.dentan = dentan;
        }

        /**
         * JsonObjectから{@link Happening}を構築します
         *
         * @param json JsonObject
         * @return {@link Happening}
         */
        public static Happening toHappening(JsonObject json) {
            Happening bean = new Happening();
            JsonHelper.bind(json)
                    .setInteger("api_type", bean::setType)
                    .setInteger("api_count", bean::setCount)
                    .setInteger("api_usemst", bean::setUsemst)
                    .setInteger("api_mst_id", bean::setMstId)
                    .setInteger("api_icon_id", bean::setIconId)
                    .setInteger("api_dentan", bean::setDentan);
            return bean;
        }
    }

    /**
     * アイテム
     *
     */
    public static class Itemget implements Serializable {

        private static final long serialVersionUID = -2502611126605062290L;

        /** api_getcount */
        private Integer getcount;

        /** api_icon_id */
        private Integer iconId;

        /** api_id */
        private Integer id;

        /** api_name */
        private String name;

        /** api_usemst */
        private Integer usemst;

        /**
         * api_getcountを取得します。
         * @return api_getcount
         */
        public Integer getGetcount() {
            return this.getcount;
        }

        /**
         * api_getcountを設定します。
         * @param getcount api_getcount
         */
        public void setGetcount(Integer getcount) {
            this.getcount = getcount;
        }

        /**
         * api_icon_idを取得します。
         * @return api_icon_id
         */
        public Integer getIconId() {
            return this.iconId;
        }

        /**
         * api_icon_idを設定します。
         * @param iconId api_icon_id
         */
        public void setIconId(Integer iconId) {
            this.iconId = iconId;
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
         * api_nameを取得します。
         * @return api_name
         */
        public String getName() {
            return this.name;
        }

        /**
         * api_nameを設定します。
         * @param name api_name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * api_usemstを取得します。
         * @return api_usemst
         */
        public Integer getUsemst() {
            return this.usemst;
        }

        /**
         * api_usemstを設定します。
         * @param usemst api_usemst
         */
        public void setUsemst(Integer usemst) {
            this.usemst = usemst;
        }

        /**
         * JsonObjectから{@link Itemget}を構築します
         *
         * @param json JsonObject
         * @return {@link Itemget}
         */
        public static Itemget toItemget(JsonObject json) {
            Itemget bean = new Itemget();
            JsonHelper.bind(json)
                    .setInteger("api_getcount", bean::setGetcount)
                    .setInteger("api_icon_id", bean::setIconId)
                    .setInteger("api_id", bean::setId)
                    .setString("api_name", bean::setName)
                    .setInteger("api_usemst", bean::setUsemst);
            return bean;
        }
    }

    /**
     * 能動分岐
     *
     */
    public static class SelectRoute implements Serializable {

        private static final long serialVersionUID = -9118982578510716000L;

        /** api_select_cells */
        private List<Integer> selectCells;

        /**
         * api_select_cellsを取得します。
         * @return api_select_cells
         */
        public List<Integer> getSelectCells() {
            return this.selectCells;
        }

        /**
         * api_select_cellsを設定します。
         * @param selectCells api_select_cells
         */
        public void setSelectCells(List<Integer> selectCells) {
            this.selectCells = selectCells;
        }

        /**
         * JsonObjectから{@link SelectRoute}を構築します
         *
         * @param json JsonObject
         * @return {@link SelectRoute}
         */
        public static SelectRoute toSelectRoute(JsonObject json) {
            SelectRoute bean = new SelectRoute();
            JsonHelper.bind(json)
                    .set("api_select_cells", bean::setSelectCells, JsonHelper::toIntegerList);
            return bean;
        }
    }
}
