package logbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import logbook.internal.Config;
import lombok.Data;

/**
 * 編成記録のコレクション
 *
 */
@Data
public class AppDeckCollection implements Serializable {

    private static final long serialVersionUID = 6506397370160062508L;

    /** 編成記録 */
    private List<AppDeck> decks = new ArrayList<>();

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppDeckCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppDeckCollection.class, AppDeckCollection::new)</code>
     * </blockquote>
     *
     * @return <code>AppDeckCollection</code>
     */
    public static AppDeckCollection get() {
        return Config.getDefault().get(AppDeckCollection.class, AppDeckCollection::new);
    }
}
