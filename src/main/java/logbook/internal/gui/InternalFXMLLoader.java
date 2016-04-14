package logbook.internal.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logbook.bean.AppConfig;
import logbook.bean.WindowLocation;
import logbook.plugin.PluginContainer;

final class InternalFXMLLoader {

    static FXMLLoader load(String name) throws IOException {
        URL url = PluginContainer.getInstance().getClassLoader().getResource(name);
        return load(url);
    }

    static FXMLLoader load(URL url) throws IOException {
        FXMLLoader loader = new FXMLLoader(url);
        return loader;
    }

    /**
     * ウインドウを開く
     *
     * @param name リソース
     * @param parent 親ウインドウ
     * @param title ウインドウタイトル
     * @throws IOException 入出力例外が発生した場合
     */
    static void showWindow(String name, Stage parent, String title) throws IOException {
        showWindow(name, parent, title, null, null);
    }

    /**
     * ウインドウを開く
     *
     * @param name リソース
     * @param parent 親ウインドウ
     * @param title ウインドウタイトル
     * @param controllerFunction コントローラーを操作するFunction
     * @param windowFunction ウインドウを操作するFunction
     * @throws IOException 入出力例外が発生した場合
     */
    static void showWindow(String name, Stage parent, String title, Consumer<WindowController> controllerFunction,
            Consumer<Stage> windowFunction) throws IOException {

        FXMLLoader loader = load(name);
        Stage stage = new Stage();
        Parent root = loader.load();
        stage.setScene(new Scene(root));

        WindowController controller = loader.getController();
        controller.setWindow(stage);

        stage.initOwner(parent);
        stage.setTitle(title);
        setIcon(stage);
        defaultCloseAction(controller);
        defaultOpenAction(controller);

        if (controllerFunction != null) {
            controllerFunction.accept(controller);
        }
        if (windowFunction != null) {
            windowFunction.accept(stage);
        }
        stage.show();
    }

    /**
     * ウインドウの設定
     * @param stage Stage
     * @throws IOException 入出力例外が発生した場合
     */
    static void setIcon(Stage stage) throws IOException {
        // アイコン
        String[] uris = { "logbook/gui/icon_256x256.png", "logbook/gui/icon_128x128.png", "logbook/gui/icon_64x64.png",
                "logbook/gui/icon_32x32.png" };

        for (String uri : uris) {
            try (InputStream is = PluginContainer.getInstance()
                    .getClassLoader()
                    .getResourceAsStream(uri)) {
                stage.getIcons().add(new Image(is));
            }
        }
    }

    static void defaultCloseAction(WindowController controller) {
        if (controller.getWindow() != null) {
            EventHandler<WindowEvent> action = e -> {
                String key = controller.getClass().getCanonicalName();
                AppConfig.get()
                        .getWindowLocationMap()
                        .put(key, controller.getWindowLocation());
            };
            controller.getWindow()
                    .setOnCloseRequest(action);
        }
    }

    static void defaultOpenAction(WindowController controller) {
        String key = controller.getClass().getCanonicalName();
        WindowLocation location = AppConfig.get()
                .getWindowLocationMap()
                .get(key);
        controller.setWindowLocation(location);
    }
}
