package logbook.internal.gui;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logbook.Messages;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.bean.SlotItem;
import logbook.bean.SlotItemCollection;
import logbook.bean.SlotitemMst;
import logbook.internal.Items;
import logbook.internal.Ships;

/**
 * 所有艦娘一覧のテーブル
 *
 */
public class ShipTablePane extends VBox {

    /** メッセージ */
    @FXML
    private Label message;

    /** テーブル */
    @FXML
    private TableView<ShipItem> table;

    /** ID */
    @FXML
    private TableColumn<ShipItem, Integer> id;

    /** 艦娘 */
    @FXML
    private TableColumn<ShipItem, Ship> ship;

    /** 艦種 */
    @FXML
    private TableColumn<ShipItem, String> type;

    /** Lv */
    @FXML
    private TableColumn<ShipItem, Integer> lv;

    /** cond */
    @FXML
    private TableColumn<ShipItem, Integer> cond;

    /** 海域 */
    @FXML
    private TableColumn<ShipItem, String> area;

    /** 制空 */
    @FXML
    private TableColumn<ShipItem, Integer> seiku;

    /** 砲戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> hPower;

    /** 雷戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> rPower;

    /** 夜戦火力 */
    @FXML
    private TableColumn<ShipItem, Integer> yPower;

    /** 対潜火力 */
    @FXML
    private TableColumn<ShipItem, Integer> tPower;

    /** 装備1 */
    @FXML
    private TableColumn<ShipItem, Integer> slot1;

    /** 装備2 */
    @FXML
    private TableColumn<ShipItem, Integer> slot2;

    /** 装備3 */
    @FXML
    private TableColumn<ShipItem, Integer> slot3;

    /** 装備4 */
    @FXML
    private TableColumn<ShipItem, Integer> slot4;

    /** 補強 */
    @FXML
    private TableColumn<ShipItem, Integer> slotEx;

    /** 艦娘達 */
    private final Supplier<List<Ship>> shipSupplier;

    /** 艦娘達 */
    private final ObservableList<ShipItem> shipItems = FXCollections.observableArrayList();

    /** 艦娘一覧のハッシュ・コード */
    private int shipsHashCode;

    /**
     * 所有艦娘一覧のテーブルのコンストラクタ
     *
     * @param port 艦隊
     */
    public ShipTablePane(DeckPort port) {
        this(() -> {
            Map<Integer, Ship> shipMap = ShipCollection.get()
                    .getShipMap();
            DeckPort newPort = DeckPortCollection.get()
                    .getDeckPortMap()
                    .get(port.getId());
            if (newPort != null) {
                return newPort.getShip().stream()
                        .map(shipMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        });
    }

    /**
     * 所有艦娘一覧のテーブルのコンストラクタ
     *
     * @param shipSupplier 艦娘達
     */
    public ShipTablePane(Supplier<List<Ship>> shipSupplier) {
        this.shipSupplier = shipSupplier;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/ship_table.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // カラムとオブジェクトのバインド
            this.id.setCellValueFactory(new PropertyValueFactory<>("id"));
            this.ship.setCellValueFactory(new PropertyValueFactory<>("ship"));
            this.ship.setCellFactory(p -> new ShipImageCell());
            this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.lv.setCellValueFactory(new PropertyValueFactory<>("lv"));
            this.cond.setCellValueFactory(new PropertyValueFactory<>("cond"));
            this.area.setCellValueFactory(new PropertyValueFactory<>("area"));
            this.seiku.setCellValueFactory(new PropertyValueFactory<>("seiku"));
            this.hPower.setCellValueFactory(new PropertyValueFactory<>("hPower"));
            this.rPower.setCellValueFactory(new PropertyValueFactory<>("rPower"));
            this.yPower.setCellValueFactory(new PropertyValueFactory<>("yPower"));
            this.tPower.setCellValueFactory(new PropertyValueFactory<>("tPower"));
            this.slot1.setCellValueFactory(new PropertyValueFactory<>("slot1"));
            this.slot1.setCellFactory(p -> new ItemImageCell());
            this.slot2.setCellValueFactory(new PropertyValueFactory<>("slot2"));
            this.slot2.setCellFactory(p -> new ItemImageCell());
            this.slot3.setCellValueFactory(new PropertyValueFactory<>("slot3"));
            this.slot3.setCellFactory(p -> new ItemImageCell());
            this.slot4.setCellValueFactory(new PropertyValueFactory<>("slot4"));
            this.slot4.setCellFactory(p -> new ItemImageCell());
            this.slotEx.setCellValueFactory(new PropertyValueFactory<>("slotEx"));
            this.slotEx.setCellFactory(p -> new ItemImageCell());

            this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.table.setItems(this.shipItems);
            this.table.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.update();

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新する
     *
     */
    public void update() {
        try {
            List<Ship> ships = this.shipSupplier.get();

            if (this.shipsHashCode != ships.hashCode()) {
                // ハッシュ・コードが変わっている場合画面の更新
                this.shipsHashCode = ships.hashCode();

                this.shipItems.clear();
                this.shipItems.addAll(ships.stream()
                        .map(ShipItem::toShipItem)
                        .collect(Collectors.toList()));
                this.message.setText("制空値計: " + ships.stream()
                        .mapToInt(Ships::airSuperiority)
                        .sum()
                        + " 索敵値(2-5式秋): " + MessageFormat.format("{0,number,#.##}", Ships.viewRange(ships))
                        + " 判定式(33): " + MessageFormat.format("{0,number,#.##}", Ships.decision33(ships)));
            }
        } catch (Exception e) {
            LoggerHolder.LOG.error("画面の更新に失敗しました", e);
        }
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
    private static class ShipImageCell extends TableCell<ShipItem, Ship> {
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
     * 装備画像のセル
     *
     */
    private static class ItemImageCell extends TableCell<ShipItem, Integer> {
        @Override
        protected void updateItem(Integer itemId, boolean empty) {
            super.updateItem(itemId, empty);

            if (!empty) {
                SlotItem item = SlotItemCollection.get()
                        .getSlotitemMap()
                        .get(itemId);
                Optional<SlotitemMst> mst = Items.slotitemMst(item);

                if (mst.isPresent()) {
                    StringBuilder text = new StringBuilder(mst.get().getName());

                    text.append(Optional.ofNullable(item.getAlv())
                            .map(alv -> Messages.getString("item.alv", alv)) //$NON-NLS-1$
                            .orElse(""));
                    text.append(Optional.ofNullable(item.getLevel())
                            .filter(lv -> lv > 0)
                            .map(lv -> Messages.getString("item.level", lv)) //$NON-NLS-1$
                            .orElse(""));
                    this.setGraphic(new ImageView(Items.itemImage(mst.get())));
                    this.setText(text.toString());
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

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(ShipTablePane.class);
    }
}
