package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
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
}
