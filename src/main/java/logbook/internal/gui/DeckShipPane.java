package logbook.internal.gui;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
import logbook.internal.Ships;
import lombok.Getter;

/**
 * 編成記録の艦娘ペイン
 *
 */
public class DeckShipPane extends VBox {

    /** 艦娘画像 */
    @FXML
    private ImageView shipImage;

    /** 艦娘Lv */
    @FXML
    private Label shipLv;

    /** 艦娘選択コンボボックス */
    @FXML
    private ComboBox<Integer> shipList;

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
            this.ship.setShipId(this.shipList.getValue());

            this.ship.getItems().clear();
            this.ship.getItems().add(Optional.ofNullable(this.itemList1.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList2.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList3.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(Optional.ofNullable(this.itemList4.getValue())
                    .map(SlotitemMst::getId).orElse(null));
            this.ship.getItems().add(null);
            this.ship.getItems().add(Optional.ofNullable(this.itemList5.getValue())
                    .map(SlotitemMst::getId).orElse(null));

            this.ship.getItemLvs().clear();
            this.ship.getItemLvs().add(this.itemLvList1.getValue());
            this.ship.getItemLvs().add(this.itemLvList2.getValue());
            this.ship.getItemLvs().add(this.itemLvList3.getValue());
            this.ship.getItemLvs().add(this.itemLvList4.getValue());
            this.ship.getItemLvs().add(null);
            this.ship.getItemLvs().add(this.itemLvList5.getValue());
        }
        this.modified.set(false);
        return this.ship;
    }

    @FXML
    void initialize() {
        this.installShip();
        this.installItemName(this.itemList1, this.itemImage1, this.itemName1);
        this.installItemName(this.itemList2, this.itemImage2, this.itemName2);
        this.installItemName(this.itemList3, this.itemImage3, this.itemName3);
        this.installItemName(this.itemList4, this.itemImage4, this.itemName4);
        this.installItemName(this.itemList5, this.itemImage5, this.itemName5);
        this.installItemLevel(this.itemLvList1, this.itemLv1);
        this.installItemLevel(this.itemLvList2, this.itemLv2);
        this.installItemLevel(this.itemLvList3, this.itemLv3);
        this.installItemLevel(this.itemLvList4, this.itemLv4);
        this.installItemLevel(this.itemLvList5, this.itemLv5);

        this.shipList.getSelectionModel().select(this.ship.getShipId());
        Map<Integer, SlotitemMst> itemMstMap = SlotitemMstCollection.get().getSlotitemMap();
        this.itemList1.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(0)));
        this.itemList2.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(1)));
        this.itemList3.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(2)));
        this.itemList4.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(3)));
        this.itemList5.getSelectionModel().select(itemMstMap.get(this.ship.getItems().get(5)));
        this.itemLvList1.getSelectionModel().select(this.ship.getItemLvs().get(0));
        this.itemLvList2.getSelectionModel().select(this.ship.getItemLvs().get(1));
        this.itemLvList3.getSelectionModel().select(this.ship.getItemLvs().get(2));
        this.itemLvList4.getSelectionModel().select(this.ship.getItemLvs().get(3));
        this.itemLvList5.getSelectionModel().select(this.ship.getItemLvs().get(5));

        this.initialized = true;
    }

    /**
     * 艦娘の初期設定
     */
    private void installShip() {
        // 艦娘選択コンボボックスに艦娘を設定
        this.shipList.getItems().add(0);
        this.shipList.getItems().addAll(ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Ship::getShipId))
                .sorted(Comparator.comparing(Ship::getLv).reversed())
                .map(Ship::getId)
                .collect(Collectors.toList()));
        this.shipList.setCellFactory(e -> new ShipCell());

        // 選ばれたときのリスナー
        this.shipList.getSelectionModel().selectedItemProperty().addListener(this::shipChangeListener);
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
        this.itemLvList1.getSelectionModel().clearSelection();
        this.itemLvList2.getSelectionModel().clearSelection();
        this.itemLvList3.getSelectionModel().clearSelection();
        this.itemLvList4.getSelectionModel().clearSelection();
        this.itemLvList5.getSelectionModel().clearSelection();

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
                this.presetItem(itemMap.get(ship.getSlotEx()), this.itemList5, this.itemLvList5);
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
        list.setConverter(ItemNameStringConverter.instance);
        list.setCellFactory(e -> new ItemNameCell());
        // 装備が選ばれた時のリスナー
        list.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
            if (this.initialized) {
                // 装備の変更を検知
                this.modified.set(true);
            }
            image.setImage(Items.itemImage(n));
            label.setText(ItemNameStringConverter.instance.toString(n));
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
    static class ShipCell extends ListCell<Integer> {
        @Override
        protected void updateItem(Integer shipid, boolean empty) {
            super.updateItem(shipid, empty);
            if (!empty) {
                Ship ship = ShipCollection.get().getShipMap().get(shipid);
                if (ship != null) {
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
                    this.setText(ItemNameStringConverter.instance.toString(item));
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
     * 装備のStringConverter
     *
     */
    static class ItemNameStringConverter extends StringConverter<SlotitemMst> {

        static final StringConverter<SlotitemMst> instance = new ItemNameStringConverter();

        @Override
        public String toString(SlotitemMst object) {
            if (object != null) {
                return object.getName();
            }
            return "";
        }

        @Override
        public SlotitemMst fromString(String string) {
            return null;
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
