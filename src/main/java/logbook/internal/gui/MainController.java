package logbook.internal.gui;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import logbook.bean.Basic;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItemCollection;
import logbook.proxy.ProxyServer;

/**
 * UIコントローラー
 *
 */
public class MainController extends WindowController {

    private String itemFormat;

    private String shipFormat;

    /** 艦隊コレクションのハッシュ・コード */
    private int portHashCode;

    /** 入渠ドックコレクションのハッシュ・コード */
    private int ndockHashCode;

    @FXML
    private Button item;

    @FXML
    private Button ship;

    @FXML
    private VBox infobox;

    @FXML
    private VBox missionbox;

    @FXML
    private VBox ndockbox;

    @FXML
    void initialize() {
        // サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
        ProxyServer.getInstance().start();

        this.itemFormat = this.item.getText();
        this.shipFormat = this.ship.getText();

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
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/item.fxml");
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            WindowController controller = loader.getController();
            controller.setWindow(stage);

            stage.initOwner(this.getWindow());
            stage.setTitle("所有装備一覧");
            stage.show();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }
    }

    /**
     * 所有艦娘
     *
     * @param e ActionEvent
     */
    @FXML
    void ships(ActionEvent e) {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/ship.fxml");
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            WindowController controller = loader.getController();
            controller.setWindow(stage);

            stage.initOwner(this.getWindow());
            stage.setTitle("所有艦娘一覧");
            stage.show();
        } catch (IOException e1) {
            // TODO 自動生成された catch ブロック
            e1.printStackTrace();
        }
    }

    /**
     * 画面の更新
     *
     * @param e
     */
    void update(ActionEvent e) {
        // 所有装備/所有艦娘
        this.button();
        // 遠征
        this.mission();
        // 入渠ドック
        this.ndock();
    }

    /**
     * 所有装備/所有艦娘の更新
     */
    private void button() {
        // 装備
        Integer slotitem = SlotItemCollection.get()
                .getSlotitemMap()
                .size();
        Integer maxSlotitem = Basic.get()
                .getMaxSlotitem();
        this.item.setText(MessageFormat.format(this.itemFormat, slotitem, maxSlotitem));

        // 艦娘
        Integer chara = ShipCollection.get()
                .getShipMap()
                .size();
        Integer maxChara = Basic.get()
                .getMaxChara();
        this.ship.setText(MessageFormat.format(this.shipFormat, chara, maxChara));
    }

    /**
     * 遠征の更新
     */
    private void mission() {
        List<DeckPort> ports = DeckPortCollection.get()
                .getDeckPorts();
        ObservableList<Node> mission = this.missionbox.getChildren();
        if (this.portHashCode != ports.hashCode()) {
            // ハッシュ・コードが変わっている場合遠征の更新
            mission.clear();
            ports.stream()
                    .skip(1)
                    .map(MissionPane::new)
                    .forEach(mission::add);
            // ハッシュ・コードの更新
            this.portHashCode = ports.hashCode();
        } else {
            // ハッシュ・コードが変わっていない場合updateメソッドを呼ぶ
            for (Node node : mission) {
                if (node instanceof MissionPane) {
                    ((MissionPane) node).update();
                }
            }
        }
    }

    /**
     * 入渠ドックの更新
     */
    private void ndock() {
        Map<Integer, Ndock> ndockMap = NdockCollection.get()
                .getNdockMap();
        ObservableList<Node> ndock = this.ndockbox.getChildren();
        if (this.ndockHashCode != ndockMap.hashCode()) {
            // ハッシュ・コードが変わっている場合入渠ドックの更新
            ndock.clear();
            ndockMap.values()
                    .stream()
                    .filter(n -> 1 < n.getCompleteTime())
                    .map(NdockPane::new)
                    .forEach(ndock::add);
            // ハッシュ・コードの更新
            this.ndockHashCode = ndockMap.hashCode();
        } else {
            // ハッシュ・コードが変わっていない場合updateメソッドを呼ぶ
            for (Node node : ndock) {
                if (node instanceof NdockPane) {
                    ((NdockPane) node).update();
                }
            }
        }
    }
}
