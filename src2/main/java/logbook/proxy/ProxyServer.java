package logbook.proxy;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.plugin.PluginContainer;

/**
 * リバースプロキシ
 *
 * サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
 */
public final class ProxyServer extends Thread {

    private static final ProxyServer SERVER = new ProxyServer();

    private ProxyServer() {
    }

    @Override
    public void run() {
        try {
            int port = AppConfig.get().getListenPort();
            boolean isAllowOnlyFromLocalhost = AppConfig.get().isAllowOnlyFromLocalhost();

            Server server = new Server();
            ServerConnector connector = new ServerConnector(server);
            if (isAllowOnlyFromLocalhost) {
                connector.setHost(InetAddress.getLoopbackAddress().getHostName());
            }
            connector.setPort(port);
            server.addConnector(connector);

            ServletHandler context = new ServletHandler();
            ServletHolder holder = new ServletHolder(new ReverseProxyServlet(this.getServices()));
            holder.setInitParameter("maxThreads", "8"); //$NON-NLS-1$ //$NON-NLS-2$
            holder.setInitParameter("idleTimeout", Long.toString(TimeUnit.MINUTES.toMillis(2))); //$NON-NLS-1$
            holder.setInitParameter("timeout", Long.toString(TimeUnit.MINUTES.toMillis(2))); //$NON-NLS-1$
            context.addServletWithMapping(holder, "/*"); //$NON-NLS-1$
            server.setHandler(context);
            try {
                try {
                    server.start();
                    try {
                        server.join();
                    } catch (InterruptedException e) {
                        // escape join
                    }
                } catch (Exception e) {
                    LoggerHolder.LOG.warn(Messages.getString("ProxyServer.5"), e); //$NON-NLS-1$

                    this.displayAlert(e);
                }
            } finally {
                server.stop();
            }
        } catch (Exception e) {
            LoggerHolder.LOG.warn(Messages.getString("ProxyServer.6"), e); //$NON-NLS-1$
        }
    }

    private List<ContentListenerSpi> getServices() {
        ServiceLoader<ContentListenerSpi> loader = ServiceLoader.load(
                ContentListenerSpi.class,
                PluginContainer.getInstance().getClassLoader());

        List<ContentListenerSpi> services = StreamSupport.stream(loader.spliterator(), false)
                .collect(Collectors.toList());

        return services;
    }

    private void displayAlert(Exception e) {
        // Title
        String title = Messages.getString("ProxyServer.7"); //$NON-NLS-1$
        // Message
        StringBuilder sb = new StringBuilder(Messages.getString("ProxyServer.8")); //$NON-NLS-1$
        if (e instanceof BindException) {
            sb.append("\n"); //$NON-NLS-1$
            sb.append(Messages.getString("ProxyServer.10")); //$NON-NLS-1$
        }
        String message = sb.toString();
        // StackTrace
        StringWriter w = new StringWriter();
        e.printStackTrace(new PrintWriter(w));
        String stackTrace = w.toString();

        Runnable runnable = () -> {
            Alert alert = new Alert(AlertType.WARNING);
            TextArea textArea = new TextArea(stackTrace);
            alert.getDialogPane().setExpandableContent(textArea);

            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.showAndWait();
        };

        Platform.runLater(runnable);
    }

    /**
     * リバースプロキシ を取得します
     * @return リバースプロキシ スレッドインスタンス
     */
    public static ProxyServer getInstance() {
        return SERVER;
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ProxyServer.class);
    }
}
