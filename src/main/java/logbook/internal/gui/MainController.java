package logbook.internal.gui;

import java.util.Map;
import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.proxy.ProxyServer;

/**
 * UIコントローラー
 *
 */
public class MainController {

    private String itemButtonFormat;
    private String shipButtonFormat;

    @FXML
    private Button item;

    @FXML
    private Button ship;

    @FXML
    private VBox infobox;

    @FXML
    private VBox missionbox;

    @FXML
    private VBox ndockbox;

    @FXML
    void initialize() {
        // サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
        ProxyServer.getInstance().start();

        this.itemButtonFormat = this.item.textProperty().get();
        this.shipButtonFormat = this.ship.textProperty().get();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(1),
                this::update));
        timeline.play();
    }

    /**
     * 所有装備
     *
     * @param e ActionEvent
     */
    @FXML
    void items(ActionEvent e) {
        // TODO 未実装
    }

    /**
     * 所有艦娘
     *
     * @param e ActionEvent
     */
    @FXML
    void ships(ActionEvent e) {
        // TODO 未実装
    }

    /**
     * 画面の更新
     *
     * @param e
     */
    void update(ActionEvent e) {

        // 入渠ドックの更新
        ObservableList<Node> ndock = this.ndockbox.getChildren();
        ndock.clear();
        Map<Integer, Ship> ships = ShipCollection.get()
                .getShipMap();
        NdockCollection.get()
                .getNdockSet()
                .stream()
                .map(ships::get)
                .filter(Objects::nonNull)
                .map(ShipPane::new)
                .forEach(ndock::add);

    }
}
