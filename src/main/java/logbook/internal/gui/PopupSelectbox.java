package logbook.internal.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

public class PopupSelectbox<T> {

    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(FXCollections.observableArrayList());

    private ObjectProperty<SelectboxCell<T>> cellFactory = new SimpleObjectProperty<>();

    private ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

    private ObservableList<String> styleSheets = FXCollections.observableArrayList();

    private ObservableList<Node> headerContents = FXCollections.observableArrayList();

    public void setItems(ObservableList<T> items) {
        this.items.set(items);
    }

    public ObservableList<T> getItems() {
        return this.items.get();
    }

    public void setCellFactory(SelectboxCell<T> factory) {
        this.cellFactory.set(factory);
    }

    public SelectboxCell<T> getCellFactory() {
        return this.cellFactory.get();
    }

    public ObjectProperty<T> selectedItemProperty() {
        return this.selectedItem;
    }

    public ObservableList<String> getStylesheets() {
        return this.styleSheets;
    }

    public ObservableList<Node> getHeaderContents() {
        return this.headerContents;
    }

    public void show(Node anchorNode) {
        PopupSelectboxContainer<T> container = new PopupSelectboxContainer<>();
        container.setItems(this.items.get());
        container.setCellFactory(this.cellFactory.get());
        container.setSelectedItem(this.selectedItem);
        container.getStylesheets().addAll(this.styleSheets);
        container.headerContents().addAll(this.headerContents);
        container.init();

        Popup stage = new Popup();
        stage.getContent().addAll(container);
        stage.setAutoHide(true);
        stage.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);

        Bounds screen = anchorNode.localToScreen(anchorNode.getLayoutBounds());
        stage.show(anchorNode.getScene().getWindow(), screen.getMinX(), screen.getMaxY());
    }

    public static class SelectboxCell<T> extends Cell<T> {
        void changed(T item, boolean empty) {
            this.updateItem(item, empty);
        }
    }
}
