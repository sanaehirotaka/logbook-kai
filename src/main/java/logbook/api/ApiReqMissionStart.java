package logbook.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import javafx.application.Platform;
import javafx.util.Duration;
import logbook.bean.AppBouyomiConfig;
import logbook.bean.AppConfig;
import logbook.bean.DeckPortCollection;
import logbook.bean.MissionCollection;
import logbook.bean.MissionCondition;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.Audios;
import logbook.internal.BouyomiChanUtils;
import logbook.internal.BouyomiChanUtils.Type;
import logbook.internal.LoggerHolder;
import logbook.internal.Missions;
import logbook.internal.Tuple;
import logbook.internal.gui.Tools;
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
            Integer deckId = Integer.valueOf(req.getParameter("api_deck_id"));
            Integer missionId = Integer.valueOf(req.getParameter("api_mission_id"));

            try {
                Optional<MissionCondition> condition = Missions.getMissionCondition(missionId);
                if (condition.isPresent()) {
                    Map<Integer, Ship> shipMap = ShipCollection.get()
                            .getShipMap();
                    List<Ship> fleet = DeckPortCollection.get().getDeckPortMap().get(deckId).getShip()
                            .stream()
                            .map(shipMap::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (!condition.get().test(fleet)) {
                        Integer id = missionId;
                        Platform.runLater(() -> displayAlert(deckId, id));
                        // 通知音再生
                        if (AppConfig.get().isUseSound()) {
                            Platform.runLater(Audios.playDefaultNotifySound());
                        }
                        // 棒読みちゃん連携
                        sendBouyomi(deckId, missionId);
                    }
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

    /**
     * 棒読みちゃん連携
     *
     * @param deckId デッキID
     * @param missionId 遠征ID
     */
    private static void sendBouyomi(Integer deckId, Integer missionId) {
        if (AppBouyomiConfig.get().isEnable()) {
            BouyomiChanUtils.speak(Type.MissionStartAlert,
                    Tuple.of("${fleetName}", DeckPortCollection.get().getDeckPortMap().get(deckId).getName()),
                    Tuple.of("${fleetNumber}", String.valueOf(deckId)),
                    Tuple.of("${missionName}", MissionCollection.get().getMissionMap().get(missionId).getName()));
        }
    }
}
