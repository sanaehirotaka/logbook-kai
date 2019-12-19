package logbook.internal.gui;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import logbook.bean.AppDeck;
import logbook.bean.AppDeck.AppDeckFleet;
import logbook.bean.AppDeckCollection;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.internal.ToStringConverter;

/**
 * 編成記録
 *
 */
public class Deck extends WindowController {

    /** 左ペイン */
    @FXML
    private VBox left;

    /** 編成リスト */
    @FXML
    private ListView<AppDeck> deckList;

    /** 艦隊リスト */
    @FXML
    private ListView<DeckFleetPane> fleetList;

    /** 右ペイン */
    @FXML
    private VBox right;

    /** 保存ステータス */
    @FXML
    private Label status;

    /** 編成テンプレート */
    @FXML
    private ComboBox<DeckPort> preFleetList;

    /** 編成 */
    @FXML
    private VBox deck;

    /** 名前 */
    @FXML
    private TextField deckName;

    /** 艦隊 */
    @FXML
    private TilePane fleets;

    /** 選択している編成 */
    private ObjectProperty<AppDeck> currentDeck = new SimpleObjectProperty<>();

    /** 変更検知 */
    private BooleanProperty modified = new SimpleBooleanProperty(false);

    @FXML
    void initialize() {
        this.right.setDisable(true);
        this.fleetList.setDisable(true);
        // 編成記録のリスト
        AppDeckCollection.get().getDecks()
                .forEach(this.deckList.getItems()::add);
        this.deckList.getSelectionModel().selectedItemProperty()
                .addListener((ov) -> {
                    this.currentDeck.set(this.deckList.getSelectionModel().getSelectedItem());
                });
        this.deckList.getItems().addListener(this::changeDeckList);
        this.deckList.setCellFactory(e -> new DeckCell());
        // 艦隊リスト
        this.fleetList.setCellFactory(e -> new DeckFleetCell());
        // テンプレート
        this.preFleetList.getItems().addAll(DeckPortCollection.get().getDeckPortMap().values());
        this.preFleetList.setConverter(ToStringConverter.of(DeckPort::getName));
        // 艦隊が変更された時のリスナー
        this.fleets.getChildren().addListener(this::changeDeckFleet);
        // 編成記録が変更された時のリスナー
        this.currentDeck.addListener(this::changeCurrent);
        // 名前が変更された時のリスナー
        this.deckName.textProperty().addListener((ov, o, n) -> this.modified.set(true));
    }

    @FXML
    void addNewDeck(ActionEvent event) {
        AppDeck deck = new AppDeck();
        deck.setName("新しい編成");
        this.currentDeck.set(deck);
        this.deckList.getItems().add(deck);
        this.deckList.getSelectionModel().select(deck);
    }

    @FXML
    void addPreFleet(ActionEvent event) {
        AppDeck deck = this.currentDeck.get();
        DeckPort port = this.preFleetList.getSelectionModel().getSelectedItem();
        if (deck != null && port != null) {
            AppDeckFleet fleet = AppDeckFleet.toAppDeckFleet(port);
            DeckFleetPane pane = new DeckFleetPane(fleet);
            pane.getModified().addListener((ov, o, n) -> this.modified.set(true));
            this.fleets.setPrefColumns(Math.min(this.fleets.getChildren().size() + 1, 2));
            this.fleets.getChildren().add(pane);
        }
    }

    @FXML
    void save(ActionEvent event) {
        AppDeck deck = this.currentDeck.get();
        this.save(deck);
    }

    @FXML
    void storeImage(ActionEvent event) {
        if (this.currentDeck.get() != null) {
            Tools.Conrtols.storeSnapshot(this.deck, "編成記録_" + this.currentDeck.get().getName(), this.getWindow());
        }
    }

    @FXML
    void up(ActionEvent event) {
        AppDeck selectedDeck = this.deckList.getSelectionModel()
                .getSelectedItem();
        if (selectedDeck != null) {
            List<AppDeck> decks = this.deckList.getItems();
            int index = decks.indexOf(selectedDeck);
            if (index > 0) {
                decks.remove(index);
                decks.add(index - 1, selectedDeck);
                this.deckList.getSelectionModel().select(selectedDeck);
                this.deckList.requestFocus();
            }
        }
    }

    @FXML
    void down(ActionEvent event) {
        AppDeck selectedDeck = this.deckList.getSelectionModel()
                .getSelectedItem();
        if (selectedDeck != null) {
            List<AppDeck> decks = this.deckList.getItems();
            int index = decks.indexOf(selectedDeck);
            if (index > -1 && (decks.size() - 1) > index) {
                decks.remove(index);
                decks.add(index + 1, selectedDeck);
                this.deckList.getSelectionModel().select(selectedDeck);
                this.deckList.requestFocus();
            }
        }
    }

