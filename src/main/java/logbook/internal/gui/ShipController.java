package logbook.internal.gui;

import java.util.Collection;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

        Collection<Ship> ships = ShipCollection.get()
                .getShipMap()
                .values();
        this.tab.getTabs()
                .add(new Tab("全員", new ShipTablePane(ships)));

    }
}
