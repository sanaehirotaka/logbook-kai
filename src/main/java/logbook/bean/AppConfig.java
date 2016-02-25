package logbook.bean;

import logbook.internal.Config;

/**
 * アプリケーションの設定
 *
 */
public final class AppConfig {

    /** ポート番号 */
    private int listenPort = 8888;

    /** ローカルループバックアドレスからの接続のみ受け入れる */
    private boolean allowOnlyFromLocalhost = true;

    /** プロキシ利用 */
    private boolean useProxy;

    /** プロキシホスト */
    private String proxyHost = "localhost"; //$NON-NLS-1$

    /** プロキシポート */
    private int proxyPort = 8080;

    /** プラグインディレクトリ */
    private String pluginsDir = "./plugins/"; //$NON-NLS-1$

    /** リソースディレクトリ */
    private String resourcesDir = "./resources/"; //$NON-NLS-1$

    /**
     * ポート番号を取得します。
     * @return ポート番号
     */
    public int getListenPort() {
        return this.listenPort;
    }

    /**
     * ポート番号を設定します。
     * @param listenPort ポート番号
     */
    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * ローカルループバックアドレスからの接続のみ受け入れるを取得します。
     * @return ローカルループバックアドレスからの接続のみ受け入れる
     */
    public boolean isAllowOnlyFromLocalhost() {
        return this.allowOnlyFromLocalhost;
    }

    /**
     * ローカルループバックアドレスからの接続のみ受け入れるを設定します。
     * @param allowOnlyFromLocalhost ローカルループバックアドレスからの接続のみ受け入れる
     */
    public void setAllowOnlyFromLocalhost(boolean allowOnlyFromLocalhost) {
        this.allowOnlyFromLocalhost = allowOnlyFromLocalhost;
    }

    /**
     * プロキシ利用を取得します。
     * @return プロキシ利用
     */
    public boolean isUseProxy() {
        return this.useProxy;
    }

    /**
     * プロキシ利用を設定します。
     * @param useProxy プロキシ利用
     */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
     * プロキシホストを取得します。
     * @return プロキシホスト
     */
    public String getProxyHost() {
        return this.proxyHost;
    }

    /**
     * プロキシホストを設定します。
     * @param proxyHost プロキシホスト
     */
    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * プロキシポートを取得します。
     * @return プロキシポート
     */
    public int getProxyPort() {
        return this.proxyPort;
    }

    /**
     * プロキシポートを設定します。
     * @param proxyPort プロキシポート
     */
    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * プラグインディレクトリを取得します。
     * @return プラグインディレクトリ
     */
    public String getPluginsDir() {
        return this.pluginsDir;
    }

    /**
     * プラグインディレクトリを設定します。
     * @param pluginsDir プラグインディレクトリ
     */
    public void setPluginsDir(String pluginsDir) {
        this.pluginsDir = pluginsDir;
    }

    /**
     * リソースディレクトリを取得します。
     * @return リソースディレクトリ
     */
    public String getResourcesDir() {
        return this.resourcesDir;
    }

    /**
     * リソースディレクトリを設定します。
     * @param resourcesDir リソースディレクトリ
     */
    public void setResourcesDir(String resourcesDir) {
        this.resourcesDir = resourcesDir;
    }

    /**
     * アプリケーションのデフォルト設定ディレクトリからアプリケーション設定を取得します、
     * これは次の記述と同等です
     * <blockquote>
     *     <code>Config.getDefault().get(AppConfig.class, AppConfig::new)</code>
     * </blockquote>
     *
     * @return アプリケーションの設定
     */
    public static AppConfig get() {
        return Config.getDefault().get(AppConfig.class, AppConfig::new);
    }
}
