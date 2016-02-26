package logbook.internal.gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFx エントリ・ポイント クラス
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Stage myStage = InternalFXMLLoader.load("logbook/gui/main.fxml").load(); //$NON-NLS-1$
        myStage.show();
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
