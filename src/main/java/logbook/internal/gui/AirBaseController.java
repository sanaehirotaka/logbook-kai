package logbook.internal.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logbook.bean.Maparea;
import logbook.bean.MapareaCollection;
import logbook.bean.Mapinfo;
import logbook.bean.Mapinfo.AirBase;
import logbook.bean.Mapinfo.PlaneInfo;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.internal.AirBases;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.plugin.PluginServices;

/**
 * 基地航空隊
 *
 */
public class AirBaseController extends WindowController {

    @FXML
    private SplitPane splitPane;

    /** 基地航空隊 テーブル */
    @FXML
    private TreeTableView<AreaTable> areaTable;

    /** 基地航空隊 */
    @FXML
    private TreeTableColumn<AreaTable, String> airBase;

    /** 行動 */
    @FXML
    private TreeTableColumn<AreaTable, String> actionKind;

    /** 制空値 */
    @FXML
    private TreeTableColumn<AreaTable, String> seiku;

    /** 戦闘行動半径 */
    @FXML
    private TreeTableColumn<AreaTable, String> distance;

    /** 制空値 */
    @FXML
    private Label seikuTotal;

    /** 劣勢 */
    @FXML
    private Label lesser;

    /** 均衡 */
    @FXML
    private Label equal;

    /** 優勢 */
    @FXML
    private Label superior;

    /** 確保 */
    @FXML
    private Label greater;

    /** 情報 */
    @FXML
    private HBox info;

    /** 中隊 テーブル */
    @FXML
    private TableView<Plane> planeTable;

    /** スロット */
    @FXML
    private TableColumn<Plane, Integer> slot;

    /** 稼働 */
    @FXML
    private TableColumn<Plane, Integer> count;

    /** 定数 */
    @FXML
    private TableColumn<Plane, String> maxCount;

    /** 疲労 */
    @FXML
    private TableColumn<Plane, Integer> cond;

    /** 中隊 */
    private ObservableList<Plane> planes = FXCollections.observableArrayList();

    private Timeline timeline;

    /** 更新検知用 ハッシュ・コード */
    private int hashCode;

