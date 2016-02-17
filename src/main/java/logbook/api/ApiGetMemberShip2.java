package logbook.api;

import java.util.List;
import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/ship2
 *
 */
@API("/kcsapi/api_get_member/ship2")
public class ApiGetMemberShip2 implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray apiData = json.getJsonArray("api_data");
        if (apiData != null) {
            this.apiShipData(apiData);
        }
        JsonArray apiDataDeck = json.getJsonArray("api_data_deck");
        if (apiDataDeck != null) {
            this.apiDeckData(apiDataDeck);
        }
    }

    /**
     * api_data.api_ship_data
     *
     * @param array api_ship_data
     */
    private void apiShipData(JsonArray array) {
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        map.putAll(JsonHelper.toMap(array, Ship::getId, Ship::toShip));
    }

    /**
     * api_data.api_deck_data
     *
     * @param array api_deck_data
     */
    private void apiDeckData(JsonArray array) {
        List<DeckPort> list = DeckPortCollection.get()
                .getDeckPorts();
        list.clear();
        list.addAll(JsonHelper.toList(array, DeckPort::toDeckPort));
    }
}
