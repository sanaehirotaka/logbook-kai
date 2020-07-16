package logbook.internal.gui;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.controlsfx.control.ToggleSwitch;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.GridPane;
import logbook.bean.ParameterFilterConfig;
import logbook.internal.ComparableFilter;
import logbook.internal.LoggerHolder;
import logbook.internal.Operator;
import logbook.internal.Ships;
import logbook.internal.ToStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

abstract class ParameterFilterPane<T> extends GridPane {
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    static final class IntegerParameter<T> {
        private final String name;
        private final Function<T, Integer> mapper;
        private final Supplier<IntegerSpinnerValueFactory> valueFactory;
        private Supplier<IntStream> values;
        private Function<Integer, String> labelMapper;
    }

    /** パラメータフィルタのオンオフ */
    @FXML
    private ChoiceBox<IntegerParameter<T>> parameter;

    /** パラメータ */
    @FXML
    private ToggleSwitch parameterFilter;

    /** パラメータ値 */
    @FXML
    private Spinner<Integer> parameterValue;

    /** パラメータ値 */
    @FXML
    private ChoiceBox<Integer> parameterValueChoice;

    /** パラメータ条件 */
    @FXML
    private ChoiceBox<Operator> parameterType;

    /** フィルター */
    private final ObjectProperty<Predicate<T>> filter = new SimpleObjectProperty<>();

    /** パラメータのリスト */
    private final List<IntegerParameter<T>> items;

