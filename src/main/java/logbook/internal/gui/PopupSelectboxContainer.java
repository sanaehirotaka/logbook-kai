package logbook.internal.gui;

import java.io.IOException;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import logbook.internal.gui.PopupSelectbox.SelectboxCell;

class PopupSelectboxContainer<T> extends VBox {

    @FXML
    private VBox header;

    @FXML
    private Pagination page;

    private ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>();

    private IntegerProperty maxPageItem = new SimpleIntegerProperty(10);

    private ObjectProperty<SelectboxCell<T>> cellFactory = new SimpleObjectProperty<>();

    private ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

    PopupSelectboxContainer() {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/popup-selectbox.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ObservableList<Node> headerContents() {
        return this.header.getChildren();
    }

    void setItems(ObservableList<T> items) {
        this.items.set(items);
    }

    void setCellFactory(SelectboxCell<T> factory) {
        this.cellFactory.set(factory);
    }

    void setSelectedItem(ObjectProperty<T> selectedItem) {
        this.selectedItem = selectedItem;
    }

    void init() {
        ObservableList<T> items = this.items.get();
        if (items != null) {
            items.addListener(this::reset);
            this.page.setPageCount((int) Math.max(Math.ceil(((double) items.size()) / this.maxPageItem.get()), 1));
        }
        this.page.setPageFactory(new PageFactory());
        this.page.setMaxPageIndicatorCount(7);
    }

    void reset(Change<? extends T> change) {
        ObservableList<T> items = this.items.get();
        this.page.setPageCount((int) Math.max(Math.ceil(((double) items.size()) / this.maxPageItem.get()), 1));
        this.page.setCurrentPageIndex(0);
        this.page.setPageFactory(new PageFactory());
    }

    void selectHandle(T selectedItem) {
        this.selectedItem.setValue(selectedItem);
    }

    class PageFactory implements Callback<Integer, Node> {
        @Override
        public Node call(Integer page) {
            VBox box = new VBox();

            List<T> items = PopupSelectboxContainer.this.items.get();
            if (items != null) {
                int start = PopupSelectboxContainer.this.maxPageItem.get() * page;
                int end = Math.min(start + PopupSelectboxContainer.this.maxPageItem.get(), items.size());
                for (int i = start; i < end; i++) {
                    Button button = new Button();
                    button.getStyleClass().add("popup-selectbox-menu");
                    T item = items.get(i);
                    SelectboxCell<T> factory = PopupSelectboxContainer.this.cellFactory.get();
                    if (factory != null) {
                        factory.changed(item, false);
                        button.setText(factory.getText());
                        button.setGraphic(factory.getGraphic());
                    } else {
                        button.setText(String.valueOf(item));
                    }
                    button.setOnAction(e -> {
                        PopupSelectboxContainer.this.selectHandle(item);
                        button.getScene().getWindow().hide();
                    });
                    box.getChildren().add(button);
                }
            }
            return box;
        }
    }
}
