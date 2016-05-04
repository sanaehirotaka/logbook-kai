package logbook.internal.gui;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import logbook.bean.AppConfig;
import logbook.internal.LogWriter;
import logbook.internal.Logs;

/**
 * 資材ログ
 *
 */
public class ResourceChartController extends WindowController {

    /** 資材ログで使用するタイムゾーン */
    private static final ZoneId TIME_ZONE = ZoneId.of("Asia/Tokyo");

    /** 資材テーブルに表示する資材のフォーマット */
    private static final MessageFormat COMPARE_FORMAT = new MessageFormat("{0,number,0}({1,number,+0;-0})");

    /** 日付書式 */
    public static final DateTimeFormatter TABLE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 期間 */
    @FXML
    private ChoiceBox<ScaleOption> term;

    /** 開始 */
    @FXML
    private DatePicker from;

    /** 終了 */
    @FXML
    private DatePicker to;

    /** 燃料 */
    @FXML
    private CheckBox fuel;

    /** 弾薬 */
    @FXML
    private CheckBox ammo;

    /** 鋼材 */
    @FXML
    private CheckBox metal;

    /** ボーキサイト */
    @FXML
    private CheckBox bauxite;

    /** 高速修復材 */
    @FXML
    private CheckBox bucket;

    /** 高速建造材 */
    @FXML
    private CheckBox burner;

    /** 開発資材 */
    @FXML
    private CheckBox research;

    /** 改修資材 */
    @FXML
    private CheckBox improve;

    /** ゼロを基準 */
    @FXML
    private CheckBox forceZero;

    /** チャートx軸 */
    @FXML
    private NumberAxis xAxis;

    /** チャートy軸 */
    @FXML
    private NumberAxis yAxis;

    /** チャート */
    @FXML
    private LineChart<Number, Number> chart;

    /** テーブル */
    @FXML
    private TableView<ResourceTable> table;

    /** 日付列 */
    @FXML
    private TableColumn<ResourceTable, String> date;

    /** 燃料列 */
    @FXML
    private TableColumn<ResourceTable, String> fuelGap;

    /** 弾薬列 */
    @FXML
    private TableColumn<ResourceTable, String> ammoGap;

    /** 鋼材列 */
    @FXML
    private TableColumn<ResourceTable, String> metalGap;

    /** ボーキ列 */
    @FXML
    private TableColumn<ResourceTable, String> bauxiteGap;

    /** 高速修復材列 */
    @FXML
    private TableColumn<ResourceTable, String> bucketGap;

    /** 高速建造材列 */
    @FXML
    private TableColumn<ResourceTable, String> burnerGap;

    /** 開発資材列 */
    @FXML
    private TableColumn<ResourceTable, String> researchGap;

    /** 改修資材列 */
    @FXML
    private TableColumn<ResourceTable, String> improveGap;

    @FXML
    void initialize() {
        // 資材ログのテーブル列をバインド
        this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.fuelGap.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        this.ammoGap.setCellValueFactory(new PropertyValueFactory<>("ammo"));
        this.metalGap.setCellValueFactory(new PropertyValueFactory<>("metal"));
        this.bauxiteGap.setCellValueFactory(new PropertyValueFactory<>("bauxite"));
        this.bucketGap.setCellValueFactory(new PropertyValueFactory<>("bucket"));
        this.burnerGap.setCellValueFactory(new PropertyValueFactory<>("burner"));
        this.researchGap.setCellValueFactory(new PropertyValueFactory<>("research"));
        this.improveGap.setCellValueFactory(new PropertyValueFactory<>("improve"));

        // 終了日付を初期値として設定
        this.to.setValue(LocalDate.from(ZonedDateTime.now(TIME_ZONE)));

        this.term.setItems(FXCollections.observableArrayList(ScaleOption.values()));
        this.term.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> this.changeScaleAction(n));
        this.term.getSelectionModel().select(2);

