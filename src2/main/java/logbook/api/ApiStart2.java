package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.SlotitemEquiptype;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.bean.Stype;
import logbook.bean.StypeCollection;
import logbook.bean.Useitem;
import logbook.bean.UseitemCollection;
import logbook.internal.Config;
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
        Config.getDefault().store();
    }

    /**
     * api_data.api_mst_ship
     *
     * @param array api_mst_ship
     */
    private void apiMstShip(JsonArray array) {
        ShipMstCollection.get()
                .setShipMap(JsonHelper.toMap(array, ShipMst::getId, ShipMst::toShip));
    }

    /**
     * api_data.api_mst_shipgraph
     *
     * @param array api_mst_shipgraph
     */
    private void apiMstShipgraph(JsonArray array) {
        Map<Integer, ShipMst> map = ShipMstCollection.get()
                .getShipMap();
        for (JsonValue val : array) {
            JsonObject json = (JsonObject) val;
            Integer key = json.getInt("api_id");
            ShipMst bean = map.get(key);
            if (bean != null) {
                bean.setGraph(json.getString("api_filename"));
            }
        }
    }

    /**
     * api_data.api_mst_slotitem_equiptype
     *
     * @param array api_mst_slotitem_equiptype
     */
    private void apiMstSlotitemEquiptype(JsonArray array) {
        SlotitemEquiptypeCollection.get()
                .setEquiptypeMap(
                        JsonHelper.toMap(array, SlotitemEquiptype::getId, SlotitemEquiptype::toSlotitemEquiptype));
    }

    /**
     * api_data.api_mst_stype
     *
     * @param array api_mst_stype
     */
    private void apiMstStype(JsonArray array) {
        StypeCollection.get()
                .setStypeMap(JsonHelper.toMap(array, Stype::getId, Stype::toStype));
    }

    /**
     * api_data.api_mst_slotitem
     *
     * @param array api_mst_slotitem
     */
    private void apiMstSlotitem(JsonArray array) {
        SlotitemMstCollection.get()
                .setSlotitemMap(JsonHelper.toMap(array, SlotitemMst::getId, SlotitemMst::toSlotitem));
    }

    /**
     * api_data.api_mst_useitem
     *
     * @param array api_mst_useitem
     */
    private void apiMstUseitem(JsonArray array) {
        UseitemCollection.get()
                .setUseitemMap(JsonHelper.toMap(array, Useitem::getId, Useitem::toMission));
    }

    /**
     * api_data.api_mst_mission
     *
     * @param array api_mst_mission
     */
    private void apiMstMission(JsonArray array) {
        MissionCollection.get()
                .setMissionMap(JsonHelper.toMap(array, Mission::getId, Mission::toMission));
    }
}
