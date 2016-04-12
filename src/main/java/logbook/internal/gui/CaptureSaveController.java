package logbook.internal.gui;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.controlsfx.control.CheckListView;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.WindowEvent;
import logbook.bean.AppConfig;
import logbook.internal.ThreadManager;
import logbook.internal.gui.ScreenCapture.ImageData;

/**
 * キャプチャ
 *
 */
public class CaptureSaveController extends WindowController {

    /** ファイル名日付書式 */
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss.SSS");

    private static final String JPEG = "jpg";

    private static final String GIF = "gif";

    @FXML
    private ChoiceBox<String> type;

    @FXML
    private TextField fps;

    @FXML
    private CheckBox tile;

    @FXML
    private TextField tileCount;

    @FXML
    private CheckListView<ImageData> list;

    @FXML
    private ScrollPane imageParent;

    @FXML
    private ImageView image;

    /** 画像リスト */
    private ObservableList<ImageData> images;

    @FXML
    void initialize() {
        this.type.setItems(FXCollections.observableArrayList(JPEG, GIF));
        this.type.getSelectionModel().selectFirst();

        this.image.fitWidthProperty().bind(this.imageParent.widthProperty());
        this.image.fitHeightProperty().bind(this.imageParent.heightProperty());
    }

    @FXML
    void tile(ActionEvent event) {
        if (this.tile.isSelected()) {
            this.tileCount.setDisable(false);
        } else {
            this.tileCount.setDisable(true);
        }
    }

    @FXML
    void save(ActionEvent event) {
        List<ImageData> selections = this.list.getCheckModel().getCheckedItems();

        if (!selections.isEmpty()) {
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
                // 保存先を覚える
                AppConfig.get().setCaptureDir(file.getAbsolutePath());

                Path path = file.toPath();

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        if (GIF.equals(CaptureSaveController.this.type.getSelectionModel().getSelectedItem())) {
                            // GIFが選ばれている時
                            CaptureSaveController.this.saveGif(selections, path);
                        } else {
                            // GIF以外
                            CaptureSaveController.this.saveJpeg(selections, path);
                        }
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();

                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.initOwner(CaptureSaveController.this.getWindow());

                        alert.setTitle("キャプチャが保存されました");
                        alert.setContentText("キャプチャが保存されました");
                        alert.show();
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        Throwable t = this.getException();

                        Alert alert = new Alert(AlertType.ERROR);
                        alert.initOwner(CaptureSaveController.this.getWindow());

                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        t.printStackTrace(pw);
                        pw.flush();
                        String stackTrace = sw.toString();

                        TextArea textArea = new TextArea(stackTrace);
                        alert.getDialogPane().setExpandableContent(textArea);

                        alert.setTitle("キャプチャの保存先に失敗しました");
                        alert.setHeaderText("キャプチャの保存先に失敗しました");
                        alert.setContentText(t.getMessage());
                        alert.show();
                    }
                };
                ThreadManager.getExecutorService().execute(task);
            }
        }
    }

    /**
     * アニメーションGIFファイルとして保存します
     *
     * @param selections 画像ファイル
     * @param dir ディレクトリ
     * @throws IOException 入出力例外
     */
    private void saveGif(List<ImageData> selections, Path dir) throws IOException {
        Duration delay = Duration.ofMillis(1000 / Math.max(Integer.parseInt(this.fps.getText()), 1));

        String fname = DATE_FORMAT.format(selections.get(0).getDateTime()) + ".gif";
        Path to = dir.resolve(fname);
        List<byte[]> bytes = selections.stream()
                .map(ImageData::getImage)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(to))) {
            ScreenCapture.createAnimetedGIF(out, bytes, delay);
        }
    }

    /**
     * JPEGファイルとして保存します
     *
     * @param selections 画像ファイル
     * @param dir ディレクトリ
     * @throws IOException 入出力例外
     */
    private void saveJpeg(List<ImageData> selections, Path dir) throws IOException {
        if (this.tile.isSelected()) {
            // 並べる場合
            int column = Math.max(Integer.parseInt(this.tileCount.getText()), 1);
            Path to = dir.resolve(DATE_FORMAT.format(selections.get(0).getDateTime()) + ".jpg");
            List<byte[]> bytes = selections.stream()
                    .map(ImageData::getImage)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            try (OutputStream out = Files.newOutputStream(to)) {
                out.write(ScreenCapture.encodeJpeg(ScreenCapture.tileImage(bytes, column)));
            }
        } else {
            // 並べない場合個別にファイル保存する
            for (ImageData image : selections) {
                byte[] data = image.getImage();
                if (data != null) {
                    Path to = dir.resolve(DATE_FORMAT.format(image.getDateTime()) + ".jpg");
                    try (OutputStream out = Files.newOutputStream(to)) {
                        out.write(data);
                    }
                }
            }
        }
    }

    void setItems(ObservableList<ImageData> images) {
        this.images = images;
        this.list.setItems(this.images);
        this.list.getCheckModel()
                .checkAll();
        this.list.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::viewImage);
        this.getWindow().setOnCloseRequest(this::onclose);
    }

    /**
     * キャプチャプレビュー
     *
     * @param image 画像データ
     */
    private void viewImage(ObservableValue<? extends ImageData> observable, ImageData oldValue, ImageData value) {
        if (value != null) {
            byte[] data = value.getImage();
            if (data != null) {
                Image img = new Image(new ByteArrayInputStream(data));
                this.image.setImage(img);
            } else {
                this.image.setImage(null);
            }
        }
    }

    /**
     * ウインドウを閉じる時のアクション
     *
     * @param event WindowEvent
     */
    private void onclose(WindowEvent event) {
        this.list.setItems(FXCollections.observableArrayList());
    }
}
