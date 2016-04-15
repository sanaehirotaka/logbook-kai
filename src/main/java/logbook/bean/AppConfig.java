package logbook.bean;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;
import logbook.internal.Rank;
import logbook.internal.SeaArea;

/**
 * アプリケーションの設定
 *
 */
public final class AppConfig implements Serializable {

    private static final long serialVersionUID = 8523468261908058871L;

    /** 遠征・入渠完了時に通知をする */
    private boolean useNotification = true;

    /** 通知でサウンドを鳴らす */
    private boolean useSound = true;

    /** 通知でトーストを表示 */
    private boolean useToast = true;

    /** 遠征完了時のリマインド */
    private boolean useRemind = true;

    /** 母港枠の空きがこれ以下でボタンを警告色に変える */
    private int shipFullyThreshold = 4;

    /** 装備枠の空きがこれ以下でボタンを警告色に変える */
    private int itemFullyThreshold = 16;

    /** 戦闘開始時に結果を反映 */
    private boolean applyBattle;

    /** 戦闘結果時に結果を反映 */
    private boolean applyResult = true;

    /** 音量 */
    private int soundLevel = 85;

    /** 報告書の保存先 */
    private String reportPath = new File("").getAbsolutePath();

    /** 資材ログ保存間隔 */
    private int materialLogInterval = 600;

    /** 最前面に表示する */
    private boolean onTop = false;

    /** 終了時に確認する */
    private boolean checkDoit = true;

    /** 起動時にアップデートチェック */
    private boolean checkUpdate = true;

    /** 通信エラーの抑止 */
    private boolean connectionClose = true;

    /** ポート番号 */
    private int listenPort = 8888;

    /** ローカルループバックアドレスからの接続のみ受け入れる */
    private boolean allowOnlyFromLocalhost = true;

    /** プロキシ利用 */
    private boolean useProxy;

    /** プロキシホスト */
    private String proxyHost = "localhost"; //$NON-NLS-1$

    /** プロキシポート */
    private int proxyPort = 8080;

    /** プラグインを有効にする */
    private boolean usePlugin = true;

    /** 遠征通知サウンドディレクトリ */
    private String missionSoundDir = "./sounds/mission/"; //$NON-NLS-1$

    /** 入渠通知サウンドディレクトリ */
    private String ndockSoundDir = "./sounds/ndock/"; //$NON-NLS-1$

    /** 警告サウンドディレクトリ */
    private String alertSoundDir = "./sounds/alert/"; //$NON-NLS-1$

    /** プラグインディレクトリ */
    private String pluginsDir = "./plugins/"; //$NON-NLS-1$

    /** リソースディレクトリ */
    private String resourcesDir = "./resources/"; //$NON-NLS-1$

    /** 戦闘ログディレクトリ */
    private String battleLogDir = "./battlelog/"; //$NON-NLS-1$

    /** 戦闘ログの保存期限 */
    private int battleLogExpires = 60;

    /** レベリング海域 */
    private SeaArea battleSeaArea = SeaArea.キス島沖;

    /** レベリング評価 */
    private Rank resultRank = Rank.S勝利;

    /** ウインドウ位置 */
    private Map<String, WindowLocation> windowLocationMap = new HashMap<>();

    /** キャプチャの保存先 */
    private String captureDir;

    /**
     * 遠征・入渠完了時に通知をするを取得します。
     * @return 遠征・入渠完了時に通知をする
     */
    public boolean isUseNotification() {
        return this.useNotification;
    }

    /**
     * 遠征・入渠完了時に通知をするを設定します。
     * @param useNotification 遠征・入渠完了時に通知をする
     */
    public void setUseNotification(boolean useNotification) {
        this.useNotification = useNotification;
    }

    /**
     * 通知でサウンドを鳴らすを取得します。
     * @return 通知でサウンドを鳴らす
     */
    public boolean isUseSound() {
        return this.useSound;
    }

    /**
     * 通知でサウンドを鳴らすを設定します。
     * @param useSound 通知でサウンドを鳴らす
     */
    public void setUseSound(boolean useSound) {
        this.useSound = useSound;
    }

