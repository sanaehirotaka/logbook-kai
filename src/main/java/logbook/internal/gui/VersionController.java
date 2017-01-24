package logbook.internal.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import logbook.internal.Version;
import logbook.plugin.PluginContainer;

/**
 * バージョン情報
 *
 */
public class VersionController extends WindowController {

    @FXML
    private Label appName;

    @FXML
    private Label appVersion;

    @FXML
    private Label appName2;

    @FXML
    private TextArea licensetext;

    private Point2D start;

    @FXML
    void initialize() {
        try {
            this.appName.setText(Optional.ofNullable(this.getClass().getPackage())
                    .map(Package::getImplementationTitle)
                    .orElse(""));
            this.appVersion.setText(Version.getCurrent().toString());
            this.appName2.setText(Optional.ofNullable(this.getClass().getPackage())
                    .map(Package::getImplementationTitle)
                    .orElse(""));
            try (InputStream in = PluginContainer.getInstance()
                    .getClassLoader()
                    .getResourceAsStream("LICENSE")) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    this.licensetext.setText(reader.lines()
                            .collect(Collectors.joining("\n")));
                }
            }
        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void close(ActionEvent event) {
        this.getWindow().close();
    }

    @FXML
    void visibleDownloadSite(ActionEvent event) {
        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://github.com/sanaehirotaka/logbook-kai/releases"));
        } catch (Exception e) {
            LoggerHolder.LOG.warn("ブラウザの起動で例外", e);
        }
    }

    @FXML
    void visibleIssue(ActionEvent event) {
        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://github.com/sanaehirotaka/logbook-kai/issues"));
        } catch (Exception e) {
            LoggerHolder.LOG.warn("ブラウザの起動で例外", e);
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);

        // ドラッグの開始
        window.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            this.start = new Point2D(window.getX() - e.getScreenX(), window.getY() - e.getScreenY());
        });
        // ドラッグ中
        window.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            window.setX(e.getScreenX() + this.start.getX());
            window.setY(e.getScreenY() + this.start.getY());
        });
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(VersionController.class);
    }
}
