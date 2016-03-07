package logbook.api;

import java.util.Map;

import javax.json.JsonArray;
import javax.json.JsonObject;

import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.internal.JsonHelper;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_get_member/deck
 *
 */
@API("/kcsapi/api_get_member/deck")
public class ApiGetMemberDeck implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonArray array = json.getJsonArray("api_data");
        if (array != null) {
            Map<Integer, DeckPort> map = DeckPortCollection.get()
                    .getDeckPortMap();
            map.clear();
            map.putAll(JsonHelper.toMap(array, DeckPort::getId, DeckPort::toDeckPort));
        }
    }

}
