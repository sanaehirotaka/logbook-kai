package logbook.internal.gui;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.Messages;
import logbook.bean.Ndock;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.Ships;
import logbook.internal.Time;

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
    private Label name;

    @FXML
    private Label time;

    /**
     * 入渠ドックのコンストラクタ
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
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // 艦娘
            Ship ship = ShipCollection.get()
                    .getShipMap()
                    .get(this.ndock.getShipId());
            // 艦娘画像
            this.ship.setImage(Ships.shipWithItemImage(ship));
            // 名前
            String name = Ships.shipMst(ship)
                    .map(ShipMst::getName)
                    .orElse("");
            this.name.setText(Messages.getString("ship.name", name, ship.getLv())); //$NON-NLS-1$
            this.update();

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新します
     */
    public void update() {
        // 残り時間を計算
        Duration d = Duration.ofMillis(this.ndock.getCompleteTime() - System.currentTimeMillis());
        // 残り時間を更新
        this.time.setText(Time.toString(d, "修復完了"));

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

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(NdockPane.class);
    }
}
