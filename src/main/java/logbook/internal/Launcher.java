package logbook.internal;

import java.beans.ExceptionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import logbook.bean.AppConfig;
import logbook.internal.gui.Main;
import logbook.internal.proxy.ProxyHolder;
import logbook.plugin.JarBasedPlugin;
import logbook.plugin.PluginContainer;

/**
 * アプリケーション
 *
 */
public final class Launcher {

    /**
     * アプリケーションの起動
     *
     * @param args アプリケーション引数
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Launcher launcher = new Launcher();
        try {
            launcher.initPlugin(args);
            launcher.initLocal(args);
        } catch (Exception | Error e) {
            LoggerHolder.get().warn("例外が発生しました", e); //$NON-NLS-1$
        } finally {
            try {
                try {
                    launcher.exitLocal();
                } finally {
                    launcher.exitPlugin();
                }
            } catch (Exception | Error e) {
                LoggerHolder.get().warn("例外が発生しました", e); //$NON-NLS-1$
            }
        }
    }

    /**
     * アプリケーションの初期化処理
     *
     * @param args アプリケーション引数
     */
    void initLocal(String[] args) {
        Main.main(args);
    }

    /**
     * プラグインの初期化処理
     *
     * @param args アプリケーション引数
     */
    void initPlugin(String[] args) {
        ExceptionListener listener = e -> LoggerHolder.get().warn("プラグインの初期化中に例外が発生", e); //$NON-NLS-1$

        Path dir = Paths.get(AppConfig.get().getPluginsDir());
        PluginContainer container = PluginContainer.getInstance();

        List<JarBasedPlugin> plugins = Collections.emptyList();
        if (AppConfig.get().isUsePlugin() && Files.isDirectory(dir)) {
            try {
                plugins = Files.list(dir)
                        .filter(Files::isRegularFile)
                        .map(p -> JarBasedPlugin.toJarBasedPlugin(p, listener))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

            } catch (Exception e) {
                listener.exceptionThrown(e);
            }
        }
        container.init(plugins);
    }

    /**
     * アプリケーションの終了処理
     */
    void exitLocal() {
        try {
            ProxyHolder.getInstance().interrupt();
        } finally {
            try {
                Config.getDefault().store();
            } finally {
                ScheduledExecutorService executor = ThreadManager.getExecutorService();
                executor.shutdownNow();
            }
        }
    }

    /**
     * プラグインの初期化処理
     */
    void exitPlugin() {
        try {
            PluginContainer container = PluginContainer.getInstance();
            container.close();
        } catch (IOException e) {
            LoggerHolder.get().warn("プラグインのクローズ中に例外が発生", e); //$NON-NLS-1$
        }
    }
}
