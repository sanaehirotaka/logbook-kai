package logbook.internal.gui;

import javafx.fxml.FXML;
import logbook.proxy.ProxyServer;

/**
 * UIコントローラー
 *
 */
public class MainController {

    @FXML
    void initialize() {
        // サーバーの起動に失敗した場合にダイアログを表示するために、UIスレッドの初期化後にサーバーを起動する必要がある
        ProxyServer.getInstance().start();
    }
}
