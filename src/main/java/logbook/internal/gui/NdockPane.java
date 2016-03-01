package logbook.internal.gui;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;

import javafx.collections.ObservableList;
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

    /** 入渠ドック */
    private final Ndock ndock;

    /** 色変化1段階目 */
    private final Duration stage1 = Duration.ofMinutes(40);

    /** 色変化2段階目 */
    private final Duration stage2 = Duration.ofMinutes(20);

    /** 色変化3段階目 */
    private final Duration stage3 = Duration.ofMinutes(10);

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
            this.update();

        } catch (Exception e) {
            LogManager.getLogger(NdockPane.class)
                    .error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新します
     */
    public void update() {
        // 残り時間を計算
        Duration d = Duration.ofMillis(this.ndock.getCompleteTime() - System.currentTimeMillis());
        // 残り時間を更新
        this.time.setText(timeText(d));

        ObservableList<String> styleClass = this.getStyleClass();

        styleClass.removeAll("stage1", "stage2", "stage3");

        // スタイルを更新
        if (d.compareTo(this.stage3) < 0) {
            styleClass.add("stage3");
        } else if (d.compareTo(this.stage2) < 0) {
            styleClass.add("stage2");
        } else if (d.compareTo(this.stage1) < 0) {
            styleClass.add("stage1");
        }
    }

    /**
     * 装備アイコンを追加します
     *
     * @param itemId 装備ID
     * @throws IOException
     */
    private void addItemIcon(Integer itemId) {
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

    /**
     * 入渠時間のテキスト表現
     *
     * @param d 期間
     * @return 入渠時間のテキスト表現
     */
    private static String timeText(Duration d) {
        long days = d.toDays();
        long hours = d.toHours() % 24;
        long minutes = d.toMinutes() % 60;
        long seconds = d.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days + "日");
        }
        if (hours > 0) {
            sb.append(hours + "時間");
        }
        if (minutes > 0) {
            sb.append(minutes + "分");
        }
        if (seconds > 0 && days == 0 && hours == 0) {
            sb.append(seconds + "秒");
        }
        if (d.isZero() || d.isNegative()) {
            sb.append("修復完了");
        }
        return sb.toString();
    }
}
