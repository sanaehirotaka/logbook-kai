package logbook.internal.gui;

import static java.util.stream.Collectors.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.bean.AppConfig;
import logbook.bean.SlotitemEquiptype;
import logbook.bean.SlotitemEquiptypeCollection;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.log.CreateitemLogFormat;
import logbook.internal.log.LogWriter;
import lombok.EqualsAndHashCode;
import lombok.val;

/**
 * 開発ログ
 *
 */
public class CreateItemController extends WindowController {

    /** 集計 */
    @FXML
    private TreeTableView<CreateItemCollect> collect;

    /** 集計 */
    @FXML
    private TreeTableColumn<CreateItemCollect, String> unit;

    /** 件数 */
    @FXML
    private TreeTableColumn<CreateItemCollect, String> count;

    /** 割合 */
    @FXML
    private TreeTableColumn<CreateItemCollect, String> ratio;

    /** トグルボタン */
    @FXML
    private ToggleGroup group;

    /** 装備→投入資材 */
    @FXML
    private ToggleButton buttonItemRecipe;

    /** 投入資材→装備 */
    @FXML
    private ToggleButton buttonRecipeItem;

    /** 明細 */
    @FXML
    private TableView<CreateItem> detail;

    /** 日付 */
    @FXML
    private TableColumn<CreateItem, String> date;

    /** 装備 */
    @FXML
    private TableColumn<CreateItem, String> item;

    /** 種類 */
    @FXML
    private TableColumn<CreateItem, String> type;

    /** 投入資材 */
    @FXML
    private TableColumn<CreateItem, Recipe> recipe;

    /** 秘書艦 */
    @FXML
    private TableColumn<CreateItem, String> secretary;

