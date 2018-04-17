package logbook.internal.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.controlsfx.control.ToggleSwitch;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import logbook.Messages;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.Operator;
import logbook.plugin.PluginServices;
import lombok.val;

/**
 * 所有装備一覧のUIコントローラー
 *
 */
public class ItemAirBaseController extends WindowController {

    // フィルター

    @FXML
    private ToggleSwitch seikuFilter;

    @FXML
    private Spinner<Integer> seikuValue;

    @FXML
    private ChoiceBox<Operator> seikuType;

    @FXML
    private ToggleSwitch interceptSeikuFilter;

    @FXML
    private Spinner<Integer> interceptSeikuValue;

    @FXML
    private ChoiceBox<Operator> interceptSeikuType;

    @FXML
    private ToggleSwitch distanceFilter;

    @FXML
    private Spinner<Integer> distanceValue;

    @FXML
    private ChoiceBox<Operator> distanceType;

    @FXML
    private ToggleSwitch distanceTaiteichanFilter;

    @FXML
    private Spinner<Integer> distanceTaiteichanValue;

    @FXML
    private ChoiceBox<Operator> distanceTaiteichanType;

    @FXML
    private ToggleSwitch distanceCatalinaFilter;

    @FXML
    private Spinner<Integer> distanceCatalinaValue;

