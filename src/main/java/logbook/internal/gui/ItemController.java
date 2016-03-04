package logbook.internal.gui;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemDescription;
import logbook.bean.SlotitemDescriptionCollection;
import logbook.internal.Ships;

/**
 * 所有装備一覧のUIコントローラー
 *
 */
public class ItemController extends WindowController {

    /** 一覧 */
    @FXML
    private TableView<Item> typeTable;

    /** 名称 */
    @FXML
    private TableColumn<Item, Item> name;

    /** 種別 */
    @FXML
    private TableColumn<Item, String> type;

    /** 個数 */
    @FXML
    private TableColumn<Item, Integer> count;

    /** 火力 */
    @FXML
    private TableColumn<Item, Integer> houg;

    /** 命中 */
    @FXML
    private TableColumn<Item, Integer> houm;

    /** 射程 */
    @FXML
    private TableColumn<Item, Integer> leng;

    /** 運 */
    @FXML
    private TableColumn<Item, Integer> luck;

    /** 回避 */
    @FXML
    private TableColumn<Item, Integer> houk;

    /** 爆装 */
    @FXML
    private TableColumn<Item, Integer> baku;

    /** 雷装 */
    @FXML
    private TableColumn<Item, Integer> raig;

    /** 索敵 */
    @FXML
    private TableColumn<Item, Integer> saku;

    /** 対潜 */
    @FXML
    private TableColumn<Item, Integer> tais;

    /** 対空 */
    @FXML
    private TableColumn<Item, Integer> tyku;

    /** 装甲 */
    @FXML
    private TableColumn<Item, Integer> souk;

    /** 詳細・名前 */
    @FXML
    private Label detailName;

    /** 詳細・一覧 */
    @FXML
    private TableView<DetailItem> detailTable;

    /** 熟練 */
    @FXML
    private TableColumn<DetailItem, String> alv;

    /** 改修 */
    @FXML
    private TableColumn<DetailItem, String> level;

    /** 所持 */
    @FXML
    private TableColumn<DetailItem, DetailItem> ship;

    /** 一覧 */
    private ObservableList<Item> typeList = FXCollections.observableArrayList();

    /** 詳細一覧 */
    private ObservableList<DetailItem> detailList = FXCollections.observableArrayList();

    @FXML
    void initialize() {

        // カラムとオブジェクトのバインド
        this.name.setCellValueFactory(new PropertyValueFactory<>("this"));
        this.name.setCellFactory(p -> new ItemImageCell(Item::nameProperty));
        this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.count.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.houg.setCellValueFactory(new PropertyValueFactory<>("houg"));
        this.houm.setCellValueFactory(new PropertyValueFactory<>("houm"));
        this.leng.setCellValueFactory(new PropertyValueFactory<>("leng"));
        this.luck.setCellValueFactory(new PropertyValueFactory<>("luck"));
        this.houk.setCellValueFactory(new PropertyValueFactory<>("houk"));
        this.baku.setCellValueFactory(new PropertyValueFactory<>("baku"));
        this.raig.setCellValueFactory(new PropertyValueFactory<>("raig"));
        this.saku.setCellValueFactory(new PropertyValueFactory<>("saku"));
        this.tais.setCellValueFactory(new PropertyValueFactory<>("tais"));
        this.tyku.setCellValueFactory(new PropertyValueFactory<>("tyku"));
        this.souk.setCellValueFactory(new PropertyValueFactory<>("souk"));

        this.alv.setCellValueFactory(new PropertyValueFactory<>("alv"));
        this.level.setCellValueFactory(new PropertyValueFactory<>("level"));
        this.ship.setCellValueFactory(new PropertyValueFactory<>("this"));
        this.ship.setCellFactory(p -> new ShipImageCell(DetailItem::nameProperty));

        // 行を作る
        List<Item> items = SlotitemDescriptionCollection.get()
                .getSlotitemMap()
                .values()
                .stream()
                .map(Item::toItem)
                .filter(e -> e.getCount() > 0)
                .sorted(Comparator.comparing(Item::getName))
                .sorted(Comparator.comparing(Item::getType3))
                .collect(Collectors.toList());
        this.typeList.addAll(items);
        this.typeTable.setItems(this.typeList);
        // 最初は空のリスト
        this.detailTable.setItems(this.detailList);

        // 装備が選択された時のリスナーを設定
        this.typeTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::detail);
    }

    /**
     * 右ペインに詳細表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void detail(ObservableValue<? extends Item> observable, Item oldValue, Item value) {
        this.detailList.clear();
        if (value != null) {
            // 選択
            this.detailName.setText(value.getName());
            // 行を作る
            List<DetailItem> items = SlotItemCollection.get()
                    .getSlotitemMap()
                    .values()
                    .stream()
                    .filter(e -> e.getSlotitemId().equals(value.getId()))
                    .sorted(Comparator.comparing(SlotItem::getAlv, Comparator.nullsFirst(Comparator.naturalOrder()))
                            .reversed())
                    .sorted(Comparator.comparing(SlotItem::getLevel, Comparator.nullsFirst(Comparator.naturalOrder()))
                            .reversed())
                    .map(DetailItem::toDetailItem)
                    .sorted(Comparator.comparing(DetailItem::getShip, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            this.detailList.addAll(items);
        } else {
            // 未選択
            this.detailName.setText("");
        }
    }

    /**
     * 装備アイコンを表示するセル
     */
    private static class ItemImageCell extends TableCell<Item, Item> {

        private Function<Item, StringProperty> property;

        private ItemImageCell(Function<Item, StringProperty> property) {
            this.property = property;
        }

        @Override
        protected void updateItem(Item item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null) {
                    SlotitemDescription desc = SlotitemDescriptionCollection.get()
                            .getSlotitemMap()
                            .get(item.getId());
                    this.setGraphic(new ImageView(Ships.itemImage(desc)));
                    this.setText(this.property.apply(item).get());
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 艦娘を表示するセル
     */
    private static class ShipImageCell extends TableCell<DetailItem, DetailItem> {

        private Function<DetailItem, StringProperty> property;

        public ShipImageCell(Function<DetailItem, StringProperty> property) {
            this.property = property;
        }

        @Override
        protected void updateItem(DetailItem item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (item != null) {
                    Ship ship = ShipCollection.get()
                            .getShipMap()
                            .get(item.getShip());

                    this.setGraphic(new ImageView(Ships.shipWithItemImage(ship)));
                    this.setText(this.property.apply(item).get());
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }
}
