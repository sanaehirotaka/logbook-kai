package logbook.internal;

import java.beans.ExceptionListener;
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
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        try {
            launcher.initPlugin(args);
            launcher.initLocal(args);
            Runtime.getRuntime().addShutdownHook(new Thread(launcher::exitLocalProxy));
            Runtime.getRuntime().addShutdownHook(new Thread(launcher::exitLocalThreadPool));
            Runtime.getRuntime().addShutdownHook(new Thread(launcher::storeConfig));
            Runtime.getRuntime().addShutdownHook(new Thread(launcher::exitPlugin));
        } catch (Exception | Error e) {
            LoggerHolder.get().warn("例外が発生しました", e); //$NON-NLS-1$
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
     * プロキシサーバースレッドの終了処理
     */
    private void exitLocalProxy() {
        try {
            ProxyHolder.getInstance().interrupt();
        } catch (Exception e) {
            LoggerHolder.get().warn("プロキシサーバースレッドの終了処理中に例外が発生", e); //$NON-NLS-1$
        }
    }

    /**
     * スレッドプールの終了処理
     */
    private void exitLocalThreadPool() {
        ScheduledExecutorService executor = ThreadManager.getExecutorService();
        executor.shutdownNow();
    }

    /**
     * アプリケーション設定ファイルの保存処理
     */
    private void storeConfig() {
        try {
            Config.getDefault().store();
        } catch (Exception e) {
            LoggerHolder.get().warn("アプリケーション設定ファイルの保存処理中に例外が発生", e); //$NON-NLS-1$
        }
    }

    /**
     * プラグインの終了処理
     */
    private void exitPlugin() {
        try {
            PluginContainer container = PluginContainer.getInstance();
            container.close();
        } catch (Exception e) {
            LoggerHolder.get().warn("プラグインのクローズ中に例外が発生", e); //$NON-NLS-1$
        }
    }
}
