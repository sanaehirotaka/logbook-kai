package logbook.internal.gui;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.internal.Ships;

/**
 * 艦隊タブの艦娘
 *
 */
public class FleetTabShipPane extends HBox {

    private Ship ship;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label level;

    @FXML
    private ProgressBar fuel;

    @FXML
    private ProgressBar bull;

    @FXML
    private Label cond;

    /**
     * 艦隊タブのコンストラクタ
     *
     * @param ship 艦娘
     */
    public FleetTabShipPane(Ship ship) {
        this.ship = ship;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_ship.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.img.setImage(Ships.shipWithItemImage(this.ship));

        Optional<ShipMst> mst = Ships.shipMst(this.ship);

        // 名前
        String name = mst.map(ShipMst::getName)
                .orElse("");
        this.name.setText(name);
        this.level.setText("(Lv" + this.ship.getLv() + ")");
        this.cond.setText(this.ship.getCond() + " cond.");
        int fuel = this.ship.getFuel();
        int bull = this.ship.getBull();
        this.fuel.setProgress((double) (fuel) / (double) mst.map(ShipMst::getFuelMax).orElse(fuel));
        this.bull.setProgress((double) (bull) / (double) mst.map(ShipMst::getBullMax).orElse(bull));

        ObservableList<String> styleClass = this.cond.getStyleClass();
        styleClass.clear();
        if (Ships.isDeepGreen(this.ship)) {
            styleClass.add("deepgreen");
        } else if (Ships.isGreen(this.ship)) {
            styleClass.add("green");
        } else if (Ships.isOrange(this.ship)) {
            styleClass.add("orange");
        } else if (Ships.isRed(this.ship)) {
            styleClass.add("red");
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabShipPane.class);
    }
}
