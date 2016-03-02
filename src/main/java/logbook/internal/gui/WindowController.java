package logbook.internal.gui;

import javafx.stage.Stage;

/**
 * ウインドウを持つコントローラー
 *
 */
public class WindowController {

    /** このコントローラーに紐づくウインドウ */
    private Stage window;


    /**
     * このコントローラーに紐づくウインドウを取得します。
     * @return このコントローラーに紐づくウインドウ
     */
    public Stage getWindow() {
        return this.window;
    }

    /**
     * このコントローラーに紐づくウインドウを設定します。
     * @param window このコントローラーに紐づくウインドウ
     */
    public void setWindow(Stage window) {
        this.window = window;
    }
}
