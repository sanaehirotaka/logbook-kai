package logbook.internal.gui;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    private Timeline timeline;

    @FXML
    void initialize() {
        try {
            ShipTablePane allPane = new ShipTablePane(() -> {
                Map<Integer, Ship> shipMap = ShipCollection.get()
                        .getShipMap();
                return shipMap.values().stream()
                        .sorted(Comparator.comparing(Ship::getShipId))
                        .sorted(Comparator.comparing(Ship::getLv).reversed())
                        .collect(Collectors.toList());
            });

            this.tab.getTabs().add(new Tab("全員", allPane));

            for (DeckPort deck : DeckPortCollection.get().getDeckPortMap().values()) {
                ShipTablePane deckPane = new ShipTablePane(deck);
                this.tab.getTabs().add(new Tab(deck.getName(), deckPane));
            }

            this.timeline = new Timeline();
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(1),
                    this::update));
            this.timeline.play();

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面の更新
     *
     * @param e ActionEvent
     */
    void update(ActionEvent e) {
        Tab selectedTab = this.tab.getSelectionModel()
                .getSelectedItem();
        if (selectedTab != null) {
            Node content = selectedTab.getContent();
            if (content instanceof ShipTablePane) {
                ((ShipTablePane) content).update();
            }
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        window.setOnCloseRequest(e -> this.timeline.stop());
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ShipController.class);
    }
}
