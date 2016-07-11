package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;
import lombok.Data;

/**
 * 出撃/進撃
 *
 */
@Data
public class MapStartNext implements Serializable {

    private static final long serialVersionUID = -4272803839336790705L;

    /** api_rashin_flg */
    private Integer rashinFlg;

    /** api_rashin_id */
    private Integer rashinId;

    /** api_maparea_id */
    private Integer mapareaId;

    /** api_mapinfo_no */
    private Integer mapinfoNo;

    /** api_no */
    private Integer no;

    /** api_color_no */
    private Integer colorNo;

    /** api_event_id */
    private Integer eventId;

    /** api_event_kind */
    private Integer eventKind;

    /** api_next */
    private Integer next;

    /** api_bosscell_no */
    private Integer bosscellNo;

    /** api_bosscomp */
    private Integer bosscomp;

    /** api_eventmap */
    private MapTypes.Eventmap eventmap;

    /** api_comment_kind */
    private Integer commentKind;

    /** api_production_kind */
    private Integer productionKind;

    /** api_enemy */
    private MapTypes.Enemy enemy;

    /** api_happening */
    private MapTypes.Happening happening;

    /** api_itemget */
    private List<MapTypes.Itemget> itemget;

    /** api_select_route */
    private MapTypes.SelectRoute selectRoute;

    /** api_from_no */
    private Integer fromNo;

    /**
     * JsonObjectから{@link MapStartNext}を構築します
     *
     * @param json JsonObject
     * @return {@link MapStartNext}
     */
    public static MapStartNext toMapStartNext(JsonObject json) {
        MapStartNext bean = new MapStartNext();
        JsonHelper.bind(json)
                .setInteger("api_rashin_flg", bean::setRashinFlg)
                .setInteger("api_rashin_id", bean::setRashinId)
                .setInteger("api_maparea_id", bean::setMapareaId)
                .setInteger("api_mapinfo_no", bean::setMapinfoNo)
                .setInteger("api_no", bean::setNo)
                .setInteger("api_color_no", bean::setColorNo)
                .setInteger("api_event_id", bean::setEventId)
                .setInteger("api_event_kind", bean::setEventKind)
                .setInteger("api_next", bean::setNext)
                .setInteger("api_bosscell_no", bean::setBosscellNo)
                .setInteger("api_bosscomp", bean::setBosscomp)
                .set("api_eventmap", bean::setEventmap, MapTypes.Eventmap::toEventmap)
                .setInteger("api_comment_kind", bean::setCommentKind)
                .setInteger("api_production_kind", bean::setProductionKind)
                .set("api_enemy", bean::setEnemy, MapTypes.Enemy::toEnemy)
                .set("api_happening", bean::setHappening, MapTypes.Happening::toHappening)
                .set("api_itemget", bean::setItemget, JsonHelper.toList(MapTypes.Itemget::toItemget))
                .set("api_select_route", bean::setSelectRoute, MapTypes.SelectRoute::toSelectRoute)
                .setInteger("api_from_no", bean::setFromNo);
        return bean;
    }
}
