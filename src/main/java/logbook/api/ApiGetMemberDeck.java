package logbook.api;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

}
