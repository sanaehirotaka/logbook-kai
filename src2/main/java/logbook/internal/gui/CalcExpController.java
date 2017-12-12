package logbook.internal.gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.Messages;
import logbook.bean.AppConfig;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.ExpTable;
import logbook.internal.Rank;
import logbook.internal.SeaArea;
import logbook.internal.Ships;

public class CalcExpController extends WindowController {

    @FXML
    private ComboBox<ShipWrapper> shipList;

    @FXML
    private Spinner<Integer> nowLv;

    @FXML
    private TextField nowExp;

    @FXML
    private Spinner<Integer> goalLv;

    @FXML
    private TextField goalExp;

    @FXML
    private ChoiceBox<SeaArea> sea;

    @FXML
    private ChoiceBox<Rank> rank;

    @FXML
    private CheckBox flagShip;

    @FXML
    private CheckBox mvp;

    @FXML
    private TextField getExp;

    @FXML
    private TextField needExp;

    @FXML
    private TextField battleCount;

    /** チャート */
    @FXML
    private LineChart<Number, Number> expChart;

    /** チャートx軸 */
    @FXML
    private NumberAxis xAxis;

    /** チャートy軸 */
    @FXML
    private NumberAxis yAxis;

    /** 改装レベル不足の艦娘 */
    @FXML
    private TableView<ShortageShipItem> shortageShip;

    @FXML
    private TableColumn<ShortageShipItem, Integer> id;

    @FXML
    private TableColumn<ShortageShipItem, Ship> ship;

    @FXML
    private TableColumn<ShortageShipItem, Integer> lv;

    @FXML
    private TableColumn<ShortageShipItem, Integer> afterLv;

    /** 艦娘のコンボボックスに表示する */
    private ObservableList<ShipWrapper> ships = FXCollections.observableArrayList();

    /** 改装レベル不足の艦娘 */
    private ObservableList<ShortageShipItem> item = FXCollections.observableArrayList();

    /** 今の経験値 */
    private int nowExpValue;

    /** 目標経験値 */
    private int goalExpValue;

