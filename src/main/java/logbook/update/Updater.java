package logbook.update;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Updater extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("航海日誌の更新");

        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("logbook/update/updater.fxml"));
        stage.setScene(new Scene(loader.load()));

        UpdaterController controller = loader.getController();
        controller.setStage(stage);
        controller.setFailed(new FXMLLoader(this.getClass().getClassLoader().getResource("logbook/update/failed.fxml")));

        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}