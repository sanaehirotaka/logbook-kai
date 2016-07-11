package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;
import lombok.Data;

/**
 * 入渠ドックのコレクション
 *
 */
@Data
public class NdockCollection implements Serializable {

    private static final long serialVersionUID = 3570762805703032390L;

    /** 入渠ドック */
    private Map<Integer, Ndock> ndockMap = new LinkedHashMap<>();

    /** 入渠中の艦娘 */
    private Set<Integer> ndockSet = new LinkedHashSet<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>NdockCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(NdockCollection.class, NdockCollection::new)</code>
     * </blockquote>
     *
     * @return <code>NdockCollection</code>
     */
    public static NdockCollection get() {
        return Config.getDefault().get(NdockCollection.class, NdockCollection::new);
    }
}
