package logbook.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import logbook.internal.Config;
import lombok.Data;

/**
 * 棒読みちゃん連携設定
 *
 */
@Data
public class AppBouyomiConfig {

    private boolean enable = false;

    private String host = "localhost";

    private int port = 50001;

    private boolean tryExecute = false;

    private String bouyomiPath = "";

    private Map<String, AppBouyomiText> text = new ConcurrentHashMap<>();

    @Data
    public static class AppBouyomiText {

        private boolean enable = true;

        private String id;

        private String text;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリから<code>AppBouyomiCollection</code>を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppBouyomiCollection.class, AppBouyomiCollection::new)</code>
     * </blockquote>
     *
     * @return <code>AppBouyomiCollection</code>
     */
    public static AppBouyomiConfig get() {
        return Config.getDefault().get(AppBouyomiConfig.class, AppBouyomiConfig::new);
    }
}
