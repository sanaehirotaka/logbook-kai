package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 艦隊のコレクション
 *
 */
public class DeckPortCollection implements Serializable {

    private static final long serialVersionUID = -7413838279692739373L;

    /** 艦隊 */
    private Map<Integer, DeckPort> deckPortMap = new LinkedHashMap<>();

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
