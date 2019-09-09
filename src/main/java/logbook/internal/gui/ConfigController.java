package logbook.internal.gui;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import logbook.bean.AppBouyomiConfig;
import logbook.bean.AppBouyomiConfig.AppBouyomiText;
import logbook.bean.AppConfig;
import logbook.internal.BouyomiChanUtils;
import logbook.internal.BouyomiChanUtils.BouyomiDefaultSettings;
import logbook.internal.BouyomiChanUtils.BouyomiSetting;
import logbook.internal.BouyomiChanUtils.Params;
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

    /** 出撃時に大破艦がいる場合に通知をする */
    @FXML
    private CheckBox alertBadlyStart;

    /** 進撃時に大破艦がいる場合に通知をする */
    @FXML
    private CheckBox alertBadlyNext;

    /** 通知でサウンドを鳴らす */
    @FXML
    private CheckBox useSound;

    /** デフォルトサウンド */
    @FXML
    private TextField defaultNotifySound;

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

    /** 戦闘ログの保存期限 */
    @FXML
    private TextField battleLogExpires;

    /** 戦闘ログの保存期間無期限 */
    @FXML
    private CheckBox indefiniteExpires;

    /** 戦闘ログを圧縮する */
    @FXML
    private CheckBox compressBattleLogs;

    /** 戦闘ログにローデータを含める */
    @FXML
    private CheckBox includeRawData;

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

    /** 所有艦娘一覧から艦娘画像を隠す */
    @FXML
    private CheckBox hideShipImageFromShipTablePane;

    /** 所有艦娘一覧から装備画像を隠す */
    @FXML
    private CheckBox hideItemImageFromShipTablePane;

    /** 艦隊タブに旗艦の立ち絵を表示 */
    @FXML
    private CheckBox visiblePoseImageOnFleetTab;

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

    @FXML
    private CheckBox storeInternal;

    @FXML
    private CheckBox storeApiStart2;

    @FXML
    private TextField storeApiStart2Dir;

    @FXML
    private Button storeApiStart2DirRef;

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

    @FXML
    private CheckBox enableBouyomi;

    @FXML
    private TextField bouyomiHost;

    @FXML
    private TextField bouyomiPort;

    @FXML
    private GridPane bouyomiTexts;

    private ObservableList<DetailPlugin> plugins = FXCollections.observableArrayList();

    private EnumMap<BouyomiChanUtils.Type, Supplier<Boolean>> enableBouyomiTextMap = new EnumMap<>(
            BouyomiChanUtils.Type.class);

    private EnumMap<BouyomiChanUtils.Type, Supplier<String>> bouyomiTextMap = new EnumMap<>(
            BouyomiChanUtils.Type.class);

    @FXML
    void initialize() {
        AppConfig conf = AppConfig.get();
        this.windowStyleSmart.setSelected("main".equals(conf.getWindowStyle()));
        this.windowStyleWide.setSelected("main_wide".equals(conf.getWindowStyle()));
        this.useNotification.setSelected(conf.isUseNotification());
        this.alertBadlyStart.setSelected(conf.isAlertBadlyStart());
        this.alertBadlyNext.setSelected(conf.isAlertBadlyNext());
        this.useSound.setSelected(conf.isUseSound());
        this.defaultNotifySound.setText(conf.getDefaultNotifySound());
        this.useToast.setSelected(conf.isUseToast());
        this.useRemind.setSelected(conf.isUseRemind());
        this.remind.setText(Integer.toString(conf.getRemind()));
        this.soundLevel.setText(Integer.toString(conf.getSoundLevel()));
        this.materialLogInterval.setText(Integer.toString(conf.getMaterialLogInterval()));
        this.applyBattle.setSelected(conf.isApplyBattle());
        this.applyResult.setSelected(conf.isApplyResult());
        this.battleLogExpires.disableProperty().bind(this.indefiniteExpires.selectedProperty());
        this.battleLogExpires.setText(Integer.toString(conf.getBattleLogExpires()));
        this.indefiniteExpires.setSelected(conf.isIndefiniteExpires());
        this.compressBattleLogs.setSelected(conf.isCompressBattleLogs());
        this.includeRawData.setSelected(conf.isIncludeRawData());
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
        this.hideShipImageFromShipTablePane.setSelected(conf.isHideShipImageFromShipTablePane());
        this.hideItemImageFromShipTablePane.setSelected(conf.isHideItemImageFromShipTablePane());
        this.visiblePoseImageOnFleetTab.setSelected(conf.isVisiblePoseImageOnFleetTab());
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
        this.storeInternal.setSelected(conf.isStoreApiStart2());
        this.storeApiStart2.setSelected(conf.isStoreApiStart2());
        this.storeApiStart2Dir.setText(conf.getStoreApiStart2Dir());
        this.storeInternal.getOnAction().handle(new ActionEvent());

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

        this.bouyomiChanInit();
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
        conf.setAlertBadlyStart(this.alertBadlyStart.isSelected());
        conf.setAlertBadlyNext(this.alertBadlyNext.isSelected());
        conf.setUseSound(this.useSound.isSelected());
        conf.setDefaultNotifySound(this.defaultNotifySound.getText());
        conf.setUseToast(this.useToast.isSelected());
        conf.setUseRemind(this.useRemind.isSelected());
        conf.setRemind(Math.max(this.toInt(this.remind.getText()), 10));
        conf.setSoundLevel(this.toInt(this.soundLevel.getText()));
        conf.setMaterialLogInterval(this.toInt(this.materialLogInterval.getText()));
        conf.setApplyBattle(this.applyBattle.isSelected());
        conf.setApplyResult(this.applyResult.isSelected());
        conf.setBattleLogExpires(this.toInt(this.battleLogExpires.getText()));
        conf.setIndefiniteExpires(this.indefiniteExpires.isSelected());
        conf.setCompressBattleLogs(this.compressBattleLogs.isSelected());
        conf.setIncludeRawData(this.includeRawData.isSelected());
        conf.setShipFullyThreshold(this.toInt(this.shipFullyThreshold.getText()));
        conf.setItemFullyThreshold(this.toInt(this.itemFullyThreshold.getText()));
        conf.setImageZoomRate(this.toInt(this.imageZoomRate.getText()));
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
        conf.setHideShipImageFromShipTablePane(this.hideShipImageFromShipTablePane.isSelected());
        conf.setHideItemImageFromShipTablePane(this.hideItemImageFromShipTablePane.isSelected());
        conf.setVisiblePoseImageOnFleetTab(this.visiblePoseImageOnFleetTab.isSelected());
        conf.setConnectionClose(this.connectionClose.isSelected());
        conf.setListenPort(this.toInt(this.listenPort.getText()));
        conf.setAllowOnlyFromLocalhost(this.allowOnlyFromLocalhost.isSelected());
        conf.setUseProxy(this.useProxy.isSelected());
        conf.setProxyPort(this.toInt(this.proxyPort.getText()));
        conf.setProxyHost(this.proxyHost.getText());
        conf.setFfmpegPath(this.ffmpegPath.getText());
        conf.setFfmpegArgs(this.ffmpegArgs.getText());
        conf.setFfmpegExt(this.ffmpegExt.getText());
        conf.setUsePlugin(this.usePlugin.isSelected());
        conf.setStoreApiStart2(this.storeApiStart2.isSelected());
        conf.setStoreApiStart2Dir(this.storeApiStart2Dir.getText());

        this.bouyomiChanStore();

        ThreadManager.getExecutorService()
                .execute(Config.getDefault()::store);
        this.getWindow().close();
    }

    @FXML
    void selectSoundFile(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("サウンドファイルの選択");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("サウンドファイル",
                "*.aif", "*.aiff", "*.fxm", "*.flv", "*.m3u8",
                "*.m3u8", "*.mp3", "*.mp4", "*.m4a", "*.m4v", "*.wav"));
        String current = this.defaultNotifySound.getText();
        if (current != null && !current.isEmpty()) {
            Path path = Paths.get(current);
            Path parent = path.getParent();
            if (parent != null && Files.exists(parent)) {
                fc.setInitialDirectory(parent.toFile());
            }
        }
        Optional.ofNullable(fc.showOpenDialog(this.getWindow()))
                .filter(File::exists)
                .map(File::getAbsolutePath)
                .ifPresent(this.defaultNotifySound::setText);
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

    @FXML
    void storeInternal(ActionEvent event) {
        if (this.storeInternal.isSelected()) {
            this.storeApiStart2.setDisable(false);
            this.storeApiStart2Dir.setDisable(false);
            this.storeApiStart2DirRef.setDisable(false);
        } else {
            this.storeApiStart2.setSelected(false);
            this.storeApiStart2.setDisable(true);
            this.storeApiStart2Dir.setDisable(true);
            this.storeApiStart2DirRef.setDisable(true);
        }
    }

    @FXML
    void storeApiStart2(ActionEvent event) {
        if (this.storeApiStart2.isSelected()) {
            this.storeInternal.setSelected(true);
        }
    }

    @FXML
    void selectApiStart2Dir(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("api_start2の保存先");
        if (Files.exists(Paths.get(this.storeApiStart2Dir.getText()))) {
            dc.setInitialDirectory(Paths.get(this.storeApiStart2Dir.getText()).toAbsolutePath().toFile());
        }
        Optional.ofNullable(dc.showDialog(this.getWindow()))
                .filter(File::exists)
                .map(File::getAbsolutePath)
                .ifPresent(this.storeApiStart2Dir::setText);
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

    private void bouyomiChanInit() {
        AppBouyomiConfig config = AppBouyomiConfig.get();

        this.enableBouyomi.setSelected(config.isEnable());
        this.bouyomiHost.setText(config.getHost());
        this.bouyomiPort.setText(Integer.toString(config.getPort()));

        BouyomiDefaultSettings settings = BouyomiChanUtils.getDefaultSettings();
        int row = 0;
        for (BouyomiSetting setting : settings.getSettings()) {
            BouyomiChanUtils.Type type = BouyomiChanUtils.Type.valueOf(setting.getId());

            AppBouyomiText bouyomiConfig = config.getText()
                    .get(setting.getId());

            CheckBox checkBox = new CheckBox(setting.getLabel());
            checkBox.setSelected(true);

            TextField text = new TextField(setting.getText());
            TextFlow params = new TextFlow();
            for (Params param : setting.getParams()) {
                Hyperlink link = new Hyperlink(param.getComment());
                link.setFocusTraversable(false);
                link.setOnAction(ev -> {
                    text.replaceSelection(param.getTag());
                });
                params.getChildren().add(link);
            }
            if (bouyomiConfig != null) {
                checkBox.setSelected(bouyomiConfig.isEnable());
                text.setText(bouyomiConfig.getText());
            }

            this.bouyomiTexts.add(checkBox, 0, row);
            this.bouyomiTexts.add(text, 1, row);
            GridPane.setHgrow(text, Priority.ALWAYS);
            this.bouyomiTexts.add(params, 1, ++row);
            row++;

            this.enableBouyomiTextMap.put(type, checkBox::isSelected);
            this.bouyomiTextMap.put(type, text::getText);
        }
    }

    private void bouyomiChanStore() {
        AppBouyomiConfig config = AppBouyomiConfig.get();
        config.setEnable(this.enableBouyomi.isSelected());
        config.setHost(this.bouyomiHost.getText());
        config.setPort(this.toInt(this.bouyomiPort.getText()));

        Map<String, AppBouyomiText> textMap = config.getText();
        textMap.clear();
        for (BouyomiChanUtils.Type type : BouyomiChanUtils.Type.values()) {
            boolean enable = this.enableBouyomiTextMap.get(type).get();
            String text = this.bouyomiTextMap.get(type).get();

            AppBouyomiText bouyomiText = new AppBouyomiText();
            bouyomiText.setEnable(enable);
            bouyomiText.setText(text);

            textMap.put(type.toString(), bouyomiText);
        }
    }

    private int toInt(String v) {
        if (v.isEmpty())
            return 0;
        for (int i = 0; i < v.length(); i++) {
            char c = v.charAt(i);
            if (!('0' <= c && '9' >= c))
                return 0;
        }
        return Integer.parseInt(v, 10);
    }
}
