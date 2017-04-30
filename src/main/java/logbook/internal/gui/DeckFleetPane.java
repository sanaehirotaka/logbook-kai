package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import logbook.bean.AppDeck.AppDeckFleet;
import lombok.Getter;

/**
 * 編成記録の艦隊ペイン
 *
 */
public class DeckFleetPane extends VBox {

    /** 艦隊名 */
    @FXML
    @Getter
    private TextField fleetName;

    /** 艦娘達 */
    @FXML
    private TilePane ships;

    /** メモ */
    @FXML
    private TextArea fleetDescription;

    /** 艦隊 */
    private AppDeckFleet fleet = new AppDeckFleet();

    /** 変更検知 */
    @Getter
    private BooleanProperty modified = new SimpleBooleanProperty(false);

    /**
     * 編成記録の艦隊ペインのコンストラクタ
     *
     * @param fleet 編成記録の艦隊
     */
    public DeckFleetPane(AppDeckFleet fleet) {
        this.fleet = fleet;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/deck_fleet.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    /**
     * 艦隊を取得します
     *
     * @return 編成記録の艦隊
     */
    public AppDeckFleet getBean() {
        if (this.modified.get()) {
            this.fleet.setName(this.fleetName.getText());
            this.fleet.setDescription(this.fleetDescription.getText());
            this.fleet.getShips().clear();
            for (Node node : this.ships.getChildren()) {
                if (node instanceof DeckShipPane) {
                    this.fleet.getShips().add(((DeckShipPane) node).getBean());
                }
            }
        }
        this.modified.set(false);
        return this.fleet;
    }

    @FXML
    void initialize() {
        this.fleetName.setText(this.fleet.getName());
        this.fleetDescription.setText(this.fleet.getDescription());
        this.fleet.getShips().forEach(id -> {
            DeckShipPane pane = new DeckShipPane(id);
            // 子の変更を検知
            pane.getModified().addListener((ov, o, n) -> this.modified.set(true));
            this.ships.getChildren().add(pane);
        });
        // 艦隊名の変更を検知
        this.fleetName.textProperty().addListener((ob, o, n) -> this.modified.set(true));
        // メモの変更を検知
        this.fleetDescription.textProperty().addListener((ob, o, n) -> this.modified.set(true));
    }

    /**
     * 艦隊名のリスナー
     * @param listener
     */
    public void setNameListener(ChangeListener<? super String> listener) {
        this.fleetName.textProperty().addListener(listener);
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(DeckFleetPane.class);
    }
}
