package logbook.internal.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * 設定コントローラー
 *
 */
public class ConfigController {

    @FXML
    private CheckBox useNotification;

    @FXML
    private CheckBox useSound;

    @FXML
    private CheckBox useToast;

    @FXML
    private CheckBox useRemind;

    @FXML
    private TextField soundLevel;

    @FXML
    private TextField materialLogInterval;

    @FXML
    private CheckBox onTop;

    @FXML
    private CheckBox checkDoit;

    @FXML
    private CheckBox checkUpdate;

    @FXML
    private CheckBox connectionClose;

    @FXML
    private TextField listenPort;

    @FXML
    private CheckBox allowOnlyFromLocalhost;

    @FXML
    private CheckBox useProxy;

    @FXML
    private TextField proxyPort;

    @FXML
    private TextField proxyHost;

    @FXML
    private CheckBox usePlugin;

    @FXML
    private TableView<?> pluginTable;

    @FXML
    private TableColumn<?, ?> pluginName;

    @FXML
    private TableColumn<?, ?> pluginVendor;

    @FXML
    private TableColumn<?, ?> pluginVersion;

    @FXML
    private TableColumn<?, ?> pluginLicense;

    @FXML
    private TableColumn<?, ?> pluginLocation;

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void ok(ActionEvent event) {

    }

}
