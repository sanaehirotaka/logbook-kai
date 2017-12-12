package logbook.api;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
 * /kcsapi/api_get_member/ship3
 *
 */
@API("/kcsapi/api_get_member/ship3")
public class ApiGetMemberShip3 implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiShipData(data.getJsonArray("api_ship_data"), req);
            this.apiDeckData(data.getJsonArray("api_deck_data"));
        }
    }

    /**
     * api_data.api_ship_data
     *
     * @param array api_ship_data
     * @param req リクエスト
     */
    private void apiShipData(JsonArray array, RequestMetaData req) {
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        if (!req.getParameterMap()
                .containsKey("api_shipid")) {
            // 艦娘の指定がない場合クリア
            map.clear();
        }
        map.putAll(JsonHelper.toMap(array, Ship::getId, Ship::toShip));
    }

    /**
     * api_data.api_deck_data
     *
     * @param array api_deck_data
     */
    private void apiDeckData(JsonArray array) {
        Map<Integer, DeckPort> deckMap = JsonHelper.toMap(array, DeckPort::getId, DeckPort::toDeckPort);
        DeckPortCollection.get()
                .setDeckPortMap(deckMap);
        DeckPortCollection.get()
                .setMissionShips(deckMap.values()
                        .stream()
                        .filter(d -> d.getMission().get(0) != 0)
                        .map(DeckPort::getShip)
                        .flatMap(List::stream)
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
    }
}
