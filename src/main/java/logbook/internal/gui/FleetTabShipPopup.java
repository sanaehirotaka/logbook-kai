package logbook.internal.gui;

import java.io.IOException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logbook.Messages;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.internal.Items;

/**
 * 艦隊タブポップアップ
 *
 */
public class FleetTabShipPopup extends VBox {

    private Ship ship;

    @FXML
    private Label onslot1;

    @FXML
    private ImageView itemImage1;

    @FXML
    private Label item1;

    @FXML
    private Label onslot2;

    @FXML
    private ImageView itemImage2;

    @FXML
    private Label item2;

    @FXML
    private Label onslot3;

    @FXML
    private ImageView itemImage3;

    @FXML
    private Label item3;

    @FXML
    private Label onslot4;

    @FXML
    private ImageView itemImage4;

    @FXML
    private Label item4;

    @FXML
    private ImageView itemImage6;

    @FXML
    private Label item6;

    @FXML
    private Label ammo;

    @FXML
    private Label ammoInfo;

    @FXML
    private Label fuel;

    /**
     * 艦隊タブポップアップのコンストラクタ
     *
     * @param ship 艦娘
     */
    public FleetTabShipPopup(Ship ship) {
        this.ship = ship;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_tab_popup.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        Label[] onslots = { this.onslot1, this.onslot2, this.onslot3, this.onslot4 };
        ImageView[] imgs = { this.itemImage1, this.itemImage2, this.itemImage3, this.itemImage4 };
        Label[] names = { this.item1, this.item2, this.item3, this.item4 };
        for (int i = 0; i < this.ship.getSlotnum(); i++) {
            SlotItem item = SlotItemCollection.get()
                    .getSlotitemMap()
                    .get(this.ship.getSlot().get(i));
            onslots[i].setText(String.valueOf(this.ship.getOnslot().get(i)));
            if (item != null) {
                imgs[i].setImage(Items.itemImage(item));
                String name = Items.slotitemMst(item)
                        .map(SlotitemMst::getName)
                        .orElse("");
                name += Optional.ofNullable(item.getLevel())
                        .filter(lv -> lv > 0)
                        .map(lv -> Messages.getString("item.level", lv)) //$NON-NLS-1$
                        .orElse("");
                name += Optional.ofNullable(item.getAlv())
                        .map(alv -> Messages.getString("item.alv", alv)) //$NON-NLS-1$
                        .orElse("");
                names[i].setText(name);
            }
        }
        if (this.ship.getSlotEx() != 0) {
            SlotItem item = SlotItemCollection.get()
                    .getSlotitemMap()
                    .get(this.ship.getSlotEx());
            // 補強増設は0(未開放)以外の場合
            this.itemImage6.setImage(Items.itemImage(item));
            if (item != null) {
                String name = Items.slotitemMst(item)
                        .map(SlotitemMst::getName)
                        .orElse("");
                name += Optional.ofNullable(item.getLevel())
                        .filter(lv -> lv > 0)
                        .map(lv -> Messages.getString("item.level", lv)) //$NON-NLS-1$
                        .orElse("");
                name += Optional.ofNullable(item.getAlv())
                        .map(alv -> Messages.getString("item.alv", alv)) //$NON-NLS-1$
                        .orElse("");
                this.item6.setText(name);
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabShipPopup.class);
    }
}
