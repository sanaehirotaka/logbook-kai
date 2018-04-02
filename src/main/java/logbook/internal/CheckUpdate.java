package logbook.internal;

import java.awt.Desktop;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import logbook.internal.gui.Tools;

/**
 * アップデートチェック
 *
 */
public class CheckUpdate {

    private static final String[] CHECK_SITES = {
            "https://kancolle.sanaechan.net/logbook-kai.txt",
            "http://kancolle.sanaechan.net/logbook-kai.txt"
    };

    public static void run(Stage stage) {
        CheckUpdate.run(false, stage);
    }

    public static void run(boolean isStartUp) {
        CheckUpdate.run(isStartUp, null);
    }

    private static void run(boolean isStartUp, Stage stage) {
        for (String checkSite : CHECK_SITES) {
            URI uri = URI.create(checkSite);

            try (InputStream in = uri.toURL().openStream()) {
                byte[] b = new byte[1024];
                int l = in.read(b, 0, b.length);
                String str = new String(b, 0, l, StandardCharsets.UTF_8);

                Version newversion = new Version(str);

                if (Version.getCurrent().compareTo(newversion) < 0) {
                    Platform.runLater(() -> CheckUpdate.openInfo(Version.getCurrent(), newversion, isStartUp, stage));
                } else if (!isStartUp) {
                    Tools.Conrtols.alert(AlertType.INFORMATION, "更新の確認", "最新のバージョンです", stage);
                }
                break;
            } catch (Exception e) {
                LoggerHolder.get().warn("アップデートチェックで例外", e);
            }
        }
    }

    private static void openInfo(Version o, Version n, boolean isStartUp, Stage stage) {
        String message = "新しいバージョンがあります。ダウンロードサイトを開きますか？\n"
                + "現在のバージョン:" + o + "\n"
                + "新しいバージョン:" + n;
        if (isStartUp) {
            message += "\n※自動アップデートチェックは[その他]-[設定]から無効に出来ます";
        }

        ButtonType update = new ButtonType("自動更新");
        ButtonType visible = new ButtonType("ダウンロードサイトを開く");
        ButtonType no = new ButtonType("後で");

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("新しいバージョン");
        alert.setHeaderText("新しいバージョン");
        alert.setContentText(message);
        alert.initOwner(stage);
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(update, visible, no);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == update)
                launchUpdate(n);
            if (result.get() == visible)
                openBrowser();
        }
    }

    private static void openBrowser() {
        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://github.com/sanaehirotaka/logbook-kai/releases"));
        } catch (Exception e) {
            LoggerHolder.get().warn("アップデートチェックで例外", e);
        }
    }

    private static void launchUpdate(Version newversion) {
        try {
            // 航海日誌のインストールディレクトリ
            Path dir = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .toPath()
                    .getParent();
            // 更新スクリプト
            InputStream is = Launcher.class.getClassLoader().getResourceAsStream("logbook/update/update.js");
            Path script = Files.createTempFile("logbook-kai-update-", ".js");
            try {
                // 更新スクリプトを一時ファイルにコピー
                Files.copy(is, script, StandardCopyOption.REPLACE_EXISTING);
                // 更新スクリプトを動かすコマンド (JAVA_HOME/bin/jjs)
                Path command = Paths.get(System.getProperty("java.home"), "bin", "jjs");

                new ProcessBuilder(command.toString(), script.toString(),
                        "-fx",
                        "-Dupdate_script=" + script,
                        "-Dinstall_target=" + dir,
                        "-Dinstall_version=" + newversion)
                                .inheritIO()
                                .start();
            } catch (Exception e) {
                // 何か起こったら一時ファイル削除
                Files.deleteIfExists(script);
                throw e;
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("アップデートチェックで例外", e);
            openBrowser();
        }
    }
}