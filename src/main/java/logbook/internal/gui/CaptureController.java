package logbook.internal.gui;

import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import logbook.bean.AppConfig;
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
    private CheckBox direct;

    @FXML
    private Label message;

    @FXML
    private ScrollPane imageParent;

    @FXML
    private ImageView image;

    /** 画像リスト */
    private ObservableList<ImageData> images = FXCollections.observableArrayList();

    /** 画像プレビュー */
    private ObjectProperty<ImageData> preview = new SimpleObjectProperty<>();

    /** スクリーンショット */
    private ScreenCapture sc;

    /** 周期キャプチャ */
    private Timeline timeline = new Timeline();

    /** 直接保存先 */
    private Path directPath;

    @FXML
    void initialize() {
        ImageIO.setUseCache(false);
        this.image.fitWidthProperty().bind(this.imageParent.widthProperty());
        this.image.fitHeightProperty().bind(this.imageParent.heightProperty());
        this.preview.addListener(this::viewImage);
        this.direct.selectedProperty().addListener((ov, o, n) -> {
            if (n) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("キャプチャの保存先");
                // 覚えた保存先をセット
                File initDir = Optional.ofNullable(AppConfig.get().getCaptureDir())
                        .map(File::new)
                        .filter(File::isDirectory)
                        .orElse(null);
                if (initDir != null) {
                    dc.setInitialDirectory(initDir);
                }
                File file = dc.showDialog(this.getWindow());
                if (file != null) {
                    this.directPath = file.toPath();
                } else {
                    this.direct.setSelected(false);
                }
            }
        });
    }

    @FXML
    void cutNone(ActionEvent event) {
        this.sc.setCutRect(ScreenCapture.CutType.NONE.getAngle());
    }

    @FXML
    void cutUnit(ActionEvent event) {
        this.sc.setCutRect(ScreenCapture.CutType.UNIT.getAngle());
    }

    @FXML
    void cutUnitWithoutShip(ActionEvent event) {
        this.sc.setCutRect(ScreenCapture.CutType.UNIT_WITHOUT_SHIP.getAngle());
    }

    @FXML
    void detect(ActionEvent event) {
        this.detectAction();
    }

    @FXML
    void detectManual(ActionEvent event) {
        this.detectManualAction();
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
                this.timeline.setCycleCount(Animation.INDEFINITE);
                this.timeline.getKeyFrames().clear();
                this.timeline.getKeyFrames()
                        .add(new KeyFrame(javafx.util.Duration.millis(100),
                                this::captureAction));
                this.timeline.play();
                // キャプチャボタンテキストの変更
                this.capture.setText("停止");
                // 閉じる時に止める
                this.getWindow().addEventHandler(WindowEvent.WINDOW_HIDDEN, this::onclose);
            }
        } else {
            this.captureAction(event);
        }
    }

    @FXML
    void save(ActionEvent event) {
        try {
            InternalFXMLLoader.showWindow("logbook/gui/capturesave.fxml", this.getWindow(), "キャプチャの保存", controller -> {
                ((CaptureSaveController) controller).setItems(this.images);
            }, null);
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
            GraphicsConfiguration gc = this.currentGraphicsConfiguration();
            Robot robot = new Robot(gc.getDevice());
            BufferedImage image = robot.createScreenCapture(gc.getBounds());
            Rectangle relative = ScreenCapture.detectGameScreen(image, 800, 480);
            Rectangle screenBounds = gc.getBounds();
            this.setBounds(robot, relative, screenBounds);
        } catch (Exception e) {
            LoggerHolder.LOG.error("座標取得に失敗しました", e);
        }
    }

    private Point2D start;

    private Point2D end;

    /**
     * 座標取得アクション
     */
    private void detectManualAction() {
        try {
            GraphicsConfiguration gcnf = this.currentGraphicsConfiguration();
            Robot robot = new Robot(gcnf.getDevice());

            BufferedImage bufferedImage = robot.createScreenCapture(gcnf.getBounds());

            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);

            Stage stage = new Stage();
            Group root = new Group();
            Canvas canvas = new Canvas();
            canvas.widthProperty().bind(stage.widthProperty());
            canvas.heightProperty().bind(stage.heightProperty());
            canvas.setCursor(Cursor.CROSSHAIR);

            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1);
            gc.setLineDashes(5, 5);

            // ドラッグの開始
            canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                this.start = new Point2D(e.getX(), e.getY());
            });
            // ドラッグ中
            canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                Point2D now = new Point2D(e.getX(), e.getY());

                double x = Math.min(this.start.getX(), now.getX()) + 0.5;
                double y = Math.min(this.start.getY(), now.getY()) + 0.5;
                double w = Math.abs(this.start.getX() - now.getX());
                double h = Math.abs(this.start.getY() - now.getY());

                gc.strokeRect(x, y, w, h);
            });
            // ドラッグの終了
            canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                this.end = new Point2D(e.getX(), e.getY());
                if (!this.start.equals(this.end)) {

                    Optional<ButtonType> buttonType = Tools.Conrtols.alert(Alert.AlertType.CONFIRMATION,
                            "矩形選択",
                            "この範囲でよろしいですか？",
                            stage);
                    if (buttonType.orElse(null) == ButtonType.OK) {
                        int x = (int) Math.min(this.start.getX(), this.end.getX());
                        int y = (int) Math.min(this.start.getY(), this.end.getY());
                        int w = (int) Math.abs(this.start.getX() - this.end.getX());
                        int h = (int) Math.abs(this.start.getY() - this.end.getY());

                        Rectangle tmp = getTrimSize(bufferedImage.getSubimage(x, y, w, h));
                        Rectangle relative = new Rectangle(
                                (int) (x + tmp.getX()),
                                (int) (y + tmp.getY()),
                                (int) tmp.getWidth(),
                                (int) tmp.getHeight());

                        Rectangle screenBounds = gcnf.getBounds();

                        this.setBounds(robot, relative, screenBounds);

                        stage.setFullScreen(false);
                    }
                }
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            });
            root.getChildren().addAll(new ImageView(image), canvas);

            stage.setScene(new Scene(root));
            stage.setTitle("座標取得");
            stage.setFullScreenExitHint("キャプチャする領域をマウスでドラッグして下さい。 [Esc]キーでキャンセル");
            stage.setFullScreen(true);
            stage.fullScreenProperty().addListener((ov, o, n) -> {
                if (!n)
                    stage.close();
            });
            stage.setAlwaysOnTop(true);
            stage.show();
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
                boolean isDirect = this.direct.isSelected() && this.directPath != null;
                if (isDirect) {
                    this.sc.captureDirect(this.directPath);
                } else {
                    this.sc.capture();
                }
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
     * @param ov 値が変更されたObservableValue
     * @param o 古い値
     * @param n 新しい値
     */
    private void viewImage(ObservableValue<? extends ImageData> ov, ImageData o, ImageData n) {
        ImageData image = this.preview.getValue();
        if (image != null) {
            this.image.setImage(new Image(new ByteArrayInputStream(image.getImage())));
        }
    }

    private static final int WHITE = java.awt.Color.WHITE.getRGB();

    /**
     * トリムサイズを返します
     *
     * @param image
     * @return
     */
    public static Rectangle getTrimSize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int startwidth = width / 2;
        int startheightTop = (height / 3) * 2;
        int startheightButton = height / 3;

        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;

        // 左トリム(上)
        for (int i = 0; i < width; i++) {
            if (image.getRGB(i, startheightTop) != WHITE) {
                x = i;
                break;
            }
        }
        // 左トリム(下)
        for (int i = 0; i < width; i++) {
            if (image.getRGB(i, startheightButton) != WHITE) {
                x = Math.min(x, i);
                break;
            }
        }
        // 上トリム
        for (int i = 0; i < height; i++) {
            if (image.getRGB(startwidth, i) != WHITE) {
                y = i;
                break;
            }
        }
        // 右トリム(上)
        for (int i = width - 1; i >= 0; i--) {
            if (image.getRGB(i, startheightTop) != WHITE) {
                w = (i - x) + 1;
                break;
            }
        }
        // 右トリム(下)
        for (int i = width - 1; i >= 0; i--) {
            if (image.getRGB(i, startheightButton) != WHITE) {
                w = Math.max(w, (i - x) + 1);
                break;
            }
        }
        // 下トリム
        for (int i = height - 1; i >= 0; i--) {
            if (image.getRGB(startwidth, i) != WHITE) {
                h = (i - y) + 1;
                break;
            }
        }

        if ((w == 0) || (h == 0)) {
            return new Rectangle(0, 0, image.getWidth(), image.getHeight());
        } else {
            return new Rectangle(x, y, w, h);
        }
    }

    private void setBounds(Robot robot, Rectangle relative, Rectangle screenBounds) {
        if (relative != null) {
            Rectangle fixed = new Rectangle(relative.x + screenBounds.x, relative.y + screenBounds.y,
                    relative.width, relative.height);

            String text = "(" + (int) fixed.getMinX() + "," + (int) fixed.getMinY() + ")";
            this.message.setText(text);
            this.capture.setDisable(false);
            this.config.setDisable(false);
            this.sc = new ScreenCapture(robot, fixed);
            this.sc.setItems(this.images);
            this.sc.setCurrent(this.preview);
        } else {
            this.message.setText("座標未設定");
            this.capture.setDisable(true);
            this.config.setDisable(true);
            this.sc = null;
        }
    }

    private GraphicsConfiguration currentGraphicsConfiguration() {
        Window window = this.getWindow();
        int x = (int) window.getX();
        int y = (int) window.getY();
        return ScreenCapture.detectScreenDevice(x, y);
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(CaptureController.class);
    }
}