    /**
     * 通知でトーストを表示を取得します。
     * @return 通知でトーストを表示
     */
    public boolean isUseToast() {
        return this.useToast;
    }

    /**
     * 通知でトーストを表示を設定します。
     * @param useToast 通知でトーストを表示
     */
    public void setUseToast(boolean useToast) {
        this.useToast = useToast;
    }

    /**
     * 遠征完了時のリマインドを取得します。
     * @return 遠征完了時のリマインド
     */
    public boolean isUseRemind() {
        return this.useRemind;
    }

    /**
     * 遠征完了時のリマインドを設定します。
     * @param useRemind 遠征完了時のリマインド
     */
    public void setUseRemind(boolean useRemind) {
        this.useRemind = useRemind;
    }

    /**
     * 母港枠の空きがこれ以下でボタンを警告色に変えるを取得します。
     * @return 母港枠の空きがこれ以下でボタンを警告色に変える
     */
    public int getShipFullyThreshold() {
        return this.shipFullyThreshold;
    }

    /**
     * 母港枠の空きがこれ以下でボタンを警告色に変えるを設定します。
     * @param shipFullyThreshold 母港枠の空きがこれ以下でボタンを警告色に変える
     */
    public void setShipFullyThreshold(int shipFullyThreshold) {
        this.shipFullyThreshold = shipFullyThreshold;
    }

    /**
     * 装備枠の空きがこれ以下でボタンを警告色に変えるを取得します。
     * @return 装備枠の空きがこれ以下でボタンを警告色に変える
     */
    public int getItemFullyThreshold() {
        return this.itemFullyThreshold;
    }

    /**
     * 装備枠の空きがこれ以下でボタンを警告色に変えるを設定します。
     * @param itemFullyThreshold 装備枠の空きがこれ以下でボタンを警告色に変える
     */
    public void setItemFullyThreshold(int itemFullyThreshold) {
        this.itemFullyThreshold = itemFullyThreshold;
    }

    /**
     * 戦闘開始時に結果を反映を取得します。
     * @return 戦闘開始時に結果を反映
     */
    public boolean isApplyBattle() {
        return applyBattle;
    }

    /**
     * 戦闘開始時に結果を反映を設定します。
     * @param applyBattle 戦闘開始時に結果を反映
     */
    public void setApplyBattle(boolean applyBattle) {
        this.applyBattle = applyBattle;
    }

    /**
     * 戦闘結果時に結果を反映を取得します。
     * @return 戦闘結果時に結果を反映
     */
    public boolean isApplyResult() {
        return applyResult;
    }

    /**
     * 戦闘結果時に結果を反映を設定します。
     * @param applyResult 戦闘結果時に結果を反映
     */
    public void setApplyResult(boolean applyResult) {
        this.applyResult = applyResult;
    }

    /**
     * 音量を取得します。
     * @return 音量
     */
    public int getSoundLevel() {
        return this.soundLevel;
    }

    /**
     * 音量を設定します。
     * @param soundLevel 音量
     */
    public void setSoundLevel(int soundLevel) {
        this.soundLevel = soundLevel;
    }

    /**
     * 報告書の保存先を取得します。
     * @return 報告書の保存先
     */
    public String getReportPath() {
        return this.reportPath;
    }

    /**
     * 報告書の保存先を設定します。
     * @param reportPath 報告書の保存先
     */
    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    /**
     * 資材ログ保存間隔を取得します。
     * @return 資材ログ保存間隔
     */
    public int getMaterialLogInterval() {
        return this.materialLogInterval;
    }

    /**
     * 資材ログ保存間隔を設定します。
     * @param materialLogInterval 資材ログ保存間隔
     */
    public void setMaterialLogInterval(int materialLogInterval) {
        this.materialLogInterval = materialLogInterval;
    }

    /**
     * 最前面に表示するを取得します。
     * @return 最前面に表示する
     */
    public boolean isOnTop() {
        return this.onTop;
    }

