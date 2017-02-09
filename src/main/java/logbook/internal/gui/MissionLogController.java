package logbook.internal.gui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import logbook.bean.AppConfig;
import logbook.internal.BattleLogs.Unit;
import logbook.internal.Logs;
import logbook.internal.log.LogWriter;
import logbook.internal.log.MissionResultLogFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 遠征ログ
 *
 */
public class MissionLogController extends WindowController {

    /** 統計 */
    @FXML
    private TreeTableView<MissionLogCollect> collect;

    /** 集計 */
    @FXML
    private TreeTableColumn<MissionLogCollect, String> unit;

    /** 大成功 */
    @FXML
    private TreeTableColumn<MissionLogCollect, Integer> successGood;

    /** 成功 */
    @FXML
    private TreeTableColumn<MissionLogCollect, Integer> success;

    /** 失敗 */
    @FXML
    private TreeTableColumn<MissionLogCollect, Integer> fail;

    /** 詳細 */
    @FXML
    private TableView<MissionLogDetail> detail;

    /** 日付 */
    @FXML
    private TableColumn<MissionLogDetail, String> date;

    /** 遠征名 */
    @FXML
    private TableColumn<MissionLogDetail, String> name;

    /** 結果 */
    @FXML
    private TableColumn<MissionLogDetail, String> result;

    /** 燃料 */
    @FXML
    private TableColumn<MissionLogDetail, Integer> fuel;

    /** 弾薬 */
    @FXML
    private TableColumn<MissionLogDetail, Integer> ammo;

    /** 鋼材 */
    @FXML
    private TableColumn<MissionLogDetail, Integer> metal;

    /** ボーキ */
    @FXML
    private TableColumn<MissionLogDetail, Integer> bauxite;

    /** アイテム1 */
    @FXML
    private TableColumn<MissionLogDetail, String> item1name;

    /** アイテム1 */
    @FXML
    private TableColumn<MissionLogDetail, String> item1count;

    /** アイテム2 */
    @FXML
    private TableColumn<MissionLogDetail, String> item2name;

    /** アイテム2 */
    @FXML
    private TableColumn<MissionLogDetail, String> item2count;

    /** 集計 */
    @FXML
    private TableView<MissionAggregate> aggregate;

    /** 資材 */
    @FXML
    private TableColumn<MissionAggregate, String> resource;

    /** 個数 */
    @FXML
    private TableColumn<MissionAggregate, Integer> count;

    /** 平均 */
    @FXML
    private TableColumn<MissionAggregate, Double> average;

    @FXML
    private PieChart chart;

    /** ログ */
    private Map<Unit, List<SimpleMissionLog>> logMap = new EnumMap<>(Unit.class);

    /** 詳細 */
    private ObservableList<MissionLogDetail> details = FXCollections.observableArrayList();

    /** 集計 */
    private ObservableList<MissionAggregate> aggregates = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        TableTool.setVisible(this.detail, this.getClass() + "#" + "detail");
        TableTool.setVisible(this.aggregate, this.getClass() + "#" + "aggregate");

        // 集計
        this.collect.setShowRoot(false);
        this.collect.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.unit.setCellValueFactory(new TreeItemPropertyValueFactory<>("unit"));
        this.successGood.setCellValueFactory(new TreeItemPropertyValueFactory<>("successGood"));
        this.success.setCellValueFactory(new TreeItemPropertyValueFactory<>("success"));
        this.fail.setCellValueFactory(new TreeItemPropertyValueFactory<>("fail"));

