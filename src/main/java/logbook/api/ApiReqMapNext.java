package logbook.api;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.json.JsonObject;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import logbook.Messages;
import logbook.bean.AppBouyomiConfig;
import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.DeckPortCollection;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.internal.Audios;
import logbook.internal.BouyomiChanUtils;
import logbook.internal.BouyomiChanUtils.Type;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;
import logbook.internal.Tuple;
import logbook.internal.gui.Tools;
import logbook.proxy.RequestMetaData;
import logbook.proxy.ResponseMetaData;

/**
 * /kcsapi/api_req_map/next
 *
 */
@API("/kcsapi/api_req_map/next")
public class ApiReqMapNext implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {

        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            MapStartNext next = MapStartNext.toMapStartNext(data);

            AppCondition condition = AppCondition.get();
            BattleLog log = condition.getBattleResult();
            if (log == null) {
                log = new BattleLog();
                condition.setBattleResult(log);
            }
            log.setCombinedType(CombinedType.toCombinedType(condition.getCombinedType()));
            log.getNext().add(next);
            // ルート情報
            condition.getRoute().add(new StringJoiner("-")
                    .add(data.getJsonNumber("api_maparea_id").toString())
                    .add(data.getJsonNumber("api_mapinfo_no").toString())
                    .add(data.getJsonNumber("api_no").toString())
                    .toString());

            if (AppConfig.get().isAlertBadlyNext() || AppBouyomiConfig.get().isEnable()) {
                // 大破した艦娘
                List<Ship> badlyShips = DeckPortCollection.get()
                        .getDeckPortMap()
                        .get(condition.getDeckId())
                        .getBadlyShips();

                // 連合艦隊時は第2艦隊も見る
                if (condition.isCombinedFlag()) {
                    badlyShips.addAll(DeckPortCollection.get()
                            .getDeckPortMap()
                            .get(2).getBadlyShips(AppConfig.get().isIgnoreSecondFlagship()));
                }

                if (!badlyShips.isEmpty()) {
                    Platform.runLater(() -> displayAlert(badlyShips));
                    // 棒読みちゃん連携
                    sendBouyomi(badlyShips);
                }
            }
            if (next.achievementGimmick1()) {
                Platform.runLater(
                        () -> Tools.Conrtols.showNotify(null, "ギミック解除", "海域に変化が確認されました。", Duration.seconds(15)));
                // 通知音再生
                if (AppConfig.get().isUseSound()) {
                    Platform.runLater(Audios.playDefaultNotifySound());
                }
                // 棒読みちゃん連携
                if (AppBouyomiConfig.get().isEnable()) {
                    BouyomiChanUtils.speak(Type.AchievementGimmick1);
                }
            }
            if (next.achievementGimmick2()) {
                Platform.runLater(
                        () -> Tools.Conrtols.showNotify(null, "ギミック解除", "ギミックの達成を確認しました。", Duration.seconds(15)));
                // 通知音再生
                if (AppConfig.get().isUseSound()) {
                    Platform.runLater(Audios.playDefaultNotifySound());
                }
                // 棒読みちゃん連携
                if (AppBouyomiConfig.get().isEnable()) {
                    BouyomiChanUtils.speak(Type.AchievementGimmick2);
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

    /**
     * 棒読みちゃん連携
     *
     * @param badlyShips 大破艦
     */
    private static void sendBouyomi(List<Ship> badlyShips) {
        if (AppBouyomiConfig.get().isEnable()) {

            List<ShipMst> shipMsts = badlyShips.stream()
                    .map(ship -> Ships.shipMst(ship).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            String hiragana = shipMsts.stream()
                    .map(ShipMst::getYomi)
                    .collect(Collectors.joining("、"));
            String kanji = shipMsts.stream()
                    .map(ShipMst::getName)
                    .collect(Collectors.joining("、"));

            BouyomiChanUtils.speak(Type.MapStartNextAlert,
                    Tuple.of("${hiraganaNames}", hiragana),
                    Tuple.of("${kanjiNames}", kanji));
        }
    }
}
