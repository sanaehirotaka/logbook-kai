package logbook.internal.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.util.Duration;
import logbook.proxy.ProxyServer;

/**
 * UIコントローラー
 *
 */
public class MainController {

    private String itemButtonFormat;
    private String shipButtonFormat;

    @FXML
    private Button itemButton;

    @FXML
    private Button shipButton;

    @FXML
    void initialize() {
        // サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
        ProxyServer.getInstance().start();

        this.itemButtonFormat = this.itemButton.textProperty().get();
        this.shipButtonFormat = this.shipButton.textProperty().get();

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(1),
                this::update));
        timeline.play();
    }

    /**
     * 所有装備
     *
     * @param e ActionEvent
     */
    @FXML
    void items(ActionEvent e) {
        // TODO 未実装
    }

    /**
     * 所有艦娘
     *
     * @param e ActionEvent
     */
    @FXML
    void ships(ActionEvent e) {
        // TODO 未実装
    }

    /**
     * 画面の更新
     *
     * @param e
     */
    void update(ActionEvent e) {
        // TODO 未実装
    }
}
