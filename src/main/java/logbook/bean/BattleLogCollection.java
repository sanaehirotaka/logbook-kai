package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import logbook.internal.Config;

public class BattleLogCollection implements Serializable {

    private static final long serialVersionUID = 504423683100886175L;

    /** 戦闘ログ */
    private List<BattleLog> logs = new ArrayList<>();

    /**
     * 戦闘ログを取得します。
     * @return 戦闘ログ
     */
    public List<BattleLog> getLogs() {
        return this.logs;
    }

    /**
     * 戦闘ログを設定します。
     * @param logs 戦闘ログ
     */
    public void setLogs(List<BattleLog> logs) {
        this.logs = logs;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link BattleLogCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(BattleLogCollection.class, BattleLogCollection::new)</code>
     * </blockquote>
     *
     * @return {@link BattleLogCollection}
     */
    public static BattleLogCollection get() {
        return Config.getDefault().get(BattleLogCollection.class, BattleLogCollection::new);
    }
}
