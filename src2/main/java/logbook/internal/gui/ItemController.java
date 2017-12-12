package logbook.internal.gui;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.Messages;
import logbook.bean.Ship;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
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
    private TableColumn<Item, Integer> name;

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
    private TableColumn<DetailItem, Ship> ship;

    /** 一覧 */
    private ObservableList<Item> types = FXCollections.observableArrayList();

    /** 詳細一覧 */
    private ObservableList<DetailItem> details = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        try {
            // カラムとオブジェクトのバインド
            this.name.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.name.setCellFactory(p -> new ItemImageCell());
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
            this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
            this.ship.setCellFactory(p -> new ShipImageCell());

            this.typeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.typeTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.detailTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.detailTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            // 行を作る
            List<Item> items = SlotitemMstCollection.get()
                    .getSlotitemMap()
                    .values()
                    .stream()
                    .map(Item::toItem)
                    .filter(e -> e.getCount() > 0)
                    .sorted(Comparator.comparing(Item::getName))
                    .sorted(Comparator.comparing(Item::getType3))
                    .collect(Collectors.toList());
            this.types.addAll(items);
            this.typeTable.setItems(this.types);
            // 最初は空のリスト
            this.detailTable.setItems(this.details);

            // 装備が選択された時のリスナーを設定
            this.typeTable.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(this::detail);

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 右ペインに詳細表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void detail(ObservableValue<? extends Item> observable, Item oldValue, Item value) {
        this.details.clear();
        if (value != null) {
            // 選択
            this.detailName.setText(value.getName());
            // 行を作る
            List<DetailItem> items = SlotItemCollection.get()
                    .getSlotitemMap()
                    .values()
                    .stream()
                    .filter(e -> e.getSlotitemId().equals(value.idProperty().get()))
                    .sorted(Comparator.comparing(SlotItem::getAlv,
                            Comparator.nullsFirst(Comparator.naturalOrder()))
                            .reversed())
                    .sorted(Comparator.comparing(SlotItem::getLevel,
                            Comparator.nullsFirst(Comparator.naturalOrder()))
                            .reversed())
                    .map(DetailItem::toDetailItem)
                    .sorted(Comparator.comparing(DetailItem::getShipId,
                            Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
            this.details.addAll(items);
        } else {
            // 未選択
            this.detailName.setText("");
        }
    }

    /**
     * 装備アイコンを表示するセル
     */
    private static class ItemImageCell extends TableCell<Item, Integer> {
        @Override
        protected void updateItem(Integer itemId, boolean empty) {
            super.updateItem(itemId, empty);
            if (!empty) {
                SlotitemMst mst = SlotitemMstCollection.get()
                        .getSlotitemMap()
                        .get(itemId);

                if (mst != null) {
                    this.setGraphic(new ImageView(Items.itemImage(mst)));
                    this.setText(mst.getName());
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
    private static class ShipImageCell extends TableCell<DetailItem, Ship> {
        @Override
        protected void updateItem(Ship ship, boolean empty) {
            super.updateItem(ship, empty);
            if (!empty) {
                this.setGraphic(new ImageView(Ships.shipWithItemImage(ship)));
                if (ship != null) {
                    String name = Ships.shipMst(ship)
                            .map(ShipMst::getName)
                            .orElse("");
                    this.setText(Messages.getString("ship.name", name, ship.getLv())); //$NON-NLS-1$
                } else {
                    this.setText("未装備");
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * (装備一覧)クリップボードにコピー
     */
    @FXML
    void copyType() {
        TableTool.selectionCopy(this.typeTable);
    }

    /**
     * (装備一覧)すべてを選択
     */
    @FXML
    void selectAllType() {
        TableTool.selectAll(this.typeTable);
    }

    /**
     * (所持)クリップボードにコピー
     */
    @FXML
    void copyDetail() {
        TableTool.selectionCopy(this.detailTable);
    }

    /**
     * (所持)すべてを選択
     */
    @FXML
    void selectAllDetail() {
        TableTool.selectAll(this.detailTable);
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ItemController.class);
    }
}
