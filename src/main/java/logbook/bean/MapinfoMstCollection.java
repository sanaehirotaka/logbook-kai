package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

@Data
public class MapinfoMstCollection implements Serializable {

    private static final long serialVersionUID = -8209654433723984736L;

    private Map<Integer, MapinfoMst> mapinfo = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>MapinfoMstCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(MapinfoMstCollection.class, MapinfoMstCollection::new)</code>
     * </blockquote>
     *
     * @return <code>MapinfoMstCollection</code>
     */
    public static MapinfoMstCollection get() {
        return Config.getDefault().get(MapinfoMstCollection.class, MapinfoMstCollection::new);
    }
}
