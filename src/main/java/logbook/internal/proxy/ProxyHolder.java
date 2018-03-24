package logbook.internal.proxy;

import java.util.List;
import java.util.stream.Collectors;

import logbook.internal.LoggerHolder;
import logbook.plugin.PluginServices;
import logbook.proxy.ProxyServerSpi;

/**
 * プロキシサーバースレッドを保持します
 *
 */
public class ProxyHolder {

    private static final Thread SERVER;

    static {
        Thread thread = null;
        try {
            List<ProxyServerSpi> proxies = PluginServices.instances(ProxyServerSpi.class)
                    .collect(Collectors.toList());
            ProxyServerSpi impl = null;
            for (ProxyServerSpi proxy : proxies) {
                // プラグインのプロキシサーバーを優先する
                if (impl == null || !(proxy instanceof logbook.internal.proxy.ProxyServerImpl)) {
                    impl = proxy;
                }
            }
            thread = new Thread(impl);
            thread.setDaemon(true);
        } catch (Exception e) {
            LoggerHolder.get().error("プロキシサーバーの初期化中に例外", e);
        }
        SERVER = thread;
    }

    /**
     * リバースプロキシスレッド を取得します
     * @return リバースプロキシ スレッドインスタンス
     */
    public static Thread getInstance() {
        return SERVER;
    }
}
