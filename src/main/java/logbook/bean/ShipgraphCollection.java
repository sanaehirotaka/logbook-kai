package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

@Data
public class ShipgraphCollection implements Serializable {
    
    private static final long serialVersionUID = 3318926490130170935L;
    
    /** 艦娘画像設定 */
    private Map<Integer, Shipgraph> shipgraphMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link ShipgraphCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(ShipgraphCollection.class, ShipgraphCollection::new)</code>
     * </blockquote>
     *
     * @return {@link ShipgraphCollection}
     */
    public static ShipgraphCollection get() {
        return Config.getDefault().get(ShipgraphCollection.class, ShipgraphCollection::new);
    }
}
