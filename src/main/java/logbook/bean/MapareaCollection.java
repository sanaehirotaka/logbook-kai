package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 出撃海域のコレクション
 *
 */
@Data
public class MapareaCollection implements Serializable {

    private static final long serialVersionUID = -5052707686530360461L;

    private Map<Integer, Maparea> maparea = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link MapareaCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(MapareaCollection.class, MapareaCollection::new)</code>
     * </blockquote>
     *
     * @return {@link MapareaCollection}
     */
    public static MapareaCollection get() {
        return Config.getDefault().get(MapareaCollection.class, MapareaCollection::new);
    }
}
