package logbook.bean;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;
import logbook.internal.Rank;
import logbook.internal.ShipImageCacheStrategy;
import lombok.Data;

/**
 * アプリケーションの設定
 *
 */
@Data
public final class AppConfig implements Serializable {

    private static final long serialVersionUID = -158061123666406172L;

    /** ウインドウスタイル */
    private String windowStyle = "main";

    /** フォントサイズ */
    private String fontSize = "default";

    /** 遠征・入渠完了時に通知をする */
    private boolean useNotification = true;

    /** 出撃時に大破艦がいる場合に通知をする */
    private boolean alertBadlyStart = true;

    /** 進撃時に大破艦がいる場合に通知をする */
    private boolean alertBadlyNext = true;

    /** 通知でサウンドを鳴らす */
    private boolean useSound = true;

    /** 通知でトーストを表示 */
    private boolean useToast = true;

    /** 遠征完了時のリマインド */
    private boolean useRemind = true;

    /** 遠征完了時のリマインド(秒) */
    private int remind = 60;

    /** 艦娘の画像に経験値バーを表示する */
    private boolean visibleExpGauge = true;

    /** 母港枠の空きがこれ以下でボタンを警告色に変える */
    private int shipFullyThreshold = 4;

    /** 装備枠の空きがこれ以下でボタンを警告色に変える */
    private int itemFullyThreshold = 16;

    /** 艦娘や装備の画像の拡大縮小割合(%) */
    private int imageZoomRate = 100;

    /** 戦闘開始時に結果を反映 */
    private boolean applyBattle;

    /** 戦闘結果時に結果を反映 */
    private boolean applyResult = true;

    /** 艦隊タブに艦隊単位のタブを追加 */
    private boolean deckTabs = false;

    /** 艦隊タブにラベル単位のタブを追加 */
    private boolean labelTabs = true;

    /** 艦隊タブの色（無傷） */
    private String tabColorNoDamage;
    /** 艦隊タブの色（健在） */
    private String tabColorLessThanSlightDamage;
    /** 艦隊タブの色（小破） */
    private String tabColorSlightDamage;
    /** 艦隊タブの色（中破） */
    private String tabColorHalfDamage;
    /** 艦隊タブの色（大破） */
    private String tabColorBadlyDamage;
    /** 艦隊タブの色（未遠征） */
    private String tabColorNoMission;
    /** 艦隊タブの色（要補給） */
    private String tabColorNeedRefuel;
    
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

    /** デフォルトサウンド */
    private String defaultNotifySound = "C:\\Windows\\media\\Windows Notify System Generic.wav"; //$NON-NLS-1$

    /** プラグインディレクトリ */
    private String pluginsDir = "./plugins/"; //$NON-NLS-1$

    /** リソースディレクトリ */
    private String resourcesDir = "./resources/"; //$NON-NLS-1$

    /** 戦闘ログディレクトリ */
    private String battleLogDir = "./battlelog/"; //$NON-NLS-1$

    /** 戦闘ログの保存期限 */
    private int battleLogExpires = 60;

    /** 戦闘ログの保存期間無期限 */
    private boolean indefiniteExpires = false;

    /** 戦闘ログの圧縮 */
    private boolean compressBattleLogs = false;

    /** 戦闘ログにローデータを含める */
    private boolean includeRawData = false;

    /** レベリング海域 */
    private int seaAreaIndex = 0;

    /** レベリング評価 */
    private Rank resultRank = Rank.S勝利;

    /** ウインドウ位置 */
    private Map<String, WindowLocation> windowLocationMap = new HashMap<>();

    /** テーブル列の表示・非表示 */
    private Map<String, Set<String>> columnVisibleMap = new HashMap<>();

    /** テーブル列の幅 */
    private Map<String, Map<String, Double>> columnWidthMap = new HashMap<>();

    /** テーブル列のソート順 */
    private Map<String, Map<String, String>> columnSortOrderMap = new LinkedHashMap<>();

    /** SplitPaneの分割サイズ */
    private Map<String, Double> dividerPositionMap = new HashMap<>();

    /** キャプチャの保存先 */
    private String captureDir;

    /** キャプチャの画像形式 */
    private String captureFormat = "jpg";

    /** FFmpeg 実行ファイル */
    private String ffmpegPath;

    /** FFmpeg 引数 */
    private String ffmpegArgs;

    /** FFmpeg 拡張子 */
    private String ffmpegExt;

    /** 艦娘画像キャッシュ設定 */
    private ShipImageCacheStrategy shipImageCacheStrategy = ShipImageCacheStrategy.ALL;

    /** 画像ファイルを再圧縮 */
    private boolean shipImageCompress = false;

    /** 所有艦娘一覧から艦娘画像を隠す */
    private boolean hideShipImageFromShipTablePane = false;

    /** 所有艦娘一覧から装備画像を隠す */
    private boolean hideItemImageFromShipTablePane = false;

    /** 艦隊タブに旗艦の立ち絵を表示 */
    private boolean visiblePoseImageOnFleetTab = false;

    /** store api_start2 */
    private boolean storeApiStart2 = false;

    /** store api_start2 directory */
    private String storeApiStart2Dir = "";

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
