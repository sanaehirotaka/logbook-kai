package logbook.internal.gui;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;

/**
 * 所有艦娘一覧のコントローラ
 *
 */
public class ShipController extends WindowController {

    @FXML
    private TabPane tab;

    @FXML
    void initialize() {

        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();

        this.tab.getTabs()
                .add(new Tab("全員",
                        new ShipTablePane(shipMap.values().stream()
                                .sorted(Comparator.comparing(Ship::getShipId))
                                .sorted(Comparator.comparing(Ship::getLv).reversed())
                                .collect(Collectors.toList()))));

        for (DeckPort deck : DeckPortCollection.get().getDeckPorts()) {
            List<Ship> ships = deck.getShip()
                    .stream()
                    .map(shipMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            this.tab.getTabs()
                    .add(new Tab(deck.getName(), new ShipTablePane(ships)));
        }

    }
}
