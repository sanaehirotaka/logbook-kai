package logbook.internal.gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logbook.bean.AppConfig;
import logbook.plugin.PluginServices;

/**
 * 自動プロキシ構成スクリプトファイル生成
 *
 */
public class CreatePacFileController extends WindowController {

    @FXML
    private TextField addr;

    @FXML
    void save(ActionEvent event) {
        try {
            String packFile;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (InputStream in = PluginServices.getResourceAsStream("logbook/proxy.pac")) {
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            packFile = new String(out.toByteArray())
                    .replace("{port}", String.valueOf(AppConfig.get().getListenPort()));

            FileChooser fc = new FileChooser();
            fc.setTitle("名前をつけて保存");
            fc.setInitialFileName("proxy.pac");

            File file = fc.showSaveDialog(this.getWindow());
            if (file != null) {

                Files.write(file.toPath(), packFile.getBytes());

                this.addr.setText("file:///" + file.toURI().toString().replaceFirst("file:/", ""));

                Tools.Conrtols.alert(AlertType.INFORMATION,
                        "自動プロキシ構成スクリプトファイル",
                        "自動プロキシ構成スクリプトファイルを生成しました。\n" + "次にブラウザの設定を行って下さい。",
                        this.getWindow());
            }
        } catch (IOException e) {
            Tools.Conrtols.alert(AlertType.ERROR,
                    "自動プロキシ構成スクリプトファイル",
                    "自動プロキシ構成スクリプトファイルの生成に失敗しました",
                    e,
                    this.getWindow());
        }
    }

}
