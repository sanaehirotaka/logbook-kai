package logbook.api;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.json.JsonArray;
import javax.json.JsonObject;

import javafx.application.Platform;
import logbook.bean.AppBouyomiConfig;
import logbook.bean.AppCondition;
import logbook.bean.AppConfig;
import logbook.bean.Basic;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Material;
import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.Audios;
import logbook.internal.BouyomiChanUtils;
import logbook.internal.BouyomiChanUtils.Type;
import logbook.internal.JsonHelper;
import logbook.internal.gui.Tools;
import logbook.internal.log.LogWriter;
import logbook.internal.log.MaterialLogFormat;
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
            this.condition();
            this.akashiTimer();
            this.detectGimmick(data);
        }
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
        // 差し替え前
        Map<Integer, Ship> before = ShipCollection.get()
                .getShipMap();
        // 差し替え前のID
        Set<Integer> beforeShipIds = before.keySet();
        // 差し替え後
        Map<Integer, Ship> afterShipMap = JsonHelper.toMap(array, Ship::getId, Ship::toShip);
        // 差し替え後のID
        Set<Integer> afterShipIds = afterShipMap.keySet();

        // cond値が更新されたかを検出
        Predicate<Ship> update = beforeShip -> {
            Ship afterShip = afterShipMap.get(beforeShip.getId());
            if (afterShip != null) {
                return !beforeShip.getCond().equals(afterShip.getCond());
            }
            return false;
        };
        if (before.values().stream()
                .filter(ship -> ship.getCond() <= 49)
                .anyMatch(update)) {
            ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());
            AppCondition.get().setCondUpdateTime(time.toEpochSecond());
        }

        // 差し替え前に存在して、差し替え後に存在しない艦娘の装備を廃棄する
        beforeShipIds.stream()
                .filter(((Predicate<Integer>) afterShipIds::contains).negate())
                .map(before::get)
                .forEach(this::destryItem);

        ShipCollection.get()
                .setShipMap(afterShipMap);
    }

    /**
     * api_data.api_deck_port
     *
     * @param array api_deck_port
     */
    private void apiDeckPort(JsonArray array) {
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

    /**
     * api_data.api_ndock
     *
     * @param array api_ndock
     */
    private void apiNdock(JsonArray array) {
        // 入渠
        Map<Integer, Ndock> map = JsonHelper.toMap(array, Ndock::getId, Ndock::toNdock);
        NdockCollection.get()
                .setNdockMap(map);
        // 入渠中の艦娘
        NdockCollection.get()
                .setNdockSet(map.entrySet()
                        .stream()
                        .map(Map.Entry::getValue)
                        .map(Ndock::getShipId)
                        .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    /**
     * api_data.api_material
     *
     * @param array api_material
     */
    private void apiMaterial(JsonArray array) {
        Duration duration = Duration.ofMillis(System.currentTimeMillis() - AppCondition.get()
                .getWroteMaterialLogLast());
        if (duration.compareTo(Duration.ofSeconds(AppConfig.get().getMaterialLogInterval())) >= 0) {
            Map<Integer, Material> material = JsonHelper.toMap(array, Material::getId, Material::toMaterial);
            LogWriter.getInstance(MaterialLogFormat::new)
                    .write(material);
            AppCondition.get()
                    .setWroteMaterialLogLast(System.currentTimeMillis());
        }
    }

    /**
     * api_data.api_combined_flag
     *
     * @param object api_data
     */
    private void apiCombinedFlag(JsonObject object) {
        Integer combinedType;
        Boolean combinedFlag;
        if (object.containsKey("api_combined_flag")) {
            combinedType = JsonHelper.toInteger(object.getJsonNumber("api_combined_flag"));
        } else {
            combinedType = 0;
        }
        combinedFlag = combinedType > 0;
        AppCondition.get().setCombinedType(combinedType);
        AppCondition.get().setCombinedFlag(combinedFlag);
    }

    /**
     * 艦娘の装備を廃棄する
     *
     * @param ship 艦娘
     */
    private void destryItem(Ship ship) {
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();
        // 持っている装備を廃棄する
        for (Integer itemId : ship.getSlot()) {
            itemMap.remove(itemId);
        }
        // 補強増設
        itemMap.remove(ship.getSlotEx());
    }

    /**
     * 出撃などの状態を更新する
     */
    private void condition() {
        AppCondition condition = AppCondition.get();
        // 出撃中ではない
        condition.setMapStart(false);
        // 退避を削除
        condition.getEscape().clear();
        // 戦闘結果を削除
        condition.setBattleResultConfirm(null);
        // 戦闘回数リセット
        condition.setBattleCount(0);
        // ルート削除
        condition.setRoute(new ArrayList<>());
    }

    /**
     * 泊地修理タイマーを更新する
     */
    private void akashiTimer() {
        long timer = AppCondition.get().getAkashiTimer();
        if (System.currentTimeMillis() - timer >= Duration.ofMinutes(20).toMillis()) {
            AppCondition.get().setAkashiTimer(System.currentTimeMillis());
        }
    }

    /**
     * api_data.api_event_object
     * 
     * @param object api_data
     */
    private void detectGimmick(JsonObject object) {
        if (object.containsKey("api_event_object")) {
            JsonObject eventObject = object.getJsonObject("api_event_object");
            if (eventObject.containsKey("api_m_flag2")) {
                if (JsonHelper.toInteger(eventObject.get("api_m_flag2")) > 0) {
                    Platform.runLater(
                            () -> Tools.Conrtols.showNotify(null, "ギミック解除", "ギミックの達成を確認しました。",
                                    javafx.util.Duration.seconds(15)));
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
    }
}