    @FXML
    void initialize() {
        // Spinnerに最小値最大値現在値を設定
        this.nowLv.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 155, 1, 1));
        this.goalLv.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 155, 100, 1));
        // コンボボックス
        this.shipList.setItems(this.ships);
        this.shipList();

        // 海域
        this.sea.setItems(FXCollections.observableArrayList(
                Arrays.stream(SeaArea.values())
                        .filter(s -> s.getSeaExp() > 0)
                        .collect(Collectors.toList())));
        this.sea.getSelectionModel().select(AppConfig.get().getBattleSeaArea());

        // 評価
        this.rank.setItems(FXCollections.observableArrayList(Rank.values()));
        this.rank.getSelectionModel().select(AppConfig.get().getResultRank());

        // カラムとオブジェクトのバインド
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.ship.setCellFactory(p -> new ShipImageTableCell());
        this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
        this.afterLv.setCellValueFactory(new PropertyValueFactory<>("afterLv"));

        // 改装レベル不足の艦娘
        this.shortageShip.setItems(this.item);
        this.shortageShip();

        // イベントリスナー
        this.shipList.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::changeShip);
        this.nowLv.getValueFactory()
                .valueProperty()
                .addListener(this::changeNowLv);
        this.goalLv.getValueFactory()
                .valueProperty()
                .addListener(this::changeGoalLv);
        this.sea.getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, o, n) -> this.update());
        this.rank.getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, o, n) -> this.update());

        // 旗艦ID
        Integer flagShipId = DeckPortCollection.get()
                .getDeckPortMap()
                .get(1)
                .getShip()
                .get(0);
        // 旗艦
        ShipWrapper flagShip = this.ships.stream()
                .filter(w -> w.getShip().getId().equals(flagShipId))
                .findAny()
                .get();
        // 発火させるためにここでselect
        this.shipList.getSelectionModel().select(flagShip);
    }

    /**
     * 更新
     *
     * @param event ActionEvent
     */
    @FXML
    void reloadAction(ActionEvent event) {
        // 選択している艦娘のID
        Integer selectId = this.shipList.getValue().getShip().getId();
        // コンボボックスを入れ替え
        this.shipList();
        // 選択する艦娘(更新時に艦娘がいなくなっている可能性を考慮)
        ShipWrapper select = this.ships.stream()
                .filter(w -> w.getShip().getId().equals(selectId))
                .findAny()
                .orElse(this.ships.get(0));
        this.shipList.getSelectionModel().select(select);
        this.update();
    }

    /**
     * 計算
     *
     * @param event ActionEvent
     */
    @FXML
    void update(ActionEvent event) {
        this.update();
    }

    /**
     * 艦娘を変えたとき
     */
    private void changeShip(ObservableValue<? extends ShipWrapper> observable, ShipWrapper oldValue,
            ShipWrapper value) {
        if (value != null) {
            Ship ship = value.getShip();
            this.nowLv.getValueFactory().setValue(ship.getLv());

            this.nowExpValue = ship.getExp().get(0);
            this.nowExp.setText(Integer.toString(this.nowExpValue));

            int afterLv = Ships.shipMst(ship)
                    .map(ShipMst::getAfterlv)
                    .orElse(0);
            int goal = Math.min(Math.max(afterLv, ship.getLv() + 1), ExpTable.maxLv());

            this.goalExpValue = ExpTable.get().get(goal);
            this.goalLv.getValueFactory().setValue(goal);
            this.goalExp.setText(String.valueOf(this.goalExpValue));
        }
    }

    /**
     * レベルを変えた時
     */
    private void changeNowLv(ObservableValue<? extends Integer> observable, Integer oldValue, Integer value) {
        if (value != null) {
            this.nowExpValue = ExpTable.get().get(value);
            this.nowExp.setText(String.valueOf(this.nowExpValue));
            this.update();
        }
    }

    /**
     * レベルを変えた時
     */
    private void changeGoalLv(ObservableValue<? extends Integer> observable, Integer oldValue, Integer value) {
        if (value != null) {
            this.goalExpValue = ExpTable.get().get(value);
            this.goalExp.setText(String.valueOf(this.goalExpValue));
            this.update();
        }
    }

    /**
     * 計算する
     */
    private void update() {
        // 海域経験値
        int base = this.sea.getValue().getSeaExp();
        // 評価
        double eval = this.rank.getValue().getRatio();
        // 1回あたり
        int getExpValue = getExp(base, eval, this.flagShip.isSelected(), this.mvp.isSelected());
        // 戦闘回数
        int battleCountValue = getCount(this.goalExpValue - this.nowExpValue, getExpValue);

        this.getExp.setText(String.valueOf(getExpValue));
        this.needExp.setText(String.valueOf(this.goalExpValue - this.nowExpValue));
        this.battleCount.setText(String.valueOf(battleCountValue));

        this.chart();

        AppConfig.get().setBattleSeaArea(this.sea.getValue());
        AppConfig.get().setResultRank(this.rank.getValue());
    }

    /**
     * 艦娘のコンボボックスを作る
     */
    private void shipList() {
        this.ships.clear();
        this.ships.addAll(ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .sorted(Comparator.comparing(Ship::getLv).reversed())
                .map(ShipWrapper::new)
                .collect(Collectors.toList()));

    }

    /**
     * 改装レベル不足の艦娘の一覧を作る
     */
    private void shortageShip() {
        this.item.addAll(ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .map(ShortageShipItem::toShipItem)
                .filter(item -> item.getAfterLv() > item.getLv())
                .sorted(Comparator.comparing(ShortageShipItem::getLv).reversed())
                .collect(Collectors.toList()));
    }

    /**
     * チャートを作る
     */
    private void chart() {
        int nowLvValue = this.nowLv.getValue();
        int goalLvValue = this.goalLv.getValue();

        XYChart.Series<Number, Number> total = new XYChart.Series<>();
        XYChart.Series<Number, Number> goal = new XYChart.Series<>();
        XYChart.Series<Number, Number> now = new XYChart.Series<>();

        if (goalLvValue <= 100) {
            total.getData().addAll(ExpTable.get().entrySet()
                    .stream()
                    .filter(e -> e.getKey() < 100)
                    .map(e -> new XYChart.Data<Number, Number>(e.getKey(), e.getValue()))
                    .collect(Collectors.toList()));
        } else {
            total.getData().addAll(ExpTable.get().entrySet()
                    .stream()
                    .map(e -> new XYChart.Data<Number, Number>(e.getKey(), e.getValue()))
                    .collect(Collectors.toList()));
        }
        goal.getData().addAll(ExpTable.get().entrySet()
                .stream()
                .filter(e -> e.getKey() <= goalLvValue)
                .map(e -> new XYChart.Data<Number, Number>(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));
        now.getData().addAll(ExpTable.get().entrySet()
                .stream()
                .filter(e -> e.getKey() <= nowLvValue)
                .map(e -> new XYChart.Data<Number, Number>(e.getKey(), e.getValue()))
                .collect(Collectors.toList()));

        this.expChart.getData().clear();
        this.expChart.getData().addAll(Arrays.asList(total, goal, now));
    }

    /**
     * 戦闘で得られる経験値を計算します
     *
     * @param baseexp 海域Exp
     * @param eval 評価倍率
     * @param isFlagship 旗艦
     * @param isMvp MVP
     * @return 得られる経験値
     */
    private static int getExp(int baseexp, double eval, boolean isFlagship, boolean isMvp) {
        double getexpd = baseexp * eval;
        if (isFlagship) {
            getexpd *= 1.5;
        }
        if (isMvp) {
            getexpd *= 2;
        }
        return (int) Math.round(getexpd);
    }

    /**
     * 必要経験値を1回あたりの経験値で割った数値を計算します。端数は切り上げされます
     *
     * @param needexp 必要経験値
     * @param exp 1回あたりの経験値
     * @return
     */
    private static int getCount(int needexp, int exp) {
        return BigDecimal.valueOf(needexp).divide(BigDecimal.valueOf(exp), RoundingMode.CEILING)
                .intValue();
    }

    /**
     * 改装レベル不足の艦娘の一覧に表示する艦娘画像のセル
     *
     */
    private static class ShipImageTableCell extends TableCell<ShortageShipItem, Ship> {
        @Override
        protected void updateItem(Ship ship, boolean empty) {
            super.updateItem(ship, empty);

            if (!empty) {
                this.setGraphic(new ImageView(Ships.shipWithItemImage(ship)));
                this.setText(Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse(""));
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    /**
     * 艦娘のラッパー(toStringを実装)
     *
     */
    private static class ShipWrapper {

        /** 艦娘 */
        private Ship ship;

        public ShipWrapper(Ship ship) {
            this.ship = ship;
        }

        /**
         * 艦娘を取得します。
         * @return 艦娘
         */
        public Ship getShip() {
            return this.ship;
        }

        @Override
        public String toString() {
            return Messages.getString("ship.name", Ships.shipMst(this.ship)
                    .map(ShipMst::getName)
                    .orElse(""), this.ship.getLv());
        }
    }
}
