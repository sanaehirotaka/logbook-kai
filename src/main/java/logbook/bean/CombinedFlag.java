package logbook.bean;

import logbook.internal.Config;

/**
 * api_combined_flag
 *
 */
public class CombinedFlag {

    /** api_combined_flag */
    private Boolean combinedFlag = Boolean.FALSE;

    /**
     * api_combined_flagを取得します。
     * @return api_combined_flag
     */
    public Boolean getCombinedFlag() {
        return combinedFlag;
    }

    /**
     * api_combined_flagを設定します。
     * @param combinedFlag api_combined_flag
     */
    public void setCombinedFlag(Boolean combinedFlag) {
        this.combinedFlag = combinedFlag;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link CombinedFlag}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(CombinedFlag.class)</code>
     * </blockquote>
     *
     * @return {@link CombinedFlag}
     */
    public static CombinedFlag get() {
        return Config.getDefault().get(CombinedFlag.class);
    }
}
