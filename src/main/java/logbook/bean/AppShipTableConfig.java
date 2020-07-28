package logbook.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 所有艦娘一覧の設定
 *
 */
@Data
public class AppShipTableConfig {

    /** 開いていたタブ */
    private int tabIndex;
    
    /** タブ別の設定 */
    private Map<String, AppShipTableTabConfig> tabConfig = new LinkedHashMap<>();

    /**
     * タブ別の設定
     *
     */
    @Data
    public static class AppShipTableTabConfig {

        /** フィルタペインの展開 */
        private boolean expanded;

        /** テキスト */
        private boolean textEnabled;

        /** テキスト */
        private String textValue;

        /** 艦種 */
        private boolean typeEnabled;

        /** 艦種 */
        private List<String> typeValue;

        /** パラメータフィルター */
        private List<ParameterFilterConfig> parameterFilters;

        /** ラベル条件 */
        private boolean labelEnabled;

        /** ラベル条件 */
        private String labelValue;

        /** 補強増設 */
        private boolean slotExEnabled;

        /** 補強増設 */
        private boolean slotExValue;

        /** 遠征 */
        private boolean missionEnabled;

        /** 遠征 */
        private boolean missionValue;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppShipTableConfig</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppShipTableConfig.class, AppShipTableConfig::new)</code>
     * </blockquote>
     *
     * @return <code>AppShipTableConfig</code>
     */
    public static AppShipTableConfig get() {
        return Config.getDefault().get(AppShipTableConfig.class, AppShipTableConfig::new);
    }
}
