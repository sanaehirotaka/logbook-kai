package logbook.internal.gui;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logbook.bean.AppCondition;
import logbook.bean.DeckPort;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipMst;
import logbook.internal.Items;
import logbook.internal.LoggerHolder;
import logbook.internal.SeaArea;
import logbook.internal.Ships;
import lombok.val;

/**
 * 艦隊タブ
 *
 */
public class FleetTabPane extends ScrollPane {

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

    /** 分岐点係数 */
    private double branchCoefficient = 1;

    /** メッセージ */
    @FXML
    private Label message;

    /** 艦娘達 */
    @FXML
    private VBox ships;

    /** 制空値アイコン */
    @FXML
    private ImageView airSuperiorityImg;

    /** 制空値 */
    @FXML
    private Label airSuperiority;

    /** 触接開始率アイコン */
    @FXML
    private ImageView touchPlaneStartProbabilityImg;

    /** 触接開始率 */
    @FXML
    private Label touchPlaneStartProbability;

    /** 判定式(33)アイコン */
    @FXML
    private ImageView decision33Img;

    /** 判定式(33) */
    @FXML
    private Label decision33;

    /** 分岐点係数ボタン */
    @FXML
    private Button branchCoefficientButton;

    /** 艦娘レベル計アイコン */
    @FXML
    private ImageView lvsumImg;

    /** 艦娘レベル計 */
    @FXML
    private Label lvsum;

    /** 疲労 */
    @FXML
    private Label cond;

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
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.update();
        this.setIcon();
    }

    /**
     * 分岐点係数を変更する
     *
     * @param event ActionEvent
     */
    @FXML
    void changeBranchCoefficient(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(Double.toString(this.branchCoefficient));
        dialog.getDialogPane().getStylesheets().add("logbook/gui/application.css");
        dialog.initOwner(this.getScene().getWindow());
        dialog.setTitle("分岐点係数を変更");
        dialog.setHeaderText("分岐点係数を数値で入力してください 例)\n"
                + SeaArea.沖ノ島沖 + " H,Iマス 係数: 1.0\n"
                + SeaArea.北方AL海域 + " Gマス 係数: 4.0\n"
                + SeaArea.中部海域哨戒線 + " E,Fマス 係数: 4.0\n"
                + SeaArea.MS諸島沖 + " F,Hマス 係数: 3.0\n"
                + SeaArea.グアノ環礁沖海域 + " Hマス 係数: 3.0");

        val result = dialog.showAndWait();
        if (result.isPresent()) {
            String value = result.get();
            if (!value.isEmpty()) {
                try {
                    this.branchCoefficient = Double.parseDouble(value);
                    this.setDecision33();
                } catch (NumberFormatException e) {
                }
            }
        }
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

        // 制空値
        this.airSuperiority
                .setText(Integer.toString(this.shipList.stream()
                        .mapToInt(Ships::airSuperiority)
                        .sum()));
        // 触接開始率
        this.touchPlaneStartProbability
                .setText((int) Math.floor(Ships.touchPlaneStartProbability(this.shipList) * 100) + "%");
        // 判定式(33)
        this.setDecision33();
        // 艦娘レベル計
        this.lvsum.setText(Integer.toString(this.shipList.stream().mapToInt(Ship::getLv).sum()));

        ObservableList<Node> childs = this.ships.getChildren();
        childs.clear();
        this.shipList.stream()
                .map(FleetTabShipPane::new)
                .forEach(childs::add);

        if (this.shipList.stream().anyMatch(Ships::isBadlyDamage)) {
            // 大破時
            this.tabCssClass = "alert";
        } else if (this.shipList.stream().anyMatch(Ships::isHalfDamage)) {
            // 中破時
            this.tabCssClass = "warn";
        } else if (this.shipList.stream()
                .anyMatch(ship -> !ship.getFuel().equals(Ships.shipMst(ship).map(ShipMst::getFuelMax).orElse(0)) ||
                        !ship.getBull().equals(Ships.shipMst(ship).map(ShipMst::getBullMax).orElse(0)))) {
            // 未補給時
            this.tabCssClass = "shortage";
        } else if (this.port.getId() > 1 && this.port.getMission().get(0) == 0L) {
            // 遠征未出撃
            this.tabCssClass = "empty";
        } else {
            this.tabCssClass = null;
        }
        // 疲労
        int minCond = this.shipList.stream()
                .mapToInt(Ship::getCond)
                .min()
                .orElse(49);
        if (minCond < 49) {

            long cut = AppCondition.get().getCondUpdateTime();
            // 疲労抜け想定時刻(エポック秒)
            long end = cut + (-Math.floorDiv(49 - minCond, -3) * 180);

            // 現在時刻
            ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
            // 現在時刻(エポック秒)
            long nowepoch = now.toEpochSecond();

            if (end > nowepoch) {
                ZonedDateTime disp = ZonedDateTime.ofInstant(Instant.ofEpochSecond(end), ZoneId.systemDefault());
                DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");

                this.cond.setText(format.format(disp));
            } else {
                this.cond.setText("");
            }
        } else {
            this.cond.setText("");
        }
    }

    /**
     * 判定式(33) を設定する
     */
    private void setDecision33() {
        // 判定式(33)
        this.decision33.setText(MessageFormat.format("{0,number,#.##}",
                Ships.decision33(this.shipList, this.branchCoefficient)));
        this.branchCoefficientButton.setText("分岐点係数:" + this.branchCoefficient);
    }

    private void setIcon() {
        Path path;
        path = Items.itemImageByType(6);
        if (path != null) {
            this.airSuperiorityImg.setImage(new Image(path.toUri().toString()));
        }
        path = Items.itemImageByType(10);
        if (path != null) {
            this.touchPlaneStartProbabilityImg.setImage(new Image(path.toUri().toString()));
        }
        path = Items.itemImageByType(9);
        if (path != null) {
            this.decision33Img.setImage(new Image(path.toUri().toString()));
        }
        path = Items.itemImageByType(28);
        if (path != null) {
            this.lvsumImg.setImage(new Image(path.toUri().toString()));
        }
    }
}
