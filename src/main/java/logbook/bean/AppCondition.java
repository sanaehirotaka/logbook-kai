package logbook.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import logbook.internal.Config;
import lombok.Data;

/**
 * 出撃などの状態
 *
 */
@Data
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

    /** 泊地修理タイマー */
    private long akashiTimer = 0;

    /** cond値更新時間(エポック秒) */
    private long condUpdateTime = 0;

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
