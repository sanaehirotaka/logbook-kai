package logbook.bean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import logbook.internal.Config;
import lombok.Data;

/**
 * アイテムのコレクション
 *
 */
@Data
public class UseitemCollection implements Serializable {

    private static final long serialVersionUID = -3813660780247992556L;

    /** アイテム */
    private Map<Integer, Useitem> useitemMap = new LinkedHashMap<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから{@link UseitemCollection}を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(UseitemCollection.class, UseitemCollection::new)</code>
     * </blockquote>
     *
     * @return {@link UseitemCollection}
     */
    public static UseitemCollection get() {
        return Config.getDefault().get(UseitemCollection.class, UseitemCollection::new);
    }
}
