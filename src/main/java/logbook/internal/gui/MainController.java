package logbook.internal.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.Notifications;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.bean.Basic;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ndock;
import logbook.bean.NdockCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.bean.SlotItemCollection;
import logbook.internal.Ships;
import logbook.plugin.gui.MainCalcMenu;
import logbook.plugin.gui.MainCommandMenu;
import logbook.plugin.gui.MainExtMenu;
import logbook.plugin.gui.Plugin;
import logbook.proxy.ProxyServer;

/**
 * UIコントローラー
 *
 */
public class MainController extends WindowController {

    /** 通知のインターバル */
    private static final Duration INTERVAL = Duration.ofMinutes(1);

    private String itemFormat;

    private String shipFormat;

    /** 艦隊コレクションのハッシュ・コード */
    private int portHashCode;

    /** 入渠ドックコレクションのハッシュ・コード */
    private int ndockHashCode;

    /** 遠征通知のタイムスタンプ */
    private Map<Integer, Long> timeStampMission = new HashMap<>();

    /** 入渠通知のタイムスタンプ */
    private Map<Integer, Long> timeStampNdock = new HashMap<>();

    /** コマンドメニュー */
    @FXML
    private Menu command;

    /** 計算機 */
    @FXML
    private Menu calc;

    /** その他 */
    @FXML
    private Menu ext;

    @FXML
    private Button item;

    @FXML
    private Button ship;

    @FXML
    private TabPane fleetTab;

    @FXML
    private VBox missionbox;

    @FXML
    private VBox ndockbox;

    private AudioClip clip;

    @FXML
    void initialize() {
        try {
            // サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
            ProxyServer.getInstance().start();

            this.itemFormat = this.item.getText();
            this.shipFormat = this.ship.getText();

            // プラグインによるメニューの追加
            Plugin.getContent(MainCommandMenu.class)
                    .forEach(this.command.getItems()::add);
            Plugin.getContent(MainCalcMenu.class)
                    .forEach(this.calc.getItems()::add);
            Plugin.getContent(MainExtMenu.class)
                    .forEach(this.ext.getItems()::add);

            Timeline timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(new KeyFrame(
                    javafx.util.Duration.seconds(1),
                    this::update));
            timeline.play();
        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
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
        } catch (Exception ex) {
            LoggerHolder.LOG.error("所有装備一覧の初期化に失敗しました", ex);
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
        } catch (Exception ex) {
            LoggerHolder.LOG.error("所有艦娘一覧の初期化に失敗しました", ex);
        }
    }

