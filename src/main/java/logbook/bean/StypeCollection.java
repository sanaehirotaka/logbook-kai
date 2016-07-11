package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 艦種のコレクション
 *
 */
@Data
public class StypeCollection implements Serializable {

    private static final long serialVersionUID = -7530294903513038451L;

    /** 艦種 */
    private Map<Integer, Stype> stypeMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link StypeCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(StypeCollection.class, StypeCollection::new)</code>
     * </blockquote>
     *
     * @return {@link StypeCollection}
     */
    public static StypeCollection get() {
        return Config.getDefault().get(StypeCollection.class, StypeCollection::new);
    }
}