    private Map<CreateItemCollect, List<CreateItem>> detailList = new HashMap<>();

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.detail, this.getClass() + "#" + "detail");
            this.detail.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.detail.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

            Map<String, SlotitemMst> unitToType3 = this.unitToType3();
            this.unit.setCellFactory(p -> new ItemIconCell(unitToType3));
            this.unit.setCellValueFactory(new TreeItemPropertyValueFactory<>("unit"));
            this.count.setCellValueFactory(new TreeItemPropertyValueFactory<>("count"));
            this.ratio.setCellValueFactory(new TreeItemPropertyValueFactory<>("ratio"));

            this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
            this.item.setCellValueFactory(new PropertyValueFactory<>("item"));
            this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.recipe.setCellValueFactory(new PropertyValueFactory<>("recipe"));
            this.secretary.setCellValueFactory(new PropertyValueFactory<>("secretary"));

            this.collect.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(this::detail);
            this.group.selectedToggleProperty()
                    .addListener(this::changeType);
            this.setCollect(this.buttonItemRecipe);
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void copyDetail(ActionEvent event) {
        TableTool.selectionCopy(this.detail);
    }

    @FXML
    void selectAllDetail(ActionEvent event) {
        TableTool.selectAll(this.detail);
    }

    @FXML
    void columnVisibleDetail(ActionEvent event) {
        try {
            TableTool.showVisibleSetting(this.detail, this.getClass() + "#" + "detail",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 集計単位からアイコンを引くためのMap
     * @return type3
     */
    private Map<String, SlotitemMst> unitToType3() {
        val type2To3 = SlotitemMstCollection.get().getSlotitemMap()
                .values()
                .stream()
                .collect(toMap(item -> item.getType().get(2), Function.identity(), (a, b) -> a));
        Map<String, SlotitemMst> unitToType3 = SlotitemEquiptypeCollection.get()
                .getEquiptypeMap()
                .values()
                .stream()
                .filter(e -> type2To3.containsKey(e.getId()))
                .collect(toMap(SlotitemEquiptype::getName, item -> type2To3.get(item.getId())));
        unitToType3.putAll(
                SlotitemMstCollection.get().getSlotitemMap()
                        .values()
                        .stream()
                        .collect(toMap(SlotitemMst::getName, Function.identity(), (a, b) -> a)));
        return unitToType3;
    }

    /**
     * 集計する
     * @param button 集計タイプ
     */
    private void setCollect(Toggle button) {
        this.detailList.clear();
        Path logFile = Paths.get(AppConfig.get().getReportPath(), new CreateitemLogFormat().fileName());
        try {
            List<CreateItem> logs;
            try (Stream<String> lines = Files.lines(logFile, LogWriter.DEFAULT_CHARSET)) {
                logs = lines.skip(1)
                        .map(CreateItem::parse)
                        .filter(Objects::nonNull)
                        .collect(toList());
            }
            Map<Recipe, Long> count = logs.stream()
                    .collect(groupingBy(CreateItem::getRecipe, counting()));

            Map<?, ?> grouping = Collections.emptyMap();

            if (button == this.buttonItemRecipe) {
                grouping = logs.stream()
                        .filter(item -> !item.getItem().isEmpty())
                        .sorted(Comparator.comparing(CreateItem::getType)
                                .thenComparing(CreateItem::getItem)
                                .thenComparing(CreateItem::getRecipe))
                        .collect(groupingBy(CreateItem::getType, LinkedHashMap::new,
                                groupingBy(CreateItem::getItem, LinkedHashMap::new,
                                        groupingBy(CreateItem::getRecipe, LinkedHashMap::new,
                                                toList()))));
            }
            if (button == this.buttonRecipeItem) {
                grouping = logs.stream()
                        .sorted(Comparator.comparing(CreateItem::getRecipe)
                                .thenComparing(CreateItem::getType)
                                .thenComparing(CreateItem::getItem))
                        .collect(groupingBy(CreateItem::getRecipe, LinkedHashMap::new,
                                groupingBy(CreateItem::getItem, LinkedHashMap::new,
                                        toList())));
            }

            TreeItem<CreateItemCollect> root = new TreeItem<>();
            this.collect.setRoot(root);
            this.collect.setShowRoot(false);
            this.setUnit(root, count, null, grouping);
        } catch (Exception e) {
            LoggerHolder.get().warn("開発報告書の読込中に例外", e);
        }
    }

    /**
     * 左ペインに表示するツリーを構築する
     * @param parent
     * @param count
     * @param recipe
     * @param grouping
     */
    private void setUnit(TreeItem<CreateItemCollect> parent,
            Map<Recipe, Long> count,
            Recipe recipe,
            Map<?, ?> grouping) {
        for (val entry : grouping.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key instanceof Recipe) {
                recipe = (Recipe) key;
            }
            CreateItemCollect item = new CreateItemCollect();
            if ("".equals(key)) {
                item.setUnit("(失敗)");
            } else {
                item.setUnit(key.toString());
            }
            if (recipe != null) {
                List<CreateItem> rows = this.getSubItem(value);
                Collections.sort(rows, Comparator.comparing(CreateItem::getDate));

                long size = rows.size();
                long total = count.get(recipe);
                String ratio = BigDecimal.valueOf(size)
                        .divide(BigDecimal.valueOf(total), 4, RoundingMode.FLOOR)
                        .multiply(BigDecimal.valueOf(100))
                        .setScale(2)
                        .toPlainString() + "%";
                item.setCount(String.valueOf(size));
                item.setRatio(ratio);

                this.detailList.put(item, rows);
            }

            TreeItem<CreateItemCollect> unitRoot = new TreeItem<>(item);
            parent.getChildren().add(unitRoot);

            if (value instanceof Map) {
                this.setUnit(unitRoot, count, recipe, (Map<?, ?>) value);
            }
        }
    }

    private List<CreateItem> getSubItem(Object maporlist) {
        List<CreateItem> list = new ArrayList<>();
        if (maporlist instanceof Map) {
            for (Object obj : ((Map<?, ?>) maporlist).values()) {
                list.addAll(this.getSubItem(obj));
            }
        }
        if (maporlist instanceof List) {
            for (Object obj : (List<?>) maporlist) {
                list.add(((CreateItem) obj));
            }
        }
        return list;
    }

    /**
     * 右ペインに詳細表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void detail(ObservableValue<? extends TreeItem<CreateItemCollect>> observable,
            TreeItem<CreateItemCollect> oldValue, TreeItem<CreateItemCollect> value) {
        if (value != null) {
            List<CreateItem> items = this.detailList.get(value.getValue());
            if (items != null) {
                this.detail.setItems(FXCollections.observableArrayList(items));
            } else {
                this.detail.getItems().clear();
            }
        }
    }

    /**
     * 集計タイプの変更
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void changeType(ObservableValue<? extends Toggle> observable,
            Toggle oldValue, Toggle value) {
        this.setCollect(value);
    }

    /**
     * 装備アイコンセル
     */
    private static class ItemIconCell extends TreeTableCell<CreateItemCollect, String> {

        private Map<String, SlotitemMst> unitToType3;

        public ItemIconCell(Map<String, SlotitemMst> unitToType3) {
            this.unitToType3 = unitToType3;
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                this.setText(item);
                SlotitemMst mst = this.unitToType3.get(item);
                if (mst != null) {
                    ImageView image = new ImageView(Items.itemImage(mst));
                    image.setFitHeight(24);
                    image.setFitWidth(24);
                    this.setGraphic(image);
                } else {
                    this.setGraphic(null);
                }
            } else {
                this.setText(item);
                this.setGraphic(null);
            }
        }
    }

    /**
     * 明細
     */
    public static class CreateItem {

        /** 日付 */
        private StringProperty date = new SimpleStringProperty();

        /** 装備 */
        private StringProperty item = new SimpleStringProperty();

        /** 種類 */
        private StringProperty type = new SimpleStringProperty();

        /** 投入資材 */
        private ObjectProperty<Recipe> recipe = new SimpleObjectProperty<Recipe>();

        /** 秘書艦 */
        private StringProperty secretary = new SimpleStringProperty();

        /**
         * 日付を取得します。
         * @return 日付
         */
        public StringProperty dateProperty() {
            return this.date;
        }

        /**
         * 日付を取得します。
         * @return 日付
         */
        public String getDate() {
            return this.date.get();
        }

        /**
         * 日付を設定します。
         * @param date 日付
         */
        public void setDate(String date) {
            this.date.set(date);
        }

        /**
         * 装備を取得します。
         * @return 装備
         */
        public StringProperty itemProperty() {
            return this.item;
        }

        /**
         * 装備を取得します。
         * @return 装備
         */
        public String getItem() {
            return this.item.get();
        }

        /**
         * 装備を設定します。
         * @param item 装備
         */
        public void setItem(String item) {
            this.item.set(item);
        }

        /**
         * 種類を取得します。
         * @return 種類
         */
        public StringProperty typeProperty() {
            return this.type;
        }

        /**
         * 種類を取得します。
         * @return 種類
         */
        public String getType() {
            return this.type.get();
        }

        /**
         * 種類を設定します。
         * @param type 種類
         */
        public void setType(String type) {
            this.type.set(type);
        }

        /**
         * 投入資材を取得します。
         * @return 投入資材
         */
        public ObjectProperty<Recipe> recipeProperty() {
            return this.recipe;
        }

        /**
         * 投入資材を取得します。
         * @return 投入資材
         */
        public Recipe getRecipe() {
            return this.recipe.get();
        }

        /**
         * 投入資材を設定します。
         * @param recipe 投入資材
         */
        public void setRecipe(Recipe recipe) {
            this.recipe.set(recipe);
        }

        /**
         * 秘書艦を取得します。
         * @return 秘書艦
         */
        public StringProperty secretaryProperty() {
            return this.secretary;
        }

        /**
         * 秘書艦を取得します。
         * @return 秘書艦
         */
        public String getSecretary() {
            return this.secretary.get();
        }

        /**
         * 秘書艦を設定します。
         * @param secretary 秘書艦
         */
        public void setSecretary(String secretary) {
            this.secretary.set(secretary);
        }

        @Override
        public String toString() {
            return new StringJoiner("\t")
                    .add(this.date.get())
                    .add(this.item.get())
                    .add(this.type.get())
                    .add(String.valueOf(this.recipe.get()))
                    .add(this.secretary.get())
                    .toString();
        }

        public static CreateItem parse(String line) {
            try {
                String[] columns = line.split(",", -1);
                CreateItem value = new CreateItem();
                value.setDate(columns[0]);
                value.setItem(columns[1]);
                value.setType(columns[2]);
                value.setRecipe(new Recipe(columns[3], columns[4], columns[5], columns[6]));
                value.setSecretary(columns[7]);
                return value;
            } catch (Exception e) {
            }
            return null;
        }
    }

    /**
     * 集計
     */
    public static class CreateItemCollect {

        /** 単位 */
        private StringProperty unit = new SimpleStringProperty();

        /** 回数 */
        private StringProperty count = new SimpleStringProperty();

        /** 割合 */
        private StringProperty ratio = new SimpleStringProperty();

        /**
         * 単位を取得します。
         * @return 単位
         */
        public StringProperty unitProperty() {
            return this.unit;
        }

        /**
         * 単位を取得します。
         * @return 単位
         */
        public String getUnit() {
            return this.unit.get();
        }

        /**
         * 単位を設定します。
         * @param unit 単位
         */
        public void setUnit(String unit) {
            this.unit.set(unit);
        }

        /**
         * 回数を取得します。
         * @return 回数
         */
        public StringProperty countProperty() {
            return this.count;
        }

        /**
         * 回数を取得します。
         * @return 回数
         */
        public String getCount() {
            return this.count.get();
        }

        /**
         * 回数を設定します。
         * @param count 回数
         */
        public void setCount(String count) {
            this.count.set(count);
        }

        /**
         * 割合を取得します。
         * @return 割合
         */
        public StringProperty ratioProperty() {
            return this.ratio;
        }

        /**
         * 割合を取得します。
         * @return 割合
         */
        public String getRatio() {
            return this.ratio.get();
        }

        /**
         * 割合を設定します。
         * @param ratio 割合
         */
        public void setRatio(String ratio) {
            this.ratio.set(ratio);
        }
    }

    /**
     * 投入資材
     */
    @EqualsAndHashCode
    private static class Recipe implements Comparable<Recipe> {

        /** 燃料 */
        private final int fuel;

        /** 弾薬 */
        private final int ammo;

        /** 鋼材 */
        private final int metal;

        /** ボーキサイト */
        private int bauxite;

        public Recipe(String fuel, String ammo, String metal, String bauxite) {
            this.fuel = Integer.parseInt(fuel);
            this.ammo = Integer.parseInt(ammo);
            this.metal = Integer.parseInt(metal);
            this.bauxite = Integer.parseInt(bauxite);
        }

        @Override
        public int compareTo(Recipe o) {
            if (this.fuel != o.fuel)
                return Integer.compare(this.fuel, o.fuel);
            if (this.ammo != o.ammo)
                return Integer.compare(this.ammo, o.ammo);
            if (this.metal != o.metal)
                return Integer.compare(this.metal, o.metal);
            if (this.bauxite != o.bauxite)
                return Integer.compare(this.bauxite, o.bauxite);
            return 0;
        }

        @Override
        public String toString() {
            return this.fuel + "/" + this.ammo + "/" + this.metal + "/" + this.bauxite;
        }
    }
}
