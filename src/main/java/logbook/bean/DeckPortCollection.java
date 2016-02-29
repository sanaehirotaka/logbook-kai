package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import logbook.internal.Config;

/**
 * 艦隊のコレクション
 *
 */
public class DeckPortCollection implements Serializable {

    private static final long serialVersionUID = 2868235673692933578L;

    /** 艦隊 */
    private List<DeckPort> deckPorts = new ArrayList<>();

    /**
     * 艦隊を取得します。
     * @return 艦隊
     */
    public List<DeckPort> getDeckPorts() {
        return this.deckPorts;
    }

    /**
     * 艦隊を設定します。
     * @param deckPorts 艦隊
     */
    public void setDeckPorts(List<DeckPort> deckPorts) {
        this.deckPorts = deckPorts;
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
