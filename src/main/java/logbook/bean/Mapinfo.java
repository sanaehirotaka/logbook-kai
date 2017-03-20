package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.internal.Config;
import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * /kcsapi/api_get_member/mapinfo
 *
 */
@Data
public class Mapinfo implements Serializable {

    private static final long serialVersionUID = -4796721957493046860L;

    /** api_map_info */
    private List<MapInfo> mapInfo = new ArrayList<>();

    /** api_air_base */
    private List<AirBase> airBase = new ArrayList<>();

    /**
     * api_map_info
     */
    @Data
    public static class MapInfo implements Serializable {

        private static final long serialVersionUID = 7388484486155838098L;

        /** api_id */
        private Integer id;

        /** api_cleared */
        private Integer cleared;

        /** api_exboss_flag */
        private Integer exbossFlag;

        /** api_defeat_count */
        private Integer defeatCount;

        /** api_air_base_decks */
        private Integer airBaseDecks;

        /**
         * JsonObjectから{@link MapInfo}を構築します
         *
         * @param json JsonObject
         * @return {@link MapInfo}
         */
        public static MapInfo toMapInfo(JsonValue json) {
            MapInfo bean = new MapInfo();
            JsonHelper.bind((JsonObject) json)
                    .setInteger("api_id", bean::setId)
                    .setInteger("api_cleared", bean::setCleared)
                    .setInteger("api_exboss_flag", bean::setExbossFlag)
                    .setInteger("api_defeat_count", bean::setDefeatCount)
                    .setInteger("api_air_base_decks", bean::setAirBaseDecks);
            return bean;
        }
    }

    /**
     * api_air_base
     */
    @Data
    public static class AirBase implements Serializable {

        private static final long serialVersionUID = 2290504744869571586L;

        /** api_area_id */
        private Integer areaId;

        /** api_rid */
        private Integer rid;

        /** api_name */
        private String name;

        /** api_distance */
        private Integer distance;

        /** api_action_kind */
        private Integer actionKind;

        /** api_plane_info */
        private List<PlaneInfo> planeInfo;

        /**
         * JsonObjectから{@link AirBase}を構築します
         *
         * @param json JsonObject
         * @return {@link AirBase}
         */
        public static AirBase toAirBase(JsonValue json) {
            AirBase bean = new AirBase();
            JsonHelper.bind((JsonObject) json)
                    .setInteger("api_area_id", bean::setAreaId)
                    .setInteger("api_rid", bean::setRid)
                    .setString("api_name", bean::setName)
                    .setInteger("api_distance", bean::setDistance)
                    .setInteger("api_action_kind", bean::setActionKind)
                    .set("api_plane_info", bean::setPlaneInfo, JsonHelper.toList(PlaneInfo::toPlaneInfo));
            return bean;
        }
    }

    /**
     * api_plane_info
     */
    @Data
    public static class PlaneInfo implements Serializable {

        private static final long serialVersionUID = 2943711012497557057L;

        /** api_squadron_id */
        private Integer squadronId;

        /** api_state */
        private Integer state;

        /** api_slotid */
        private Integer slotid;

        /** api_count */
        private Integer count;

        /** api_max_count */
        private Integer maxCount;

        /** api_cond */
        private Integer cond;

        /**
         * JsonObjectから{@link PlaneInfo}を構築します
         *
         * @param json JsonObject
         * @return {@link PlaneInfo}
         */
        public static PlaneInfo toPlaneInfo(JsonValue json) {
            PlaneInfo bean = new PlaneInfo();
            JsonHelper.bind((JsonObject) json)
                    .setInteger("api_squadron_id", bean::setSquadronId)
                    .setInteger("api_state", bean::setState)
                    .setInteger("api_slotid", bean::setSlotid)
                    .setInteger("api_count", bean::setCount)
                    .setInteger("api_max_count", bean::setMaxCount)
                    .setInteger("api_cond", bean::setCond);
            return bean;
        }
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>Mapinfo</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(Mapinfo.class, Mapinfo::new)</code>
     * </blockquote>
     *
     * @return <code>Mapinfo</code>
     */
    public static Mapinfo get() {
        return Config.getDefault().get(Mapinfo.class, Mapinfo::new);
    }
}
