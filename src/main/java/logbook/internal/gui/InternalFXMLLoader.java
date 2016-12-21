package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
        Tools.Windows.setIcon(stage);
        Tools.Windows.defaultCloseAction(controller);
        Tools.Windows.defaultOpenAction(controller);

        if (controllerFunction != null) {
            controllerFunction.accept(controller);
        }
        if (windowFunction != null) {
            windowFunction.accept(stage);
        }
        stage.show();
    }
}
