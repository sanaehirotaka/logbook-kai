package logbook.update;

import java.text.MessageFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class FailedController {

    private Stage stage;

    @FXML
    private Label message;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMessage(String message) {
        this.message.setText(MessageFormat.format(this.message.getText(), message));
    }

    @FXML
    void close(ActionEvent e) {
        this.stage.close();
    }
}