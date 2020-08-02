package logbook.update;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.net.URI;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import logbook.internal.Tuple;
import logbook.internal.Tuple.Pair;
import logbook.internal.ThreadManager;

public class UpdaterController {

    private Stage stage;

    private JsonObject release;

    private JsonObject asset;

    /** GitHub Releases API */
    private static final String RELEASES_API = "https://api.github.com/repos/sanaehirotaka/logbook-kai/releases/tags/v";

    @FXML
    private Text version;

    @FXML
    private WebView releaseNote;

    @FXML
    private Button updateButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void initialize() {
        this.version.setText(System.getProperty("install_version"));
        WebEngine webEngine = this.releaseNote.getEngine();
        String releaseNoteFrame = this.getClass().getClassLoader().getResource("logbook/update/release-note.html").toExternalForm();
        webEngine.load(releaseNoteFrame);
        webEngine.getLoadWorker().stateProperty().addListener((ob, o, n) -> {
            if (n == State.SUCCEEDED) {
                this.updateReleaseNote();
            }
        });
        Task<Pair<JsonObject, JsonObject>> task = this.fetchReleaseNote();
        task.setOnFailed((WorkerStateEvent e) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(this.stage);
            alert.setTitle("更新情報を取得できませんでした");
            alert.setContentText(task.getException().getMessage());
            alert.showAndWait();

            // this.failed() // 中断シーン
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            Pair<JsonObject, JsonObject> result = task.getValue();
            this.release = result.get1();
            this.asset = result.get2();
            this.updateReleaseNote();
        });
        ThreadManager.getExecutorService().execute(task);
    }

    @FXML
    void hadQuit(ActionEvent e) {
        if (e.getTarget() instanceof CheckBox) {
            Boolean isSelected = ((CheckBox) e.getTarget()).isSelected();
            this.updateButton.setDisable(!isSelected);
        }
    }

    @FXML
    void update(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("logbook/update/progress.fxml"));
        this.stage.setScene(new Scene(loader.load()));
        ProgressController controller = loader.getController();
        controller.setStage(this.stage);
        controller.setAsset(this.asset);
        return;
    }

    private void updateReleaseNote() {
        WebEngine webEngine = this.releaseNote.getEngine();
        if (webEngine.executeScript("window") == null
                || webEngine.executeScript("window.marked") == null
                || webEngine.executeScript("document.body") == null
                || this.release == null
                || this.release.getString("body", "") == "") {
            return;
        }

        JSObject body = (JSObject) webEngine.executeScript("document.body");
        JSObject window = (JSObject) webEngine.executeScript("window");
        body.setMember("innerHTML", window.call("marked", this.release.getString("body")));

        EventListener linkListener = (Event ev) -> {
            String openURL = ((Element) ev.getTarget()).getAttribute("href");
            try {
                Desktop.getDesktop().browse(URI.create(openURL));
            } catch (Exception e) {
            }
            ev.preventDefault();
        };

        JSObject nodes = (JSObject) webEngine.executeScript("document.getElementsByTagName('a')");
        for (int i = 0; i < ((Integer) nodes.getMember("length")); i++) {
            ((EventTarget) nodes.getSlot(i)).addEventListener("click", linkListener, false);
        }
    }

    private Task<Pair<JsonObject, JsonObject>> fetchReleaseNote() {
        Task<Pair<JsonObject, JsonObject>> task = new Task<>() {
            @Override
            protected Pair<JsonObject, JsonObject> call() throws Exception {
                String releaseURL = RELEASES_API + System.getProperty("install_version");

                JsonObject release;
                try (JsonReader r = Json.createReader(new BufferedInputStream(URI.create(releaseURL).toURL().openStream()))) {
                    release = r.readObject();
                }

                if (release.getJsonArray("assets") == null)
                    throw new Exception("リリース情報はありません。");

                if ((!Boolean.parseBoolean(System.getProperty("use_prerelease")) && release.getBoolean("prerelease", false)) || release.getBoolean("draft", false))
                    throw new Exception(release.getString("name", "unknown") + "は試験的なバージョンであるため更新されませんでした。手動での更新をお願いします。");

                JsonObject asset = release.getJsonArray("assets").stream()
                    .map(val -> val.asJsonObject())
                    .filter(val -> {
                        String name = val.getString("name");
                        String prefix = "1.8".equals(System.getProperty("java.specification.version")) ? "logbook-kai_" : "logbook-kai-java11_";
                        return name.startsWith(prefix) && name.endsWith(".zip");
                    })
                    .findFirst()
                    .orElseThrow(() -> new Exception("リリース情報はありません。"));

                return Tuple.of(release, asset);
            }
        };

        return task;
    }
}