    /**
     * 最前面に表示するを設定します。
     * @param onTop 最前面に表示する
     */
    public void setOnTop(boolean onTop) {
        this.onTop = onTop;
    }

    /**
     * 終了時に確認するを取得します。
     * @return 終了時に確認する
     */
    public boolean isCheckDoit() {
        return this.checkDoit;
    }

    /**
     * 終了時に確認するを設定します。
     * @param checkDoit 終了時に確認する
     */
    public void setCheckDoit(boolean checkDoit) {
        this.checkDoit = checkDoit;
    }

    /**
     * 起動時にアップデートチェックを取得します。
     * @return 起動時にアップデートチェック
     */
    public boolean isCheckUpdate() {
        return this.checkUpdate;
    }

    /**
     * 起動時にアップデートチェックを設定します。
     * @param checkUpdate 起動時にアップデートチェック
     */
    public void setCheckUpdate(boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }

    /**
     * 通信エラーの抑止を取得します。
     * @return 通信エラーの抑止
     */
    public boolean isConnectionClose() {
        return this.connectionClose;
    }

    /**
     * 通信エラーの抑止を設定します。
     * @param connectionClose 通信エラーの抑止
     */
    public void setConnectionClose(boolean connectionClose) {
        this.connectionClose = connectionClose;
    }

    /**
     * ポート番号を取得します。
     * @return ポート番号
     */
    public int getListenPort() {
        return this.listenPort;
    }

    /**
     * ポート番号を設定します。
     * @param listenPort ポート番号
     */
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * ローカルループバックアドレスからの接続のみ受け入れるを取得します。
     * @return ローカルループバックアドレスからの接続のみ受け入れる
     */
    public boolean isAllowOnlyFromLocalhost() {
        return this.allowOnlyFromLocalhost;
    }

    /**
     * ローカルループバックアドレスからの接続のみ受け入れるを設定します。
     * @param allowOnlyFromLocalhost ローカルループバックアドレスからの接続のみ受け入れる
     */
    public void setAllowOnlyFromLocalhost(boolean allowOnlyFromLocalhost) {
        this.allowOnlyFromLocalhost = allowOnlyFromLocalhost;
    }

    /**
     * プロキシ利用を取得します。
     * @return プロキシ利用
     */
    public boolean isUseProxy() {
        return this.useProxy;
    }

    /**
     * プロキシ利用を設定します。
     * @param useProxy プロキシ利用
     */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
     * プロキシホストを取得します。
     * @return プロキシホスト
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * プロキシホストを設定します。
     * @param proxyHost プロキシホスト
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * プロキシポートを取得します。
     * @return プロキシポート
     */
    public int getProxyPort() {
        return this.proxyPort;
    }

    /**
     * プロキシポートを設定します。
     * @param proxyPort プロキシポート
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * プラグインを有効にするを取得します。
     * @return プラグインを有効にする
     */
    public boolean isUsePlugin() {
        return this.usePlugin;
    }

    /**
     * プラグインを有効にするを設定します。
     * @param usePlugin プラグインを有効にする
     */
    public void setUsePlugin(boolean usePlugin) {
        this.usePlugin = usePlugin;
    }

    /**
     * 遠征通知サウンドディレクトリを取得します。
     * @return 遠征通知サウンドディレクトリ
     */
    public String getMissionSoundDir() {
        return this.missionSoundDir;
    }

    /**
     * 遠征通知サウンドディレクトリを設定します。
     * @param missionSoundDir 遠征通知サウンドディレクトリ
     */
    public void setMissionSoundDir(String missionSoundDir) {
        this.missionSoundDir = missionSoundDir;
    }

    /**
     * 入渠通知サウンドディレクトリを取得します。
     * @return 入渠通知サウンドディレクトリ
     */
    public String getNdockSoundDir() {
        return this.ndockSoundDir;
    }

