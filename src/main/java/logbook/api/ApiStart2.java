package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.Useitem;
import logbook.bean.UseitemCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_start2
 *
 */
@API("/kcsapi/api_start2")
public class ApiStart2 implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiMstShip(data.getJsonArray("api_mst_ship"));
            this.apiMstShipgraph(data.getJsonArray("api_mst_shipgraph"));
            this.apiMstSlotitemEquiptype(data.getJsonArray("api_mst_slotitem_equiptype"));
            this.apiMstStype(data.getJsonArray("api_mst_stype"));
            this.apiMstSlotitem(data.getJsonArray("api_mst_slotitem"));
            this.apiMstUseitem(data.getJsonArray("api_mst_useitem"));
            this.apiMstMission(data.getJsonArray("api_mst_mission"));
        }
    }

    /**
     * api_data.api_mst_ship
     *
     * @param array api_mst_ship
     */
    private void apiMstShip(JsonArray array) {

    }

    /**
     * api_data.api_mst_shipgraph
     *
     * @param array api_mst_shipgraph
     */
    private void apiMstShipgraph(JsonArray array) {

    }

    /**
     * api_data.api_mst_slotitem_equiptype
     *
     * @param array api_mst_slotitem_equiptype
     */
    private void apiMstSlotitemEquiptype(JsonArray array) {

    }

    /**
     * api_data.api_mst_stype
     *
     * @param array api_mst_stype
     */
    private void apiMstStype(JsonArray array) {

    }

    /**
     * api_data.api_mst_slotitem
     *
     * @param array api_mst_slotitem
     */
    private void apiMstSlotitem(JsonArray array) {

    }

    /**
     * api_data.api_mst_useitem
     *
     * @param array api_mst_useitem
     */
    private void apiMstUseitem(JsonArray array) {
        UseitemCollection collection = UseitemCollection.get();
        Map<Integer, Useitem> map = collection.getUseitemMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Useitem::getId, Useitem::toMission));

    }

    /**
     * api_data.api_mst_mission
     *
     * @param array api_mst_mission
     */
    private void apiMstMission(JsonArray array) {
        MissionCollection collection = MissionCollection.get();
        Map<Integer, Mission> map = collection.getMissionMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Mission::getId, Mission::toMission));
    }
}
