package logbook.bean;

import java.util.HashMap;
import java.util.Map;

import logbook.internal.Config;

/**
 * 入渠ドックのコレクション
 *
 */
public class NdockCollection {

    /** 入渠ドック */
    private Map<Integer, Ndock> ndockMap = new HashMap<>();

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