    /**
     * 入渠通知サウンドディレクトリを設定します。
     * @param ndockSoundDir 入渠通知サウンドディレクトリ
     */
    public void setNdockSoundDir(String ndockSoundDir) {
        this.ndockSoundDir = ndockSoundDir;
    }

    /**
     * 警告サウンドディレクトリを取得します。
     * @return 警告サウンドディレクトリ
     */
    public String getAlertSoundDir() {
        return this.alertSoundDir;
    }

    /**
     * 警告サウンドディレクトリを設定します。
     * @param alertSoundDir 警告サウンドディレクトリ
     */
    public void setAlertSoundDir(String alertSoundDir) {
        this.alertSoundDir = alertSoundDir;
    }

    /**
     * プラグインディレクトリを取得します。
     * @return プラグインディレクトリ
     */
    public String getPluginsDir() {
        return this.pluginsDir;
    }

    /**
     * プラグインディレクトリを設定します。
     * @param pluginsDir プラグインディレクトリ
     */
    public void setPluginsDir(String pluginsDir) {
        this.pluginsDir = pluginsDir;
    }

    /**
     * リソースディレクトリを取得します。
     * @return リソースディレクトリ
     */
    public String getResourcesDir() {
        return this.resourcesDir;
    }

    /**
     * リソースディレクトリを設定します。
     * @param resourcesDir リソースディレクトリ
     */
    public void setResourcesDir(String resourcesDir) {
        this.resourcesDir = resourcesDir;
    }

    /**
     * 戦闘ログディレクトリを取得します。
     * @return 戦闘ログディレクトリ
     */
    public String getBattleLogDir() {
        return this.battleLogDir;
    }

    /**
     * 戦闘ログディレクトリを設定します。
     * @param battleLogDir 戦闘ログディレクトリ
     */
    public void setBattleLogDir(String battleLogDir) {
        this.battleLogDir = battleLogDir;
    }

    /**
     * 戦闘ログの保存期限を取得します。
     * @return 戦闘ログの保存期限
     */
    public int getBattleLogExpires() {
        return this.battleLogExpires;
    }

    /**
     * 戦闘ログの保存期限を設定します。
     * @param battleLogExpires 戦闘ログの保存期限
     */
    public void setBattleLogExpires(int battleLogExpires) {
        this.battleLogExpires = battleLogExpires;
    }

    /**
     * レベリング海域を取得します。
     * @return レベリング海域
     */
    public SeaArea getBattleSeaArea() {
        return this.battleSeaArea;
    }

    /**
     * レベリング海域を設定します。
     * @param battleSeaArea レベリング海域
     */
    public void setBattleSeaArea(SeaArea battleSeaArea) {
        this.battleSeaArea = battleSeaArea;
    }

    /**
     * レベリング評価を取得します。
     * @return レベリング評価
     */
    public Rank getResultRank() {
        return this.resultRank;
    }

    /**
     * レベリング評価を設定します。
     * @param resultRank レベリング評価
     */
    public void setResultRank(Rank resultRank) {
        this.resultRank = resultRank;
    }

    /**
     * ウインドウ位置を取得します。
     * @return ウインドウ位置
     */
    public Map<String, WindowLocation> getWindowLocationMap() {
        return this.windowLocationMap;
    }

    /**
     * ウインドウ位置を設定します。
     * @param windowLocationMap ウインドウ位置
     */
    public void setWindowLocationMap(Map<String, WindowLocation> windowLocationMap) {
        this.windowLocationMap = windowLocationMap;
    }

    /**
     * キャプチャの保存先を取得します。
     * @return キャプチャの保存先
     */
    public String getCaptureDir() {
        return this.captureDir;
    }

    /**
     * キャプチャの保存先を設定します。
     * @param captureDir キャプチャの保存先
     */
    public void setCaptureDir(String captureDir) {
        this.captureDir = captureDir;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリからアプリケーション設定を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppConfig.class, AppConfig::new)</code>
     * </blockquote>
     *
     * @return アプリケーションの設定
     */
    public static AppConfig get() {
        return Config.getDefault().get(AppConfig.class, AppConfig::new);
    }
}
