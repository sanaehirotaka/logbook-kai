package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

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
    @Data
    public static class Eventmap implements Serializable {

        private static final long serialVersionUID = -4400495600829555494L;

        /** api_max_maphp */
        private Integer maxMaphp;

        /** api_now_maphp */
        private Integer nowMaphp;

        /** api_dmg */
        private Integer dmg;

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
    @Data
    public static class Enemy implements Serializable {

        private static final long serialVersionUID = 4031791674782777895L;

        /** api_result */
        private Integer result;

        /** api_result_str */
        private String resultStr;

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
    @Data
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
    @Data
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
    @Data
    public static class SelectRoute implements Serializable {

        private static final long serialVersionUID = -9118982578510716000L;

        /** api_select_cells */
        private List<Integer> selectCells;

        /**
         * JsonObjectから{@link SelectRoute}を構築します
         *
         * @param json JsonObject
         * @return {@link SelectRoute}
         */
        public static SelectRoute toSelectRoute(JsonObject json) {
            SelectRoute bean = new SelectRoute();
            JsonHelper.bind(json)
                    .setIntegerList("api_select_cells", bean::setSelectCells);
            return bean;
        }
    }
}
