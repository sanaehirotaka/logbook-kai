package logbook.internal.gui;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logbook.Messages;
import logbook.bean.Chara;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;

/**
 * 艦隊タブポップアップ
 *
 */
public class FleetTabShipPopup extends VBox {

    private Chara chara;

    /**
     * 艦隊タブポップアップのコンストラクタ
     *
     * @param ship 艦娘
     */
    public FleetTabShipPopup(Chara chara) {
        this.chara = chara;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_tab_popup.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        if (this.chara instanceof Ship) {
            Ship ship = (Ship) this.chara;

            for (int i = 0; i < ship.getSlotnum(); i++) {
                this.getChildren().add(new FleetTabShipPopupItem(this.chara, i));
            }
            if (ship.getSlotEx() != 0) {
                SlotItem item = SlotItemCollection.get()
                        .getSlotitemMap()
                        .get(ship.getSlotEx());
                if (item != null) {
                    this.getChildren().add(new FleetTabShipPopupItem(this.chara));
                }
            }
        } else {
            for (int i = 0; i < this.chara.getSlot().size(); i++) {
                if (this.chara.getSlot().get(i) > 0) {
                    this.getChildren().add(new FleetTabShipPopupItem(this.chara, i));
                }
            }
        }
    }

    /**
     * 艦隊タブポップアップの装備
     *
     */
    private static class FleetTabShipPopupItem extends HBox {

        private static final int SLOT_EX = 6;

        @FXML
        private Label onslot;

        @FXML
        private Label maxeq;

        @FXML
        private ImageView image;

        @FXML
        private Label name;

        /** キャラクター */
        private Chara chara;

        /** スロット番号 */
        private int slotIndex;

        /**
         * 艦隊タブポップアップのコンストラクタ(補強増設)
         *
         * @param chara キャラクター
         */
        public FleetTabShipPopupItem(Chara chara) {
            this(chara, SLOT_EX);
        }

        /**
         * 艦隊タブポップアップのコンストラクタ
         *
         * @param chara キャラクター
         * @param slotIndex スロット番号
         */
        public FleetTabShipPopupItem(Chara chara, int slotIndex) {
            this.chara = chara;
            this.slotIndex = slotIndex;
            try {
                FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_tab_popup_item.fxml");
                loader.setRoot(this);
                loader.setController(this);
                loader.load();
            } catch (IOException e) {
                LoggerHolder.get().error("FXMLのロードに失敗しました", e);
            }
        }

        @FXML
        void initialize() {
            if (this.chara instanceof Ship) {
                Ship ship = (Ship) this.chara;

                Integer itemId = this.slotIndex == SLOT_EX
                        ? ship.getSlotEx() : this.chara.getSlot().get(this.slotIndex);

                SlotItem item = SlotItemCollection.get()
                        .getSlotitemMap()
                        .get(itemId);

                Integer slotEq = Ships.shipMst(this.chara)
                        .map(ShipMst::getMaxeq)
                        .map(eq -> eq.size() > this.slotIndex ? eq.get(this.slotIndex) : 0)
                        .orElse(0);
                if (slotEq != null && slotEq > 0) {
                    Integer onslot = ship.getOnslot().get(this.slotIndex);

                    this.onslot.setText(String.valueOf(onslot));
                    if (!slotEq.equals(onslot)) {
                        this.onslot.getStyleClass().add("alert");
                        this.maxeq.setText("/" + slotEq);
                    }
                }
                if (item != null) {
                    this.image.setImage(Items.itemImage(item));
                    String name = Items.slotitemMst(item)
                            .map(SlotitemMst::getName)
                            .orElse("");
                    name += Optional.ofNullable(item.getLevel())
                            .filter(lv -> lv > 0)
                            .map(lv -> Messages.getString("item.level", lv))
                            .orElse("");
                    name += Optional.ofNullable(item.getAlv())
                            .map(alv -> Messages.getString("item.alv", alv))
                            .orElse("");
                    this.name.setText(name);
                } else {
                    this.name.setText("-");
                }
            } else {
                SlotitemMst item = SlotitemMstCollection.get()
                        .getSlotitemMap()
                        .get(this.chara.getSlot().get(this.slotIndex));
                this.image.setImage(Items.itemImage(item));
                this.name.setText(item.getName());
            }
        }
    }
}
