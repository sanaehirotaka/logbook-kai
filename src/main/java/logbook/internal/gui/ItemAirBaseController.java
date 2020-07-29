package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.controlsfx.control.ToggleSwitch;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import logbook.Messages;
import logbook.bean.AppItemTableConfig;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.plugin.PluginServices;

/**
 * 基地航空隊一覧のUIコントローラー
 *
 */
public class ItemAirBaseController extends WindowController {

    // フィルター
    @FXML
    private TitledPane filter;

    /** グルーピングをしない */
    @FXML
    private ToggleSwitch disableGrouping;
    
    @FXML
    private FlowPane filters;

    // 一覧

    /** 一覧 */
    @FXML
    private TableView<AirBaseItem> itemTable;

    /** 名称 */
    @FXML
    private TableColumn<AirBaseItem, Integer> name;

    /** 種別 */
    @FXML
    private TableColumn<AirBaseItem, String> type;

    /** 熟練 */
    @FXML
    private TableColumn<AirBaseItem, Integer> alv;

    /** 改修 */
    @FXML
    private TableColumn<AirBaseItem, Integer> level;

    /** 個数 */
    @FXML
    private TableColumn<AirBaseItem, Integer> count;

    /** 出撃時制空 */
    @FXML
    private TableColumn<AirBaseItem, Integer> seiku;

    /** 防空時制空 */
    @FXML
    private TableColumn<AirBaseItem, Integer> interceptSeiku;

    /** 半径 */
    @FXML
    private TableColumn<AirBaseItem, Integer> distance;

    /** 大艇入り半径 */
    @FXML
    private TableColumn<AirBaseItem, Integer> distanceTaiteichan;

    /** Catalina入り半径 */
    @FXML
    private TableColumn<AirBaseItem, Integer> distanceCatalina;

    /** 配置コスト */
    @FXML
    private TableColumn<AirBaseItem, Integer> cost;

    /** 対空 */
    @FXML
    private TableColumn<AirBaseItem, Integer> tyku;

    /** 対爆 */
    @FXML
    private TableColumn<AirBaseItem, Integer> houm;

    /** 迎撃 */
    @FXML
    private TableColumn<AirBaseItem, Integer> houk;

    /** 雷装 */
    @FXML
    private TableColumn<AirBaseItem, Integer> raig;

    /** 爆装 */
    @FXML
    private TableColumn<AirBaseItem, Integer> baku;

    /** 対潜 */
    @FXML
    private TableColumn<AirBaseItem, Integer> tais;

    /** 索敵 */
    @FXML
    private TableColumn<AirBaseItem, Integer> saku;

    /** 基地航空隊 */
    private ObservableList<AirBaseItem> items;

    /** フィルターされた基地航空隊リスト */
    private FilteredList<AirBaseItem> filteredItems;

    /** フィルター */
    private List<ParameterFilterPane.AirBaseParameterFilterPane> parameterFilters;

