package logbook.bean;

import java.io.Serializable;

import logbook.internal.Config;

/**
 * 出撃などの状態
 *
 */
public class AppCondition implements Serializable {

    private static final long serialVersionUID = 6218938657590214575L;

    /** 連合艦隊 */
    private Boolean combinedFlag = Boolean.FALSE;

    /** 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊)) */
    private Integer combinedType = 0;

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
     * 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))を取得します。
     * @return 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))
     */
    public Integer getCombinedType() {
        return this.combinedType;
    }

    /**
     * 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))を設定します。
     * @param combinedType 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))
     */
    public void setCombinedType(Integer combinedType) {
        this.combinedType = combinedType;
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
