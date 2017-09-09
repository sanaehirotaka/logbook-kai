package logbook.internal;

import java.beans.ExceptionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import logbook.Messages;
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
            LoggerHolder.get().warn(Messages.getString("Launcher.0"), e); //$NON-NLS-1$
        } finally {
            try {
                try {
                    launcher.exitLocal();
                } finally {
                    launcher.exitPlugin();
                }
            } catch (Exception | Error e) {
                LoggerHolder.get().warn(Messages.getString("Launcher.0"), e); //$NON-NLS-1$
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

        Set<String> blackList = this.getBlackList(listener);

        Path dir = Paths.get(AppConfig.get().getPluginsDir());
        PluginContainer container = PluginContainer.getInstance();

        List<JarBasedPlugin> plugins = Collections.emptyList();
        if (AppConfig.get().isUsePlugin() && Files.isDirectory(dir)) {
            try {
                plugins = Files.list(dir)
                        .filter(Files::isRegularFile)
                        .map(p -> JarBasedPlugin.toJarBasedPlugin(p, listener))
                        .filter(Objects::nonNull)
                        .filter(p -> !blackList.contains(p.getDigest()))
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
            Config.getDefault().store();
            try {
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
        ExceptionListener listener = e -> LoggerHolder.get().warn("プラグインのクローズ中に例外が発生", e); //$NON-NLS-1$
        try {
            PluginContainer container = PluginContainer.getInstance();
            container.close();
        } catch (IOException e) {
            listener.exceptionThrown(e);
        }
    }

    /**
     * プラグインブラックリストの読み込み
     *
     * @param listener ExceptionListener
     * @return プラグインブラックリスト
     */
    private Set<String> getBlackList(ExceptionListener listener) {
        Set<String> blackList = Collections.emptySet();

        InputStream in = Launcher.class.getClassLoader().getResourceAsStream("logbook/plugin-black-list"); //$NON-NLS-1$
        if (in != null) {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                blackList = r.lines()
                        .filter(l -> l.length() >= 64)
                        .map(l -> l.substring(0, 64))
                        .collect(Collectors.toSet());
            } catch (IOException e) {
                listener.exceptionThrown(e);
            }
        }
        return blackList;
    }
}
