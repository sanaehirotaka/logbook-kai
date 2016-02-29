package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.bean.Ndock;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipDescription;
import logbook.bean.ShipDescriptionCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.Ships;

/**
 * 入渠ドック
 *
 */
public class NdockPane extends HBox {

    private static final int ONE_MINUTES = 60;
    private static final int ONE_HOUR = 60 * 60;
    private static final int ONE_DAY = 60 * 60 * 24;

    /** 入渠ドック */
    private final Ndock ndock;

    @FXML
    private ImageView ship;

    @FXML
    private HBox items;

    @FXML
    private Label name;

    @FXML
    private Label time;

    /**
     * 入渠ドックペインのコンストラクタ
     *
     * @param ndock 入渠ドック
     */
    public NdockPane(Ndock ndock) {
        this.ndock = ndock;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/ndock.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LogManager.getLogger(NdockPane.class)
                    .error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // 艦娘
            Ship ship = ShipCollection.get()
                    .getShipMap()
                    .get(this.ndock.getShipId());
            // 艦船
            ShipDescription desc = ShipDescriptionCollection.get()
                    .getShipMap()
                    .get(ship.getShipId());

            // 艦娘画像
            this.ship.setImage(Ships.shipImage(ship));

            for (Integer itemId : ship.getSlot()) {
                // 装備アイコン
                this.addItemIcon(itemId);
            }
            if (ship.getSlotEx() != 0) {
                // 補強増設は0(未開放)以外の場合
                this.addItemIcon(ship.getSlotEx());
            }
            // 名前
            this.name.setText(desc.getName() + " (Lv" + ship.getLv() + ")");
            // 名前
            this.time.setText(dockTime(this.ndock));

        } catch (Exception e) {
            LogManager.getLogger(NdockPane.class)
                    .error("FXMLの初期化に失敗しました", e);
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

    private static String dockTime(Ndock ndock) {
        // TODO:クソみたいなコード あとで直したい
        long r = (ndock.getCompleteTime() - System.currentTimeMillis()) / 1000;
        if (r > 0) {
            if (r > ONE_DAY) {
                return (r / ONE_DAY) + "日" + ((r % ONE_DAY) / ONE_HOUR) + "時間"
                        + ((r % ONE_HOUR) / ONE_MINUTES) + "分";
            } else if (r > ONE_HOUR) {
                return (r / ONE_HOUR) + "時間" + ((r % ONE_HOUR) / ONE_MINUTES) + "分";
            } else if (r > ONE_MINUTES) {
                return (r / ONE_MINUTES) + "分" + (r % ONE_MINUTES) + "秒";
            } else {
                return r + "秒";
            }
        } else {
            return null;
        }
    }
}
