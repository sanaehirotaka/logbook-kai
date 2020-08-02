package logbook.update;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipFile;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javax.json.JsonObject;

import logbook.internal.ThreadManager;

public class ProgressController {

    private Stage stage;

    private JsonObject asset;

    @FXML
    private ListView<String> progress;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAsset(JsonObject asset) {
        this.asset = asset;
        Task<Void> task = this.downloadAsset();
        task.messageProperty().addListener((ob, o, n) -> {
            this.progress.getItems().add(n);
        });
        task.setOnFailed(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(this.stage);
            alert.setTitle("更新が出来ませんでした");
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();

            // this.failed() // 中断シーン
        });
        task.setOnSucceeded(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(this.stage);
            alert.setTitle("完了しました");
            alert.setContentText("更新が完了しました");
            alert.showAndWait();

            // this.succeeded() // 成功シーン
        });
        ThreadManager.getExecutorService().execute(task);
    }

    @FXML
    void finish(ActionEvent e) {
        return;
    }

    private Task<Void> downloadAsset() {
        String DOWNLOAD_URL = this.asset.getString("browser_download_url", "");
        String ASSET_NAME = this.asset.getString("name", "");
        Integer ASSET_SIZE = this.asset.getInt("size", 0);
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (DOWNLOAD_URL == "") {
                    throw new Exception("ダウンロードURLが不明です");
                }
                Path tempDir = Files.createTempDirectory("logbook-kai");
                Path tempZip = tempDir.resolve(ASSET_NAME);
                Integer retry = 0;
                do {
                    if (retry >= 3)
                        throw new Exception("リトライ回数が上限に達しました");
                    if (retry > 0)
                        this.updateMessage("ファイルサイズが一致しませんでした。再ダウンロードします(最大3回)");

                    this.updateMessage(DOWNLOAD_URL + " をダウンロードしています");
                    InputStream is = URI.create(DOWNLOAD_URL).toURL().openStream();
                    try {
                        Files.copy(is, tempZip, StandardCopyOption.REPLACE_EXISTING);
                    } finally {
                        is.close();
                    }

                    retry++;
                } while (Files.size(tempZip) != ASSET_SIZE);

                this.updateMessage("ファイルを更新しています");

                ZipFile zipFile = new ZipFile(tempZip.toFile());
                try {
                    zipFile.stream()
                        .filter(e -> !e.isDirectory())
                        .filter(e -> e.getName().endsWith(".jar"))
                        .forEach(e -> {
                            Path to = Paths.get(System.getProperty("install_target"), e.getName());
                            try {
                                if (to.getParent() != null && !Files.exists(to.getParent())) {
                                    Files.createDirectories(to.getParent());
                                }
                                this.updateMessage("[更新]" + to.toAbsolutePath());
                                Files.copy(zipFile.getInputStream(e), to, StandardCopyOption.REPLACE_EXISTING);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        });
                } finally {
                    zipFile.close();
                    this.updateMessage("一時ファイルを削除しています");
                    Files.deleteIfExists(tempZip);
                    Files.deleteIfExists(tempDir);
                }

                return null;
            }
        };

        return task;
    }
}