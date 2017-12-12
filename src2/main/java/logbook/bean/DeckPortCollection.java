package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;

/**
 * 艦隊のコレクション
 *
 */
public class DeckPortCollection implements Serializable {

    private static final long serialVersionUID = -1465703933249298173L;

    /** 艦隊 */
    private Map<Integer, DeckPort> deckPortMap = new LinkedHashMap<>();

    /** 遠征中の艦娘 */
    private Set<Integer> missionShips = new LinkedHashSet<>();

    /**
     * 艦隊を取得します。
     * @return 艦隊
     */
    public Map<Integer, DeckPort> getDeckPortMap() {
        return this.deckPortMap;
    }

    /**
     * 艦隊を設定します。
     * @param deckPortMap 艦隊
     */
    public void setDeckPortMap(Map<Integer, DeckPort> deckPortMap) {
        this.deckPortMap = deckPortMap;
    }

    /**
     * 遠征中の艦娘を取得します。
     * @return 遠征中の艦娘
     */
    public Set<Integer> getMissionShips() {
        return this.missionShips;
    }

    /**
     * 遠征中の艦娘を設定します。
     * @param missionShips 遠征中の艦娘
     */
    public void setMissionShips(Set<Integer> missionShips) {
        this.missionShips = missionShips;
    }

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
