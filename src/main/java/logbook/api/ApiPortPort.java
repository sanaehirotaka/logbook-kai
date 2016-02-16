package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.Basic;
import logbook.bean.CombinedFlag;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Material;
import logbook.bean.MaterialCollection;
import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.Config;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_port/port
 *
 */
@API("/kcsapi/api_port/port")
public class ApiPortPort implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiBasic(data.getJsonObject("api_basic"));
            this.apiShip(data.getJsonArray("api_ship"));
            this.apiDeckPort(data.getJsonArray("api_deck_port"));
            this.apiNdock(data.getJsonArray("api_ndock"));
            this.apiMaterial(data.getJsonArray("api_material"));
            this.apiCombinedFlag(data);
        }
        Config.getDefault().store();
    }

    /**
     * api_data.api_basic
     *
     * @param object api_basic
     */
    private void apiBasic(JsonObject object) {
        Basic.updateBasic(Basic.get(), object);
    }

    /**
     * api_data.api_ship
     *
     * @param array api_ship
     */
    private void apiShip(JsonArray array) {
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Ship::getId, Ship::toShip));
    }

    /**
     * api_data.api_deck_port
     *
     * @param array api_deck_port
     */
    private void apiDeckPort(JsonArray array) {
        List<DeckPort> list = DeckPortCollection.get()
                .getDeckPorts();
        list.clear();
        list.addAll(JsonHelper.toList(array, DeckPort::toDeckPort));
    }

    /**
     * api_data.api_ndock
     *
     * @param array api_ndock
     */
    private void apiNdock(JsonArray array) {
        Map<Integer, Ndock> map = NdockCollection.get()
                .getNdockMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Ndock::getId, Ndock::toNdock));
    }

    /**
     * api_data.api_material
     *
     * @param array api_material
     */
    private void apiMaterial(JsonArray array) {
        Map<Integer, Material> map = MaterialCollection.get()
                .getMaterialMap();
        map.clear();
        map.putAll(JsonHelper.toMap(array, Material::getId, Material::toMaterial));
    }

    /**
     * api_data.api_combined_flag
     *
     * @param object api_data
     */
    private void apiCombinedFlag(JsonObject object) {
        Boolean combinedFlag;
        if (object.containsKey("api_combined_flag")) {
            combinedFlag = JsonHelper.toBoolean(object.getJsonNumber("api_combined_flag"));
        } else {
            combinedFlag = Boolean.FALSE;
        }
        CombinedFlag.get()
                .setCombinedFlag(combinedFlag);
    }

}
