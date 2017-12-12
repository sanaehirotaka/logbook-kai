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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.internal.gui.Main;
import logbook.plugin.JarBasedPlugin;
import logbook.plugin.PluginContainer;
import logbook.proxy.ProxyServer;

/**
 * アプリケーション
 *
 */
public final class Launcher {

    /**
     * アプリケーションの起動
     *
     * @param args アプリケーション引数
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        try {
            launcher.initPlugin(args);
            launcher.initLocal(args);
        } catch (Exception | Error e) {
            LoggerHolder.LOG.warn(Messages.getString("Launcher.0"), e); //$NON-NLS-1$
        } finally {
            try {
                launcher.exitLocal();
            } finally {
                launcher.exitPlugin();
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
        ExceptionListener listener = e -> LoggerHolder.LOG.warn("プラグインの初期化中に例外が発生", e); //$NON-NLS-1$

        Path dir = Paths.get(AppConfig.get().getPluginsDir());
        PluginContainer container = PluginContainer.getInstance();

        List<JarBasedPlugin> plugins = Collections.emptyList();
        if (Files.isDirectory(dir)) {
            try {
                plugins = Files.list(dir)
                        .filter(Files::isRegularFile)
                        .map(p -> JarBasedPlugin.toJarBasedPlugin(p, listener))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

            } catch (IOException e) {
                listener.exceptionThrown(e);
            }
        }
        container.init(plugins);
    }

    /**
     * アプリケーションの終了処理
     */
    void exitLocal() {
        Config.getDefault().store();
        ProxyServer.getInstance().interrupt();
        ScheduledExecutorService executor = ThreadManager.getExecutorService();
        executor.shutdownNow();
    }

    /**
     * プラグインの初期化処理
     */
    void exitPlugin() {
        ExceptionListener listener = e -> LoggerHolder.LOG.warn("プラグインのクローズ中に例外が発生", e); //$NON-NLS-1$
        try {
            PluginContainer container = PluginContainer.getInstance();
            container.close();
        } catch (IOException e) {
            listener.exceptionThrown(e);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(Launcher.class);
    }
}
