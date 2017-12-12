package logbook.internal.gui;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.Ships;

public class RequireNdockController extends WindowController {

    @FXML
    private TableView<RequireNdock> table;

    /** ID */
    @FXML
    private TableColumn<RequireNdock, Integer> id;

    /** 艦娘 */
    @FXML
    private TableColumn<RequireNdock, Ship> ship;

    /** Lv */
    @FXML
    private TableColumn<RequireNdock, Integer> lv;

    /** 時間 */
    @FXML
    private TableColumn<RequireNdock, String> time;

    /** 今から */
    @FXML
    private TableColumn<RequireNdock, String> end;

    /** 燃料 */
    @FXML
    private TableColumn<RequireNdock, Integer> fuel;

    /** 鋼材 */
    @FXML
    private TableColumn<RequireNdock, Integer> metal;

    ObservableList<RequireNdock> ndocks = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // カラムとオブジェクトのバインド
        this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
        this.ship.setCellFactory(p -> new ShipImageCell());
        this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
        this.time.setCellValueFactory(new PropertyValueFactory<>("time"));
        this.end.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.fuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
        this.metal.setCellValueFactory(new PropertyValueFactory<>("metal"));

        this.table.setItems(this.ndocks);
        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.table.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);

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
     * 艦娘画像のセル
     *
     */
    private static class ShipImageCell extends TableCell<RequireNdock, Ship> {
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
}