        // 詳細
        SortedList<MissionLogDetail> sortedList = new SortedList<>(this.details);
        this.detail.setItems(this.details);
        sortedList.comparatorProperty().bind(this.detail.comparatorProperty());
        this.detail.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.detail.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.result.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.fuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        this.ammo.setCellValueFactory(new PropertyValueFactory<>("ammo"));
        this.metal.setCellValueFactory(new PropertyValueFactory<>("metal"));
        this.bauxite.setCellValueFactory(new PropertyValueFactory<>("bauxite"));
        this.item1name.setCellValueFactory(new PropertyValueFactory<>("item1name"));
        this.item1count.setCellValueFactory(new PropertyValueFactory<>("item1count"));
        this.item2name.setCellValueFactory(new PropertyValueFactory<>("item2name"));
        this.item2count.setCellValueFactory(new PropertyValueFactory<>("item2count"));

        // 集計
        SortedList<MissionAggregate> sortedList2 = new SortedList<>(this.aggregates);
        this.aggregate.setItems(this.aggregates);
        sortedList2.comparatorProperty().bind(this.aggregate.comparatorProperty());
        this.aggregate.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.aggregate.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
        this.resource.setCellValueFactory(new PropertyValueFactory<>("resource"));
        this.count.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.average.setCellValueFactory(new PropertyValueFactory<>("average"));

        this.readLog();
        this.setCollect();

