package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.TextFields;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import logbook.Messages;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;
import logbook.plugin.PluginServices;
import lombok.Data;

/**
 * 所有装備一覧のUIコントローラー
 *
 */
public class ItemItemController extends WindowController {

    // フィルター

    /** テキスト */
    @FXML
    private ToggleSwitch typeFilter;

    /** テキスト */
    @FXML
    private ComboBox<String> typeValue;

    // 一覧(装備一覧)

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

    // 一覧(所持)

    /** 詳細・名前 */
    @FXML
    private Label detailName;

    /** 詳細・一覧 */
    @FXML
    private TableView<DetailItem> detailTable;

    /** 熟練 */
    @FXML
    private TableColumn<DetailItem, Integer> alv;

    /** 改修 */
    @FXML
    private TableColumn<DetailItem, Integer> level;

    /** 所持 */
    @FXML
    private TableColumn<DetailItem, Ship> ship;

    /** 一覧 */
    private FilteredList<Item> types;

    /** 詳細一覧 */
    private ObservableList<DetailItem> details = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.typeTable, this.getClass().toString() + "#" + "typeTable");
            TableTool.setVisible(this.detailTable, this.getClass().toString() + "#" + "detailTable");

            this.typeFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.typeValue.setDisable(!nv);
                this.typeValue.setDisable(!nv);
            });
            this.typeFilter.selectedProperty().addListener(this::filterAction);
            this.typeValue.getSelectionModel().selectedItemProperty().addListener(this::filterAction);

            // カラムとオブジェクトのバインド
            this.name.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.name.setCellFactory(p -> new ItemImageCell<>());
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
            this.alv.setCellFactory(p -> new ItemAlvCell<>());
            this.level.setCellValueFactory(new PropertyValueFactory<>("level"));
            this.level.setCellFactory(p -> new ItemLevelCell<>());
            this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
            this.ship.setCellFactory(p -> new ShipImageCell());

            this.typeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.typeTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.detailTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.detailTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            // 行を作る
            ObservableList<Item> items = SlotitemMstCollection.get()
                    .getSlotitemMap()
                    .values()
                    .stream()
                    .map(Item::toItem)
                    .filter(e -> e.getCount() > 0)
                    .sorted(Comparator.comparing(Item::getType3).thenComparing(Comparator.comparing(Item::getName)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            // テキストフィルター
            this.typeValue.setItems(items.stream()
                    .map(Item::typeProperty)
                    .map(StringProperty::get)
                    .distinct()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
            TextFields.bindAutoCompletion(this.typeValue.getEditor(),
                    new SuggestSupport(String::contains, items.stream()
                            .flatMap(i -> Stream.of(i.typeProperty().get(), i.getName()))
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList())));

            // 装備一覧(装備一覧)
            this.types = new FilteredList<>(items);
            SortedList<Item> sortedListTypes = new SortedList<>(this.types);
            this.typeTable.setItems(sortedListTypes);
            sortedListTypes.comparatorProperty().bind(this.typeTable.comparatorProperty());
            // 装備一覧(所持) 最初は空のリスト
            SortedList<DetailItem> sortedListDetail = new SortedList<>(this.details);
            this.detailTable.setItems(sortedListDetail);
            sortedListDetail.comparatorProperty().bind(this.detailTable.comparatorProperty());

            // 装備が選択された時のリスナーを設定
            this.typeTable.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(this::detail);
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * フィルターを設定する
     */
    private void filterAction(ObservableValue<?> observable, Object oldValue, Object newValue) {
        ItemFilter filter = ItemFilter.DefaultFilter.builder()
                .typeFilter(this.typeFilter.isSelected())
                .typeValue(this.typeValue.getValue() == null ? "" : this.typeValue.getValue())
                .build();
        this.types.setPredicate(filter);
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
    private static class ItemImageCell<T> extends TableCell<T, Integer> {
        @Override
        protected void updateItem(Integer itemId, boolean empty) {
            super.updateItem(itemId, empty);
            if (!empty) {
                SlotitemMst mst = SlotitemMstCollection.get()
                        .getSlotitemMap()
                        .get(itemId);

                if (mst != null) {
                    ImageView img = new ImageView(Items.itemImage(mst));
                    img.setFitWidth(36);
                    img.setFitHeight(36);
                    this.setGraphic(img);
                    this.setText(mst.getName());
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 熟練度を表示するセル
     */
    private static class ItemAlvCell<T> extends TableCell<T, Integer> {
        @Override
        protected void updateItem(Integer alv, boolean empty) {
            super.updateItem(alv, empty);
            if (!empty && alv != null && alv > 0) {
                URL url = PluginServices.getResource("logbook/gui/alv" + alv + ".png");
                Pane pane = new StackPane(new ImageView(url.toString()));
                pane.setPrefWidth(16);
                pane.setPrefHeight(16);
                this.setGraphic(pane);
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 改修を表示するセル
     */
    private static class ItemLevelCell<T> extends TableCell<T, Integer> {

        @Override
        protected void updateItem(Integer lv, boolean empty) {
            super.updateItem(lv, empty);
            if (!empty) {
                this.setText(Optional.ofNullable(lv)
                        .filter(v -> v > 0)
                        .map(v -> Messages.getString("item.level", v)) //$NON-NLS-1$
                        .orElse(""));
                if (!this.getStyleClass().contains("level")) {
                    this.getStyleClass().add("level");
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
                this.setGraphic(Tools.Conrtols.zoomImage(new ImageView(Ships.shipWithItemImage(ship))));
                if (ship != null) {
                    this.setText(Ships.toName(ship));
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
     * (装備一覧)CSVファイルとして保存
     */
    @FXML
    void storeType() {
        try {
            TableTool.store(this.typeTable, "所有装備一覧", this.getWindow());
        } catch (IOException e) {
            LoggerHolder.get().error("CSVファイルとして保存に失敗しました", e);
        }
    }

    /**
     * 艦隊分析
     */
    @FXML
    void kancolleFleetanalysis() {
        try {
            List<KancolleFleetanalysisItem> list = SlotItemCollection.get().getSlotitemMap().values().stream()
                    .filter(item -> item.getLocked())
                    .map(KancolleFleetanalysisItem::toItem)
                    .sorted(Comparator.comparing(KancolleFleetanalysisItem::getId)
                            .thenComparing(Comparator.comparing(KancolleFleetanalysisItem::getLv)))
                    .collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            String input = mapper.writeValueAsString(list);

            ClipboardContent content = new ClipboardContent();
            content.putString(input);
            Clipboard.getSystemClipboard().setContent(content);
        } catch (Exception e) {
            LoggerHolder.get().error("艦隊分析のロック装備をクリップボードにコピーに失敗しました", e);
        }
    }

    /**
     * (装備一覧)テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisibleType() {
        try {
            TableTool.showVisibleSetting(this.typeTable, this.getClass().toString() + "#" + "typeTable",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
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

    /**
     * (所持)テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisibleDetail() {
        try {
            TableTool.showVisibleSetting(this.detailTable, this.getClass().toString() + "#" + "detailTable",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    @Data
    private static class KancolleFleetanalysisItem {

        @JsonProperty("api_slotitem_id")
        private int id;

        @JsonProperty("api_level")
        private int lv;

        public static KancolleFleetanalysisItem toItem(SlotItem item) {
            KancolleFleetanalysisItem kfi = new KancolleFleetanalysisItem();
            kfi.id = item.getSlotitemId();
            kfi.lv = item.getLevel();
            return kfi;
        }
    }
}
