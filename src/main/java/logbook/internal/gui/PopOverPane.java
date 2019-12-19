package logbook.internal.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logbook.internal.LoggerHolder;

/**
 * {@link PopOver}のルートノードとして使用する。
 *
 */
public class PopOverPane extends VBox {

    @FXML
    private Label title;

    private StringProperty titleProperty = new SimpleStringProperty();

    private ObjectProperty<Node> graphicProperty = new SimpleObjectProperty<>();

    /**
     * タイトルと内容テキストを持つ{@code PopOverPane}を作成します。
     *
     * @param title タイトル
     * @param text 内容テキスト
     */
    public PopOverPane(String title, String text) {
        this(title, new Label(text));
    }

    /**
     * タイトルと内容ノードを持つ{@code PopOverPane}を作成します。
     *
     * @param title タイトル
     * @param graphic 内容ノード
     */
    public PopOverPane(String title, Node graphic) {
        this.titleProperty.setValue(title);
        this.graphicProperty.setValue(graphic);
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/pop-over.fxml");
            loader.setRoot(this);
            loader.setController(this);
            InternalFXMLLoader.setGlobal(loader.load());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.title.setText(this.titleProperty.get());
        if (this.graphicProperty.get() != null) {
            this.getChildren().add(this.graphicProperty.get());
        }
    }
}
