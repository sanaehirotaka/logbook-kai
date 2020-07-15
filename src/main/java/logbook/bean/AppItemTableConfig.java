package logbook.bean;

import logbook.internal.Config;
import lombok.Data;

/**
 * 所有装備一覧の設定
 *
 */
@Data
public class AppItemTableConfig {

    /** インデックス */
    private int tabIndex;

    /** 装備タブの設定 */
    private ItemTabConfig itemTabConfig;
    
    /** 基地航空隊タブの設定 */
    private AirbaseTabConfig airbaseTabConfig;
    
    /**
     * 装備タブの設定
     */
    @Data
    public static class ItemTabConfig {
        private boolean filterExpanded;
        private boolean textFilterEnabled;
        private String textFilter;
    }

    /**
     * 基地航空隊タブの設定
     */
    @Data
    public static class AirbaseTabConfig {
        private boolean filterExpanded;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppItemTableConfig</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppItemTableConfig.class, AppItemTableConfig::new)</code>
     * </blockquote>
     *
     * @return <code>AppShipTableConfig</code>
     */
    public static AppItemTableConfig get() {
        return Config.getDefault().get(AppItemTableConfig.class, AppItemTableConfig::new);
    }
}
