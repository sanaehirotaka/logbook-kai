package logbook.internal.gui;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import logbook.bean.DeckPort;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.Ships;

/**
 * 艦隊タブ
 *
 */
public class FleetTabPane extends ScrollPane {

    /** 警告CSSクラス名 */
    private static final String ALERT = "alert";

    /** 注意CSSクラス名 */
    private static final String WARN = "warn";

    /** 艦隊 */
    private DeckPort port;

    /** 艦娘達 */
    private List<Ship> shipList;

    /** 艦隊のハッシュ・コード */
    private int portHashCode;

    /** 艦娘達のハッシュ・コード */
    private int shipsHashCode;

    /** Tabのクラス名(タブ色を変えるのに使用) */
    private String tabCssClass;

    /** メッセージ */
    @FXML
    private Label message;

    /** 艦娘達 */
    @FXML
    private VBox ships;

    /** 色々な情報 */
    @FXML
    private Label info;

    /**
     * 艦隊ペインのコンストラクタ
     *
     * @param port 艦隊
     */
    public FleetTabPane(DeckPort port) {
        this.port = port;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/fleet_tab.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.update();
    }

    /**
     * 画面を更新します
     *
     * @param port 艦隊
     */
    public void update(DeckPort port) {
        this.port = port;
        this.update();
    }

    /**
     * 画面を更新します
     */
    public void update() {
        Map<Integer, Ship> shipMap = ShipCollection.get()
                .getShipMap();
        this.shipList = this.port.getShip()
                .stream()
                .map(shipMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (this.portHashCode != this.port.hashCode() || this.shipsHashCode != this.shipList.hashCode()) {
            this.updateShips();
        }
        this.portHashCode = this.port.hashCode();
        this.shipsHashCode = this.shipList.hashCode();
    }

    /**
     * タブに設定するCSSクラス名
     *
     * @return CSSクラス名
     */
    public String tabCssClass() {
        return this.tabCssClass;
    }

    private void updateShips() {
        this.message.setText(this.port.getName());

        String info = new StringBuilder()
                .append("制空値計: " + this.shipList.stream()
                        .mapToInt(Ships::airSuperiority)
                        .sum())
                .append("\n")
                .append("索敵値(2-5式秋): " + MessageFormat.format("{0,number,#.##}", Ships.viewRange(this.shipList)))
                .append("\n")
                .append("判定式(33): " + MessageFormat.format("{0,number,#.##}", Ships.decision33(this.shipList)))
                .append("\n")
                .append("触接開始率: " + (int) Math.floor(Ships.touchPlaneStartProbability(this.shipList) * 100))
                .append("%")
                .append("\n")
                .append("艦娘レベル合計: " + this.shipList.stream().mapToInt(Ship::getLv).sum())
                .toString();

        this.info.setText(info);

        ObservableList<Node> childs = this.ships.getChildren();
        childs.clear();
        this.shipList.stream()
                .map(FleetTabShipPane::new)
                .forEach(childs::add);

        if (this.shipList.stream().anyMatch(Ships::isBadlyDamage)) {
            // 大破時
            this.tabCssClass = ALERT;
        } else if (this.shipList.stream().anyMatch(Ships::isHalfDamage)) {
            // 中破時
            this.tabCssClass = WARN;
        } else if (this.shipList.stream()
                .anyMatch(ship -> !ship.getFuel().equals(Ships.shipMst(ship).map(ShipMst::getFuelMax).orElse(0)) ||
                        !ship.getBull().equals(Ships.shipMst(ship).map(ShipMst::getBullMax).orElse(0)))) {
            // 未補給時
            this.tabCssClass = WARN;
        } else {
            this.tabCssClass = null;
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(FleetTabPane.class);
    }
}