    /**
     * 編成記録の保存
     *
     * @param deck 編成
     */
    private void save(AppDeck deck) {
        if (deck != null) {
            deck.setName(this.deckName.getText());
            deck.getFleets().clear();
            for (Node node : this.fleets.getChildren()) {
                if (node instanceof DeckFleetPane) {
                    DeckFleetPane fleet = (DeckFleetPane) node;
                    deck.getFleets().add(fleet.getBean());
                }
            }
        }
        this.modified.set(false);
        this.deckList.refresh();
    }

    /**
     * 選択している編成記録が変更された時のリスナー
     *
     * @param ob
     * @param oldValue
     * @param newValue
     */
    private void changeCurrent(ObservableValue<? extends AppDeck> ob, AppDeck oldValue, AppDeck newValue) {
        // 編成記録が保存されていない場合に確認する
        if (oldValue != null && this.modified.get()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.getDialogPane().getStylesheets().add("logbook/gui/application.css");
            InternalFXMLLoader.setGlobal(alert.getDialogPane());
            alert.initOwner(this.deckList.getScene().getWindow());
            alert.setTitle("編成記録の保存");
            alert.setContentText("保存されていない編成記録「" + this.deckName.getText() + "」を保存しますか?");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().orElse(null) == ButtonType.YES) {
                this.save(oldValue);
            }
        }
        // クリア
        this.fleetList.getItems().clear();
        this.fleets.getChildren().clear();
        this.deckName.setText(null);
        AppDeck value = this.currentDeck.get();
        if (value != null) {
            this.right.setDisable(false);
            this.fleetList.setDisable(false);

            this.deckName.setText(value.getName());
            this.fleets.setPrefColumns(Math.min(value.getFleets().size(), 2));
            value.getFleets().forEach(f -> {
                DeckFleetPane pane = new DeckFleetPane(f);
                pane.getModified().addListener((ov, o, n) -> this.modified.set(true));
                this.fleets.getChildren().add(pane);
            });
        } else {
            this.right.setDisable(true);
            this.fleetList.setDisable(true);
        }
        this.status.setText(null);
        this.modified = new SimpleBooleanProperty(false);
        this.modified.addListener((ov, o, n) -> {
            if (n) {
                this.status.setText("保存されていません");
            } else {
                this.status.setText(null);
            }
        });
    }

    /**
     * 編成記録のリストが変更された時のリスナー
     *
     */
    private void changeDeckList(Change<?> change) {
        AppDeckCollection.get().getDecks().clear();
        AppDeckCollection.get().getDecks().addAll(this.deckList.getItems());
    }

    /**
     * 艦隊が変更された時のリスナー
     */
    private void changeDeckFleet(Change<?> change) {
        this.fleetList.getItems().clear();
        for (Node node : this.fleets.getChildren()) {
            if (node instanceof DeckFleetPane) {
                DeckFleetPane fleet = (DeckFleetPane) node;
                this.fleetList.getItems().add(fleet);
            }
        }
        this.modified.set(true);
    }

    /**
     * 編成記録のリストセル
     *
     */
    private class DeckCell extends ListCell<AppDeck> {
        @Override
        protected void updateItem(AppDeck item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null) {
                    Label text = new Label(item.getName());
                    Pane pane = new Pane();
                    Button del = new Button("除去");
                    del.getStyleClass().add("delete");
                    del.setOnAction(e -> {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.getDialogPane().getStylesheets().add("logbook/gui/application.css");
                        InternalFXMLLoader.setGlobal(alert.getDialogPane());
                        alert.initOwner(Deck.this.deckList.getScene().getWindow());
                        alert.setTitle("編成記録の除去");
                        alert.setContentText("「" + item.getName() + "」を除去しますか?");
                        alert.getButtonTypes().clear();
                        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                        if (alert.showAndWait().orElse(null) == ButtonType.YES) {
                            Deck.this.modified.set(false);
                            Deck.this.deckList.getItems().remove(item);
                        }
                    });
                    HBox box = new HBox(text, pane, del);
                    HBox.setHgrow(pane, Priority.ALWAYS);

                    this.setGraphic(box);
                } else {
                    this.setGraphic(null);
                }
            } else {
                this.setGraphic(null);
            }
        }
    }

    /**
     * 編成記録の艦隊のリストセル
     *
     */
    private class DeckFleetCell extends ListCell<DeckFleetPane> {
        @Override
        protected void updateItem(DeckFleetPane item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null) {
                    Label text = new Label();
                    text.textProperty().bind(item.getFleetName().textProperty());
                    Pane pane = new Pane();
                    Button del = new Button("除去");
                    del.getStyleClass().add("delete");
                    del.setOnAction(e -> {
                        Deck.this.fleetList.getItems().remove(item);
                        Deck.this.fleets.getChildren().removeIf(node -> node == item);
                    });
                    HBox box = new HBox(text, pane, del);
                    HBox.setHgrow(pane, Priority.ALWAYS);

                    this.setGraphic(box);
                } else {
                    this.setGraphic(null);
                }
            } else {
                this.setGraphic(null);
            }
        }
    }
}
