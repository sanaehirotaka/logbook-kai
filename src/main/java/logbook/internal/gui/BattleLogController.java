package logbook.internal.gui;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import logbook.bean.BattleLog;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.Ship;
import logbook.internal.BattleLogs;
import logbook.internal.BattleLogs.SimpleBattleLog;
import logbook.internal.BattleLogs.Unit;

/**
 * 戦闘ログのUIコントローラー
 *
 */
public class BattleLogController extends WindowController {

    /** 統計 */
    @FXML
    private TreeTableView<BattleLogCollect> collect;

    /** 集計 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> unit;

    /** 出撃  */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> start;

    /** 勝利  */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> win;

    /** S勝利 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> s;

    /** A勝利 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> a;

    /** B勝利 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> b;

    /** C敗北 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> c;

    /** D敗北 */
    @FXML
    private TreeTableColumn<BattleLogCollect, String> d;

    /** 詳細 */
    @FXML
    private TableView<BattleLogDetail> detail;

    /** 日付 */
    @FXML
    private TableColumn<BattleLogDetail, String> date;

    /** 海域 */
    @FXML
    private TableColumn<BattleLogDetail, String> area;

    /** マス */
    @FXML
    private TableColumn<BattleLogDetail, String> cell;

    /** ボス */
    @FXML
    private TableColumn<BattleLogDetail, String> boss;

    /** 評価 */
    @FXML
    private TableColumn<BattleLogDetail, String> rank;

    /** 艦隊行動 */
    @FXML
    private TableColumn<BattleLogDetail, String> intercept;

    /** 味方陣形 */
    @FXML
    private TableColumn<BattleLogDetail, String> fformation;

    /** 敵陣形 */
    @FXML
    private TableColumn<BattleLogDetail, String> eformation;

    /** 制空権 */
    @FXML
    private TableColumn<BattleLogDetail, String> dispseiku;

    /** 味方触接 */
    @FXML
    private TableColumn<BattleLogDetail, String> ftouch;

    /** 敵触接 */
    @FXML
    private TableColumn<BattleLogDetail, String> etouch;

    /** 敵艦隊 */
    @FXML
    private TableColumn<BattleLogDetail, String> efleet;

    /** ドロップ艦種 */
    @FXML
    private TableColumn<BattleLogDetail, String> dropType;

    /** ドロップ艦娘 */
    @FXML
    private TableColumn<BattleLogDetail, String> dropShip;

    /** 戦闘ログ */
    private Map<Unit, List<SimpleBattleLog>> logMap;

