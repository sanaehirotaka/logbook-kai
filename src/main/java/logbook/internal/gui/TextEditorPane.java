package logbook.internal.gui;

import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import logbook.internal.LoggerHolder;
import logbook.plugin.PluginServices;
import netscape.javascript.JSObject;

/**
 * 簡易テキストエディタ
 */
class TextEditorPane extends AnchorPane {

    @FXML
    private WebView webview;

    private String lang;

    private String source;

    private Boolean readOnly;

    public TextEditorPane() {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/text_editor_pane.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        WebEngine engine = this.webview.getEngine();
        engine.load(PluginServices.getResource("logbook/gui/text_editor_pane.html").toString());
        engine.getLoadWorker().stateProperty().addListener(
                (ob, o, n) -> {
                    if (n == Worker.State.SUCCEEDED) {
                        this.setting();
                    }
                });

        KeyCombination copy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        KeyCombination cut = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);

        this.webview.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (copy.match(event) || cut.match(event)) {
                String text = String.valueOf(engine.executeScript("getCopyText()"));

                Platform.runLater(() -> {
                    ClipboardContent content = new ClipboardContent();
                    content.putString(text);
                    Clipboard.getSystemClipboard().setContent(content);
                });
            }
        });
    }

    private void setting() {
        WebEngine engine = this.webview.getEngine();
        JSObject window = (JSObject) engine.executeScript("window");
        Object ace = null;
        try {
            ace = engine.executeScript("ace");
        } catch (Exception e) {
        }
        if (ace != null && ace != engine.executeScript("undefined")) {
            if (this.lang != null) {
                window.call("start", this.lang);
                this.lang = null;
            }
            if (this.source != null) {
                window.call("set", this.source);
                this.source = null;
            }
            if (this.readOnly != null) {
                window.call("setReadOnly", this.readOnly);
                this.readOnly = null;
            }
        }
    }

    /**
     * 言語を指定して開始します
     * @param lang 言語
     */
    public void start(String lang) {
        this.lang = lang;
        this.setting();
    }

    /**
     * エディタの内容を設定します
     * @param source エディタの内容
     */
    public void set(String source) {
        this.source = source;
        this.setting();
    }

    /**
     * エディタの内容を取得します
     * @return エディタの内容
     */
    public String get() {
        WebEngine engine = this.webview.getEngine();
        JSObject window = (JSObject) engine.executeScript("window");
        return String.valueOf(window.call("get"));
    }

    /**
     * エディタを読み取り専用にします
     * @param readOnly 読み取り専用
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        this.setting();
    }
}
