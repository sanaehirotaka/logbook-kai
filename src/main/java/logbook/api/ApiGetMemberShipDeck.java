package logbook.api;

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
 * /kcsapi/api_get_member/ship_deck
 *
 */
@API("/kcsapi/api_get_member/ship_deck")
public class ApiGetMemberShipDeck implements APIListenerSpi {

    @Override
    public void accept(JsonObject json, RequestMetaData req, ResponseMetaData res) {
        JsonObject data = json.getJsonObject("api_data");
        if (data != null) {
            this.apiShipData(data.getJsonArray("api_ship_data"));
            this.apiDeckData(data.getJsonArray("api_deck_data"));
        }
    }

    /**
     * api_data.api_ship_data
     *
     * @param array api_ship_data
     */
    private void apiShipData(JsonArray array) {
        // 差し替え前
//        Map<Integer, Ship> before = ShipCollection.get()
//                .getShipMap();

        // 差し替え
        Map<Integer, Ship> map = ShipCollection.get()
                .getShipMap();
        map.putAll(JsonHelper.toMap(array, Ship::getId, Ship::toShip));
        
        // 以下のコードは実際動いてなかった（setCondUpdateTime() にたどり着くことがなかった）。
        // 理由は上で before に差し替え前の ShipMap を保持しているが、
        // 差し替え後の ShipMap (map) も同じ ShipCollection.get().getShipMap() を参照しており、
        // 常に before == map となっていたため cond 値の差を計算しても常に同じ値だったため。
        // しかし、実際正しく動いていたとした場合、ここにたどり着くのは出撃途中の戦闘後になるので、
        // cond 値は自然回復以外にも変動する可能性がある。
        // 以上の理由からここで setCondUpdateTime() を更新するコードは削除する。
//        Predicate<Ship> update = beforeShip -> {
//            Ship afterShip = map.get(beforeShip.getId());
//            if (afterShip != null) {
//                return !beforeShip.getCond().equals(afterShip.getCond());
//            }
//            return false;
//        };
//        if (before.values().stream()
//                .filter(ship -> ship.getCond() <= 49)
//                .anyMatch(update)) {
//            ZonedDateTime time = ZonedDateTime.now(ZoneId.systemDefault());
//            AppCondition.get().setCondUpdateTime(time.toEpochSecond());
//        }
    }

    /**
     * api_data.api_deck_data
     *
     * @param array api_deck_data
     */
    private void apiDeckData(JsonArray array) {
        Map<Integer, DeckPort> map = DeckPortCollection.get()
                .getDeckPortMap();
        map.putAll(JsonHelper.toMap(array, DeckPort::getId, DeckPort::toDeckPort));
    }
}
