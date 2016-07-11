package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * 遠征のコレクション
 *
 */
@Data
public class MissionCollection implements Serializable {

    private static final long serialVersionUID = 3540653891280633865L;

    /** 遠征 */
    private Map<Integer, Mission> missionMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>MissionCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(MissionCollection.class)</code>
     * </blockquote>
     *
     * @return <code>MissionCollection</code>
     */
    public static MissionCollection get() {
        return Config.getDefault().get(MissionCollection.class, MissionCollection::new);
    }
}
