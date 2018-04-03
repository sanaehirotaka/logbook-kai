package logbook.internal.gui;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import logbook.bean.AppConfig;
import logbook.internal.Config;
import logbook.internal.LoggerHolder;
import logbook.internal.ShipImageCacheStrategy;
import logbook.internal.ThreadManager;
import logbook.internal.ToStringConverter;
import logbook.plugin.PluginContainer;
import logbook.plugin.PluginServices;

/**
 * 設定コントローラー
 *
 */
public class ConfigController extends WindowController {

    @FXML
    private ToggleGroup windowStyle;

    /** メイン画面のスタイル-スマート */
    @FXML
    private RadioButton windowStyleSmart;

    /** メイン画面のスタイル-ワイド */
    @FXML
    private RadioButton windowStyleWide;

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

    /** 遠征完了時のリマインド(秒) */
    @FXML
    private TextField remind;

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

    /** 画像拡大縮小割合(%) */
    @FXML
    private TextField imageZoomRate;

    /** 艦隊タブに艦隊単位のタブを追加 */
    @FXML
    private CheckBox deckTabs;

    /** 艦隊タブにラベル単位のタブを追加 */
    @FXML
    private CheckBox labelTabs;

    /** 最前面に表示する */
    @FXML
    private CheckBox onTop;

    /** 終了時に確認する */
    @FXML
    private CheckBox checkDoit;

    /** 起動時にアップデートチェック */
    @FXML
    private CheckBox checkUpdate;

    /** 報告書の保存先 */
    @FXML
    private TextField reportDir;

    @FXML
    private ToggleGroup shipImage;

    /** 艦娘画像キャッシュ設定-全て */
    @FXML
    private RadioButton shipImageCacheStrategyAll;

    /** 艦娘画像キャッシュ設定-使用される画像のみ */
    @FXML
    private RadioButton shipImageCacheStrategyUsed;

    /** 艦娘画像キャッシュ設定-制限 */
    @FXML
    private RadioButton shipImageCacheStrategyLimit;

    /** 画像ファイルを再圧縮 */
    @FXML
    private CheckBox shipImageCompress;

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

    /** FFmpeg 実行ファイル */
    @FXML
    private TextField ffmpegPath;

    /** FFmpeg 引数テンプレート */
    @FXML
    private ChoiceBox<Map<?, ?>> ffmpegTemplate;

    /** FFmpeg 引数 */
    @FXML
    private TextArea ffmpegArgs;

