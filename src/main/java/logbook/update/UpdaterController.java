package logbook.update;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        ChangeListener<State> listener = (ObservableValue<? extends State> ov, State oldState, State newState) -> {
            if (newState == State.SUCCEEDED) {
                this.updateReleaseNote();
            }
        };
        webEngine.getLoadWorker().stateProperty().addListener(listener);
        try {
            this.fetchReleaseNote();
        } catch(Exception e) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.initOwner(this.stage);
            alert.setTitle("更新情報を取得できませんでした");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

            return;
            // this.failed() // 中断シーン
        }
        this.updateReleaseNote();
    }

    @FXML
    void hadQuit(ActionEvent e) {
        if (e.getTarget() instanceof CheckBox) {
            Boolean isSelected = ((CheckBox) e.getTarget()).isSelected();
            this.updateButton.setDisable(!isSelected);
        }
    }

    @FXML
    void update(ActionEvent e) {
        return;
    }

    private void updateReleaseNote() {
        WebEngine webEngine = this.releaseNote.getEngine();
        if (this.release == null || this.release.getString("body", null) == null || webEngine.executeScript("window.marked") == null) {
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

    private void fetchReleaseNote() throws Exception {
        String releaseURL = RELEASES_API + this.version.getText();

        try (JsonReader r = Json.createReader(new BufferedInputStream(URI.create(releaseURL).toURL().openStream()))) {
            this.release = r.readObject();
        }

        if (this.release.getJsonArray("assets") == null)
            throw new Exception("リリース情報はありません。");

        if ((!Boolean.parseBoolean(System.getProperty("use_prerelease")) && this.release.getBoolean("prerelease", false)) || this.release.getBoolean("draft", false))
            throw new Exception(this.release.getString("name", "unknown") + "は試験的なバージョンであるため更新されませんでした。手動での更新をお願いします。");

        this.asset = this.release.getJsonArray("assets").stream()
                .map(val -> val.asJsonObject())
                .filter(val -> {
                    String name = val.getString("name");
                    String prefix = "1.8".equals(System.getProperty("java.specification.version")) ? "logbook-kai_" : "logbook-kai-java11_";
                    return name.startsWith(prefix) && name.endsWith(".zip");
                })
                .findFirst()
                .orElseThrow(() -> new Exception("リリース情報はありません。"));
    }
}