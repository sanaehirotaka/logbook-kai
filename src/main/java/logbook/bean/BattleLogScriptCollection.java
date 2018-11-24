package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;

import logbook.internal.Config;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 集計スクリプト
 */
@Data
public class BattleLogScriptCollection implements Serializable {

    private static final long serialVersionUID = -6241076437441929518L;

    /** 集計スクリプト */
    private ArrayList<BattleLogScript> scripts = new ArrayList<>();

    /**
     * 集計スクリプト
     */
    @Getter
    @Setter
    public static class BattleLogScript implements Serializable {

        private static final long serialVersionUID = 950556288365558034L;

        /** 名前 */
        private String name = "";

        /** 集計スクリプト */
        private String script = "";

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link BattleLogScriptCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(BattleLogScriptCollection.class, BattleLogScriptCollection::new)</code>
     * </blockquote>
     *
     * @return {@link BattleLogScriptCollection}
     */
    public static BattleLogScriptCollection get() {
        return Config.getDefault().get(BattleLogScriptCollection.class, BattleLogScriptCollection::new);
    }
}