    @FXML
    private ChoiceBox<Operator> distanceCatalinaType;

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
    private FilteredList<AirBaseItem> items;

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.itemTable, this.getClass().toString() + "#" + "airBaseItemTable");

            // フィルター 初期値
            this.seikuType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.seikuType.getSelectionModel().select(Operator.GE);
            this.interceptSeikuType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.interceptSeikuType.getSelectionModel().select(Operator.GE);
            this.distanceType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.distanceType.getSelectionModel().select(Operator.GE);
            this.distanceTaiteichanType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.distanceTaiteichanType.getSelectionModel().select(Operator.GE);
            this.distanceCatalinaType.setItems(FXCollections.observableArrayList(Operator.values()));
            this.distanceCatalinaType.getSelectionModel().select(Operator.GE);

            this.seikuValue.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 103));
            this.interceptSeikuValue.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 101));
            this.distanceValue.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 7));
            this.distanceCatalinaValue.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 9));
            this.distanceTaiteichanValue.setValueFactory(new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 10));
            // フィルターのリスナー
            this.seikuFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.seikuValue.setDisable(!nv);
                this.seikuType.setDisable(!nv);
            });
            this.interceptSeikuFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.interceptSeikuValue.setDisable(!nv);
                this.interceptSeikuType.setDisable(!nv);
            });
            this.distanceFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.distanceValue.setDisable(!nv);
                this.distanceType.setDisable(!nv);
            });
            this.distanceTaiteichanFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.distanceTaiteichanValue.setDisable(!nv);
                this.distanceTaiteichanType.setDisable(!nv);
            });
            this.distanceCatalinaFilter.selectedProperty().addListener((ob, ov, nv) -> {
                this.distanceCatalinaValue.setDisable(!nv);
                this.distanceCatalinaType.setDisable(!nv);
            });

            this.seikuFilter.selectedProperty().addListener(this::filterAction);
            this.seikuValue.valueProperty().addListener(this::filterAction);
            this.spinnerHandller(this.seikuValue);
            this.seikuType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.interceptSeikuFilter.selectedProperty().addListener(this::filterAction);
            this.interceptSeikuValue.valueProperty().addListener(this::filterAction);
            this.spinnerHandller(this.interceptSeikuValue);
            this.interceptSeikuType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.distanceFilter.selectedProperty().addListener(this::filterAction);
            this.distanceValue.valueProperty().addListener(this::filterAction);
            this.spinnerHandller(this.distanceValue);
            this.distanceType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.distanceTaiteichanFilter.selectedProperty().addListener(this::filterAction);
            this.distanceTaiteichanValue.valueProperty().addListener(this::filterAction);
            this.spinnerHandller(this.distanceTaiteichanValue);
            this.distanceTaiteichanType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
            this.distanceCatalinaFilter.selectedProperty().addListener(this::filterAction);
            this.distanceCatalinaValue.valueProperty().addListener(this::filterAction);
            this.distanceCatalinaValue.getEditor().textProperty().addListener(this::filterAction);
            this.spinnerHandller(this.distanceCatalinaValue);
            this.distanceCatalinaType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);

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

            // 一覧を作る
            val itemCount = SlotItemCollection.get()
                    .getSlotitemMap()
                    .values().stream()
                    .filter(AirBaseItem::isTarget)
                    .map(AirBaseItem::toAirBaseItem)
                    .collect(Collectors.groupingBy(e -> e));
            ObservableList<AirBaseItem> items = itemCount
                    .entrySet().stream()
                    .map(e -> {
                        AirBaseItem v = e.getKey();
                        v.setCount(e.getValue().size());
                        return v;
                    })
                    .sorted(Comparator.comparing(AirBaseItem::getType3)
                            .thenComparing(AirBaseItem::getType2)
                            .thenComparing(Comparator.comparing(AirBaseItem::getName)))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            this.items = new FilteredList<>(items);
            SortedList<AirBaseItem> sortedAirBaseItems = new SortedList<>(this.items);
            this.itemTable.setItems(sortedAirBaseItems);
            sortedAirBaseItems.comparatorProperty().bind(this.itemTable.comparatorProperty());

        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * Spinnerの変更を即座に反映する
     *
     * @param spinner Spinner
     */
    private <T> void spinnerHandller(Spinner<T> spinner) {
        spinner.getEditor().textProperty().addListener((ob, o, n) -> {
            SpinnerValueFactory<T> f = spinner.getValueFactory();
            T value = f.getConverter().fromString("".equals(n) ? "0" : n);
            if (!value.equals(f.getValue())) {
                f.setValue(value);
            }
        });
    }

    /**
     * フィルターを設定する
     */
    private void filterAction(ObservableValue<?> observable, Object oldValue, Object newValue) {
        this.items.setPredicate(this.createFilter());
    }

    /**
     * フィルターを作成する
     * @return フィルター
     */
    private Predicate<AirBaseItem> createFilter() {
        Predicate<AirBaseItem> filter = null;
        if (this.seikuFilter.isSelected()) {
            filter = this.filterAnd(filter,
                    IntegerPropertyFilter.build(this.seikuType.getValue(),
                            this.seikuValue.getValue(),
                            AirBaseItem::seikuProperty));
        }
        if (this.interceptSeikuFilter.isSelected()) {
            filter = this.filterAnd(filter,
                    IntegerPropertyFilter.build(this.interceptSeikuType.getValue(),
                            this.interceptSeikuValue.getValue(),
                            AirBaseItem::interceptSeikuProperty));
        }
        if (this.distanceFilter.isSelected()) {
            filter = this.filterAnd(filter,
                    IntegerPropertyFilter.build(this.distanceType.getValue(),
                            this.distanceValue.getValue(),
                            AirBaseItem::distanceProperty));
        }
        if (this.distanceTaiteichanFilter.isSelected()) {
            filter = this.filterAnd(filter,
                    IntegerPropertyFilter.build(this.distanceTaiteichanType.getValue(),
                            this.distanceTaiteichanValue.getValue(),
                            AirBaseItem::distanceTaiteichanProperty));
        }
        if (this.distanceCatalinaFilter.isSelected()) {
            filter = this.filterAnd(filter,
                    IntegerPropertyFilter.build(this.distanceCatalinaType.getValue(),
                            this.distanceCatalinaValue.getValue(),
                            AirBaseItem::distanceCatalinaProperty));
        }
        return filter;
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

    /**
     * フィルター
     *
     * @param <T>
     */
    private static class IntegerPropertyFilter<T> implements Predicate<T> {

        private Operator operator;

        private Integer value;

        private Function<T, IntegerProperty> func;

        @Override
        public boolean test(T t) {
            return this.operator.compare(this.func.apply(t).getValue(), this.value);
        }

        public static <T> IntegerPropertyFilter<T> build(Operator operator, Integer value,
                Function<T, IntegerProperty> func) {
            val f = new IntegerPropertyFilter<T>();
            f.operator = operator;
            f.value = value;
            f.func = func;
            return f;
        }
    }
}
