package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    static void show(String url, String title, WindowController parent) {
        show(url, title, parent, null);
    }

    static void show(String url, String title, WindowController parent, Consumer<Stage> apply) {
        try {
            FXMLLoader loader = load(url);
            Stage stage = new Stage();
            Parent root = loader.load();
            stage.setScene(new Scene(root));

            WindowController controller = loader.getController();
            controller.setWindow(stage);

            stage.initOwner(parent.getWindow());
            stage.getIcons().clear();
            stage.getIcons().addAll(parent.getWindow().getIcons());
            stage.setTitle(title);
            if (apply != null) {
                apply.accept(stage);
            }
            stage.show();
        } catch (Exception ex) {
            LoggerHolder.LOG.error(url + " の初期化に失敗しました", ex);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(InternalFXMLLoader.class);
    }
}