    /** フィルターの更新を停止 */
    private boolean disableFilterUpdate;

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.itemTable, this.getClass().toString() + "#" + "airBaseItemTable");
            
            this.filter.expandedProperty().addListener((ob, o, n) -> saveConfig());

            // フィルター 初期化
            this.parameterFilters = IntStream.range(0, 5).mapToObj(i -> new ParameterFilterPane.AirBaseParameterFilterPane()).collect(Collectors.toList());
            this.parameterFilters.forEach(f -> f.filterProperty().addListener(this::filterAction));
            this.filters.getChildren().addAll(this.parameterFilters);

            // カラムのバインド
            this.name.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.name.setCellFactory(p -> new ItemImageCell<>());
            this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.alv.setCellValueFactory(new PropertyValueFactory<>("alv"));
            this.alv.setCellFactory(p -> new ItemAlvCell<>());
            this.level.setCellValueFactory(new PropertyValueFactory<>("level"));
            this.level.setCellFactory(p -> new ItemLevelCell<>());
            this.count.setCellValueFactory(new PropertyValueFactory<>("count"));
            this.seiku.setCellValueFactory(new PropertyValueFactory<>("seiku"));
            this.interceptSeiku.setCellValueFactory(new PropertyValueFactory<>("interceptSeiku"));
            this.distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
            this.distanceTaiteichan.setCellValueFactory(new PropertyValueFactory<>("distanceTaiteichan"));
            this.distanceCatalina.setCellValueFactory(new PropertyValueFactory<>("distanceCatalina"));
            this.cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
            this.tyku.setCellValueFactory(new PropertyValueFactory<>("tyku"));
            this.houm.setCellValueFactory(new PropertyValueFactory<>("houm"));
            this.houk.setCellValueFactory(new PropertyValueFactory<>("houk"));
            this.raig.setCellValueFactory(new PropertyValueFactory<>("raig"));
            this.baku.setCellValueFactory(new PropertyValueFactory<>("baku"));
            this.tais.setCellValueFactory(new PropertyValueFactory<>("tais"));
            this.saku.setCellValueFactory(new PropertyValueFactory<>("saku"));

            this.itemTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.itemTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.disableGrouping.selectedProperty().addListener((ob, o, n) -> {
                setItems();
                saveConfig();
            });

            this.items = FXCollections.observableArrayList();
            this.filteredItems = new FilteredList<>(this.items);
            SortedList<AirBaseItem> sortedAirBaseItems = new SortedList<>(this.filteredItems);
            this.itemTable.setItems(sortedAirBaseItems);
            sortedAirBaseItems.comparatorProperty().bind(this.itemTable.comparatorProperty());

            loadConfig();
            setItems();
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 装備をセットする
     */
    private void setItems() {
        // 一覧を作る
        Stream<AirBaseItem> listItems = SlotItemCollection.get()
                .getSlotitemMap()
                .values().stream()
                .filter(AirBaseItem::isTarget)
                .map(AirBaseItem::toAirBaseItem);
        
        if (!this.disableGrouping.isSelected()) {
            listItems = listItems
                .collect(Collectors.groupingBy(e -> e))
                .entrySet().stream()
                .map(e -> {
                    AirBaseItem v = e.getKey();
                    v.setCount(e.getValue().size());
                    return v;
                });
        }

        List<AirBaseItem> items = this.items;
        items.clear();
        listItems
                .sorted(Comparator.comparing(AirBaseItem::getType3)
                        .thenComparing(AirBaseItem::getType2)
                        .thenComparing(Comparator.comparing(AirBaseItem::getName))
                        .thenComparing(Comparator.comparing(item -> item.seikuProperty().get())))
                .forEach(items::add);
    }

    /**
     * フィルターを設定する
     */
    private void filterAction(ObservableValue<?> observable, Object oldValue, Object newValue) {
        createFilter();
        saveConfig();
    }

    /**
     * フィルターを作成して設定する
     */
    private void createFilter() {
        this.filteredItems.setPredicate(this.parameterFilters.stream()
            .map(ParameterFilterPane::filterProperty)
            .map(ReadOnlyObjectProperty::get)
            .filter(Objects::nonNull)
            .reduce((acc, val) -> this.filterAnd(acc, val))
            .orElse(null));
    }

    private <T> Predicate<T> filterAnd(Predicate<T> base, Predicate<T> add) {
        if (base != null) {
            return base.and(add);
        }
        return add;
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
                    this.setGraphic(Tools.Conrtols.zoomImage(new ImageView(Items.itemImage(mst))));
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
     * (基地航空隊)クリップボードにコピー
     */
    @FXML
    void copyAirBase() {
        TableTool.selectionCopy(this.itemTable);
    }

    /**
     * (基地航空隊)すべてを選択
     */
    @FXML
    void selectAllAirBase() {
        TableTool.selectAll(this.itemTable);
    }

    /**
     * (基地航空隊)CSVファイルとして保存
     */
    @FXML
    void storeAirBase() {
        try {
            TableTool.store(this.itemTable, "所有装備一覧_基地航空隊", this.getWindow());
        } catch (IOException e) {
            LoggerHolder.get().error("CSVファイルとして保存に失敗しました", e);
        }
    }

    /**
     * (基地航空隊)デッキビルダー形式でクリップボードにコピー
     */
    @FXML
    void deckBuilderSelectionCopy() {
        ShipTablePane.DeckBuilder.airbaseSelectionCopy(this.itemTable);
    }

    /**
     * (基地航空隊)テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisibleAirBase() {
        try {
            TableTool.showVisibleSetting(this.itemTable, this.getClass().toString() + "#" + "airBaseItemTable",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    private void loadConfig() {
        this.disableFilterUpdate = true;
        try {
            Optional.ofNullable(AppItemTableConfig.get()).map(AppItemTableConfig::getAirbaseTabConfig).ifPresent((config) -> {
                this.filter.setExpanded(config.isFilterExpanded());
                this.disableGrouping.setSelected(config.isDisableGrouping());
                Optional.ofNullable(config.getParameterFilters()).ifPresent((list) -> {
                    for (int i = 0; i < Math.min(list.size(), this.parameterFilters.size()); i++) {
                        this.parameterFilters.get(i).loadConfig(list.get(i));
                    }
                });
            });
        } finally {
            this.disableFilterUpdate = false;
        }
        createFilter();
    }

    private void saveConfig() {
        if (this.disableFilterUpdate) {
            return;
        }
        AppItemTableConfig config = AppItemTableConfig.get();
        AppItemTableConfig.AirbaseTabConfig airbaseTabConfig = new AppItemTableConfig.AirbaseTabConfig();
        airbaseTabConfig.setFilterExpanded(this.filter.isExpanded());
        airbaseTabConfig.setDisableGrouping(this.disableGrouping.isSelected());
        airbaseTabConfig.setParameterFilters(this.parameterFilters.stream().map(ParameterFilterPane::saveConfig).collect(Collectors.toList()));
        config.setAirbaseTabConfig(airbaseTabConfig);
    }
}
