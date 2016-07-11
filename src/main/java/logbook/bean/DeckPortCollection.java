package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;
import lombok.Data;

/**
 * 艦隊のコレクション
 *
 */
@Data
public class DeckPortCollection implements Serializable {

    private static final long serialVersionUID = -1465703933249298173L;

    /** 艦隊 */
    private Map<Integer, DeckPort> deckPortMap = new LinkedHashMap<>();

    /** 遠征中の艦娘 */
    private Set<Integer> missionShips = new LinkedHashSet<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>DeckPortCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(DeckPortCollection.class, DeckPortCollection::new)</code>
     * </blockquote>
     *
     * @return <code>DeckPortCollection</code>
     */
    public static DeckPortCollection get() {
        return Config.getDefault().get(DeckPortCollection.class, DeckPortCollection::new);
    }
}