    /** 詳細 */
    private ObservableList<BattleLogDetail> details = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        try {
            // 統計
            this.collect.setShowRoot(false);
            this.collect.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            this.unit.setCellValueFactory(new TreeItemPropertyValueFactory<>("unit"));
            this.start.setCellValueFactory(new TreeItemPropertyValueFactory<>("start"));
            this.win.setCellValueFactory(new TreeItemPropertyValueFactory<>("win"));
            this.s.setCellValueFactory(new TreeItemPropertyValueFactory<>("s"));
            this.a.setCellValueFactory(new TreeItemPropertyValueFactory<>("a"));
            this.b.setCellValueFactory(new TreeItemPropertyValueFactory<>("b"));
            this.c.setCellValueFactory(new TreeItemPropertyValueFactory<>("c"));
            this.d.setCellValueFactory(new TreeItemPropertyValueFactory<>("d"));

            // 選択された時のリスナーを設定
            this.collect.getSelectionModel()
                    .selectedItemProperty()
                    .addListener(this::detail);

            // 詳細
            this.detail.setRowFactory(tv -> {
                TableRow<BattleLogDetail> r = new TableRow<>();
                r.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2 && (!r.isEmpty())) {
                        BattleLogDetail d = r.getItem();
                        BattleLog log = BattleLogs.read(d.getDate());
                        if (log != null) {
                            try {
                                CombinedType combinedType = log.getCombinedType();
                                Map<Integer, List<Ship>> deckMap = log.getDeckMap();
                                IFormation battle = log.getBattle();
                                IMidnightBattle midnight = log.getMidnight();

                                InternalFXMLLoader.showWindow("logbook/gui/battle_detail.fxml", this.getWindow(),
                                        "戦闘ログ", c -> {
                                    ((BattleDetail) c).setData(combinedType, deckMap, battle, midnight);
                                } , null);
                            } catch (Exception ex) {
                                LoggerHolder.LOG.error("詳細の表示に失敗しました", ex);
                            }
                        }
                    }
                });
                return r;
            });
            this.detail.setItems(this.details);
            this.detail.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.detail.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

            this.date.setCellValueFactory(new PropertyValueFactory<>("date"));
            this.area.setCellValueFactory(new PropertyValueFactory<>("area"));
            this.cell.setCellValueFactory(new PropertyValueFactory<>("cell"));
            this.boss.setCellValueFactory(new PropertyValueFactory<>("boss"));
            this.rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
            this.intercept.setCellValueFactory(new PropertyValueFactory<>("intercept"));
            this.fformation.setCellValueFactory(new PropertyValueFactory<>("fformation"));
            this.eformation.setCellValueFactory(new PropertyValueFactory<>("eformation"));
            this.dispseiku.setCellValueFactory(new PropertyValueFactory<>("dispseiku"));
            this.ftouch.setCellValueFactory(new PropertyValueFactory<>("ftouch"));
            this.etouch.setCellValueFactory(new PropertyValueFactory<>("etouch"));
            this.efleet.setCellValueFactory(new PropertyValueFactory<>("efleet"));
            this.dropType.setCellValueFactory(new PropertyValueFactory<>("dropType"));
            this.dropShip.setCellValueFactory(new PropertyValueFactory<>("dropShip"));

            // 統計
            // ルート要素(非表示)
            TreeItem<BattleLogCollect> root = new TreeItem<BattleLogCollect>(new BattleLogCollect());
            this.collect.setRoot(root);

            // 集計単位がキーのマップ
            this.logMap = BattleLogs.readSimpleLog();
            for (Unit unit : Unit.values()) {
                List<SimpleBattleLog> list = this.logMap.get(unit);

                // 単位のルート
                BattleLogCollect unitRootValue = BattleLogs.collect(list, null, false);
                unitRootValue.setUnit(unit.getName());
                unitRootValue.setCollectUnit(unit);

                TreeItem<BattleLogCollect> unitRoot = new TreeItem<BattleLogCollect>(unitRootValue);
                unitRoot.setExpanded(true);

                // ボス
                BattleLogCollect bossValue = BattleLogs.collect(list, null, true);
                bossValue.setUnit("ボス");
                bossValue.setCollectUnit(unit);
                bossValue.setBoss(true);

                TreeItem<BattleLogCollect> boss = new TreeItem<BattleLogCollect>(bossValue);
                unitRoot.getChildren().add(boss);

                // 海域の名前
                List<String> areaNames = list.stream()
                        .map(SimpleBattleLog::getArea)
                        .distinct()
                        .sorted(Comparator.naturalOrder())
                        .collect(Collectors.toList());
                for (String area : areaNames) {
                    // 海域毎の集計
                    BattleLogCollect areaValue = BattleLogs.collect(list, area, false);
                    areaValue.setUnit(area);
                    areaValue.setCollectUnit(unit);
                    areaValue.setArea(area);

                    TreeItem<BattleLogCollect> areaRoot = new TreeItem<BattleLogCollect>(areaValue);

                    // 海域ボス
                    BattleLogCollect areaBossValue = BattleLogs.collect(list, area, true);
                    areaBossValue.setUnit("ボス");
                    areaBossValue.setCollectUnit(unit);
                    areaBossValue.setArea(area);
                    areaBossValue.setBoss(true);

                    TreeItem<BattleLogCollect> areaBoss = new TreeItem<BattleLogCollect>(areaBossValue);
                    areaRoot.getChildren().add(areaBoss);

                    unitRoot.getChildren().add(areaRoot);
                }

                root.getChildren().add(unitRoot);

            }

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * クリップボードにコピー
     */
    @FXML
    void copyDetail() {
        TableTool.selectionCopy(this.detail);
    }

    /**
     * すべてを選択
     */
    @FXML
    void selectAllDetail() {
        TableTool.selectAll(this.detail);
    }

    /**
     * 右ペインに詳細表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void detail(ObservableValue<? extends TreeItem<BattleLogCollect>> observable,
            TreeItem<BattleLogCollect> oldValue, TreeItem<BattleLogCollect> value) {
        this.details.clear();
        if (value != null) {
            BattleLogCollect collect = value.getValue();
            String area = collect.getArea();
            boolean boss = collect.isBoss();

            Predicate<BattleLogDetail> anyFilter = e -> true;
            // 海域フィルタ
            Predicate<BattleLogDetail> areaFilter = area != null ? e -> area.equals(e.getArea()) : anyFilter;
            // ボスフィルタ
            Predicate<BattleLogDetail> bossFilter = boss ? e -> e.getBoss().indexOf("ボス") != -1 : anyFilter;

            this.details.addAll(this.logMap.get(collect.getCollectUnit())
                    .stream()
                    .map(BattleLogDetail::toBattleLogDetail)
                    .filter(areaFilter)
                    .filter(bossFilter)
                    .sorted(Comparator.comparing(BattleLogDetail::getDate).reversed())
                    .collect(Collectors.toList()));
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BattleLogController.class);
    }
}
