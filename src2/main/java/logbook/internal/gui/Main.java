package logbook.internal.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import logbook.bean.AppConfig;
import logbook.internal.Version;

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
        // アイコンの設定
        InternalFXMLLoader.setIcon(stage);
        // 最前面に表示する
        stage.setAlwaysOnTop(AppConfig.get().isOnTop());

        stage.setTitle("航海日誌 " + Version.getCurrent());

        stage.setOnCloseRequest(e -> {
            if (AppConfig.get().isCheckDoit()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(stage);
                alert.setTitle("終了の確認");
                alert.setHeaderText("終了の確認");
                alert.setContentText("航海日誌を終了しますか？");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                alert.showAndWait()
                        .filter(ButtonType.NO::equals)
                        .ifPresent(t -> e.consume());
            }
            if (!e.isConsumed()) {
                AppConfig.get()
                        .getWindowLocationMap()
                        .put(controller.getClass().getCanonicalName(), controller.getWindowLocation());
            }
        });
        InternalFXMLLoader.defaultOpenAction(controller);

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