        // 選択された時のリスナーを設定
        this.collect.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::detail);
        this.aggregate.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::chart);
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
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void copyAggregate(ActionEvent event) {
        TableTool.selectionCopy(this.aggregate);
    }

    @FXML
    void selectAllAggregate(ActionEvent event) {
        TableTool.selectAll(this.aggregate);
    }

    @FXML
    void columnVisibleAggregate(ActionEvent event) {
        try {
            TableTool.showVisibleSetting(this.aggregate, this.getClass() + "#" + "aggregate",
                    this.getWindow());
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
    private void detail(ObservableValue<? extends TreeItem<MissionLogCollect>> observable,
            TreeItem<MissionLogCollect> oldValue, TreeItem<MissionLogCollect> value) {
        this.details.clear();
        this.aggregates.clear();

        this.chart.getData().clear();

        if (value != null) {
            this.chart.setTitle("取得資材");

            MissionLogCollect collect = value.getValue();

            List<SimpleMissionLog> subLog = this.logMap.get(collect.getCollectUnit())
                    .stream()
                    .filter(e -> collect.getName() == null || e.getName().equals(collect.getName()))
                    .collect(Collectors.toList());

            this.details.addAll(subLog.stream()
                    .map(MissionLogDetail::toMissionLogDetail)
                    .collect(Collectors.toList()));

            List<Pair> aggregateBase = this.aggregateBase(subLog);

            BiConsumer<Map<String, Integer>, Pair> accumulator = (map, pair) -> {
                map.merge(pair.getName(), pair.getValue(), Integer::sum);
            };
            BiConsumer<Map<String, Integer>, Map<String, Integer>> combiner = (map1, map2) -> {
                for (Entry<String, Integer> entry : map2.entrySet()) {
                    map1.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            };
            Map<String, Integer> resources = aggregateBase.stream()
                    .collect(LinkedHashMap<String, Integer>::new, accumulator, combiner);

            for (Entry<String, Integer> entry : resources.entrySet()) {
                MissionAggregate agg = new MissionAggregate();
                agg.setResource(entry.getKey());
                agg.setCount(entry.getValue());
                agg.setAverage(((double) entry.getValue()) / subLog.size());

                // 資材別の合計を表示する
                this.aggregates.add(agg);

                // 右ペインのチャートに資材別の取得割合を表示する
                if (entry.getValue() > 0) {
                    this.chart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    /**
     * 右ペインのチャートに遠征・資材別の取得割合を表示する
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void chart(ObservableValue<? extends MissionAggregate> observable,
            MissionAggregate oldValue, MissionAggregate value) {
        this.chart.getData().clear();

        if (value != null) {
            TreeItem<MissionLogCollect> ti = this.collect.getSelectionModel()
                    .getSelectedItem();

            if (ti != null) {
                this.chart.setTitle(value.resourceProperty().get() + " の取得元");

                MissionLogCollect collect = ti.getValue();
                Map<String, List<SimpleMissionLog>> subLog = this.logMap.get(collect.getCollectUnit())
                        .stream()
                        .filter(e -> collect.getName() == null || e.getName().equals(collect.getName()))
                        .collect(Collectors.groupingBy(SimpleMissionLog::getName));

                for (Entry<String, List<SimpleMissionLog>> entry : subLog.entrySet()) {
                    int sum = this.aggregateBase(entry.getValue()).stream()
                            .filter(p -> p.getName().equals(value.resourceProperty().get()))
                            .mapToInt(Pair::getValue)
                            .sum();
                    if (sum > 0) {
                        this.chart.getData().add(new PieChart.Data(entry.getKey(), sum));
                    }
                }
            }
        }
    }

    /**
     * ログを読み込む
     */
    private void readLog() {
        Function<String, SimpleMissionLog> mapper = line -> {
            try {
                return new SimpleMissionLog(line);
            } catch (Exception e) {
                LoggerHolder.LOG.warn("遠征報告書の読み込み中に例外", e);
            }
            return null;
        };
        Path dir = Paths.get(AppConfig.get().getReportPath());
        Path path = dir.resolve(new MissionResultLogFormat().fileName());

        // 今日
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT+04:00"))
                .truncatedTo(ChronoUnit.DAYS);
        // ログ読み込み制限
        ZonedDateTime limit = now.minusMonths(2);

        Stream<String> tmp;
        if (Files.exists(path)) {
            try {
                tmp = Files.lines(path, LogWriter.DEFAULT_CHARSET);
            } catch (IOException e) {
                tmp = Stream.empty();
            }
        } else {
            tmp = Stream.empty();
        }
        try (Stream<String> lines = tmp) {
            List<SimpleMissionLog> all = lines.skip(1)
                    .filter(l -> !l.isEmpty())
                    .map(mapper)
                    .filter(Objects::nonNull)
                    .filter(log -> log.getDate().compareTo(limit) > 0)
                    .collect(Collectors.toList());

            for (Unit unit : Unit.values()) {
                this.logMap.put(unit, all.stream()
                        .filter(log -> unit.accept(log.getDate(), now))
                        .collect(Collectors.toList()));
            }
        }
    }

    /**
     * 左ペインの集計
     */
    private void setCollect() {
        // 統計
        // ルート要素(非表示)
        TreeItem<MissionLogCollect> root = new TreeItem<MissionLogCollect>(new MissionLogCollect());
        this.collect.setRoot(root);

        for (Unit unit : Unit.values()) {
            List<SimpleMissionLog> list = this.logMap.get(unit);
            // 単位のルート
            MissionLogCollect rootValue = collect(list, null);
            rootValue.setUnit(unit.getName());
            rootValue.setCollectUnit(unit);

            TreeItem<MissionLogCollect> unitRoot = new TreeItem<MissionLogCollect>(rootValue);
            unitRoot.setExpanded(true);

            // 遠征の名前
            List<String> names = list.stream()
                    .map(SimpleMissionLog::getName)
                    .distinct()
                    .sorted(Comparator.naturalOrder())
                    .collect(Collectors.toList());
            for (String name : names) {
                // 遠征毎の集計
                MissionLogCollect subValue = collect(list, name);
                subValue.setCollectUnit(unit);
                subValue.setName(name);

                TreeItem<MissionLogCollect> subRow = new TreeItem<MissionLogCollect>(subValue);
                unitRoot.getChildren().add(subRow);
            }
            root.getChildren().add(unitRoot);
        }
    }

    /**
     * 集計行の作成
     *
     * @param list 集計対象
     * @param name 遠征名
     * @return 集計行(左ペインの行)
     */
    private static MissionLogCollect collect(List<SimpleMissionLog> list, String name) {
        MissionLogCollect row = new MissionLogCollect();

        List<SimpleMissionLog> subList;
        if (name != null) {
            subList = list.stream()
                    .filter(e -> e.getName().equals(name))
                    .collect(Collectors.toList());
            row.setUnit(name);
        } else {
            subList = list;
            row.setUnit("-");
        }
        row.setSuccessGood((int) subList.stream().map(SimpleMissionLog::getResult)
                .filter("大成功"::equals).count());
        row.setSuccess((int) subList.stream().map(SimpleMissionLog::getResult)
                .filter("成功"::equals).count());
        row.setFail((int) subList.stream().map(SimpleMissionLog::getResult)
                .filter("失敗"::equals).count());
        return row;
    }

    private List<Pair> aggregateBase(List<SimpleMissionLog> log) {
        return log.stream()
                .flatMap(e -> {
                    List<Pair> pairs = new ArrayList<>();
                    pairs.add(new Pair("燃料", e.getFuel()));
                    pairs.add(new Pair("弾薬", e.getAmmo()));
                    pairs.add(new Pair("鋼材", e.getMetal()));
                    pairs.add(new Pair("ボーキ", e.getBauxite()));
                    if (!e.getItem1name().isEmpty()) {
                        pairs.add(new Pair(e.getItem1name(), e.getItem1count()));
                    }
                    if (!e.getItem2name().isEmpty()) {
                        pairs.add(new Pair(e.getItem2name(), e.getItem2count()));
                    }
                    return pairs.stream();
                })
                .collect(Collectors.toList());
    }

    /**
     * 遠征統計のベース
     *
     */
    @Data
    public static class SimpleMissionLog {

        /** 日付 */
        private ZonedDateTime date;
        /** 結果 */
        private String result;
        /** 海域 */
        private String area;
        /** 遠征名 */
        private String name;
        /** 燃料 */
        private int fuel;
        /** 弾薬 */
        private int ammo;
        /** 鋼材 */
        private int metal;
        /** ボーキ */
        private int bauxite;
        /** アイテム1名前 */
        private String item1name = "";
        /** アイテム1個数 */
        private int item1count;
        /** アイテム2名前 */
        private String item2name = "";
        /** アイテム2個数 */
        private int item2count;
        /** 取得経験値計 */
        private int exp;

        /**
         * 遠征報告書.csvから遠征統計のベースを作成します
         *
         * @param line 遠征報告書.csvの行
         */
        public SimpleMissionLog(String line) {
            String[] columns = line.split(",", -1);

            // 任務の更新時間が午前5時のため
            // 日付文字列を日本時間として解釈した後、GMT+04:00のタイムゾーンに変更します
            TemporalAccessor ta = Logs.DATE_FORMAT.parse(columns[0]);
            ZonedDateTime date = ZonedDateTime.of(LocalDateTime.from(ta), ZoneId.of("Asia/Tokyo"))
                    .withZoneSameInstant(ZoneId.of("GMT+04:00"));
            this.setDate(date);
            this.setResult(columns[1]);
            this.setArea(columns[2]);
            this.setName(columns[3]);
            this.setFuel(Integer.parseInt(columns[4]));
            this.setAmmo(Integer.parseInt(columns[5]));
            this.setMetal(Integer.parseInt(columns[6]));
            this.setBauxite(Integer.parseInt(columns[7]));
            if (columns.length > 8)
                this.setItem1name(columns[8]);
            if (columns.length > 9)
                this.setItem1count(columns[9].isEmpty() ? 0 : Integer.parseInt(columns[9]));
            if (columns.length > 10)
                this.setItem2name(columns[10]);
            if (columns.length > 11)
                this.setItem2count(columns[11].isEmpty() ? 0 : Integer.parseInt(columns[11]));
            if (columns.length > 12)
                this.setExp(columns[12].isEmpty() ? 0 : Integer.parseInt(columns[12]));
        }
    }

    /**
     * 名前と値のペア
     */
    @Data
    @AllArgsConstructor
    private static class Pair {

        private String name;

        private int value;
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(MissionLogController.class);
    }
}
