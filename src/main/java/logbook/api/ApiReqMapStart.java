package logbook.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.json.JsonObject;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import logbook.Messages;
import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.DeckPortCollection;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.internal.Audios;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;
import logbook.internal.gui.Tools;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_map/start
 *
 */
@API("/kcsapi/api_req_map/start")
public class ApiReqMapStart implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            BattleLog log = new BattleLog();
            log.setCombinedType(CombinedType.toCombinedType(AppCondition.get().getCombinedType()));
            log.getNext().add(MapStartNext.toMapStartNext(data));
            AppCondition.get()
                    .setBattleResult(log);

            AppCondition.get()
                    .setMapStart(Boolean.TRUE);
            AppCondition.get()
                    .setDeckId(Integer.parseInt(req.getParameter("api_deck_id")));

            if (AppConfig.get().isAlertBadlyStart()) {
                // 大破した艦娘
                List<Ship> badlyShips = DeckPortCollection.get()
                        .getDeckPortMap()
                        .get(AppCondition.get().getDeckId())
                        .getBadlyShips();

                // 連合艦隊時は第2艦隊も見る
                if (AppCondition.get().isCombinedFlag()) {
                    badlyShips.addAll(DeckPortCollection.get()
                            .getDeckPortMap()
                            .get(2).getBadlyShips());
                }

                if (!badlyShips.isEmpty()) {
                    Platform.runLater(() -> displayAlert(badlyShips));
                }
            }
        }
    }

    /**
     * 大破警告
     *
     * @param badlyShips 大破艦
     */
    private static void displayAlert(List<Ship> badlyShips) {
        try {
            Path dir = Paths.get(AppConfig.get().getAlertSoundDir());
            Path p = Audios.randomAudioFile(dir);
            if (p != null) {
                AudioClip clip = new AudioClip(p.toUri().toString());
                clip.setVolume(AppConfig.get().getSoundLevel() / 100D);
                clip.play();
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("サウンド通知に失敗しました", e);
        }
        for (Ship ship : badlyShips) {
            ImageView node = new ImageView(Ships.shipWithItemImage(ship));

            String message = Messages.getString("ship.badly", Ships.shipMst(ship) //$NON-NLS-1$
                    .map(ShipMst::getName)
                    .orElse(""), ship.getLv());

            Tools.Conrtols.showNotify(node, "大破警告", message, Duration.seconds(30));
        }
    }

}
