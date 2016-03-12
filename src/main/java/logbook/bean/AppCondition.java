package logbook.bean;

import java.io.Serializable;

import logbook.internal.Config;

/**
 * 出撃などの状態
 *
 */
public class AppCondition implements Serializable {

    private static final long serialVersionUID = 5501928210713593353L;

    /** 連合艦隊 */
    private Boolean combinedFlag = Boolean.FALSE;

    /** 出撃中 */
    private Boolean mapStart = Boolean.FALSE;

    /**
     * 連合艦隊を取得します。
     * @return 連合艦隊
     */
    public Boolean getCombinedFlag() {
        return this.combinedFlag;
    }

    /**
     * 連合艦隊を設定します。
     * @param combinedFlag 連合艦隊
     */
    public void setCombinedFlag(Boolean combinedFlag) {
        this.combinedFlag = combinedFlag;
    }

    /**
     * 出撃中を取得します。
     * @return 出撃中
     */
    public Boolean getMapStart() {
        return this.mapStart;
    }

    /**
     * 出撃中を設定します。
     * @param mapStart 出撃中
     */
    public void setMapStart(Boolean mapStart) {
        this.mapStart = mapStart;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link AppCondition}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppCondition.class, AppCondition::new)</code>
     * </blockquote>
     *
     * @return {@link AppCondition}
     */
    public static AppCondition get() {
        return Config.getDefault().get(AppCondition.class, AppCondition::new);
    }
}
