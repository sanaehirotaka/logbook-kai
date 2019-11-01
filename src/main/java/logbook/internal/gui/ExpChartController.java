package logbook.internal.gui;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import logbook.internal.BattleLogs;
import logbook.internal.BattleLogs.SimpleBattleLog;

/**
 * 経験値チャート
 *
 */
public class ExpChartController extends WindowController {

    @FXML
    private ChoiceBox<TypeOption> type;

    @FXML
    private ChoiceBox<ScaleOption> term;

    @FXML
    private CheckBox forceZero;

    @FXML
    private CheckBox stacked;

    @FXML
    private BarChart<String, Number> chart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    void initialize() {
        // 選択肢を追加
        this.type.setItems(FXCollections.observableArrayList(TypeOption.values()));
        this.term.setItems(FXCollections.observableArrayList(ScaleOption.values()));
        this.type.getSelectionModel().select(0);
        this.term.getSelectionModel().select(2);
        this.type.getSelectionModel().selectedItemProperty().addListener(this::changed);
        this.term.getSelectionModel().selectedItemProperty().addListener(this::changed);
        this.change();
    }

    @FXML
    void change(ActionEvent event) {
        this.change();
    }

    @FXML
    void forceZeroChange(ActionEvent event) {
        this.yAxis.setForceZeroInRange(this.forceZero.isSelected());
    }

    private void changed(ObservableValue<?> observable, Object oldValue, Object Object) {
        this.change();
    }

    /**
     * 選択肢が変更された時の処理
     */
    private void change() {
        TypeOption type = this.type.getSelectionModel().getSelectedItem();
        ScaleOption scale = this.term.getSelectionModel().getSelectedItem();

        ZonedDateTime baseDate = scale.convert(ZonedDateTime.now(), type);
        ZonedDateTime min = scale.min(baseDate);
        ZonedDateTime max = scale.max(baseDate);

        Map<ZonedDateTime, Double> data = this.load(type, scale, min, max);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type.toString());

        boolean stacked = this.stacked.isSelected();
        ObservableList<String> categories = FXCollections.observableArrayList();
        double current = 0D;
        for (Entry<ZonedDateTime, Double> entry : data.entrySet()) {
            String key = scale.getFormat().format(entry.getKey().withZoneSameInstant(ZoneId.of("Asia/Tokyo")));
            if (stacked) {
                current += entry.getValue();
            } else {
                current = entry.getValue();
            }
            categories.add(key);
            series.getData().add(new XYChart.Data<>(key, current));
        }
        this.xAxis.getCategories().clear();
        this.xAxis.getCategories().addAll(categories);
        this.xAxis.setCategories(categories);
        this.chart.getData().clear();
        this.chart.getData().add(series);
    }

    /**
     * グラフデータを読み込み
     * 
     * @param type 種類
     * @param scale 期間
     * @param min 期間の最小(自身を含む)
     * @param max 期間の最大(自身を含まない)
     * @return グラフデータ
     */
    private Map<ZonedDateTime, Double> load(TypeOption type, ScaleOption scale, ZonedDateTime min, ZonedDateTime max) {
        Map<ZonedDateTime, Double> map = new LinkedHashMap<>();
        // 空のデータを作る
        ZonedDateTime current = min;
        while (current.compareTo(max) < 0) {
            map.put(current, 0D);
            current = current.plus(scale.getTick());
        }
        // ログから読み込み
        Instant minInstant = min.toInstant();
        Instant maxInstant = max.toInstant();

        List<SimpleBattleLog> logs = BattleLogs.readSimpleLog(log -> {
            Instant a = log.getDate().toInstant();
            return a.compareTo(minInstant) >= 0 && a.compareTo(maxInstant) < 0;
        });
        map.putAll(logs.stream()
                .collect(Collectors.groupingBy(log -> scale.convert(log.getDate(), type),
                        Collectors.summingDouble(type::convert))));
        return map;
    }

    /**
     * 種類
     *
     */
    private enum TypeOption {
        SHIP_EXP("艦娘経験値") {
            @Override
            public double convert(SimpleBattleLog log) {
                String str = log.getShipExp();
                if (str == null || str.isEmpty())
                    return 0D;
                return Double.parseDouble(str);
            }
        },
        EXP("提督経験値") {
            @Override
            public double convert(SimpleBattleLog log) {
                String str = log.getExp();
                if (str == null || str.isEmpty())
                    return 0D;
                return Double.parseDouble(str);
            }
        },
        SENKA("戦果") {
            @Override
            public double convert(SimpleBattleLog log) {
                String str = log.getExp();
                if (str == null || str.isEmpty())
                    return 0D;
                return Double.parseDouble(str) / 1428.571D;
            }
        };

        private String name;

        private TypeOption(String name) {
            this.name = name;
        }

        public double convert(SimpleBattleLog log) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * スケールの選択肢
     *
     */
    private enum ScaleOption {
        /** 今日 */
        NOW_DAY("今日", "HH:mm", Duration.ofHours(1)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.HOURS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .plusDays(1);
            }
        },
        /** 昨日 */
        LAST_DAY("昨日", "HH:mm", Duration.ofHours(1)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.HOURS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .minusDays(1);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS);
            }
        },
        /** 今週 */
        NOW_WEEK("今週", "d日a", Duration.ofHours(12)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.HALF_DAYS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .minusDays(base.getDayOfWeek().getValue() - 1);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .plusWeeks(1)
                        .minusDays(base.getDayOfWeek().getValue() - 1);
            }
        },
        /** 先週 */
        LAST_WEEK("先週", "d日a", Duration.ofHours(12)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.HALF_DAYS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .minusWeeks(1)
                        .minusDays(base.getDayOfWeek().getValue() - 1);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .minusDays(base.getDayOfWeek().getValue() - 1);
            }
        },
        /** 今月 */
        NOW_MONTH("今月", "d日", Duration.ofDays(1)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.DAYS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .withDayOfMonth(1);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .withDayOfMonth(1)
                        .plusMonths(1);
            }
        },
        /** 先月 */
        LAST_MONTH("先月", "d日", Duration.ofDays(1)) {
            @Override
            public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
                return super.convert(time, type).truncatedTo(ChronoUnit.DAYS);
            }

            @Override
            public ZonedDateTime min(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .withDayOfMonth(1)
                        .minusMonths(1);
            }

            @Override
            public ZonedDateTime max(ZonedDateTime base) {
                return base.truncatedTo(ChronoUnit.DAYS)
                        .withDayOfMonth(1);
            }
        };

        private String name;
        private DateTimeFormatter format;
        private Duration tick;

        ScaleOption(String name, String format, Duration tick) {
            this.name = name;
            this.format = DateTimeFormatter.ofPattern(format);
            this.tick = tick;
        }

        public ZonedDateTime convert(ZonedDateTime time, TypeOption type) {
            // 戦果を選んだ場合日本時間午前2時が0時になるタイムゾーンを使用する
            if (type == TypeOption.SENKA) {
                return time.withZoneSameInstant(ZoneId.of("UTC+07:00"));
            } else {
                return time.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
            }
        }

        public ZonedDateTime min(ZonedDateTime base) {
            throw new UnsupportedOperationException();
        }

        public ZonedDateTime max(ZonedDateTime base) {
            throw new UnsupportedOperationException();
        }

        public DateTimeFormatter getFormat() {
            return this.format;
        }

        public Duration getTick() {
            return this.tick;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