    /** FFmpeg 拡張子 */
    @FXML
    private TextField ffmpegExt;

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
        this.windowStyleSmart.setSelected("main".equals(conf.getWindowStyle()));
        this.windowStyleWide.setSelected("main_wide".equals(conf.getWindowStyle()));
        this.useNotification.setSelected(conf.isUseNotification());
        this.useSound.setSelected(conf.isUseSound());
        this.useToast.setSelected(conf.isUseToast());
        this.useRemind.setSelected(conf.isUseRemind());
        this.remind.setText(Integer.toString(conf.getRemind()));
        this.soundLevel.setText(Integer.toString(conf.getSoundLevel()));
        this.materialLogInterval.setText(Integer.toString(conf.getMaterialLogInterval()));
        this.applyBattle.setSelected(conf.isApplyBattle());
        this.applyResult.setSelected(conf.isApplyResult());
        this.shipFullyThreshold.setText(Integer.toString(conf.getShipFullyThreshold()));
        this.itemFullyThreshold.setText(Integer.toString(conf.getItemFullyThreshold()));
        this.imageZoomRate.setText(Integer.toString(conf.getImageZoomRate()));
        this.deckTabs.setSelected(conf.isDeckTabs());
        this.labelTabs.setSelected(conf.isLabelTabs());
        this.onTop.setSelected(conf.isOnTop());
        this.checkDoit.setSelected(conf.isCheckDoit());
        this.checkUpdate.setSelected(conf.isCheckUpdate());
        this.reportDir.setText(conf.getReportPath());
        ShipImageCacheStrategy shipImageCacheStrategy = conf.getShipImageCacheStrategy();
        if (shipImageCacheStrategy != null) {
            switch (shipImageCacheStrategy) {
            case ALL:
                this.shipImageCacheStrategyAll.setSelected(true);
                break;
            case USED:
                this.shipImageCacheStrategyUsed.setSelected(true);
                break;
            case LIMIT:
                this.shipImageCacheStrategyLimit.setSelected(true);
                break;
            }
        } else {
            this.shipImageCacheStrategyAll.setSelected(true);
        }
        this.shipImageCompress.setSelected(conf.isShipImageCompress());
        this.connectionClose.setSelected(conf.isConnectionClose());
        this.listenPort.setText(Integer.toString(conf.getListenPort()));
        this.allowOnlyFromLocalhost.setSelected(conf.isAllowOnlyFromLocalhost());
        this.useProxy.setSelected(conf.isUseProxy());
        this.proxyPort.setText(Integer.toString(conf.getProxyPort()));
        this.proxyHost.setText(conf.getProxyHost());
        this.ffmpegPath.setText(conf.getFfmpegPath());
        this.setFFmpegTemplate();
        this.ffmpegArgs.setText(conf.getFfmpegArgs());
        this.ffmpegExt.setText(conf.getFfmpegExt());
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
        String windowStyle = "main";
        if (this.windowStyleSmart.isSelected())
            windowStyle = "main";
        if (this.windowStyleWide.isSelected())
            windowStyle = "main_wide";
        conf.setWindowStyle(windowStyle);
        conf.setUseNotification(this.useNotification.isSelected());
        conf.setUseSound(this.useSound.isSelected());
        conf.setUseToast(this.useToast.isSelected());
        conf.setUseRemind(this.useRemind.isSelected());
        conf.setRemind(Math.max(Integer.parseInt(this.remind.getText()), 10));
        conf.setSoundLevel(Integer.parseInt(this.soundLevel.getText()));
        conf.setMaterialLogInterval(Integer.parseInt(this.materialLogInterval.getText()));
        conf.setApplyBattle(this.applyBattle.isSelected());
        conf.setApplyResult(this.applyResult.isSelected());
        conf.setShipFullyThreshold(Integer.parseInt(this.shipFullyThreshold.getText()));
        conf.setItemFullyThreshold(Integer.parseInt(this.itemFullyThreshold.getText()));
        conf.setImageZoomRate(Integer.parseInt(this.imageZoomRate.getText()));
        conf.setDeckTabs(this.deckTabs.isSelected());
        conf.setLabelTabs(this.labelTabs.isSelected());
        conf.setOnTop(this.onTop.isSelected());
        conf.setCheckDoit(this.checkDoit.isSelected());
        conf.setCheckUpdate(this.checkUpdate.isSelected());
        conf.setReportPath(this.reportDir.getText());
        ShipImageCacheStrategy shipImageCacheStrategy = ShipImageCacheStrategy.ALL;
        if (this.shipImageCacheStrategyAll.isSelected())
            shipImageCacheStrategy = ShipImageCacheStrategy.ALL;
        if (this.shipImageCacheStrategyUsed.isSelected())
            shipImageCacheStrategy = ShipImageCacheStrategy.USED;
        if (this.shipImageCacheStrategyLimit.isSelected())
            shipImageCacheStrategy = ShipImageCacheStrategy.LIMIT;
        conf.setShipImageCacheStrategy(shipImageCacheStrategy);
        conf.setShipImageCompress(this.shipImageCompress.isSelected());
        conf.setConnectionClose(this.connectionClose.isSelected());
        conf.setListenPort(Integer.parseInt(this.listenPort.getText()));
        conf.setAllowOnlyFromLocalhost(this.allowOnlyFromLocalhost.isSelected());
        conf.setUseProxy(this.useProxy.isSelected());
        conf.setProxyPort(Integer.parseInt(this.proxyPort.getText()));
        conf.setProxyHost(this.proxyHost.getText());
        conf.setFfmpegPath(this.ffmpegPath.getText());
        conf.setFfmpegArgs(this.ffmpegArgs.getText());
        conf.setFfmpegExt(this.ffmpegExt.getText());
        conf.setUsePlugin(this.usePlugin.isSelected());

        ThreadManager.getExecutorService()
                .execute(Config.getDefault()::store);
        this.getWindow().close();
    }

    @FXML
    void selectReportDir(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("報告書の保存先");
        dc.setInitialDirectory(new File(this.reportDir.getText()));
        Optional.ofNullable(dc.showDialog(this.getWindow()))
                .filter(File::exists)
                .map(File::getAbsolutePath)
                .ifPresent(this.reportDir::setText);
    }

    @FXML
    void selectFFmpegPath(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("FFmpegの選択");
        Optional.ofNullable(fc.showOpenDialog(this.getWindow()))
                .filter(File::exists)
                .map(File::getAbsolutePath)
                .ifPresent(this.ffmpegPath::setText);
    }

    private void setFFmpegTemplate() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<?> list;
            try (InputStream is = PluginServices.getResourceAsStream("logbook/capture_options/list.json")) {
                list = mapper.readValue(is, List.class);
            }
            for (Object path : list) {
                Map<?, ?> option;
                try (InputStream is = PluginServices.getResourceAsStream(path.toString())) {
                    option = mapper.readValue(is, Map.class);
                }
                this.ffmpegTemplate.getItems().add(option);
            }
            this.ffmpegTemplate.setConverter(ToStringConverter.of(map -> map.get("name").toString()));
            this.ffmpegTemplate.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
                if (nv != null) {
                    this.ffmpegArgs.setText(nv.get("params").toString());
                    this.ffmpegExt.setText(nv.get("ext").toString());
                }
            });
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }
}
