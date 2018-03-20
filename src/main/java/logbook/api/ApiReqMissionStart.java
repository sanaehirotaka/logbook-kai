package logbook.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.util.Duration;
import logbook.bean.DeckPortCollection;
import logbook.bean.MissionCollection;
import logbook.bean.MissionCondition;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.LoggerHolder;
import logbook.internal.gui.Tools;
import logbook.plugin.PluginContainer;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_mission/start
 *
 */
@API("/kcsapi/api_req_mission/start")
public class ApiReqMissionStart implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            Map<String, List<String>> param = req.getParameterMap();
            Integer deckId = Integer.valueOf(param.get("api_deck_id").get(0));
            Integer missionId = Integer.valueOf(param.get("api_mission_id").get(0));

            try {
                ClassLoader classLoader = PluginContainer.getInstance().getClassLoader();
                InputStream is = classLoader.getResourceAsStream("logbook/mission/" + missionId + ".json");
                if (is == null)
                    return;
                MissionCondition condition;
                try {
                    condition = new ObjectMapper().readValue(is, MissionCondition.class);
                } finally {
                    is.close();
                }

                Map<Integer, Ship> shipMap = ShipCollection.get()
                        .getShipMap();
                List<Ship> fleet = DeckPortCollection.get().getDeckPortMap().get(deckId).getShip()
                        .stream()
                        .map(shipMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                if (!condition.test(fleet)) {
                    Platform.runLater(() -> displayAlert(deckId, missionId));
                }
            } catch (Exception e) {
                LoggerHolder.get().error("遠征開始で例外", e);
            }
        }
    }

    /**
     * 遠征警告
     *
     * @param deckId デッキID
     * @param missionId 遠征ID
     */
    private static void displayAlert(Integer deckId, Integer missionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("「")
                .append(DeckPortCollection.get().getDeckPortMap().get(deckId).getName())
                .append("」")
                .append("は")
                .append("「")
                .append(MissionCollection.get().getMissionMap().get(missionId).getName())
                .append("」")
                .append("の条件を満たしていません");

        Tools.Conrtols.showNotify(null, "遠征警告", sb.toString(), Duration.seconds(10));
    }
}
