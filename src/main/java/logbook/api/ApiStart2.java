package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.Slotitem;
import logbook.bean.SlotitemCollection;
import logbook.bean.SlotitemEquiptype;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.Stype;
import logbook.bean.StypeCollection;
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
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Ship::getId, Ship::toShip));
    }

    /**
     * api_data.api_mst_shipgraph
     *
     * @param array api_mst_shipgraph
     */
    private void apiMstShipgraph(JsonArray array) {
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        for (JsonValue val : array) {
            JsonObject json = (JsonObject) val;
            Integer key = json.getInt("api_id");
            Ship bean = map.get(key);
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
        Map<Integer, SlotitemEquiptype> map = SlotitemEquiptypeCollection.get()
                .getEquiptypeMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, SlotitemEquiptype::getId, SlotitemEquiptype::toSlotitemEquiptype));
    }

    /**
     * api_data.api_mst_stype
     *
     * @param array api_mst_stype
     */
    private void apiMstStype(JsonArray array) {
        Map<Integer, Stype> map = StypeCollection.get()
                .getStypeMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Stype::getId, Stype::toStype));
    }

    /**
     * api_data.api_mst_slotitem
     *
     * @param array api_mst_slotitem
     */
    private void apiMstSlotitem(JsonArray array) {
        Map<Integer, Slotitem> map = SlotitemCollection.get()
                .getSlotitemMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Slotitem::getId, Slotitem::toSlotitem));
    }

    /**
     * api_data.api_mst_useitem
     *
     * @param array api_mst_useitem
     */
    private void apiMstUseitem(JsonArray array) {
        Map<Integer, Useitem> map = UseitemCollection.get()
                .getUseitemMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Useitem::getId, Useitem::toMission));
    }

    /**
     * api_data.api_mst_mission
     *
     * @param array api_mst_mission
     */
    private void apiMstMission(JsonArray array) {
        Map<Integer, Mission> map = MissionCollection.get()
                .getMissionMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Mission::getId, Mission::toMission));
    }
}
