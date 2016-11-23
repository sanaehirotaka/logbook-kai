package logbook.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;
import lombok.Data;

/**
 * 艦娘のラベル
 *
 */
@Data
public class ShipLabelCollection implements Serializable {

    private static final long serialVersionUID = -5742379498848962924L;

    private Map<Integer, Set<String>> labels = new HashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>MissionCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(MissionCollection.class)</code>
     * </blockquote>
     *
     * @return <code>MissionCollection</code>
     */
    public static ShipLabelCollection get() {
        return Config.getDefault().get(ShipLabelCollection.class, ShipLabelCollection::new);
    }
}
