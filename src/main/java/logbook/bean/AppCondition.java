package logbook.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import logbook.internal.Config;

/**
 * 出撃などの状態
 *
 */
public class AppCondition implements Serializable {

    private static final long serialVersionUID = -7213537629508340991L;

    /** 連合艦隊 */
    private boolean combinedFlag;

    /** 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊)) */
    private int combinedType = 0;

    /** 出撃中 */
    private boolean mapStart;

    /** 出撃艦隊 */
    private int deckId = 0;

    /** 戦闘結果 */
    private BattleLog battleResult;

    /** 最後の戦闘結果 */
    private BattleLog battleResultConfirm;

    /** 退避艦ID */
    private Set<Integer> escape = new HashSet<>();

    /** 最後に資材ログに書き込んだ時間 */
    private long wroteMaterialLogLast = 0;

    /**
     * 連合艦隊を取得します。
     * @return 連合艦隊
     */
    public boolean isCombinedFlag() {
        return this.combinedFlag;
    }

    /**
     * 連合艦隊を設定します。
     * @param combinedFlag 連合艦隊
     */
    public void setCombinedFlag(boolean combinedFlag) {
        this.combinedFlag = combinedFlag;
    }

    /**
     * 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))を取得します。
     * @return 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))
     */
    public int getCombinedType() {
        return this.combinedType;
    }

    /**
     * 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))を設定します。
     * @param combinedType 連合艦隊 (0=未結成, 1=機動部隊, 2=水上部隊, 3=輸送部隊, -x=解隊(-1=機動部隊, -2=水上部隊))
     */
    public void setCombinedType(int combinedType) {
        this.combinedType = combinedType;
    }

    /**
     * 出撃中を取得します。
     * @return 出撃中
     */
    public boolean isMapStart() {
        return this.mapStart;
    }

    /**
     * 出撃中を設定します。
     * @param mapStart 出撃中
     */
    public void setMapStart(boolean mapStart) {
        this.mapStart = mapStart;
    }

    /**
     * 出撃艦隊を取得します。
     * @return 出撃艦隊
     */
    public int getDeckId() {
        return this.deckId;
    }

    /**
     * 出撃艦隊を設定します。
     * @param deckId 出撃艦隊
     */
    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    /**
     * 戦闘結果を取得します。
     * @return 戦闘結果
     */
    public BattleLog getBattleResult() {
        return this.battleResult;
    }

    /**
     * 戦闘結果を設定します。
     * @param battleResult 戦闘結果
     */
    public void setBattleResult(BattleLog battleResult) {
        this.battleResult = battleResult;
    }

    /**
     * 最後の戦闘結果を取得します。
     * @return 最後の戦闘結果
     */
    public BattleLog getBattleResultConfirm() {
        return battleResultConfirm;
    }

    /**
     * 最後の戦闘結果を設定します。
     * @param battleResultConfirm 最後の戦闘結果
     */
    public void setBattleResultConfirm(BattleLog battleResultConfirm) {
        this.battleResultConfirm = battleResultConfirm;
    }

    /**
     * 退避艦IDを取得します。
     * @return 退避艦ID
     */
    public Set<Integer> getEscape() {
        return this.escape;
    }

    /**
     * 退避艦IDを設定します。
     * @param escape 退避艦ID
     */
    public void setEscape(Set<Integer> escape) {
        this.escape = escape;
    }

    /**
     * 最後に資材ログに書き込んだ時間を取得します。
     * @return 最後に資材ログに書き込んだ時間
     */
    public long getWroteMaterialLogLast() {
        return this.wroteMaterialLogLast;
    }

    /**
     * 最後に資材ログに書き込んだ時間を設定します。
     * @param wroteMaterialLogLast 最後に資材ログに書き込んだ時間
     */
    public void setWroteMaterialLogLast(long wroteMaterialLogLast) {
        this.wroteMaterialLogLast = wroteMaterialLogLast;
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