    protected ParameterFilterPane(List<IntegerParameter<T>> items) {
        this.items = items;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/param_filter_pane.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    public ReadOnlyObjectProperty<Predicate<T>> filterProperty() {
        return this.filter;
    }

    @FXML
    void initialize() {
        this.parameterType.setItems(FXCollections.observableArrayList(Operator.values()));
        this.parameterType.getSelectionModel().select(Operator.GE);
        this.parameter.setConverter(ToStringConverter.of(IntegerParameter::getName));
        this.parameter.setItems(FXCollections.observableArrayList(this.items));
        this.parameter.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            if (n != null) {
                if (n.getLabelMapper() != null) {
                    ObservableList<Integer> items = FXCollections.observableArrayList(n.getValues().get().mapToObj(Integer::valueOf).collect(Collectors.toList()));
                    this.parameterValueChoice.setItems(items);
                    this.parameterValueChoice.setConverter(ToStringConverter.of(n.getLabelMapper()));
                    this.parameterValueChoice.setVisible(true);
                    this.parameterValue.setVisible(false);
                    this.parameterValueChoice.getSelectionModel().select(0);
                } else {
                    String text = this.parameterValue.getEditor().getText();
                    this.parameterValue.setValueFactory(n.getValueFactory().get());
                    if (text != null) {
                        try {
                            Integer.parseInt(text);
                            this.parameterValue.getEditor().setText(text);  // ちゃんと数字だったときのみセットする
                        } catch (Throwable e) {
                            // ignore
                        }
                    }
                    this.parameterValueChoice.setVisible(false);
                    this.parameterValue.setVisible(true);
                }
            }
        });
        this.parameter.getSelectionModel().select(0);
        this.parameterFilter.selectedProperty().addListener((ob, ov, nv) -> {
            this.parameter.setDisable(!nv);
            this.parameterValue.setDisable(!nv);
            this.parameterType.setDisable(!nv);
        });
        this.parameterValue.setEditable(true);
        this.parameterFilter.selectedProperty().addListener(this::filterAction);
        this.parameter.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
        this.parameterValue.valueProperty().addListener(this::filterAction);
        this.parameterValue.getEditor().textProperty().addListener(this::filterAction);
        this.parameterValueChoice.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
        this.parameterType.getSelectionModel().selectedItemProperty().addListener(this::filterAction);
    }
    
    /**
     * フィルターを設定するアクション
     */
    private void filterAction(ObservableValue<?> observable, Object oldValue, Object newValue) {
        this.filter.set(this.createFilter());
    }

    /**
     * フィルターを作る
     * @return フィルター
     */
    private Predicate<T> createFilter() {
        if (this.parameterFilter.isSelected()) {
            Integer value = null;
            if (this.parameterValue.isVisible()) {
                try {
                    value = Integer.parseInt(this.parameterValue.getEditor().getText());
                } catch (Throwable e) {
                    value = this.parameterValue.getValue(); // just in case
                }
            } else {
                value = this.parameterValueChoice.getValue();
            }
            ComparableFilter.ComparableFilterBuilder<T, Integer> b = ComparableFilter.builder();
            return b.mapper(this.parameter.getValue().getMapper()).value(Optional.ofNullable(value).orElse(0)).type( this.parameterType.getValue()).build();
        }
        return null;
    }

    /**
     * 設定をロードする
     * @param parameterFilterConfig 設定
     */
    public void loadConfig(ParameterFilterConfig parameterFilterConfig) {
        this.parameterFilter.setSelected(parameterFilterConfig.isEnabled());
        Optional.ofNullable(parameterFilterConfig.getName())
            .flatMap(p -> this.items.stream().filter((ip) -> ip.getName().equals(p)).findAny())
            .ifPresent(this.parameter::setValue);
        if (this.parameterValue.getValueFactory() != null) {
            Optional.ofNullable(parameterFilterConfig.getValue()).ifPresent(this.parameterValue.getValueFactory()::setValue);
        }
        Optional.ofNullable(parameterFilterConfig.getValueChoice()).ifPresent(this.parameterValueChoice.getSelectionModel()::select);
    }
    
    /**
     * 設定を保存するためのオブジェクトを作成
     * @return 設定保存用のオブジェクト
     */
    public ParameterFilterConfig saveConfig() {
        ParameterFilterConfig config = new ParameterFilterConfig();
        config.setEnabled(this.parameterFilter.isSelected());
        Optional.ofNullable(this.parameter.getValue()).map(IntegerParameter::getName).ifPresent(config::setName);
        Integer value = null;
        try {
            value = Integer.parseInt(this.parameterValue.getEditor().getText());
        } catch (Throwable e) {
            value = this.parameterValue.getValue(); // just in case
        }
        Optional.ofNullable(value).ifPresent(config::setValue);
        Optional.ofNullable(this.parameterValueChoice.getValue()).ifPresent(config::setValueChoice);
        return config;
    }

    /**
     * 艦娘のパラメータ用のペイン
     */
    static class ShipItemParameterFilterPane extends ParameterFilterPane<ShipItem> {
        
        private static final List<IntegerParameter<ShipItem>> SHIPITEM_PARAMETERS;
        
        static {
            SHIPITEM_PARAMETERS = Stream.of(
                    new IntegerParameter<ShipItem>("cond", ShipItem::getCond, () -> new IntegerSpinnerValueFactory(0, 100, 53)),
                    new IntegerParameter<ShipItem>("Lv", ShipItem::getLv, () -> new IntegerSpinnerValueFactory(1, 175, 99)),
                    new IntegerParameter<ShipItem>("ID", ShipItem::getId, () -> new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("exp", ShipItem::getExp, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("next", ShipItem::getNext, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("制空", ShipItem::getSeiku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("砲戦火力", ShipItem::getHPower, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("雷戦火力", ShipItem::getRPower, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("夜戦火力", ShipItem::getYPower, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("対潜火力", ShipItem::getTPower, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("火力(素)", ShipItem::getKaryoku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("雷装(素)", ShipItem::getRaisou, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("対空(素)", ShipItem::getTaiku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("対潜(素)", ShipItem::getTais, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("索敵(素)", ShipItem::getSakuteki, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("運(素)", ShipItem::getLucky, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("耐久", ShipItem::getMaxhp, () -> new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("装甲(素)", ShipItem::getSoukou, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("回避(素)", ShipItem::getKaihi, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE)),
                    new IntegerParameter<ShipItem>("速力", ShipItem::getSoku, null, () -> IntStream.of(5, 10, 15, 20), Ships::sokuText),
                    new IntegerParameter<ShipItem>("射程", ShipItem::getLeng, null, () -> IntStream.range(1, 5), Ships::lengText)
            ).collect(Collectors.toList());
        }

        ShipItemParameterFilterPane() {
            super(SHIPITEM_PARAMETERS);
        }
    }

    /**
     * 装備一覧のパラメータ用のペイン
     */
    static class ItemParameterFilterPane extends ParameterFilterPane<Item> {
        
        private static final List<IntegerParameter<Item>> ITEM_PARAMETERS;
        
        static {
            ITEM_PARAMETERS = Stream.of(
                    new IntegerParameter<Item>("所持", Item::getCount, () -> new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<Item>("火力", Item::getHoug, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("命中", Item::getHoum, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("射程", Item::getLeng, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("運", Item::getLuck, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("回避", Item::getHouk, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("爆装", Item::getBaku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("雷装", Item::getRaig, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("索敵", Item::getSaku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("対潜", Item::getTais, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("対空", Item::getTyku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0)),
                    new IntegerParameter<Item>("装甲", Item::getSouk, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0))
            ).collect(Collectors.toList());
        }

        ItemParameterFilterPane() {
            super(ITEM_PARAMETERS);
        }
    }

    /**
     * 基地航空隊のパラメータ用のペイン
     */
    static class AirBaseParameterFilterPane extends ParameterFilterPane<AirBaseItem> {
        
        private static final List<IntegerParameter<AirBaseItem>> AIRBASE_PARAMETERS;
        
        static {
            AIRBASE_PARAMETERS = Stream.of(
                    new IntegerParameter<AirBaseItem>("熟練", AirBaseItem::getAlv, () -> new IntegerSpinnerValueFactory(0, 7, 0)),
                    new IntegerParameter<AirBaseItem>("改修", AirBaseItem::getLevel, () -> new IntegerSpinnerValueFactory(0, 10, 0)),
                    new IntegerParameter<AirBaseItem>("所持", AirBaseItem::getCount, () -> new IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("制空(出撃)", AirBaseItem::getSeiku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 103)),
                    new IntegerParameter<AirBaseItem>("制空(防空)", AirBaseItem::getInterceptSeiku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 101)),
                    new IntegerParameter<AirBaseItem>("半径(素)", AirBaseItem::getDistance, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 7)),
                    new IntegerParameter<AirBaseItem>("半径(+大艇)", AirBaseItem::getDistanceTaiteichan, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 9)),
                    new IntegerParameter<AirBaseItem>("半径(+Cata)", AirBaseItem::getDistanceCatalina, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 10)),
                    new IntegerParameter<AirBaseItem>("配置コスト", AirBaseItem::getCost, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 100)),
                    new IntegerParameter<AirBaseItem>("対空", AirBaseItem::getTyku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 5)),
                    new IntegerParameter<AirBaseItem>("対爆", AirBaseItem::getHoum, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("迎撃", AirBaseItem::getHouk, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("雷装", AirBaseItem::getRaig, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("爆装", AirBaseItem::getBaku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("対潜", AirBaseItem::getTais, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1)),
                    new IntegerParameter<AirBaseItem>("索敵", AirBaseItem::getSaku, () -> new IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1))
            ).collect(Collectors.toList());
        }

        AirBaseParameterFilterPane() {
            super(AIRBASE_PARAMETERS);
        }
    }
}