    /**
     * お風呂に入りたい艦娘
     *
     * @param e ActionEvent
     */
    @FXML
    void ndock(ActionEvent e) {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/require_ndock.fxml");
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            WindowController controller = loader.getController();
            controller.setWindow(stage);

            stage.initOwner(this.getWindow());
            stage.setTitle("お風呂に入りたい艦娘");
            stage.show();
        } catch (Exception ex) {
            LoggerHolder.LOG.error("お風呂に入りたい艦娘の初期化に失敗しました", ex);
        }
    }

    /**
     * 設定
     *
     * @param e ActionEvent
     */
    @FXML
    void config(ActionEvent e) {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/config.fxml");
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            WindowController controller = loader.getController();
            controller.setWindow(stage);

            stage.initOwner(this.getWindow());
            stage.setTitle("設定");
            stage.show();
        } catch (Exception ex) {
            LoggerHolder.LOG.error("設定の初期化に失敗しました", ex);
        }
    }

    /**
     * 画面の更新
     *
     * @param e
     */
    void update(ActionEvent e) {
        try {
            // 所有装備/所有艦娘
            this.button();
            // 艦隊タブ・遠征
            this.checkPort();
            // 入渠ドック
            this.ndock();

            // 遠征・入渠完了時に通知をする
            if (AppConfig.get().isUseNotification()) {
                // 遠征の通知
                this.checkNotifyMission();
                // 入渠ドックの通知
                this.checkNotifyNdock();
            }
        } catch (Exception ex) {
            LoggerHolder.LOG.error("設定の初期化に失敗しました", ex);
        }
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
     * 艦隊の確認
     */
    private void checkPort() {
        Map<Integer, DeckPort> ports = DeckPortCollection.get()
                .getDeckPortMap();
        boolean change = this.portHashCode != ports.hashCode();
        this.portHashCode = ports.hashCode();

        this.fleetTab(change);
        this.mission(change);
    }

    /**
     * 艦隊タブの更新
     *
     * @param change 艦隊の変更フラグ
     */
    private void fleetTab(boolean change) {
        Map<Integer, DeckPort> ports = DeckPortCollection.get()
                .getDeckPortMap();
        ObservableList<Tab> tabs = this.fleetTab.getTabs();
        if (change) {
            if (ports.size() != tabs.size() - 1) {
                for (int i = tabs.size() - 1; i > 0; i--) {
                    tabs.remove(i);
                }
                for (DeckPort port : ports.values()) {
                    FleetTabPane pane = new FleetTabPane(port);
                    Tab tab = new Tab(port.getName(), pane);
                    tab.setClosable(false);
                    tab.getStyleClass().removeIf(s -> !s.equals("tab"));
                    Optional.ofNullable(pane.tabCssClass())
                            .ifPresent(tab.getStyleClass()::add);
                    tabs.add(tab);
                }
            } else {
                Iterator<DeckPort> ite = ports.values().iterator();
                for (int i = 0; ite.hasNext(); i++) {
                    DeckPort port = ite.next();
                    Tab tab = tabs.get(i + 1);
                    Node node = tab.getContent();
                    if (node instanceof FleetTabPane) {
                        FleetTabPane pane = (FleetTabPane) node;
                        pane.update(port);
                        tab.getStyleClass().removeIf(s -> !s.equals("tab"));
                        Optional.ofNullable(pane.tabCssClass())
                                .ifPresent(tab.getStyleClass()::add);
                    }
                }
            }
        } else {
            for (Tab tab : tabs) {
                Node node = tab.getContent();
                if (node instanceof FleetTabPane) {
                    FleetTabPane pane = (FleetTabPane) node;
                    pane.update();
                    tab.getStyleClass().removeIf(s -> !s.equals("tab"));
                    Optional.ofNullable(pane.tabCssClass())
                            .ifPresent(tab.getStyleClass()::add);
                }
            }
        }
    }

    /**
     * 遠征の更新
     *
     * @param change 艦隊の変更フラグ
     */
    private void mission(boolean change) {
        ObservableList<Node> mission = this.missionbox.getChildren();
        if (change) {
            Map<Integer, DeckPort> ports = DeckPortCollection.get()
                    .getDeckPortMap();
            mission.clear();
            ports.values().stream()
                    .skip(1)
                    .map(MissionPane::new)
                    .forEach(mission::add);
        } else {
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

    /**
     * 遠征の通知をチェックします
     */
    private void checkNotifyMission() {
        Map<Integer, DeckPort> ports = DeckPortCollection.get()
                .getDeckPortMap();
        for (DeckPort port : ports.values()) {
            // 0=未出撃, 1=遠征中, 2=遠征帰還, 3=遠征中止
            int state = port.getMission().get(0).intValue();
            // 帰還時間
            long time = port.getMission().get(2);

            if (0 == state) {
                this.timeStampMission.put(port.getId(), 0L);
            } else {
                // 残り時間を計算
                Duration now = Duration.ofMillis(time - System.currentTimeMillis());
                // 前回の通知の時間
                long timeStamp = this.timeStampMission.getOrDefault(port.getId(), 0L);
                if (this.requireNotify(now, timeStamp, AppConfig.get().isUseRemind())) {
                    this.timeStampMission.put(port.getId(), System.currentTimeMillis());
                    this.pushNotifyMission(port);
                }
            }
        }
    }

    /**
     * 遠征通知
     *
     * @param port 艦隊
     */
    private void pushNotifyMission(DeckPort port) {
        if (AppConfig.get().isUseToast()) {
            String message = Messages.getString("mission.complete", port.getName()); //$NON-NLS-1$
            this.showNotify(null, "遠征完了", message);
        }
        if (AppConfig.get().isUseSound()) {
            this.soundNotify();
        }
    }

    /**
     * 入渠ドックの通知をチェックします
     */
    private void checkNotifyNdock() {
        Map<Integer, Ndock> ndockMap = NdockCollection.get()
                .getNdockMap();

        for (Ndock ndock : ndockMap.values()) {
            // 完了時間
            long time = ndock.getCompleteTime();

            if (1 > time) {
                this.timeStampNdock.put(ndock.getId(), 0L);
            } else {
                // 残り時間を計算
                Duration now = Duration.ofMillis(time - System.currentTimeMillis());
                // 前回の通知の時間
                long timeStamp = this.timeStampNdock.getOrDefault(ndock.getId(), 0L);

                if (this.requireNotify(now, timeStamp, false)) {
                    this.timeStampNdock.put(ndock.getId(), System.currentTimeMillis());
                    this.pushNotifyNdock(ndock);
                }
            }
        }
    }

    /**
     * 入渠ドックの通知
     *
     * @param ndock 入渠ドック
     */
    private void pushNotifyNdock(Ndock ndock) {
        if (AppConfig.get().isUseToast()) {
            Ship ship = ShipCollection.get()
                    .getShipMap()
                    .get(ndock.getShipId());
            String name = Ships.shipMst(ship)
                    .map(ShipMst::getName)
                    .orElse("");
            String message = Messages.getString("ship.ndock", name, ship.getLv()); //$NON-NLS-1$

            ImageView img = new ImageView(Ships.shipWithItemImage(ship));

            this.showNotify(img, "修復完了", message);
        }
        if (AppConfig.get().isUseSound()) {
            this.soundNotify();
        }
    }

    /**
     * 通知するか判断します
     *
     * @param now 残り時間
     * @param timeStamp 前回の通知の時間
     * @param remind リマインド
     */
    private boolean requireNotify(Duration now, long timeStamp, boolean remind) {
        if (now.compareTo(INTERVAL) <= 0) {
            // 前回の通知からの経過時間
            Duration course = Duration.ofMillis(System.currentTimeMillis() - timeStamp);
            if (course.compareTo(INTERVAL) >= 0) {
                if (timeStamp == 0L || remind) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 通知を表示する
     *
     * @param node グラフィック
     * @param title タイトル
     * @param message メッセージ
     */
    private void showNotify(Node node, String title, String message) {
        Notifications notifications = Notifications.create()
                .graphic(node)
                .title(title)
                .text(message)
                .position(Pos.BOTTOM_RIGHT);
        if (node == null) {
            notifications.showInformation();
        } else {
            notifications.show();
        }
    }

    /**
     * サウンド通知
     */
    private void soundNotify() {
        if (this.clip == null || !this.clip.isPlaying()) {
            try {
                Path dir = Paths.get(AppConfig.get().getNotifySoundDir());
                if (Files.isDirectory(dir)) {
                    List<Path> list = Files.list(dir)
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toList());
                    if (list.size() > 0) {
                        Collections.shuffle(list);
                        Path file = list.get(0);

                        this.clip = new AudioClip(file.toUri().toString());
                        this.clip.setVolume(AppConfig.get().getSoundLevel() / 100D);
                        this.clip.play();
                    }
                }
            } catch (Exception e) {
                LoggerHolder.LOG.warn("サウンド通知に失敗しました", e);
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(MainController.class);
    }
}
