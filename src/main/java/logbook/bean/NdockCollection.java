package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import logbook.internal.Config;

/**
 * 入渠ドックのコレクション
 *
 */
public class NdockCollection implements Serializable {

    private static final long serialVersionUID = 3570762805703032390L;

    /** 入渠ドック */
    private Map<Integer, Ndock> ndockMap = new LinkedHashMap<>();

    /** 入渠中の艦娘 */
    private Set<Integer> ndockSet = new LinkedHashSet<>();

    /**
     * 入渠ドックを取得します。
     * @return 入渠ドック
     */
    public Map<Integer, Ndock> getNdockMap() {
        return this.ndockMap;
    }

    /**
     * 入渠ドックを設定します。
     * @param ndockMap 入渠ドック
     */
    public void setNdockMap(Map<Integer, Ndock> ndockMap) {
        this.ndockMap = ndockMap;
    }

    /**
     * 入渠中の艦娘を取得します。
     * @return 入渠中の艦娘
     */
    public Set<Integer> getNdockSet() {
        return this.ndockSet;
    }

    /**
     * 入渠中の艦娘を設定します。
     * @param ndockSet 入渠中の艦娘
     */
    public void setNdockSet(Set<Integer> ndockSet) {
        this.ndockSet = ndockSet;
    }

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
