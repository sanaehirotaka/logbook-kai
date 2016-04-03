package logbook.internal.gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.text.MessageFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import logbook.bean.AppConfig;
import logbook.plugin.PluginContainer;

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
            try (InputStream in = PluginContainer.getInstance()
                    .getClassLoader()
                    .getResourceAsStream("logbook/proxy.pac")) {
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
            packFile = MessageFormat.format(new String(out.toByteArray()), AppConfig.get().getListenPort());

            FileChooser fc = new FileChooser();
            fc.setTitle("名前をつけて保存");
            fc.setInitialFileName("proxy.pac");

            File file = fc.showSaveDialog(this.getWindow());
            if (file != null) {

                Files.write(file.toPath(), packFile.getBytes());

                this.addr.setText("file:///" + file.toURI().toString().replaceFirst("file:/", ""));

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.initOwner(this.getWindow());

                alert.setTitle("キャプチャが保存されました");
                alert.setContentText("自動プロキシ構成スクリプトファイルを生成しました。\r\n"
                        + "次にブラウザの設定を行って下さい。");
                alert.show();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(this.getWindow());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            String stackTrace = sw.toString();

            TextArea textArea = new TextArea(stackTrace);
            alert.getDialogPane().setExpandableContent(textArea);

            alert.setTitle("自動プロキシ構成スクリプトファイルの生成に失敗しました");
            alert.setHeaderText("自動プロキシ構成スクリプトファイルの生成に失敗しました");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

}
