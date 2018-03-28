package logbook.internal.gui;

import java.time.Duration;
import java.util.Comparator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;
import logbook.internal.Time;

/**
 * お風呂に入りたい艦娘のコントローラー
 *
 */
public class RequireNdockController extends WindowController {

    @FXML
    private TableView<RequireNdock> table;

    /** 行番号 */
    @FXML
    private TableColumn<RequireNdock, Integer> row;

    /** 艦隊 */
    @FXML
    private TableColumn<RequireNdock, Integer> deck;

    /** 艦娘 */
    @FXML
    private TableColumn<RequireNdock, Ship> ship;

    /** Lv */
    @FXML
    private TableColumn<RequireNdock, Integer> lv;

    /** 時間 */
    @FXML
    private TableColumn<RequireNdock, Duration> time;

    /** 今から */
    @FXML
    private TableColumn<RequireNdock, String> end;

    /** 燃料 */
    @FXML
    private TableColumn<RequireNdock, Integer> fuel;

    /** 鋼材 */
    @FXML
    private TableColumn<RequireNdock, Integer> metal;

    private ObservableList<RequireNdock> ndocks = FXCollections.observableArrayList();

    private Timeline timeline;

    @FXML
    void initialize() {
        TableTool.setVisible(this.table, this.getClass().toString() + "#" + "table");

        // カラムとオブジェクトのバインド
        this.row.setCellFactory(e -> {
            TableCell<RequireNdock, Integer> cell = new TableCell<RequireNdock, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!empty) {
                        TableRow<?> currentRow = this.getTableRow();
                        this.setText(Integer.toString(currentRow.getIndex() + 1));
                    } else {
                        this.setText(null);
                    }
                }
            };
            return cell;
        });
        this.deck.setCellValueFactory(new PropertyValueFactory<>("deck"));
        this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.ship.setCellFactory(p -> new ShipImageCell());
        this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.time.setCellFactory(p -> new TimeCell());
        this.end.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.fuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        this.metal.setCellValueFactory(new PropertyValueFactory<>("metal"));

        SortedList<RequireNdock> sortedList = new SortedList<>(this.ndocks);
        this.table.setItems(sortedList);
        sortedList.comparatorProperty().bind(this.table.comparatorProperty());
        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.table.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.getKeyFrames().add(new KeyFrame(
                javafx.util.Duration.seconds(1),
                this::update));
        this.timeline.play();

        this.update(null);
    }

    /**
     * 画面の更新
     *
     * @param e ActionEvent
     */
    void update(ActionEvent e) {
        this.ndocks.clear();
        ShipCollection.get()
                .getShipMap()
                .values()
                .stream()
                .filter(s -> s.getNdockTime() > 0)
                .sorted(Comparator.comparing(Ship::getNdockTime, Comparator.reverseOrder()))
                .map(RequireNdock::toRequireNdock)
                .forEach(this.ndocks::add);
    }

    /**
     * クリップボードにコピー
     */
    @FXML
    void copy() {
        TableTool.selectionCopy(this.table);
    }

    /**
     * すべてを選択
     */
    @FXML
    void selectAll() {
        TableTool.selectAll(this.table);
    }

    /**
     * テーブル列の表示・非表示の設定
     */
    @FXML
    void columnVisible() {
        try {
            TableTool.showVisibleSetting(this.table, this.getClass().toString() + "#" + "table",
                    this.getWindow());
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 時間のセル
     *
     */
    private static class TimeCell extends TableCell<RequireNdock, Duration> {
        @Override
        protected void updateItem(Duration time, boolean empty) {
            super.updateItem(time, empty);

            if (!empty && time != null) {
                this.setText(Time.toString(time, "修復完了"));
            } else {
                this.setText(null);
            }
        }
    }

    /**
     * 艦娘画像のセル
     *
     */
    private static class ShipImageCell extends TableCell<RequireNdock, Ship> {
        @Override
        protected void updateItem(Ship ship, boolean empty) {
            super.updateItem(ship, empty);

            if (!empty) {
                this.setGraphic(Tools.Conrtols.zoomImage(new ImageView(Ships.shipWithItemImage(ship))));
                this.setText(Ships.shipMst(ship)
                        .map(ShipMst::getName)
                        .orElse(""));
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        window.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> this.timeline.stop());
    }
}