    @FXML
    void initialize() {
        try {
            TableTool.setVisible(this.planeTable, this.getClass() + "#" + "planeTable");
            // SplitPaneの分割サイズ
            Timeline x = new Timeline();
            x.getKeyFrames().add(new KeyFrame(Duration.millis(1), (e) -> {
                Tools.Conrtols.setSplitWidth(this.splitPane, this.getClass() + "#" + "splitPane");
            }));
            x.play();
            this.areaTable.setShowRoot(false);
            this.airBase.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
            this.actionKind.setCellValueFactory(new TreeItemPropertyValueFactory<>("actionKind"));
            this.seiku.setCellValueFactory(new TreeItemPropertyValueFactory<>("seiku"));
            this.distance.setCellValueFactory(new TreeItemPropertyValueFactory<>("distance"));

            this.slot.setCellValueFactory(new PropertyValueFactory<>("slot"));
            this.slot.setCellFactory(p -> new ItemImageCell());
            this.count.setCellValueFactory(new PropertyValueFactory<>("count"));
            this.count.setCellFactory(p -> new CountImageCell());
            this.maxCount.setCellValueFactory(new PropertyValueFactory<>("maxCount"));
            this.cond.setCellValueFactory(new PropertyValueFactory<>("cond"));
            this.cond.setCellFactory(p -> new CondImageCell());

            SortedList<Plane> sortedList2 = new SortedList<>(this.planes);
            this.planeTable.setItems(this.planes);
            sortedList2.comparatorProperty().bind(this.planeTable.comparatorProperty());
            this.planeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.planeTable.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

            this.areaTable.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(this::plane);

            this.timeline = new Timeline();
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.getKeyFrames().add(new KeyFrame(
                    javafx.util.Duration.seconds(1),
                    this::update));
            this.timeline.play();
            this.setAirBase();
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void deckBuilderCopy() {
        List<AirBase> airbases = new ArrayList<>();
        addItem(airbases, this.areaTable.getSelectionModel().getSelectedItem());
        DeckBuilder.airbaseSelectionCopy(airbases);
    }
    
    void addItem(List<AirBase> list, TreeItem<AreaTable> item) {
        Optional.ofNullable(item.getValue()).map(AreaTable::getAirBase).ifPresent(list::add);
        Optional.ofNullable(item.getChildren()).ifPresent(children -> children.forEach(child -> addItem(list, child)));
    }

    /**
     * クリップボードにコピー
     */
    @FXML
    void copy() {
        TableTool.selectionCopy(this.planeTable);
    }

    /**
     * すべてを選択
     */
    @FXML
    void selectAll() {
        TableTool.selectAll(this.planeTable);
    }

    /**
     * テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisible() {
        try {
            TableTool.showVisibleSetting(this.planeTable, this.getClass().toString() + "#" + "planeTable",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面の更新
     *
     * @param e ActionEvent
     */
    void update(ActionEvent e) {
        int hashCode = Mapinfo.get()
                .getAirBase()
                .hashCode();
        if (this.hashCode != hashCode) {
            int selection = this.areaTable.getSelectionModel().getSelectedIndex();
            this.setAirBase();
            this.areaTable.getSelectionModel().select(selection);
            this.hashCode = hashCode;
        }
    }

    /**
     * 基地航空隊を設定する
     */
    private void setAirBase() {
        this.hashCode = Mapinfo.get()
                .getAirBase()
                .hashCode();
        // ルート要素(非表示)
        TreeItem<AreaTable> root = new TreeItem<AreaTable>(new AreaTable());
        this.areaTable.setRoot(root);

        // 海域
        Map<Integer, Maparea> mapareaMap = MapareaCollection.get().getMaparea();

        // 通常海域 ツリー
        AreaTable normalArea = new AreaTable();
        normalArea.setName("通常海域");
        TreeItem<AreaTable> normal = new TreeItem<AreaTable>(normalArea);
        root.getChildren().add(normal);

        // イベント海域 ツリー
        AreaTable eventArea = new AreaTable();
        eventArea.setName("イベント海域");
        TreeItem<AreaTable> event = new TreeItem<AreaTable>(eventArea);
        root.getChildren().add(event);

        Map<Integer, List<AirBase>> airBaseMap = Mapinfo.get()
                .getAirBase()
                .stream()
                .sorted(Comparator.comparing(AirBase::getAreaId).thenComparing(AirBase::getRid))
                .collect(Collectors.groupingBy(AirBase::getAreaId));

        for (Entry<Integer, List<AirBase>> entry : airBaseMap.entrySet()) {
            Maparea maparea = mapareaMap.get(entry.getKey());
            if (maparea != null) {
                AreaTable seaArea = new AreaTable();
                seaArea.setName(maparea.getName());
                TreeItem<AreaTable> sea = new TreeItem<AreaTable>(seaArea);

                for (AirBase airBase : entry.getValue()) {
                    AreaTable baseRow = new AreaTable();
                    TreeItem<AreaTable> base = new TreeItem<AreaTable>(baseRow);

                    baseRow.setName(airBase.getName());
                    baseRow.setActionKind(this.actionKind(airBase.getActionKind()));
                    baseRow.setSeiku(Integer.toString(AirBases.airSuperiority(airBase)));

                    Mapinfo.Distance distance = airBase.getDistance();
                    baseRow.setDistance(Integer.toString(distance.getBase() + distance.getBonus()));

                    for (PlaneInfo planeInfo : airBase.getPlaneInfo()) {
                        Plane plane = new Plane();

                        plane.setSlot(planeInfo.getSlotid());
                        plane.setCount(planeInfo.getCount());
                        plane.setMaxCount(planeInfo.getMaxCount());
                        plane.setCond(planeInfo.getCond());
                        plane.setPlaneInfo(planeInfo);

                        baseRow.getPlanes().add(plane);
                    }
                    baseRow.setAirBase(airBase);

                    sea.getChildren().add(base);
                }
                if (maparea.getType() == 0) {
                    normal.getChildren().add(sea);
                } else {
                    event.getChildren().add(sea);
                }
                sea.setExpanded(true);
            }
        }
        normal.setExpanded(true);
        event.setExpanded(true);
    }

    private String actionKind(int actionKind) {
        switch (actionKind) {
        case 0:
            return "待機";
        case 1:
            return "出撃";
        case 2:
            return "防空";
        case 3:
            return "退避";
        case 4:
            return "休息";
        }
        return "";
    }

    /**
     * 右ペインに中隊を表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void plane(ObservableValue<? extends TreeItem<AreaTable>> observable,
            TreeItem<AreaTable> oldValue, TreeItem<AreaTable> value) {
        this.planes.clear();
        this.info.getChildren().clear();

        if (value == null) {
            return;
        }

        AreaTable row = value.getValue();
        if (row != null && row.getAirBase() != null) {
            this.planes.addAll(row.getPlanes());

            int seiku = AirBases.airSuperiority(row.getAirBase());

            this.seikuTotal.setText(Integer.toString(seiku));

            this.lesser.setText(String.valueOf(seiku * 3));
            this.equal.setText(String.valueOf((int) (seiku * (3D / 2D))));
            this.superior.setText(String.valueOf((int) (seiku * (2D / 3D))));
            this.greater.setText(String.valueOf((int) (seiku * (1D / 3D))));

            boolean lowsupply = row.getAirBase().getPlaneInfo().stream()
                    .filter(info -> info.getState() == 1)
                    .filter(info -> Objects.nonNull(info.getCount()))
                    .anyMatch(info -> !info.getMaxCount().equals(info.getCount()));
            if (lowsupply) {
                Button button = new Button("補給不足");
                HBox.setMargin(button, new Insets(1));
                this.info.getChildren().add(button);
            }
            boolean cond = row.getAirBase().getPlaneInfo().stream()
                    .filter(info -> info.getState() == 1)
                    .filter(info -> Objects.nonNull(info.getCond()))
                    .anyMatch(info -> info.getCond() != 1);
            if (cond) {
                Button button = new Button("疲労");
                HBox.setMargin(button, new Insets(1));
                this.info.getChildren().add(button);
            }
            boolean change = row.getAirBase().getPlaneInfo().stream()
                    .anyMatch(info -> info.getState() == 2);
            if (change) {
                Button button = new Button("配置転換中");
                HBox.setMargin(button, new Insets(1));
                this.info.getChildren().add(button);
            }
        } else {
            this.seikuTotal.setText("0");
            this.lesser.setText("0");
            this.equal.setText("0");
            this.superior.setText("0");
            this.greater.setText("0");
        }
    }

    /**
     * ウインドウを閉じる時のアクション
     *
     * @param e WindowEvent
     */
    @Override
    protected void onWindowHidden(WindowEvent e) {
        if (this.timeline != null) {
            this.timeline.stop();
        }
    }

    /**
     * 基地航空隊
     */
    public static class AreaTable {

        /** 基地航空隊 */
        private StringProperty name;

        /** 行動 */
        private StringProperty actionKind;

        /** 制空値 */
        private StringProperty seiku;

        /** 戦闘行動半径 */
        private StringProperty distance;

        /** 中隊 */
        private List<Plane> planes = new ArrayList<>();

        /** 基地航空隊 */
        private AirBase airBase;

        /**
         * 基地航空隊を設定します。
         * @param name 基地航空隊計
         */
        public void setName(String name) {
            this.name = new SimpleStringProperty(name);
        }

        /**
         * 基地航空隊を取得します。
         * @return 基地航空隊
         */
        public StringProperty nameProperty() {
            return this.name;
        }

        /**
         * 行動を設定します。
         * @param actionKind 行動
         */
        public void setActionKind(String actionKind) {
            this.actionKind = new SimpleStringProperty(actionKind);
        }

        /**
         * 行動を取得します。
         * @return 行動
         */
        public StringProperty actionKindProperty() {
            return this.actionKind;
        }

        /**
         * 制空値を設定します。
         * @param seiku 制空値
         */
        public void setSeiku(String seiku) {
            this.seiku = new SimpleStringProperty(seiku);
        }

        /**
         * 制空値を取得します。
         * @return 制空値
         */
        public StringProperty seikuProperty() {
            return this.seiku;
        }

        /**
         * 戦闘行動半径を設定します。
         * @param distance 戦闘行動半径
         */
        public void setDistance(String distance) {
            this.distance = new SimpleStringProperty(distance);
        }

        /**
         * 戦闘行動半径を取得します。
         * @return 戦闘行動半径
         */
        public StringProperty distanceProperty() {
            return this.distance;
        }

        /**
         * 中隊を取得します。
         * @return 中隊
         */
        public List<Plane> getPlanes() {
            return this.planes;
        }

        /**
         * 基地航空隊を設定します。
         * @param airBase 基地航空隊
         */
        public void setAirBase(AirBase airBase) {
            this.airBase = airBase;
        }

        /**
         * 基地航空隊を取得します。
         * @return 基地航空隊
         */
        public AirBase getAirBase() {
            return this.airBase;
        }

        @Override
        public String toString() {
            return new StringJoiner("\t")
                    .add(this.name.get())
                    .add(this.actionKind.get())
                    .add(this.seiku.get())
                    .add(this.distance.get())
                    .toString();
        }
    }

    /**
     * 中隊
     */
    public static class Plane {

        /** スロット */
        private IntegerProperty slot;

        /** 稼働 */
        private IntegerProperty count;

        /** 定数 */
        private IntegerProperty maxCount;

        /** 疲労 */
        private IntegerProperty cond;

        /** 中隊 */
        private PlaneInfo planeInfo;

        /**
         * スロットを設定します。
         * @param slot スロット
         */
        public void setSlot(Integer slot) {
            this.slot = new SimpleIntegerProperty(slot);
        }

        /**
         * スロットを取得します。
         * @return スロット
         */
        public IntegerProperty slotProperty() {
            return this.slot;
        }

        /**
         * 稼働を設定します。
         * @param count 稼働
         */
        public void setCount(Integer count) {
            if (count != null)
                this.count = new SimpleIntegerProperty(count);
        }

        /**
         * 稼働を取得します。
         * @return 稼働
         */
        public IntegerProperty countProperty() {
            return this.count;
        }

        /**
         * 定数を設定します。
         * @param maxCount 定数
         */
        public void setMaxCount(Integer maxCount) {
            if (maxCount != null)
                this.maxCount = new SimpleIntegerProperty(maxCount);
        }

        /**
         * 定数を取得します。
         * @return 定数
         */
        public IntegerProperty maxCountProperty() {
            return this.maxCount;
        }

        /**
         * 疲労を設定します。
         * @param cond 疲労
         */
        public void setCond(Integer cond) {
            if (cond != null)
                this.cond = new SimpleIntegerProperty(cond);
        }

        /**
         * 疲労を取得します。
         * @return 疲労
         */
        public IntegerProperty condProperty() {
            return this.cond;
        }

        /**
         * 中隊を設定します。
         * @param planeInfo 中隊
         */
        public void setPlaneInfo(PlaneInfo planeInfo) {
            this.planeInfo = planeInfo;
        }

        /**
         * 中隊を取得します。
         * @return 中隊
         */
        public PlaneInfo getPlaneInfo() {
            return this.planeInfo;
        }

        @Override
        public String toString() {
            Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                    .getSlotitemMap();
            return new StringJoiner("\t")
                    .add(Items.name(itemMap.get(this.slot.get())))
                    .add(Optional.ofNullable(this.count).map(IntegerProperty::get).map(String::valueOf).orElse(""))
                    .add(Optional.ofNullable(this.maxCount).map(IntegerProperty::get).map(String::valueOf).orElse(""))
                    .add(Optional.ofNullable(this.cond).map(IntegerProperty::get).map(String::valueOf).orElse(""))
                    .toString();
        }
    }

    /**
     * 装備画像のセル
     *
     */
    private static class ItemImageCell extends TableCell<Plane, Integer> {
        @Override
        protected void updateItem(Integer itemId, boolean empty) {
            super.updateItem(itemId, empty);

            this.getStyleClass().removeAll("change");

            if (!empty) {
                Map<Integer, SlotItem> itemMap = SlotItemCollection.get()
                        .getSlotitemMap();

                SlotItem item = itemMap.get(itemId);

                if (item != null) {
                    this.setGraphic(new ImageView(Items.itemImage(item)));
                    this.setText(Items.name(item));
                } else {
                    this.setGraphic(null);
                    this.setText("未配備");
                }

                @SuppressWarnings("unchecked")
                TableRow<Plane> row = this.getTableRow();
                if (row != null) {
                    Plane plane = row.getItem();
                    if (plane != null && plane.getPlaneInfo() != null) {
                        PlaneInfo planeInfo = plane.getPlaneInfo();
                        if (planeInfo.getState() != 1) {
                            this.getStyleClass().add("change");
                        }
                    }
                }
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 稼働のセル
     *
     */
    private static class CountImageCell extends TableCell<Plane, Integer> {

        @Override
        protected void updateItem(Integer count, boolean empty) {
            super.updateItem(count, empty);

            this.getStyleClass().removeAll("lowsupply");

            if (!empty) {
                if (count != null) {
                    @SuppressWarnings("unchecked")
                    TableRow<Plane> row = this.getTableRow();
                    if (row != null) {
                        Plane plane = row.getItem();
                        if (plane != null && plane.getPlaneInfo() != null) {
                            PlaneInfo planeInfo = plane.getPlaneInfo();
                            if (planeInfo.getCount() != null && !planeInfo.getCount().equals(planeInfo.getMaxCount())) {
                                this.getStyleClass().add("lowsupply");
                            }
                        }
                    }
                    this.setText(String.valueOf(count));
                } else {
                    this.setText(null);
                }
            } else {
                this.setText(null);
            }
        }
    }

    /**
     * 疲労画像のセル
     *
     */
    private static class CondImageCell extends TableCell<Plane, Integer> {

        @Override
        protected void updateItem(Integer cond, boolean empty) {
            super.updateItem(cond, empty);

            this.getStyleClass().removeAll("cond0", "cond1", "cond2", "cond3");

            if (!empty) {
                if (cond != null) {
                    URL url = null;
                    if (cond == 2) {
                        url = PluginServices.getResource("logbook/gui/cond_orange.png");
                    } else if (cond == 3) {
                        url = PluginServices.getResource("logbook/gui/cond_red.png");
                    }
                    if (url != null) {
                        this.setGraphic(new ImageView(new Image(url.toString())));
                    }
                    this.setText(cond.toString());
                    this.getStyleClass().add("cond" + cond);
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
}
