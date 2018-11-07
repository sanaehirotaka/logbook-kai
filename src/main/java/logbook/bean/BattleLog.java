package logbook.bean;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.internal.LoggerHolder;
import logbook.proxy.RequestMetaData;
import lombok.Data;

/**
 * 戦闘ログ
 *
 */
@Data
public class BattleLog implements Serializable {

    private static final long serialVersionUID = -6163406897520116392L;

    /** 連合艦隊 */
    private CombinedType combinedType = CombinedType.未結成;

    /** 開始/進撃(順番に複数存在する) */
    private List<MapStartNext> next = new ArrayList<>();

    /** 戦闘(昼戦、特殊夜戦) */
    private IFormation battle;

    /** 夜戦 */
    private IMidnightBattle midnight;

    /** 戦闘結果 */
    private BattleResult result;

    /** 艦隊スナップショット */
    private Map<Integer, List<Ship>> deckMap;

    /** 装備スナップショット */
    private Map<Integer, SlotItem> itemMap;

    /** 退避艦IDスナップショット */
    private Set<Integer> escape;

    /** 日時(戦闘結果の取得日時) */
    private String time;

    /** ローデータ */
    private RawData raw;

    /**
     * 艦隊スナップショットを作成します
     * @param log 戦闘ログ
     * @param dockIds 艦隊ID
     */
    public static void snapshot(BattleLog log, Integer... dockIds) {
        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                .getSlotitemMap();

        Map<Integer, List<Ship>> deckMap = new HashMap<>();
        Map<Integer, SlotItem> cloneItem = new HashMap<>();

        for (Integer dockId : dockIds) {
            List<Ship> ships = new ArrayList<>();
            for (Integer shipId : DeckPortCollection.get()
                    .getDeckPortMap()
                    .get(dockId)
                    .getShip()) {
                Ship ship = shipMap.get(shipId);
                if (ship != null) {
                    ship = ship.clone();
                    if (ship.getSlot() != null) {
                        for (Integer itemId : ship.getSlot()) {
                            SlotItem item = itemMap.get(itemId);
                            if (item != null) {
                                cloneItem.put(itemId, item);
                            }
                        }
                        {
                            SlotItem item = itemMap.get(ship.getSlotEx());
                            if (item != null) {
                                cloneItem.put(ship.getSlotEx(), item);
                            }
                        }
                    }
                }
                ships.add(ship);
            }
            deckMap.put(dockId, ships);
        }
        log.setDeckMap(deckMap);
        log.setItemMap(cloneItem);
        log.setEscape(AppCondition.get().getEscape());
    }

    /**
     * ローデータを設定する
     *
     * @param log 戦闘ログ
     * @param consumer setter
     * @param json 設定するjson
     * @param req リクエスト
     */
    @SuppressWarnings("unchecked")
    public static void setRawData(BattleLog log, BiConsumer<RawData, ApiData> consumer,
            JsonObject json, RequestMetaData req) {
        RawData rawData = log.getRaw();
        if (rawData == null) {
            rawData = new RawData();
            log.setRaw(rawData);
        }
        ObjectMapper mapper = new ObjectMapper();
        StringWriter sw = new StringWriter(1024 * 4);
        try (JsonWriter jw = Json.createWriter(sw)) {
            jw.writeObject(json);
        }
        try {
            ApiData data = new ApiData();
            data.setUri(req.getRequestURI());
            data.setApidata(mapper.readValue(sw.toString(), Map.class));

            consumer.accept(rawData, data);
        } catch (Exception e) {
            LoggerHolder.get().warn("ローデータの設定に失敗しました", e);
        }
    }

    /**
     * ローデータ
     */
    @Data
    public static class RawData {

        /** 戦闘(昼戦、特殊夜戦) */
        private ApiData battle;

        /** 夜戦 */
        private ApiData midnight;

        /** 戦闘結果 */
        private ApiData result;
    }

    /**
     * ローデータ
     */
    @Data
    public static class ApiData {

        /** URI */
        private String uri;

        /** api_data */
        @JsonProperty("api_data")
        private Map<Object, Object> apidata;
    }
}
