package logbook.internal.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import logbook.bean.Ship;
import logbook.bean.ShipDescription;
import logbook.bean.ShipDescriptionCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemDescription;
import logbook.bean.SlotitemDescriptionCollection;

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
        ShipDescription shipDescription = ShipDescriptionCollection.get()
                .getShipMap()
                .get(this.bean.getShipId());
        // TODO: 健在/小破/中破/大破 画像を作る
        Path p = ShipDescription.getResourcePathDir(shipDescription)
                .resolve("1.jpg");
        if (Files.isReadable(p)) {
            this.ship.setImage(new Image(p.toUri().toString()));
        }

        for (Integer itemId : this.bean.getSlot()) {
            // 装備アイコン
            this.addItemIcon(itemId);
        }
        if (this.bean.getSlotEx() != 0) {
            // 補強増設は0(未開放)以外の場合
            this.addItemIcon(this.bean.getSlotEx());
        }
    }

    private void addItemIcon(Integer itemId) {
        Path iconPath;
        SlotItem item = SlotItemCollection.get()
                .getSlotitemMap()
                .get(itemId);

        if (item != null) {
            SlotitemDescription slotitemDescription = SlotitemDescriptionCollection.get()
                    .getSlotitemMap()
                    .get(item.getSlotitemId());
            iconPath = SlotitemDescription.getBorderedImagePath(slotitemDescription);
        } else {
            iconPath = SlotitemDescription.getEmptyImagePath();
        }
        if (Files.isReadable(iconPath)) {
            ImageView iv = new ImageView(iconPath.toUri().toString());
            iv.setFitHeight(24);
            iv.setFitWidth(24);

            this.items.getChildren()
                    .add(iv);
        }
    }
}
