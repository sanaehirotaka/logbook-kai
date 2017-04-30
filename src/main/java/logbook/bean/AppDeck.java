package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 編成記録
 *
 */
@Getter
@Setter
public class AppDeck implements Serializable {

    private static final long serialVersionUID = -8608415733587694654L;

    /** 編成の名前 */
    private String name;

    /** 艦隊 */
    private List<AppDeckFleet> fleets = new ArrayList<>();

    /**
     * 編成記録の艦隊
     *
     */
    @Data
    public static class AppDeckFleet implements Serializable {

        private static final long serialVersionUID = -442573580025545733L;

        /** 艦隊の名前 */
        private String name;

        /** メモ */
        private String description;

        /** 艦娘達 */
        private List<AppDeckShip> ships = new ArrayList<>();

        /**
         * DeckPortをAppDeckFleetに変換
         *
         * @param port 艦隊
         * @return AppDeckFleet
         */
        public static AppDeckFleet toAppDeckFleet(DeckPort port) {
            AppDeckFleet fleet = new AppDeckFleet();
            fleet.setName(port.getName());

            List<Integer> ships = port.getShip();
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get().getSlotitemMap();

            for (Integer shipid : ships) {
                Ship ship = ShipCollection.get().getShipMap().get(shipid);
                if (ship != null) {
                    AppDeckShip deckShip = new AppDeckShip();
                    List<Integer> items = ship.getSlot();
                    deckShip.setShipId(shipid);
                    for (Integer itemid : items) {
                        SlotItem item = itemMap.get(itemid);
                        if (item != null) {
                            deckShip.getItems().add(item.getSlotitemId());
                            deckShip.getItemLvs().add(item.getLevel());
                        } else {
                            deckShip.getItems().add(null);
                            deckShip.getItemLvs().add(null);
                        }
                    }
                    SlotItem itemEx = itemMap.get(ship.getSlotEx());
                    if (itemEx != null) {
                        deckShip.getItems().add(itemEx.getSlotitemId());
                        deckShip.getItemLvs().add(itemEx.getLevel());
                    } else {
                        deckShip.getItems().add(null);
                        deckShip.getItemLvs().add(null);
                    }
                    fleet.getShips().add(deckShip);
                }
            }
            return fleet;
        }
    }

    /**
     * 編成記録の艦娘
     *
     */
    @Data
    public static class AppDeckShip implements Serializable {

        private static final long serialVersionUID = -6719777410606952140L;

        /** 艦娘 */
        private Integer shipId;

        /** 装備 */
        private List<Integer> items = new ArrayList<>();

        /** 装備改修 */
        private List<Integer> itemLvs = new ArrayList<>();
    }
}
