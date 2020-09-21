package logbook.update;

import java.text.MessageFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SucceededController {

    private Stage stage;

    @FXML
    private Label version;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void initialize() {
        this.version.setText(MessageFormat.format(this.version.getText(), System.getProperty("install_version")));
    }

    @FXML
    void close(ActionEvent e) {
        this.stage.close();
    }
}