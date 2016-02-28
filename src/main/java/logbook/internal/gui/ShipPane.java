package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.Ships;

/**
 * 艦娘
 *
 */
public class ShipPane extends AnchorPane {

    private final Ship bean;

    @FXML
    private ImageView ship;

    @FXML
    private HBox items;

    public ShipPane(Ship ship) {
        this.bean = ship;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/ship.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LogManager.getLogger(ShipPane.class)
                    .error("logbook/gui/ship.fxml", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // 艦娘画像
            this.ship.setImage(Ships.shipImage(this.bean));

            for (Integer itemId : this.bean.getSlot()) {
                // 装備アイコン
                this.addItemIcon(itemId);
            }
            if (this.bean.getSlotEx() != 0) {
                // 補強増設は0(未開放)以外の場合
                this.addItemIcon(this.bean.getSlotEx());
            }
        } catch (Exception e) {

        }
    }

    private void addItemIcon(Integer itemId) throws IOException {
        SlotItem item = SlotItemCollection.get()
                .getSlotitemMap()
                .get(itemId);

        if (item != null) {
            Image image = Ships.borderedItemImage(item);

            if (image != null) {
                ImageView iv = new ImageView(image);
                iv.setFitHeight(24);
                iv.setFitWidth(24);
                this.items.getChildren()
                        .add(iv);
            }
        }
    }
}
