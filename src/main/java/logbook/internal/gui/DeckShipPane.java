package logbook.internal.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import logbook.bean.AppDeck.AppDeckShip;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.ShipFilter;
import logbook.internal.ShipTypeGroup;
import logbook.internal.Ships;
import logbook.internal.ToStringConverter;
import logbook.internal.gui.PopupSelectbox.SelectboxCell;
import lombok.Getter;

/**
 * 編成記録の艦娘ペイン
 *
 */
public class DeckShipPane extends VBox {

    /** 装備のStringConverter */
    private static StringConverter<SlotitemMst> itemNameStringConverter = ToStringConverter.of(SlotitemMst::getName);

    /** 艦娘画像 */
    @FXML
    private ImageView shipImage;

    /** 艦娘Lv */
    @FXML
    private Label shipLv;

    /** 艦娘選択 */
    @FXML
    private Button shipButton;

    /** 装備1画像 */
    @FXML
    private ImageView itemImage1;

    /** 装備1名前 */
    @FXML
    private Label itemName1;

    /** 装備1改修レベル */
    @FXML
    private Label itemLv1;

    /** 装備1選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList1;

    /** 装備1改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList1;

    /** 装備2画像 */
    @FXML
    private ImageView itemImage2;

    /** 装備2名前 */
    @FXML
    private Label itemName2;

    /** 装備2改修レベル */
    @FXML
    private Label itemLv2;

    /** 装備2選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList2;

    /** 装備2改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList2;

    /** 装備3画像 */
    @FXML
    private ImageView itemImage3;

    /** 装備3名前 */
    @FXML
    private Label itemName3;

    /** 装備3改修レベル */
    @FXML
    private Label itemLv3;

    /** 装備3選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList3;

    /** 装備3改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList3;

    /** 装備4画像 */
    @FXML
    private ImageView itemImage4;

    /** 装備4名前 */
    @FXML
    private Label itemName4;

    /** 装備4改修レベル */
    @FXML
    private Label itemLv4;

    /** 装備4選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList4;

    /** 装備4改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList4;

    /** 装備5画像 */
    @FXML
    private ImageView itemImage5;

    /** 装備5名前 */
    @FXML
    private Label itemName5;

    /** 装備5改修レベル */
    @FXML
    private Label itemLv5;

    /** 装備5選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList5;

    /** 装備5改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList5;

    /** 装備Ex画像 */
    @FXML
    private ImageView itemImage6;

    /** 装備Ex名前 */
    @FXML
    private Label itemName6;

    /** 装備Ex改修レベル */
    @FXML
    private Label itemLv6;

    /** 装備Ex選択肢 */
    @FXML
    private ComboBox<SlotitemMst> itemList6;

    /** 装備Ex改修レベル選択肢 */
    @FXML
    private ComboBox<Integer> itemLvList6;

    /** 選択した艦娘 */
    private ObjectProperty<Integer> selectedShip = new SimpleObjectProperty<>(0);

    /** 艦娘 */
    private AppDeckShip ship = new AppDeckShip();

    /** 変更検知 */
    @Getter
    private BooleanProperty modified = new SimpleBooleanProperty(false);

    private boolean initialized = false;

