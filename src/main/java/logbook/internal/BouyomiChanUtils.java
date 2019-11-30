package logbook.internal;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.fasterxml.jackson.databind.ObjectMapper;

import logbook.bean.AppBouyomiConfig;
import logbook.bean.AppBouyomiConfig.AppBouyomiText;
import logbook.plugin.PluginServices;
import lombok.Data;

public class BouyomiChanUtils {

    private static final AtomicBoolean tryExecute = new AtomicBoolean(false);

    public enum Type {
        MapStartNextAlert,
        MissionStartAlert,
        MissionComplete,
        NdockComplete,
        AchievementGimmick1,
        AchievementGimmick2;
    }

    @SafeVarargs
    public static void speak(Type type, Tuple.Pair<String, String>... params) {
        String id = type.toString();
        String text = "";
        AppBouyomiText config = AppBouyomiConfig.get()
                .getText()
                .get(id);
        if (config != null && !config.isEnable()) {
            return;
        }
        if (config != null) {
            text = config.getText();
        } else {
            BouyomiDefaultSettings settings = getDefaultSettings();
            for (BouyomiSetting setting : settings.getSettings()) {
                if (id.equals(setting.getId())) {
                    text = setting.getText();
                    break;
                }
            }
        }
        if (text == null || text.isEmpty()) {
            return;
        }
        for (Tuple.Pair<String, String> param : params) {
            text = text.replace(param.getKey(), param.getValue());
        }
        speak(text);
    }

    public static void speak(String text) {
        ThreadManager.getExecutorService().execute(() -> {
            AppBouyomiConfig config = AppBouyomiConfig.get();
            speak(BouyomiChan.create(config.getHost(), config.getPort()), text);
        });
    }

    public static BouyomiDefaultSettings getDefaultSettings() {
        ObjectMapper mapper = new ObjectMapper();
        BouyomiDefaultSettings settings;
        try (InputStream is = PluginServices.getResourceAsStream("logbook/bouyomi/settings.json")) {
            settings = mapper.readValue(is, BouyomiDefaultSettings.class);
        } catch (Exception e) {
            LoggerHolder.get().warn("設定ファイルの読み込みに失敗しました", e);
            settings = null;
        }
        return settings;
    }

    private static void speak(BouyomiChan bouyomiChan, String text) {
        try {
            bouyomiChan.speak(text);
        } catch (Exception e) {
            if (AppBouyomiConfig.get().isTryExecute()) {
                tryExecute();
            }
            LoggerHolder.get().info("棒読みちゃんとの接続で例外が発生しました", e);
        }
    }

    private static void tryExecute() {
        if (!tryExecute.getAndSet(true)) {
            Path path = Paths.get(AppBouyomiConfig.get().getBouyomiPath());
            if (!Files.exists(path)) {
                return;
            }
            try {
                new ProcessBuilder(path.toString())
                        .directory(path.getParent().toFile())
                        .start();
            } catch (Exception e) {
            }
        }
    }

    @Data
    public static class BouyomiDefaultSettings {

        private List<BouyomiSetting> settings;
    }

    @Data
    public static class BouyomiSetting {

        private String id;

        private String label;

        private String text;

        private List<Params> params;
    }

    @Data
    public static class Params {

        private String tag;

        private String comment;
    }

}
