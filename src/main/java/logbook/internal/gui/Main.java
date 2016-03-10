package logbook.internal.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logbook.bean.AppConfig;

/**
 * JavaFx エントリ・ポイント クラス
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/main.fxml"); //$NON-NLS-1$
        Parent root = loader.load();
        stage.setScene(new Scene(root));

        WindowController controller = loader.getController();
        controller.setWindow(stage);

        // 最前面に表示する
        stage.setAlwaysOnTop(AppConfig.get().isOnTop());

        stage.setTitle("航海日誌");
        stage.show();
    }

    /**
     * JavaFx アプリケーションの起動を行う
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // Bitmapped font should not be sub-pixel rendered.
        System.setProperty("prism.lcdtext", "false"); //$NON-NLS-1$ //$NON-NLS-2$
        launch(args);
    }
}
