package logbook.internal.gui;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.Stype;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;

/**
 * 統計
 *
 */
public class StatisticsPane extends VBox {

    @FXML
    private VBox content;

    /** 総計 */
    @FXML
    private Text total;

    /** 比率 */
    @FXML
    private PieChart ratio;

    /** 平均レベル */
    @FXML
    private StackedBarChart<String, Double> average;

    /** 平均レベル カテゴリ軸 */
    @FXML
    private CategoryAxis averageCategory;

    /** レベル分布 */
    @FXML
    private StackedBarChart<String, Long> spectrum;

    /** レベル分布 カテゴリ軸 */
    @FXML
    private CategoryAxis spectrumCategory;

    public StatisticsPane() {
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/statistics.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // 対象艦抽出
            List<Ship> ships = ShipCollection.get()
                    .getShipMap()
                    .values()
                    .stream()
                    // ロックしている艦
                    .filter(Ship::getLocked)
                    .collect(Collectors.toList());
            // 総計
            this.setTotal(ships);
            // 経験値比率
            this.setRatio(ships);
            // 平均レベル
            this.setAverage(ships);
            // レベル分布
            this.setSpectrum(ships);

        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画像ファイルに保存
     * @param event ActionEvent
     */
    @FXML
    void storeImageAction(ActionEvent event) {
        Tools.Conrtols.storeSnapshot(this.content, "統計情報", this.content.getScene().getWindow());
    }

    /**
     * 総計
     * @param ships 対象艦
     */
    private void setTotal(List<Ship> ships) {
        this.total.setText(this.suffixDecimal(ships.stream()
                .mapToLong(this::getExp)
                .sum()));
    }

    /**
     * 経験値比率
     * @param ships 対象艦
     */
    private void setRatio(List<Ship> ships) {
        Map<TypeGroup, Long> collect = ships.stream()
                .collect(Collectors.groupingBy(TypeGroup::toTypeGroup, Collectors.summingLong(this::getExp)));

        ObservableList<PieChart.Data> value = FXCollections.observableArrayList();
        for (Entry<TypeGroup, Long> data : collect.entrySet()) {
            if (data.getKey() != null)
                value.add(new PieChart.Data(data.getKey().name(), data.getValue()));
        }
        Collections.sort(value, Comparator.comparing(PieChart.Data::getPieValue).reversed());
        this.ratio.setData(value);
    }

    /**
     * 平均レベル
     * @param ships 対象艦
     */
    private void setAverage(List<Ship> ships) {
        ObservableList<XYChart.Data<String, Double>> data = ships.stream()
                .collect(Collectors.groupingBy(TypeGroup::toTypeGroup, Collectors.averagingLong(Ship::getLv)))
                .entrySet().stream()
                .filter(e -> e.getKey() != null)
                .map(e -> new XYChart.Data<>(e.getKey().name(), e.getValue()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        this.averageCategory.setCategories(
                Arrays.stream(TypeGroup.values())
                        .map(TypeGroup::name)
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.getData().addAll(data);
        this.average.getData().add(series);
    }

    /**
     * レベル分布
     * @param ships 対象艦
     */
    private void setSpectrum(List<Ship> ships) {
        Map<TypeGroup, Map<Integer, Long>> value = ships.stream()
                .collect(Collectors.groupingBy(TypeGroup::toTypeGroup, Collectors.mapping(this::tickLevel,
                        Collectors.groupingBy(Function.identity(), Collectors.counting()))));
        this.spectrumCategory.setCategories(
                IntStream.rangeClosed(1, 165)
                        .map(i -> i / 10 * 10)
                        .distinct()
                        .mapToObj(Integer::valueOf)
                        .sorted(Comparator.reverseOrder())
                        .map(i -> i + "-" + (i + 9))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        for (TypeGroup group : TypeGroup.values()) {
            Map<Integer, Long> tick = value.get(group);
            if (tick != null) {

                ObservableList<XYChart.Data<String, Long>> data = tick.entrySet()
                        .stream()
                        .map(e -> new XYChart.Data<>(e.getKey() + "-" + (e.getKey() + 9), e.getValue()))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                XYChart.Series<String, Long> series = new XYChart.Series<>();
                series.setName(group.name());
                series.getData().addAll(data);
                this.spectrum.getData().add(series);
            }
        }
    }

    /**
     * 接尾辞付きの数値に変換する
     * @param v 数値
     * @return 接尾辞付きの数値 (100Mなど)
     */
    private String suffixDecimal(long v) {
        BigDecimal d;
        String prefix;
        int scale;
        if (1000_000_000L <= v) {
            d = BigDecimal.valueOf(1000_000_000L);
            prefix = "G";
            scale = 3;
        } else if (1000_000L <= v) {
            d = BigDecimal.valueOf(1000_000L);
            prefix = "M";
            scale = 2;
        } else {
            d = BigDecimal.valueOf(1000L);
            prefix = "K";
            scale = 1;
        }
        return BigDecimal.valueOf(v)
                .divide(d, scale, RoundingMode.HALF_EVEN)
                .toPlainString() + " " + prefix;
    }

    private long getExp(Ship ship) {
        return ship.getExp().get(0);
    }

    private int tickLevel(Ship ship) {
        return ship.getLv() / 10 * 10;
    }

    /**
     * 艦種のグループわけ
     */
    private enum TypeGroup {

        駆逐艦("駆逐艦"),
        海防艦("海防艦"),
        巡洋艦("軽巡洋艦", "重雷装巡洋艦", "重巡洋艦", "航空巡洋艦", "練習巡洋艦"),
        空母("軽空母", "正規空母", "装甲空母"),
        戦艦("戦艦", "航空戦艦"),
        潜水艦("潜水艦", "潜水空母"),
        特殊艦("水上機母艦", "揚陸艦", "工作艦", "潜水母艦", "補給艦");

        private String[] group;

        private TypeGroup(String... shipTypes) {
            this.group = shipTypes;
        }

        public static TypeGroup toTypeGroup(Ship ship) {
            Stype stype = Ships.stype(ship).orElse(null);
            if (stype != null) {
                String name = stype.getName();
                for (TypeGroup group : values()) {
                    for (String v : group.group) {
                        if (v.equals(name))
                            return group;
                    }
                }
            }
            return null;
        }
    }
}
