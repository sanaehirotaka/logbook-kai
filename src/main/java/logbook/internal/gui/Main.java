package logbook.internal.gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logbook.internal.PluginContainer;

/**
 * JavaFx エントリ・ポイント クラス
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        URL url = PluginContainer.getInstance().getClassLoader()
                .getResource("logbook/gui/main.fxml"); //$NON-NLS-1$

        FXMLLoader loader = new FXMLLoader(url);
        Pane myPane = loader.load();

        Scene myScene = new Scene(myPane);
        stage.setScene(myScene);
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