    /**
     * 編成記録の艦娘ペインのコンストラクタ
     *
     * @param ship 艦娘
     */
    public DeckShipPane(AppDeckShip ship) {
        this.ship = ship;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/deck_ship.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    /**
     * 艦娘を取得します
     *
     * @return 編成記録の艦娘
     */
    public AppDeckShip getBean() {
        if (this.modified.get()) {
            this.ship.setShipId(this.selectedShip.get());

            this.ship.getItems().clear();
            this.ship.getItems().add(Optional.ofNullable(this.itemList1.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList2.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList3.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList4.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList5.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList6.getValue())
                    .map(SlotitemMst::getId).orElse(null));

            this.ship.getItemLvs().clear();
            this.ship.getItemLvs().add(this.itemLvList1.getValue());
            this.ship.getItemLvs().add(this.itemLvList2.getValue());
            this.ship.getItemLvs().add(this.itemLvList3.getValue());
            this.ship.getItemLvs().add(this.itemLvList4.getValue());
            this.ship.getItemLvs().add(this.itemLvList5.getValue());
            this.ship.getItemLvs().add(this.itemLvList6.getValue());
        }
        this.modified.set(false);
        return this.ship;
    }

    @FXML
    void initialize() {
        this.selectedShip.addListener(this::shipChangeListener);
        this.selectedShip.set(this.ship.getShipId());

        this.installItemName(this.itemList1, this.itemImage1, this.itemName1);
        this.installItemName(this.itemList2, this.itemImage2, this.itemName2);
        this.installItemName(this.itemList3, this.itemImage3, this.itemName3);
        this.installItemName(this.itemList4, this.itemImage4, this.itemName4);
        this.installItemName(this.itemList5, this.itemImage5, this.itemName5);
        this.installItemName(this.itemList6, this.itemImage6, this.itemName6);
        this.installItemLevel(this.itemLvList1, this.itemLv1);
        this.installItemLevel(this.itemLvList2, this.itemLv2);
        this.installItemLevel(this.itemLvList3, this.itemLv3);
        this.installItemLevel(this.itemLvList4, this.itemLv4);
        this.installItemLevel(this.itemLvList5, this.itemLv5);
        this.installItemLevel(this.itemLvList6, this.itemLv6);

        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get().getSlotitemMap();
        this.itemList1.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(0)));
        this.itemList2.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(1)));
        this.itemList3.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(2)));
        this.itemList4.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(3)));
        this.itemList5.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(4)));
        this.itemList6.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(5)));
        this.itemLvList1.getSelectionModel().select(this.ship.getItemLvs().get(0));
        this.itemLvList2.getSelectionModel().select(this.ship.getItemLvs().get(1));
        this.itemLvList3.getSelectionModel().select(this.ship.getItemLvs().get(2));
        this.itemLvList4.getSelectionModel().select(this.ship.getItemLvs().get(3));
        this.itemLvList5.getSelectionModel().select(this.ship.getItemLvs().get(4));
        this.itemLvList6.getSelectionModel().select(this.ship.getItemLvs().get(5));

        this.initialized = true;
    }

    /**
     * 艦娘選択
     *
     * @param event
     */
    @FXML
    void shipChange(ActionEvent event) {
        PopupSelectbox<ShipItem> box = new PopupSelectbox<>();

        ObservableList<ShipItem> ships = FXCollections.observableArrayList();
        ships.add(new ShipItem());
        ships.addAll(ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Ship::getShipId))
                .sorted(Comparator.comparing(Ship::getLv).reversed())
                .map(ShipItem::toShipItem)
                .collect(Collectors.toList()));

        FilteredList<ShipItem> filtered = new FilteredList<>(ships);
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(260);
        for (ShipTypeGroup group : ShipTypeGroup.values()) {
            CheckBox checkBox = new CheckBox(group.name());
            checkBox.selectedProperty().addListener((ov, o, n) -> {
                Set<String> types = new HashSet<>();
                textFlow.getChildrenUnmodifiable()
                        .stream()
                        .filter(node -> node instanceof CheckBox)
                        .map(node -> (CheckBox) node)
                        .filter(CheckBox::isSelected)
                        .map(CheckBox::getText)
                        .map(ShipTypeGroup::valueOf)
                        .map(ShipTypeGroup::shipTypes)
                        .forEach(types::addAll);

                filtered.setPredicate(ShipFilter.TypeFilter.builder()
                        .types(types)
                        .build());
            });
            textFlow.getChildren().add(checkBox);
        }
        TitledPane titledPane = new TitledPane("フィルター", new VBox(textFlow));
        titledPane.setAnimated(false);
        Accordion accordion = new Accordion(titledPane);

        box.getHeaderContents().add(accordion);
        box.setItems(filtered);
        box.setCellFactory(new ShipCell());
        box.selectedItemProperty().addListener((ov, o, n) -> {
            this.selectedShip.setValue(n.idProperty() != null ? n.getId() : 0);
        });
        box.getStylesheets().add("logbook/gui/deck.css");
        box.show(this.shipButton);
    }

    /**
     * 艦娘が選ばれたときのリスナー
     *
     * @param ov 値が変更されたObservableValueoldValue
     * @param o 古い値
     * @param n 新しい値
     */
    private void shipChangeListener(ObservableValue<? extends Integer> ov, Integer o, Integer n) {
        if (this.initialized) {
            // 艦娘の変更を検知
            this.modified.set(true);
        }
        Ship ship = ShipCollection.get().getShipMap().get(n);

        this.itemList1.getSelectionModel().clearSelection();
        this.itemList2.getSelectionModel().clearSelection();
        this.itemList3.getSelectionModel().clearSelection();
        this.itemList4.getSelectionModel().clearSelection();
        this.itemList5.getSelectionModel().clearSelection();
        this.itemList6.getSelectionModel().clearSelection();
        this.itemLvList1.getSelectionModel().clearSelection();
        this.itemLvList2.getSelectionModel().clearSelection();
        this.itemLvList3.getSelectionModel().clearSelection();
        this.itemLvList4.getSelectionModel().clearSelection();
        this.itemLvList5.getSelectionModel().clearSelection();
        this.itemLvList6.getSelectionModel().clearSelection();

        if (ship != null) {
            // 艦娘が変わった
            this.shipImage.setImage(Ships.getBackgroundLoading(ship));
            this.shipLv.setText("Lv" + ship.getLv());
            if (this.initialized) {
                Map<Integer, SlotItem> itemMap = SlotItemCollection.get().getSlotitemMap();
                this.presetItem(itemMap.get(ship.getSlot().get(0)), this.itemList1, this.itemLvList1);
                this.presetItem(itemMap.get(ship.getSlot().get(1)), this.itemList2, this.itemLvList2);
                this.presetItem(itemMap.get(ship.getSlot().get(2)), this.itemList3, this.itemLvList3);
                this.presetItem(itemMap.get(ship.getSlot().get(3)), this.itemList4, this.itemLvList4);
                this.presetItem(itemMap.get(ship.getSlot().get(4)), this.itemList5, this.itemLvList5);
                this.presetItem(itemMap.get(ship.getSlotEx()), this.itemList6, this.itemLvList6);
            }
        } else {
            // 艦娘の選択なしに変更
            this.shipImage.setImage(null);
            this.shipLv.setText("");
        }
    }

    /**
     * 指定された装備をセットする
     *
     * @param item 装備
     * @param itemList 装備コンボボックス
     * @param itemLvList 装備改修コンボボックス
     */
    private void presetItem(SlotItem item, ComboBox<SlotitemMst> itemList, ComboBox<Integer> itemLvList) {
        Items.slotitemMst(item)
                .ifPresent(itemList.getSelectionModel()::select);
        Optional.ofNullable(item)
                .map(SlotItem::getLevel)
                .ifPresent(itemLvList.getSelectionModel()::select);
    }

    /**
     * 装備の初期設定
     *
     * @param list
     * @param image
     * @param label
     */
    private void installItemName(ComboBox<SlotitemMst> list, ImageView image, Label label) {
        list.setConverter(itemNameStringConverter);
        list.setCellFactory(e -> new ItemNameCell());
        // 装備が選ばれた時のリスナー
        list.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            if (this.initialized) {
                // 装備の変更を検知
                this.modified.set(true);
            }
            image.setImage(Items.itemImage(n));
            label.setText(itemNameStringConverter.toString(n));
        });
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get().getSlotitemMap();
        list.getItems().addAll(SlotItemCollection.get()
                .getSlotitemMap()
                .values()
                .stream()
                .map(SlotItem::getSlotitemId)
                .distinct()
                .map(itemMstMap::get)
                .sorted(Comparator.comparing(SlotitemMst::getName))
                .sorted(Comparator.comparing(m -> m.getType().get(3)))
                .collect(Collectors.toList()));
    }

    /**
     * 装備改修レベルの初期設定
     *
     * @param list
     * @param label
     */
    private void installItemLevel(ComboBox<Integer> list, Label label) {
        list.setConverter(ItemLevelStringConverter.instance);
        list.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            label.setText(ItemLevelStringConverter.instance.toString(n));
        });
        list.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    /**
     * 艦娘を表示するセル
     */
    static class ShipCell extends SelectboxCell<ShipItem> {
        @Override
        protected void updateItem(ShipItem shipItem, boolean empty) {
            super.updateItem(shipItem, empty);
            if (!empty) {
                if (shipItem != null && shipItem.shipProperty() != null) {
                    Ship ship = shipItem.getShip();
                    ImageView view = new ImageView(Ships.getBackgroundLoading(ship));
                    view.setFitWidth(160);
                    view.setFitHeight(40);
                    this.setGraphic(view);
                    this.setText(Ships.toName(ship));
                } else {
                    ImageView view = new ImageView();
                    view.setFitWidth(160);
                    view.setFitHeight(40);
                    this.setGraphic(view);
                    this.setText(null);
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 装備のListCell
     *
     */
    static class ItemNameCell extends ListCell<SlotitemMst> {
        @Override
        protected void updateItem(SlotitemMst item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null) {
                    ImageView view = new ImageView(Items.itemImage(item));
                    view.setFitHeight(24);
                    view.setFitWidth(24);
                    this.setGraphic(view);
                    this.setText(itemNameStringConverter.toString(item));
                } else {
                    this.setGraphic(null);
                    this.setText(null);
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 装備改修レベルのStringConverter
     *
     */
    static class ItemLevelStringConverter extends StringConverter<Integer> {

        static final StringConverter<Integer> instance = new ItemLevelStringConverter();

        @Override
        public String toString(Integer object) {
            if (object != null && object > 0) {
                if (object == 10) {
                    return "★Max";
                }
                return "★+" + object;
            }
            return "";
        }

        @Override
        public Integer fromString(String string) {
            return null;
        }
    }
}
