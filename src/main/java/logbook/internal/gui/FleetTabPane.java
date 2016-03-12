package logbook.internal.gui;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import logbook.bean.DeckPort;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.internal.Ships;

/**
 * 艦隊タブ
 *
 */
public class FleetTabPane extends ScrollPane {

    /** 艦隊 */
    private DeckPort port;

    /** 艦娘達 */
    private List<Ship> shipList;

    /** 艦隊のハッシュ・コード */
    private int portHashCode;

    /** 艦娘達のハッシュ・コード */
    private int shipsHashCode;

    /** メッセージ */
    @FXML
    private Label message;

    /** 艦娘達 */
    @FXML
    private VBox ships;

    /**
     * 艦隊ペインのコンストラクタ
     *
     * @param port 艦隊
     */
    public FleetTabPane(DeckPort port) {
        this.port = port;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_tab.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    /**
     * 画面を更新します
     */
    public void update(DeckPort port) {
        this.port = port;
        this.update();
    }

    /**
     * 画面を更新します
     */
    public void update() {
        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        this.shipList = this.port.getShip()
                .stream()
                .map(shipMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (this.portHashCode != this.port.hashCode() || this.shipsHashCode != this.shipList.hashCode()) {
            this.updateShips();
        }
        this.portHashCode = this.port.hashCode();
        this.shipsHashCode = this.shipList.hashCode();
    }

    private void updateShips() {
        this.message.setText("制空値計: " + this.shipList.stream()
                .mapToInt(Ships::airSuperiority)
                .sum()
                + " 索敵値(2-5式秋): " + Ships.viewRange(this.shipList));

        ObservableList<Node> childs = this.ships.getChildren();
        childs.clear();
        this.shipList.stream()
                .map(FleetTabShipPane::new)
                .forEach(childs::add);
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabPane.class);
    }
}
