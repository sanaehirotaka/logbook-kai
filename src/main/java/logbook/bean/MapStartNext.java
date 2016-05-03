package logbook.bean;

import java.io.Serializable;
import java.util.List;

import javax.json.JsonObject;

import logbook.internal.JsonHelper;

/**
 * 出撃/進撃
 *
 */
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
     * api_rashin_flgを取得します。
     * @return api_rashin_flg
     */
    public Integer getRashinFlg() {
        return this.rashinFlg;
    }

    /**
     * api_rashin_flgを設定します。
     * @param rashinFlg api_rashin_flg
     */
    public void setRashinFlg(Integer rashinFlg) {
        this.rashinFlg = rashinFlg;
    }

    /**
     * api_rashin_idを取得します。
     * @return api_rashin_id
     */
    public Integer getRashinId() {
        return this.rashinId;
    }

    /**
     * api_rashin_idを設定します。
     * @param rashinId api_rashin_id
     */
    public void setRashinId(Integer rashinId) {
        this.rashinId = rashinId;
    }

    /**
     * api_maparea_idを取得します。
     * @return api_maparea_id
     */
    public Integer getMapareaId() {
        return this.mapareaId;
    }

    /**
     * api_maparea_idを設定します。
     * @param mapareaId api_maparea_id
     */
    public void setMapareaId(Integer mapareaId) {
        this.mapareaId = mapareaId;
    }

    /**
     * api_mapinfo_noを取得します。
     * @return api_mapinfo_no
     */
    public Integer getMapinfoNo() {
        return this.mapinfoNo;
    }

    /**
     * api_mapinfo_noを設定します。
     * @param mapinfoNo api_mapinfo_no
     */
    public void setMapinfoNo(Integer mapinfoNo) {
        this.mapinfoNo = mapinfoNo;
    }

    /**
     * api_noを取得します。
     * @return api_no
     */
    public Integer getNo() {
        return this.no;
    }

    /**
     * api_noを設定します。
     * @param no api_no
     */
    public void setNo(Integer no) {
        this.no = no;
    }

    /**
     * api_color_noを取得します。
     * @return api_color_no
     */
    public Integer getColorNo() {
        return this.colorNo;
    }

    /**
     * api_color_noを設定します。
     * @param colorNo api_color_no
     */
    public void setColorNo(Integer colorNo) {
        this.colorNo = colorNo;
    }

    /**
     * api_event_idを取得します。
     * @return api_event_id
     */
    public Integer getEventId() {
        return this.eventId;
    }

    /**
     * api_event_idを設定します。
     * @param eventId api_event_id
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * api_event_kindを取得します。
     * @return api_event_kind
     */
    public Integer getEventKind() {
        return this.eventKind;
    }

    /**
     * api_event_kindを設定します。
     * @param eventKind api_event_kind
     */
    public void setEventKind(Integer eventKind) {
        this.eventKind = eventKind;
    }

    /**
     * api_nextを取得します。
     * @return api_next
     */
    public Integer getNext() {
        return this.next;
    }

    /**
     * api_nextを設定します。
     * @param next api_next
     */
    public void setNext(Integer next) {
        this.next = next;
    }

    /**
     * api_bosscell_noを取得します。
     * @return api_bosscell_no
     */
    public Integer getBosscellNo() {
        return this.bosscellNo;
    }

    /**
     * api_bosscell_noを設定します。
     * @param bosscellNo api_bosscell_no
     */
    public void setBosscellNo(Integer bosscellNo) {
        this.bosscellNo = bosscellNo;
    }

    /**
     * api_bosscompを取得します。
     * @return api_bosscomp
     */
    public Integer getBosscomp() {
        return this.bosscomp;
    }

    /**
     * api_bosscompを設定します。
     * @param bosscomp api_bosscomp
     */
    public void setBosscomp(Integer bosscomp) {
        this.bosscomp = bosscomp;
    }

    /**
     * api_eventmapを取得します。
     * @return api_eventmap
     */
    public MapTypes.Eventmap getEventmap() {
        return this.eventmap;
    }

    /**
     * api_eventmapを設定します。
     * @param eventmap api_eventmap
     */
    public void setEventmap(MapTypes.Eventmap eventmap) {
        this.eventmap = eventmap;
    }

    /**
     * api_comment_kindを取得します。
     * @return api_comment_kind
     */
    public Integer getCommentKind() {
        return this.commentKind;
    }

    /**
     * api_comment_kindを設定します。
     * @param commentKind api_comment_kind
     */
    public void setCommentKind(Integer commentKind) {
        this.commentKind = commentKind;
    }

    /**
     * api_production_kindを取得します。
     * @return api_production_kind
     */
    public Integer getProductionKind() {
        return this.productionKind;
    }

    /**
     * api_production_kindを設定します。
     * @param productionKind api_production_kind
     */
    public void setProductionKind(Integer productionKind) {
        this.productionKind = productionKind;
    }

    /**
     * api_enemyを取得します。
     * @return api_enemy
     */
    public MapTypes.Enemy getEnemy() {
        return this.enemy;
    }

    /**
     * api_enemyを設定します。
     * @param enemy api_enemy
     */
    public void setEnemy(MapTypes.Enemy enemy) {
        this.enemy = enemy;
    }

    /**
     * api_happeningを取得します。
     * @return api_happening
     */
    public MapTypes.Happening getHappening() {
        return this.happening;
    }

    /**
     * api_happeningを設定します。
     * @param happening api_happening
     */
    public void setHappening(MapTypes.Happening happening) {
        this.happening = happening;
    }

    /**
     * api_itemgetを取得します。
     * @return api_itemget
     */
    public List<MapTypes.Itemget> getItemget() {
        return this.itemget;
    }

    /**
     * api_itemgetを設定します。
     * @param itemget api_itemget
     */
    public void setItemget(List<MapTypes.Itemget> itemget) {
        this.itemget = itemget;
    }

    /**
     * api_select_routeを取得します。
     * @return api_select_route
     */
    public MapTypes.SelectRoute getSelectRoute() {
        return this.selectRoute;
    }

    /**
     * api_select_routeを設定します。
     * @param selectRoute api_select_route
     */
    public void setSelectRoute(MapTypes.SelectRoute selectRoute) {
        this.selectRoute = selectRoute;
    }

    /**
     * api_from_noを取得します。
     * @return api_from_no
     */
    public Integer getFromNo() {
        return this.fromNo;
    }

    /**
     * api_from_noを設定します。
     * @param fromNo api_from_no
     */
    public void setFromNo(Integer fromNo) {
        this.fromNo = fromNo;
    }

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
