package logbook.internal.gui;

import java.io.IOException;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import logbook.bean.AppCondition;

/**
 * 泊地修理タイマー
 *
 */
public class AkashiTimerPane extends AnchorPane {

    @FXML
    private Label time;

    /**
     * 泊地修理タイマーペインのコンストラクタ
     *
     * @param port 艦隊
     */
    public AkashiTimerPane() {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/akashi_timer.fxml");
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
            this.update();
        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新します
     */
    public void update() {
        long timer = AppCondition.get().getAkashiTimer();
        if (timer > 0) {
            this.time.setText(timeText(Duration.ofMillis(System.currentTimeMillis() - timer)));
        } else {
            this.time.setText("");
        }
    }

    /**
     * 時間のテキスト表現
     *
     * @param d 期間
     * @return 時間のテキスト表現
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
        return sb.toString();
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(AkashiTimerPane.class);
    }
}
