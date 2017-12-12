package logbook.internal.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import logbook.bean.AppConfig;
import logbook.internal.Config;
import logbook.internal.ThreadManager;
import logbook.plugin.PluginContainer;

/**
 * 設定コントローラー
 *
 */
public class ConfigController extends WindowController {

    /** 遠征・入渠完了時に通知をする */
    @FXML
    private CheckBox useNotification;

    /** 通知でサウンドを鳴らす */
    @FXML
    private CheckBox useSound;

    /** 通知でトーストを表示 */
    @FXML
    private CheckBox useToast;

    /** 遠征完了時のリマインド */
    @FXML
    private CheckBox useRemind;

    /** 音量 */
    @FXML
    private TextField soundLevel;

    /** 資材ログ保存間隔 */
    @FXML
    private TextField materialLogInterval;

    /** 戦闘開始時に結果を反映 */
    @FXML
    private CheckBox applyBattle;

    /** 戦闘開始時に結果を反映 */
    @FXML
    private CheckBox applyResult;

    /** 母港枠 */
    @FXML
    private TextField shipFullyThreshold;

    /** 装備枠 */
    @FXML
    private TextField itemFullyThreshold;

    /** 最前面に表示する */
    @FXML
    private CheckBox onTop;

    /** 終了時に確認する */
    @FXML
    private CheckBox checkDoit;

    /** 起動時にアップデートチェック */
    @FXML
    private CheckBox checkUpdate;

    /** 通信エラーの抑止 */
    @FXML
    private CheckBox connectionClose;

    /** ポート番号 */
    @FXML
    private TextField listenPort;

    /** ローカルループバックアドレスからの接続のみ受け入れる */
    @FXML
    private CheckBox allowOnlyFromLocalhost;

    /** 接続にプロキシを使用する */
    @FXML
    private CheckBox useProxy;

    /** プロキシポート番号 */
    @FXML
    private TextField proxyPort;

    /** プロキシホスト */
    @FXML
    private TextField proxyHost;

    /** プラグインを有効にする */
    @FXML
    private CheckBox usePlugin;

    /** プラグイン一覧 */
    @FXML
    private TableView<DetailPlugin> pluginTable;

    /** 名称 */
    @FXML
    private TableColumn<DetailPlugin, String> pluginName;

    /** 作者 */
    @FXML
    private TableColumn<DetailPlugin, String> pluginVendor;

    /** バージョン */
    @FXML
    private TableColumn<DetailPlugin, String> pluginVersion;

    /** ライセンス */
    @FXML
    private TableColumn<DetailPlugin, String> pluginLicense;

    /** 場所 */
    @FXML
    private TableColumn<DetailPlugin, String> pluginLocation;

    private ObservableList<DetailPlugin> plugins = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        AppConfig conf = AppConfig.get();
        this.useNotification.setSelected(conf.isUseNotification());
        this.useSound.setSelected(conf.isUseSound());
        this.useToast.setSelected(conf.isUseToast());
        this.useRemind.setSelected(conf.isUseRemind());
        this.soundLevel.setText(Integer.toString(conf.getSoundLevel()));
        this.materialLogInterval.setText(Integer.toString(conf.getMaterialLogInterval()));
        this.applyBattle.setSelected(conf.isApplyBattle());
        this.applyResult.setSelected(conf.isApplyResult());
        this.shipFullyThreshold.setText(Integer.toString(conf.getShipFullyThreshold()));
        this.itemFullyThreshold.setText(Integer.toString(conf.getItemFullyThreshold()));
        this.onTop.setSelected(conf.isOnTop());
        this.checkDoit.setSelected(conf.isCheckDoit());
        this.checkUpdate.setSelected(conf.isCheckUpdate());
        this.connectionClose.setSelected(conf.isConnectionClose());
        this.listenPort.setText(Integer.toString(conf.getListenPort()));
        this.allowOnlyFromLocalhost.setSelected(conf.isAllowOnlyFromLocalhost());
        this.useProxy.setSelected(conf.isUseProxy());
        this.proxyPort.setText(Integer.toString(conf.getProxyPort()));
        this.proxyHost.setText(conf.getProxyHost());
        this.usePlugin.setSelected(conf.isUsePlugin());

        this.pluginName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.pluginVendor.setCellValueFactory(new PropertyValueFactory<>("vendor"));
        this.pluginVersion.setCellValueFactory(new PropertyValueFactory<>("version"));
        this.pluginLicense.setCellValueFactory(new PropertyValueFactory<>("license"));
        this.pluginLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        PluginContainer.getInstance()
                .getPlugins()
                .stream()
                .map(DetailPlugin::toDetailPlugin)
                .forEach(this.plugins::add);
        this.pluginTable.setItems(this.plugins);
    }

    /**
     * キャンセル
     *
     * @param event ActionEvent
     */
    @FXML
    void cancel(ActionEvent event) {
        this.getWindow().close();
    }

    /**
     * 設定の反映
     *
     * @param event ActionEvent
     */
    @FXML
    void ok(ActionEvent event) {
        AppConfig conf = AppConfig.get();
        conf.setUseNotification(this.useNotification.isSelected());
        conf.setUseSound(this.useSound.isSelected());
        conf.setUseToast(this.useToast.isSelected());
        conf.setUseRemind(this.useRemind.isSelected());
        conf.setSoundLevel(Integer.parseInt(this.soundLevel.getText()));
        conf.setMaterialLogInterval(Integer.parseInt(this.materialLogInterval.getText()));
        conf.setApplyBattle(this.applyBattle.isSelected());
        conf.setApplyResult(this.applyResult.isSelected());
        conf.setShipFullyThreshold(Integer.parseInt(this.shipFullyThreshold.getText()));
        conf.setItemFullyThreshold(Integer.parseInt(this.itemFullyThreshold.getText()));
        conf.setOnTop(this.onTop.isSelected());
        conf.setCheckDoit(this.checkDoit.isSelected());
        conf.setCheckUpdate(this.checkUpdate.isSelected());
        conf.setConnectionClose(this.connectionClose.isSelected());
        conf.setListenPort(Integer.parseInt(this.listenPort.getText()));
        conf.setAllowOnlyFromLocalhost(this.allowOnlyFromLocalhost.isSelected());
        conf.setUseProxy(this.useProxy.isSelected());
        conf.setProxyPort(Integer.parseInt(this.proxyPort.getText()));
        conf.setProxyHost(this.proxyHost.getText());
        conf.setUsePlugin(this.usePlugin.isSelected());

        ThreadManager.getExecutorService()
                .execute(Config.getDefault()::store);
        this.getWindow().close();
    }

}