        // 資材ログのテーブル読み込み
        this.loadTable();
    }

    @FXML
    void change(ActionEvent event) {
        this.changeAction();
    }

    /**
     * 期間が変更された場合
     */
    private void changeScaleAction(ScaleOption scale) {
        if (scale != null) {
            // 終了日付を基準に期間を減算して開始日時を設定する
            LocalDate toDate = this.to.getValue();
            ZonedDateTime fromDateTime = ZonedDateTime.of(toDate.minusDays(scale.getDay()), LocalTime.MIN, TIME_ZONE);
            this.from.setValue(fromDateTime.toLocalDate());
            this.changeAction();
        }
    }

    /**
     * 変更された場合
     */
    private void changeAction() {
        // 開始日時(自身を含む)
        ZonedDateTime fromDateTime = ZonedDateTime.of(this.from.getValue(), LocalTime.MIN, TIME_ZONE);
        // 終了日時(自身を含む)
        ZonedDateTime toDateTime = ZonedDateTime.of(this.to.getValue(), LocalTime.MAX, TIME_ZONE);

        // 横軸のtick及びフォーマットは"期間"のセレクションボックスから取得
        ScaleOption scale = this.term.getSelectionModel().getSelectedItem();

        // 横軸の目盛り設定
        if (scale != null) {
            this.xAxis.setTickUnit(scale.getTickUnit());
            this.xAxis.setTickLabelFormatter(new DateTimeConverter(fromDateTime, scale.getFormat()));
        }

        // ゼロを基準
        this.yAxis.setForceZeroInRange(this.forceZero.isSelected());

        try {
            Path logFile = Paths.get(AppConfig.get().getReportPath(), Logs.MATERIAL);
            List<ResourceLog> log;
            try (Stream<String> lines = Files.lines(logFile, LogWriter.DEFAULT_CHARSET)) {
                log = lines.skip(1)
                        .map(ResourceLog::new)
                        .filter(l -> l.getDate() != null)
                        .filter(l -> l.getDate().compareTo(fromDateTime) >= 0)
                        .filter(l -> l.getDate().compareTo(toDateTime) <= 0)
                        .collect(Collectors.toList());
            }
            ResourceSeries series = new ResourceSeries();
            // 燃料
            if (this.fuel.isSelected())
                series.setFuel(log.stream()
                        .map(r -> r.getFuelSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 弾薬
            if (this.ammo.isSelected())
                series.setAmmo(log.stream()
                        .map(r -> r.getAmmoSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 鋼材
            if (this.metal.isSelected())
                series.setMetal(log.stream()
                        .map(r -> r.getMetalSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // ボーキ
            if (this.bauxite.isSelected())
                series.setBauxite(log.stream()
                        .map(r -> r.getBauxiteSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 高速修復材
            if (this.bucket.isSelected())
                series.setBucket(log.stream()
                        .map(r -> r.getBucketSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 高速建造材
            if (this.burner.isSelected())
                series.setBurner(log.stream()
                        .map(r -> r.getBurnerSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 開発資材
            if (this.research.isSelected())
                series.setResearch(log.stream()
                        .map(r -> r.getResearchSeries(fromDateTime))
                        .collect(Collectors.toList()));
            // 改修資材
            if (this.improve.isSelected())
                series.setImprove(log.stream()
                        .map(r -> r.getImproveSeries(fromDateTime))
                        .collect(Collectors.toList()));

            this.chart.getData().clear();
            this.chart.getData().addAll(
                    Arrays.asList(series.getFuel(),
                            series.getAmmo(),
                            series.getMetal(),
                            series.getBauxite(),
                            series.getBucket(),
                            series.getBurner(),
                            series.getResearch(),
                            series.getImprove()));

        } catch (Exception e) {
            LoggerHolder.LOG.warn("資材ログの読込中に例外", e);
        }
    }

    /**
     * 資材ログのテーブルを作成する
     */
    private void loadTable() {
        Path logFile = Paths.get(AppConfig.get().getReportPath(), Logs.MATERIAL);
        Map<LocalDate, ResourceLog> logs;
        try {
            try (Stream<String> lines = Files.lines(logFile, LogWriter.DEFAULT_CHARSET)) {

                Function<ResourceLog, LocalDate> mapping = r -> r.getDate().toLocalDate();

                logs = lines
                        .skip(1)
                        .map(ResourceLog::new)
                        .filter(l -> l.getDate() != null)
                        .sorted(Comparator.comparing(ResourceLog::getDate))
                        .collect(Collectors.toMap(mapping, d -> d, (d1, d2) -> d2, LinkedHashMap::new));
            }
            ObservableList<ResourceTable> tableBody = FXCollections.observableArrayList();
            // 前日
            ResourceLog before = null;
            for (ResourceLog log : logs.values()) {
                ResourceTable row = new ResourceTable();
                row.setDate(TABLE_DATE_FORMAT.format(log.getDate()));
                row.setFuel(COMPARE_FORMAT.format(new Integer[] { log.getFuel(),
                        Optional.ofNullable(before)
                                .map(r -> log.getFuel() - r.getFuel())
                                .orElse(0) }));
                row.setAmmo(COMPARE_FORMAT.format(new Integer[] { log.getAmmo(),
                        Optional.ofNullable(before)
                                .map(r -> log.getAmmo() - r.getAmmo())
                                .orElse(0) }));
                row.setMetal(COMPARE_FORMAT.format(new Integer[] { log.getMetal(),
                        Optional.ofNullable(before)
                                .map(r -> log.getMetal() - r.getMetal())
                                .orElse(0) }));
                row.setBauxite(COMPARE_FORMAT.format(new Integer[] { log.getBauxite(),
                        Optional.ofNullable(before)
                                .map(r -> log.getBauxite() - r.getBauxite())
                                .orElse(0) }));
                row.setBucket(COMPARE_FORMAT.format(new Integer[] { log.getBucket(),
                        Optional.ofNullable(before)
                                .map(r -> log.getBucket() - r.getBucket())
                                .orElse(0) }));
                row.setBurner(COMPARE_FORMAT.format(new Integer[] { log.getBurner(),
                        Optional.ofNullable(before)
                                .map(r -> log.getBurner() - r.getBurner())
                                .orElse(0) }));
                row.setResearch(COMPARE_FORMAT.format(new Integer[] { log.getResearch(),
                        Optional.ofNullable(before)
                                .map(r -> log.getResearch() - r.getResearch())
                                .orElse(0) }));
                row.setImprove(COMPARE_FORMAT.format(new Integer[] { log.getImprove(),
                        Optional.ofNullable(before)
                                .map(r -> log.getImprove() - r.getImprove())
                                .orElse(0) }));
                tableBody.add(row);
                before = log;
            }
            Collections.reverse(tableBody);
            this.table.setItems(tableBody);
        } catch (Exception e) {
            LoggerHolder.LOG.warn("資材ログの読込中に例外", e);
        }
    }

    /**
     * チャートの時間軸ラベルに表示するテキスト
     *
     */
    private static class DateTimeConverter extends StringConverter<Number> {
        /** チャートに設定する最小の時刻 */
        private final long from;
        /** フォーマッター */
        private final DateTimeFormatter format;

        /**
         * @param from チャートに設定する最小の時刻
         */
        public DateTimeConverter(ZonedDateTime from, String format) {
            this.from = from.toEpochSecond();
            this.format = DateTimeFormatter.ofPattern(format);
        }

        @Override
        public Number fromString(String str) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString(Number n) {
            Instant instant = Instant.ofEpochSecond(n.longValue() + this.from);
            return this.format.format(ZonedDateTime.ofInstant(instant, TIME_ZONE));
        }
    }

    /**
     * スケールの選択肢
     *
     */
    private enum ScaleOption {
        /** 1日 */
        ONE_DAY("1日", "HH:mm", 1, TimeUnit.HOURS.toMillis(2)),
        /** 1週間 */
        ONE_WEEK("1週間", "M月d日", 7, TimeUnit.DAYS.toMillis(1)),
        /** 2週間 */
        TWO_WEEK("2週間", "M月d日", 14, TimeUnit.DAYS.toMillis(1)),
        /** 1ヶ月 */
        ONE_MONTH("1ヶ月", "M月d日", 30, TimeUnit.DAYS.toMillis(2)),
        /** 2ヶ月 */
        TWO_MONTH("2ヶ月", "M月d日", 60, TimeUnit.DAYS.toMillis(5)),
        /** 3ヶ月 */
        THREE_MONTH("3ヶ月", "M月d日", 90, TimeUnit.DAYS.toMillis(10)),
        /** 半年 */
        HALF_YEAR("半年", "M月d日", 180, TimeUnit.DAYS.toMillis(15)),
        /** 1年 */
        ONE_YEAR("1年", "M月d日", 365, TimeUnit.DAYS.toMillis(30));

        private String name;
        private String format;
        private int day;
        private long tickUnit;

        ScaleOption(String name, String format, int day, long tickUnit) {
            this.name = name;
            this.format = format;
            this.day = day;
            this.tickUnit = tickUnit;
        }

        public String getFormat() {
            return this.format;
        }

        public int getDay() {
            return this.day;
        }

        public long getTickUnit() {
            return this.tickUnit;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * 資材ログ
     *
     */
    private static class ResourceLog {

        /** 日付 */
        private ZonedDateTime date;

        /** 燃料 */
        private int fuel;

        /** 弾薬 */
        private int ammo;

        /** 鋼材 */
        private int metal;

        /** ボーキ */
        private int bauxite;

        /** 高速修復材 */
        private int bucket;

        /** 高速建造材 */
        private int burner;

        /** 開発資材 */
        private int research;

        /** 改修資材 */
        private int improve;

        /**
         * 資材ログのCSVからカンマ区切りで読み込む
         *
         * @param line 資材ログ
         */
        public ResourceLog(String line) {
            try {
                String[] columns = line.split(",", -1);
                // 日付
                TemporalAccessor ta = Logs.DATE_FORMAT.parse(columns[0]);
                this.setDate(ZonedDateTime.of(LocalDateTime.from(ta), TIME_ZONE));
                // 燃料
                this.setFuel(Integer.parseInt(columns[1]));
                // 弾薬
                this.setAmmo(Integer.parseInt(columns[2]));
                // 鋼材
                this.setMetal(Integer.parseInt(columns[3]));
                // ボーキ
                this.setBauxite(Integer.parseInt(columns[4]));
                // 高速修復材
                this.setBucket(Integer.parseInt(columns[5]));
                // 高速建造材
                this.setBurner(Integer.parseInt(columns[6]));
                // 開発資材
                this.setResearch(Integer.parseInt(columns[7]));
                // 改修資材
                if (columns.length > 8)
                    this.setImprove(Integer.parseInt(columns[8]));
            } catch (Exception e) {
                LoggerHolder.LOG.info("資材ログの読み込みに失敗しました", e);
            }
        }

        /**
         * 日付を取得します。
         * @return 日付
         */
        public ZonedDateTime getDate() {
            return this.date;
        }

        /**
         * 日付を設定します。
         * @param date 日付
         */
        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

        /**
         * 燃料を取得します。
         * @param from 開始日時
         * @return 燃料
         */
        public XYChart.Data<Number, Number> getFuelSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.fuel);
        }

        /**
         * 燃料を取得します。
         * @return 燃料
         */
        public int getFuel() {
            return this.fuel;
        }

        /**
         * 燃料を設定します。
         * @param fuel 燃料
         */
        public void setFuel(int fuel) {
            this.fuel = fuel;
        }

        /**
         * 弾薬を取得します。
         * @param from 開始日時
         * @return 弾薬
         */
        public XYChart.Data<Number, Number> getAmmoSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.ammo);
        }

        /**
         * 弾薬を取得します。
         * @return 弾薬
         */
        public int getAmmo() {
            return this.ammo;
        }

        /**
         * 弾薬を設定します。
         * @param ammo 弾薬
         */
        public void setAmmo(int ammo) {
            this.ammo = ammo;
        }

        /**
         * 鋼材を取得します。
         * @param from 開始日時
         * @return 鋼材
         */
        public XYChart.Data<Number, Number> getMetalSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.metal);
        }

        /**
         * 鋼材を取得します。
         * @return 鋼材
         */
        public int getMetal() {
            return this.metal;
        }

        /**
         * 鋼材を設定します。
         * @param metal 鋼材
         */
        public void setMetal(int metal) {
            this.metal = metal;
        }

        /**
         * ボーキサイトを取得します。
         * @param from 開始日時
         * @return ボーキサイト
         */
        public XYChart.Data<Number, Number> getBauxiteSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.bauxite);
        }

        /**
         * ボーキを取得します。
         * @return ボーキ
         */
        public int getBauxite() {
            return this.bauxite;
        }

        /**
         * ボーキサイトを設定します。
         * @param bauxite ボーキサイト
         */
        public void setBauxite(int bauxite) {
            this.bauxite = bauxite;
        }

        /**
         * 高速修復材を取得します。
         * @param from 開始日時
         * @return 高速修復材
         */
        public XYChart.Data<Number, Number> getBucketSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.bucket);
        }

        /**
         * 高速修復材を取得します。
         * @return 高速修復材
         */
        public int getBucket() {
            return this.bucket;
        }

        /**
         * 高速修復材を設定します。
         * @param bucket 高速修復材
         */
        public void setBucket(int bucket) {
            this.bucket = bucket;
        }

        /**
         * 高速建造材を取得します。
         * @param from 開始日時
         * @return 高速建造材
         */
        public XYChart.Data<Number, Number> getBurnerSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.burner);
        }

        /**
         * 高速建造材を取得します。
         * @return 高速建造材
         */
        public int getBurner() {
            return this.burner;
        }

        /**
         * 高速建造材を設定します。
         * @param burner 高速建造材
         */
        public void setBurner(int burner) {
            this.burner = burner;
        }

        /**
         * 開発資材を取得します。
         * @param from 開始日時
         * @return 開発資材
         */
        public XYChart.Data<Number, Number> getResearchSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.research);
        }

        /**
         * 開発資材を取得します。
         * @return 開発資材
         */
        public int getResearch() {
            return this.research;
        }

        /**
         * 開発資材を設定します。
         * @param research 開発資材
         */
        public void setResearch(int research) {
            this.research = research;
        }

        /**
         * 改修資材を取得します。
         * @param from 開始日時
         * @return 改修資材
         */
        public XYChart.Data<Number, Number> getImproveSeries(ZonedDateTime from) {
            return new XYChart.Data<>(this.date.toEpochSecond() - from.toEpochSecond(), this.improve);
        }

        /**
         * 改修資材を取得します。
         * @return 改修資材
         */
        public int getImprove() {
            return this.improve;
        }

        /**
         * 改修資材を設定します。
         * @param improve 改修資材
         */
        public void setImprove(int improve) {
            this.improve = improve;
        }
    }

    /**
     * 資材ログのSeriesを纏めた物
     *
     */
    private static class ResourceSeries {

        /** 燃料 */
        private XYChart.Series<Number, Number> fuel = new XYChart.Series<>();

        /** 弾薬 */
        private XYChart.Series<Number, Number> ammo = new XYChart.Series<>();

        /** 鋼材 */
        private XYChart.Series<Number, Number> metal = new XYChart.Series<>();

        /** ボーキ */
        private XYChart.Series<Number, Number> bauxite = new XYChart.Series<>();

        /** 高速修復材 */
        private XYChart.Series<Number, Number> bucket = new XYChart.Series<>();

        /** 高速建造材 */
        private XYChart.Series<Number, Number> burner = new XYChart.Series<>();

        /** 開発資材 */
        private XYChart.Series<Number, Number> research = new XYChart.Series<>();

        /** 改修資材 */
        private XYChart.Series<Number, Number> improve = new XYChart.Series<>();

        /**
         * ラベルを設定する
         */
        public ResourceSeries() {
            this.fuel.setName("燃料");
            this.ammo.setName("弾薬");
            this.metal.setName("鋼材");
            this.bauxite.setName("ボーキ");
            this.bucket.setName("高速修復材");
            this.burner.setName("高速建造材");
            this.research.setName("開発資材");
            this.improve.setName("改修資材");
        }

        /**
         * 燃料を取得します。
         * @return 燃料
         */
        public XYChart.Series<Number, Number> getFuel() {
            return this.fuel;
        }

        /**
         * 燃料を設定します。
         * @param fuel 燃料
         */
        public void setFuel(Collection<XYChart.Data<Number, Number>> fuel) {
            this.fuel.getData().addAll(fuel);
        }

        /**
         * 弾薬を取得します。
         * @return 弾薬
         */
        public XYChart.Series<Number, Number> getAmmo() {
            return this.ammo;
        }

        /**
         * 弾薬を設定します。
         * @param ammo 弾薬
         */
        public void setAmmo(Collection<XYChart.Data<Number, Number>> ammo) {
            this.ammo.getData().addAll(ammo);
        }

        /**
         * 鋼材を取得します。
         * @return 鋼材
         */
        public XYChart.Series<Number, Number> getMetal() {
            return this.metal;
        }

        /**
         * 鋼材を設定します。
         * @param metal 鋼材
         */
        public void setMetal(Collection<XYChart.Data<Number, Number>> metal) {
            this.metal.getData().addAll(metal);
        }

        /**
         * ボーキを取得します。
         * @return ボーキ
         */
        public XYChart.Series<Number, Number> getBauxite() {
            return this.bauxite;
        }

        /**
         * ボーキを設定します。
         * @param bauxite ボーキ
         */
        public void setBauxite(Collection<XYChart.Data<Number, Number>> bauxite) {
            this.bauxite.getData().addAll(bauxite);
        }

        /**
         * 高速修復材を取得します。
         * @return 高速修復材
         */
        public XYChart.Series<Number, Number> getBucket() {
            return this.bucket;
        }

        /**
         * 高速修復材を設定します。
         * @param bucket 高速修復材
         */
        public void setBucket(Collection<XYChart.Data<Number, Number>> bucket) {
            this.bucket.getData().addAll(bucket);
        }

        /**
         * 高速建造材を取得します。
         * @return 高速建造材
         */
        public XYChart.Series<Number, Number> getBurner() {
            return this.burner;
        }

        /**
         * 高速建造材を設定します。
         * @param burner 高速建造材
         */
        public void setBurner(Collection<XYChart.Data<Number, Number>> burner) {
            this.burner.getData().addAll(burner);
        }

        /**
         * 開発資材を取得します。
         * @return 開発資材
         */
        public XYChart.Series<Number, Number> getResearch() {
            return this.research;
        }

        /**
         * 開発資材を設定します。
         * @param research 開発資材
         */
        public void setResearch(Collection<XYChart.Data<Number, Number>> research) {
            this.research.getData().addAll(research);
        }

        /**
         * 改修資材を取得します。
         * @return 改修資材
         */
        public XYChart.Series<Number, Number> getImprove() {
            return this.improve;
        }

        /**
         * 改修資材を設定します。
         * @param improve 改修資材
         */
        public void setImprove(Collection<XYChart.Data<Number, Number>> improve) {
            this.improve.getData().addAll(improve);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabPane.class);
    }
}
