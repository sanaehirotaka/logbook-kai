package logbook.internal.gui;

import java.util.WeakHashMap;
import java.util.function.BiFunction;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Screen;

/**
 * ノードへのマウスオーバー時に指定されたノードの内容をマウス位置に表示する
 *
 */
public class PopOver<T> {

    /** ポップアップ */
    protected Popup popup;

    /** ポップアップの内容 */
    protected BiFunction<Node, T, Parent> nodeSupplier;

    /** ユーザーデータ */
    protected WeakHashMap<Node, T> userData = new WeakHashMap<>();

    /**
     * コンストラクタ
     *
     * @param nodeSupplier ポップアップの内容を供給する{@link BiFunction}
     */
    public PopOver(BiFunction<Node, T, Parent> nodeSupplier) {
        this.popup = new Popup();
        this.nodeSupplier = nodeSupplier;
    }

    /**
     * 指定されたノードへポップアップを設定します
     *
     * @param anchorNode アンカーノード
     * @param userData ユーザーデータ
     */
    public void install(Node anchorNode, T userData) {
        anchorNode.setOnMouseEntered(this::setOnMouseEntered);
        anchorNode.setOnMouseMoved(this::setOnMouseMoved);
        anchorNode.setOnMouseExited(this::setOnMouseExited);
        this.userData.put(anchorNode, userData);
    }

    /**
     * ポップアップを初期化します
     *
     * @param anchorNode アンカーノード
     * @return ポップアップ
     */
    protected Popup initPopup(Node anchorNode) {
        Parent node = this.nodeSupplier.apply(anchorNode, this.userData.get(anchorNode));
        node.setOnMouseExited(this::setOnMouseExited);
        this.popup.getScene().setRoot(node);
        return this.popup;
    }

    /**
     * マウスがこのアンカーノードに入るときに呼び出される関数を定義します。
     *
     * @param event {@link MouseEvent}
     */
    protected void setOnMouseEntered(MouseEvent event) {
        Node anchorNode = (Node) event.getSource();
        Popup popup = this.initPopup(anchorNode);
        Bounds screen = anchorNode.localToScreen(anchorNode.getLayoutBounds());
        popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
        popup.show(anchorNode.getScene().getWindow(), screen.getMinX(), screen.getMaxY());
        this.setLocation(popup, anchorNode, event);
    }

    /**
     * このアンカーノード内をマウス・カーソルが移動するが、ボタンが押されなかった場合に呼び出される関数を定義します。
     *
     * @param event {@link MouseEvent}
     */
    protected void setOnMouseMoved(MouseEvent event) {
        if (this.popup.isShowing()) {
            Node anchorNode = (Node) event.getSource();
            this.setLocation(this.popup, anchorNode, event);
        }
    }

    /**
     * マウスがこのアンカーノードから出るときに呼び出される関数を定義します。
     *
     * @param event {@link MouseEvent}
     */
    protected void setOnMouseExited(MouseEvent event) {
        this.popup.hide();
    }

    /**
     * 表示位置のギャップを取得します
     * @return 表示位置のギャップ
     */
    protected double getGapSize() {
        return 12;
    }

    /**
     * ポップアップの表示位置を設定します
     *
     * @param popup ポップアップ
     * @param anchorNode アンカーノード
     * @param event {@link MouseEvent}
     */
    protected void setLocation(Popup popup, Node anchorNode, MouseEvent event) {
        double x = event.getScreenX();
        double y = event.getScreenY();
        double width = popup.getWidth();
        double height = popup.getHeight();
        double gapSize = this.getGapSize();

        PopupWindow.AnchorLocation anchorLocation = PopupWindow.AnchorLocation.CONTENT_TOP_LEFT;
        double gapX = gapSize;
        double gapY = gapSize;

        for (Screen screen : Screen.getScreens()) {
            Rectangle2D screenRect = screen.getVisualBounds();

            // 右下 に表示可能であれば
            if (screenRect.contains(x + gapSize, y + gapSize, width, height)) {
                // PopupWindow視点でアンカーノードがTOP_LEFTの位置
                anchorLocation = PopupWindow.AnchorLocation.CONTENT_TOP_LEFT;
                gapX = gapSize;
                gapY = gapSize;
                break;
            }
            // 左下
            if (screenRect.contains(x - gapSize - width, y + gapSize, width, height)) {
                anchorLocation = PopupWindow.AnchorLocation.CONTENT_TOP_RIGHT;
                gapX = -gapSize;
                gapY = gapSize;
                break;
            }
            // 右上
            if (screenRect.contains(x + gapSize, y - gapSize - height, width, height)) {
                anchorLocation = PopupWindow.AnchorLocation.CONTENT_BOTTOM_LEFT;
                gapX = gapSize;
                gapY = -gapSize;
                break;
            }
            // 左上
            if (screenRect.contains(x - gapSize - width, y - gapSize - height, width, height)) {
                anchorLocation = PopupWindow.AnchorLocation.CONTENT_BOTTOM_RIGHT;
                gapX = -gapSize;
                gapY = -gapSize;
                break;
            }
        }

        popup.setAnchorLocation(anchorLocation);
        popup.setAnchorX(x + gapX);
        popup.setAnchorY(y + gapY);
    }
}
