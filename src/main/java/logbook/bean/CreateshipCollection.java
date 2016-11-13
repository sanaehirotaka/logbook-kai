package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 建造
 *
 */
@Data
public class CreateshipCollection implements Serializable {

    private static final long serialVersionUID = -1055716236895968695L;

    /** 建造 */
    private Map<Integer, Createship> createshipMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>CreateshipCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(CreateshipCollection.class, CreateshipCollection::new)</code>
     * </blockquote>
     *
     * @return <code>CreateshipCollection</code>
     */
    public static CreateshipCollection get() {
        return Config.getDefault().get(CreateshipCollection.class, CreateshipCollection::new);
    }
}
