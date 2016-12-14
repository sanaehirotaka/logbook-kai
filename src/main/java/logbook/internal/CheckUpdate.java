package logbook.internal;

import java.awt.Desktop;
import java.io.InputStream;
import java.net.URI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import logbook.bean.AppConfig;
import logbook.plugin.lifecycle.StartUp;

/**
 * アップデートチェック
 *
 */
public class CheckUpdate implements StartUp {

    private static final String[] CHECK_SITES = {
            "https://kancolle.sanaechan.net/logbook-kai.txt",
            "http://kancolle.sanaechan.net/logbook-kai.txt"
    };

    @Override
    public void run() {
        if (!AppConfig.get().isCheckUpdate()) {
            return;
        }
        for (String checkSite : CHECK_SITES) {
            URI uri = URI.create(checkSite);

            try (InputStream in = uri.toURL().openStream()) {
                byte[] b = new byte[1024];
                int l = in.read(b, 0, b.length);
                String str = new String(b, 0, l);

                Version newversion = new Version(str);

                if (Version.getCurrent().compareTo(newversion) < 0) {
                    Platform.runLater(() -> CheckUpdate.openInfo(Version.getCurrent(), newversion));
                }
                break;
            } catch (Exception e) {
                LoggerHolder.LOG.warn("アップデートチェックで例外", e);
            }
        }
    }

    private static void openInfo(Version o, Version n) {
        String message = "新しいバージョンがあります。ダウンロードサイトを開きますか？\n"
                + "現在のバージョン:" + o + "\n"
                + "新しいバージョン:" + n + "\n"
                + "※自動アップデートチェックは[その他]-[設定]からOFFに出来ます";

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("新しいバージョン");
        alert.setHeaderText("新しいバージョン");
        alert.setContentText(message);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        alert.showAndWait()
                .filter(ButtonType.YES::equals)
                .ifPresent(e -> openBrowser());

    }

    private static void openBrowser() {
        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://github.com/sanaehirotaka/logbook-kai/releases"));
        } catch (Exception e) {
            LoggerHolder.LOG.warn("アップデートチェックで例外", e);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(CheckUpdate.class);
    }
}