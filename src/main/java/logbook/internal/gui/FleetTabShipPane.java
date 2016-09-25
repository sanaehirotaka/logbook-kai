package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.internal.Ships;
import logbook.plugin.PluginContainer;

/**
 * 艦隊タブの艦娘
 *
 */
public class FleetTabShipPane extends HBox {

    private Ship ship;

    private PopOver pop;

    @FXML
    private ImageView img;

    @FXML
    private Label name;

    @FXML
    private Label level;

    @FXML
    private Label hp;

    @FXML
    private ImageView supply;

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
        this.hp.setText(this.ship.getNowhp() + "/" + this.ship.getMaxhp());
        this.supply.setImage(Ships.supplyGaugeImage(this.ship));
        this.cond.setText(this.ship.getCond() + "cond.");

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

        this.setOnMouseEntered(e -> {
            if (this.pop != null) {
                this.pop.hide();
            }
            this.pop = new PopOver(new FleetTabShipPopup(this.ship));
            this.pop.setOpacity(0.95D);
            this.pop.setDetached(true);
            this.pop.setCornerRadius(0);
            this.pop.setTitle(name);
            this.pop.setArrowLocation(ArrowLocation.TOP_LEFT);
            URL url = PluginContainer.getInstance()
                    .getClassLoader()
                    .getResource("logbook/gui/popup.css");
            this.pop.getRoot()
                    .getStylesheets()
                    .add(url.toString());
            this.pop.show(this);
        });
        this.setOnMouseExited(e -> {
            if (this.pop != null) {
                this.pop.hide();
            }
        });
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabShipPane.class);
    }
}
