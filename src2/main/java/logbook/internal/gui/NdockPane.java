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

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(NdockPane.class);
    }
}
