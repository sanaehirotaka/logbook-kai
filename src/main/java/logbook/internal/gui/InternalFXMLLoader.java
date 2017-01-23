package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * @param controllerFunction コントローラーを操作するConsumer
     * @param windowFunction ウインドウを操作するConsumer
     * @throws IOException 入出力例外が発生した場合
     */
    static void showWindow(String name, Stage parent, String title, Consumer<WindowController> controllerFunction,
            Consumer<Stage> windowFunction) throws IOException {
        showWindow(name, parent, title, null, controllerFunction, windowFunction);
    }

    /**
     * ウインドウを開く
     *
     * @param name リソース
     * @param parent 親ウインドウ
     * @param title ウインドウタイトル
     * @param sceneFunction シーン・グラフを操作するFunction
     * @param controllerFunction コントローラーを操作するConsumer
     * @param windowFunction ウインドウを操作するConsumer
     * @throws IOException 入出力例外が発生した場合
     */
    static void showWindow(String name, Stage parent, String title, Function<Parent, Scene> sceneFunction,
            Consumer<WindowController> controllerConsumer,
            Consumer<Stage> windowConsumer) throws IOException {

        FXMLLoader loader = load(name);
        Stage stage = new Stage();
        Parent root = loader.load();
        if (sceneFunction != null) {
            stage.setScene(sceneFunction.apply(root));
        } else {
            stage.setScene(new Scene(root));
        }

        WindowController controller = loader.getController();
        controller.setWindow(stage);

        if (windowConsumer != null) {
            windowConsumer.accept(stage);
        }
        if (controllerConsumer != null) {
            controllerConsumer.accept(controller);
        }

        stage.initOwner(parent);
        stage.setTitle(title);
        Tools.Windows.setIcon(stage);
        Tools.Windows.defaultCloseAction(controller);
        Tools.Windows.defaultOpenAction(controller);
        stage.show();
    }
}
