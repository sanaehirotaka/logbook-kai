package logbook.internal.gui;

import javafx.stage.Screen;
import javafx.stage.Stage;
import logbook.bean.WindowLocation;

/**
 * ウインドウを持つコントローラー
 *
 */
public abstract class WindowController {

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

    /**
     * ウインドウの位置とサイズを設定します。
     * @param location ウインドウの位置とサイズ
     */
    public void setWindowLocation(WindowLocation location) {
        if (location != null) {
            boolean intersect = Screen.getScreens()
                    .stream()
                    .map(Screen::getVisualBounds)
                    .anyMatch(r -> r.intersects(
                            location.getX(), location.getY(), location.getWidth(), location.getHeight()));

            if (intersect) {
                this.window.setX(location.getX());
                this.window.setY(location.getY());
                this.window.setWidth(location.getWidth());
                this.window.setHeight(location.getHeight());
            }
        }
    }

    /**
     * ウインドウの位置とサイズを取得します。
     * @return ウインドウの位置とサイズ
     */
    public WindowLocation getWindowLocation() {
        WindowLocation location = new WindowLocation();
        location.setX(this.window.getX());
        location.setY(this.window.getY());
        location.setWidth(this.window.getWidth());
        location.setHeight(this.window.getHeight());
        return location;
    }
}
