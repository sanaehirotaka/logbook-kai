package logbook.api;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import logbook.bean.AppConfig;
import logbook.bean.Maparea;
import logbook.bean.MapareaCollection;
import logbook.bean.MapinfoMst;
import logbook.bean.MapinfoMstCollection;
import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.ShipMst;
import logbook.bean.ShipMstCollection;
import logbook.bean.Shipgraph;
import logbook.bean.ShipgraphCollection;
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
import logbook.internal.LoggerHolder;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_start2
 *
 */
@API("/kcsapi/api_start2/getData")
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
            this.apiMstMaparea(data.getJsonArray("api_mst_maparea"));
            this.apiMstMapinfo(data.getJsonArray("api_mst_mapinfo"));
            this.store(data);
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
        ShipgraphCollection.get()
                .setShipgraphMap(JsonHelper.toMap(array, Shipgraph::getId, Shipgraph::toShipgraph));
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

    /**
     * api_data.api_mst_maparea
     *
     * @param array api_mst_maparea
     */
    private void apiMstMaparea(JsonArray array) {
        MapareaCollection.get()
                .setMaparea(JsonHelper.toMap(array, Maparea::getId, Maparea::toMaparea));
    }

    /**
     * api_data.api_mst_mapinfo
     *
     * @param array api_mst_mapinfo
     */
    private void apiMstMapinfo(JsonArray array) {
        MapinfoMstCollection.get()
                .setMapinfo(JsonHelper.toMap(array, MapinfoMst::getId, MapinfoMst::toMapinfoMst));
    }

    /**
     * store
     * 
     * @param obj api_data
     */
    private void store(JsonObject root) {
        if (AppConfig.get().isStoreApiStart2()) {
            try {
                String dir = AppConfig.get().getStoreApiStart2Dir();
                if (dir == null || "".equals(dir))
                    return;

                Path dirPath = Paths.get(dir);
                Path parent = dirPath.getParent();
                if (parent != null && !Files.exists(parent)) {
                    Files.createDirectories(parent);
                }

                JsonWriterFactory factory = Json
                        .createWriterFactory(Collections.singletonMap(JsonGenerator.PRETTY_PRINTING, true));
                for (Entry<String, JsonValue> entry : root.entrySet()) {
                    String key = entry.getKey();
                    JsonValue val = entry.getValue();
                    JsonObject obj = Json.createObjectBuilder().add(key, val).build();

                    Path outPath = dirPath.resolve(key + ".json");

                    try (OutputStream out = Files.newOutputStream(outPath)) {
                        try (JsonWriter writer = factory.createWriter(out)) {
                            writer.write(obj);
                        }
                    }
                }
            } catch (Exception e) {
                LoggerHolder.get().warn("api_start2の保存に失敗しました", e);
            }
        }
    }
}
