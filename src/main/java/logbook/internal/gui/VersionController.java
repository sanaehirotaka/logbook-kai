package logbook.internal.gui;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Skinnable;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logbook.internal.CheckUpdate;
import logbook.internal.LoggerHolder;
import logbook.internal.ThreadManager;
import logbook.internal.Version;
import logbook.plugin.PluginServices;

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
            try (InputStream in = PluginServices.getResourceAsStream("LICENSE")) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                    this.licensetext.setText(reader.lines()
                            .collect(Collectors.joining("\n")));
                }
            }
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void close(ActionEvent event) {
        this.getWindow().close();
    }

    @FXML
    void visibleDownloadSite(ActionEvent event) {
        try {
            ThreadManager.getExecutorService()
                    .submit(() -> {
                        Desktop.getDesktop()
                                .browse(URI.create("https://github.com/" + CheckUpdate.REPOSITORY_PATH + "/releases"));
                        return null;
                    });
        } catch (Exception e) {
            LoggerHolder.get().warn("ブラウザの起動で例外", e);
        }
    }

    @FXML
    void visibleIssue(ActionEvent event) {
        try {
            ThreadManager.getExecutorService()
                    .submit(() -> {
                        Desktop.getDesktop()
                                .browse(URI.create("https://github.com/" + CheckUpdate.REPOSITORY_PATH + "/issues"));
                        return null;
                    });
        } catch (Exception e) {
            LoggerHolder.get().warn("ブラウザの起動で例外", e);
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);

        // ドラッグの開始
        window.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getTarget() instanceof Pane) {
                Parent parent = ((Pane) e.getTarget()).getParent();
                if (parent == null || !(parent instanceof Skinnable)) {
                    this.start = new Point2D(window.getX() - e.getScreenX(), window.getY() - e.getScreenY());
                    e.consume();
                }
            }
        });
        // ドラッグ中
        window.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.getTarget() instanceof Pane) {
                Parent parent = ((Pane) e.getTarget()).getParent();
                if (parent == null || !(parent instanceof Skinnable)) {
                    window.setX(e.getScreenX() + this.start.getX());
                    window.setY(e.getScreenY() + this.start.getY());
                    e.consume();
                }
            }
        });
    }
}
