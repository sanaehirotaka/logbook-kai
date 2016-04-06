package logbook.internal.gui;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import logbook.internal.gui.ScreenCapture.ImageData;

/**
 * キャプチャ
 *
 */
public class CaptureController extends WindowController {

    /** 設定 */
    @FXML
    private MenuButton config;

    /** 連写 */
    @FXML
    private CheckMenuItem cyclic;

    /** ラジオボタングループ */
    @FXML
    private ToggleGroup cut;

    /** キャプチャ */
    @FXML
    private Button capture;

    /** 保存 */
    @FXML
    private Button save;

    @FXML
    private Label message;

    @FXML
    private ScrollPane imageParent;

    @FXML
    private ImageView image;

    /** 画像リスト */
    private ObservableList<ImageData> images = FXCollections.observableArrayList();

    /** スクリーンショット */
    private ScreenCapture sc;

    /** 周期キャプチャ */
    private Timeline timeline = new Timeline();

    @FXML
    void initialize() {
        ImageIO.setUseCache(false);
        this.image.fitWidthProperty().bind(this.imageParent.widthProperty());
        this.image.fitHeightProperty().bind(this.imageParent.heightProperty());
        this.images.addListener(this::viewImage);
    }

    @FXML
    void cutNone(ActionEvent event) {
        this.sc.setCutRect(null);
    }

    @FXML
    void cutUnit(ActionEvent event) {
        this.sc.setCutRect(ScreenCapture.UNIT_RECT);
    }

    @FXML
    void cutUnitWithoutShip(ActionEvent event) {
        this.sc.setCutRect(ScreenCapture.UNIT_WITHOUT_SHIP_RECT);
    }

    @FXML
    void detect(ActionEvent event) {
        this.detectAction();
    }

    @FXML
    void cyclic(ActionEvent event) {
        if (this.timeline.getStatus() == Status.RUNNING) {
            // キャプチャ中であれば止める
            this.timeline.stop();
        }

        if (this.cyclic.isSelected()) {
            // キャプチャボタンテキストの変更
            this.capture.setText("開始");
        } else {
            // キャプチャボタンテキストの変更
            this.capture.setText("キャプチャ");
        }
    }

    @FXML
    void capture(ActionEvent event) {
        if (this.cyclic.isSelected()) {
            // 周期キャプチャの場合
            if (this.timeline.getStatus() == Status.RUNNING) {
                // キャプチャ中であれば止める
                this.timeline.stop();
                // キャプチャボタンテキストの変更
                this.capture.setText("開始");
            } else {
                // キャプチャ中で無ければ開始する
                this.timeline.setCycleCount(Timeline.INDEFINITE);
                this.timeline.getKeyFrames().clear();
                this.timeline.getKeyFrames()
                        .add(new KeyFrame(javafx.util.Duration.millis(100),
                                this::captureAction));
                this.timeline.play();
                // キャプチャボタンテキストの変更
                this.capture.setText("停止");
                // 閉じる時に止める
                this.getWindow().setOnCloseRequest(this::onclose);
            }
        } else {
            this.captureAction(event);
        }
    }

    @FXML
    void save(ActionEvent event) {
        try {
            InternalFXMLLoader.showWindow("logbook/gui/capturesave.fxml", this.getWindow(), "キャプチャの保存", controller -> {
                ((CaptureSaveController)controller).setItems(this.images);
            } , null);
        } catch (Exception ex) {
            LoggerHolder.LOG.error("キャプチャの保存に失敗しました", ex);
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        this.detectAction();
    }

    /**
     * 座標取得アクション
     */
    private void detectAction() {
        try {
            Window window = this.getWindow();
            int x = (int) window.getX();
            int y = (int) window.getY();
            GraphicsConfiguration gc = ScreenCapture.detectScreenDevice(x, y);

            Robot robot = new Robot(gc.getDevice());
            BufferedImage image = robot.createScreenCapture(gc.getBounds());

            Rectangle relative = ScreenCapture.detectGameScreen(image, 800, 480);

            if (relative != null) {
                Rectangle fixed = new Rectangle(relative.x + gc.getBounds().x, relative.y + gc.getBounds().y,
                        relative.width, relative.height);

                String text = "(" + (int) fixed.getMinX() + "," + (int) fixed.getMinY() + ")";
                this.message.setText(text);
                this.capture.setDisable(false);
                this.config.setDisable(false);
                this.sc = new ScreenCapture(robot, fixed);
                this.sc.setItems(this.images);
            } else {
                this.message.setText("座標未設定");
                this.capture.setDisable(true);
                this.config.setDisable(true);
                this.sc = null;
            }
        } catch (Exception e) {
            LoggerHolder.LOG.error("座標取得に失敗しました", e);
        }
    }

    /**
     * キャプチャアクション
     */
    private void captureAction(ActionEvent event) {
        try {
            if (this.sc != null) {
                this.sc.capture();
            }
        } catch (Exception e) {
            LoggerHolder.LOG.error("キャプチャに失敗しました", e);
        }
    }

    /**
     * ウインドウを閉じる時のアクション
     *
     * @param event WindowEvent
     */
    private void onclose(WindowEvent event) {
        this.images.clear();
        this.timeline.stop();
    }

    /**
     * キャプチャプレビュー
     *
     * @param image 画像データ
     */
    private void viewImage(Change<? extends ImageData> change) {
        while (change.next()) {
            if (change.wasAdded()) {
                List<? extends ImageData> images = change.getAddedSubList();
                byte[] data = images.get(images.size() - 1).getImage();
                this.image.setImage(new Image(new ByteArrayInputStream(data)));
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(CaptureController.class);
    }
}
