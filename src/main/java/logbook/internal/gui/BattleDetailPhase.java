package logbook.internal.gui;

import java.io.IOException;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logbook.bean.Chara;
import logbook.bean.Enemy;
import logbook.bean.Ship;
import logbook.internal.LoggerHolder;
import logbook.internal.PhaseState;
import logbook.internal.Ships;

/**
 * 戦闘ログ詳細のフェーズ
 *
 */
public class BattleDetailPhase extends TitledPane {

    /** フェイズ */
    private PhaseState phase;

    /** 付加情報 */
    private List<? extends Node> nodes;

    /** 付加情報 */
    @FXML
    private VBox infomation;

    /** 第1艦隊 */
    @FXML
    private VBox afterFriend;

    /** 第2艦隊 */
    @FXML
    private VBox afterFriendCombined;

    /** 敵第2艦隊 */
    @FXML
    private VBox afterEnemyCombined;

    /** 敵第1艦隊 */
    @FXML
    private VBox afterEnemy;

    /** 詳細 */
    @FXML
    private VBox detail;

    /**
    * 戦闘ログ詳細のフェーズのコンストラクタ
    *
    * @param phase フェイズ
    */
    public BattleDetailPhase(PhaseState phase) {
        this(phase, null);
    }

    /**
    * 戦闘ログ詳細のフェーズのコンストラクタ
    *
    * @param phase フェイズ
    * @param infomation 付加情報
    */
    public BattleDetailPhase(PhaseState phase, List<? extends Node> infomation) {
        this.phase = phase;
        this.nodes = infomation;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/battle_detail_phase.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {

        if (this.nodes != null) {
            this.infomation.getChildren().addAll(this.nodes);
        }

        for (Ship ship : this.phase.getAfterFriend()) {
            if (ship != null) {
                this.afterFriend.getChildren().add(new BattleDetailPhaseShip(ship,
                        this.phase.getItemMap(), this.phase.getEscape()));
            }
        }
        for (Ship ship : this.phase.getAfterFriendCombined()) {
            if (ship != null) {
                this.afterFriendCombined.getChildren().add(new BattleDetailPhaseShip(ship,
                        this.phase.getItemMap(), this.phase.getEscape()));
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemyCombined()) {
            if (enemy != null) {
                this.afterEnemyCombined.getChildren().add(new BattleDetailPhaseShip(enemy, null, null));
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemy()) {
            if (enemy != null) {
                this.afterEnemy.getChildren().add(new BattleDetailPhaseShip(enemy, null, null));
            }
        }

        if (!this.phase.getAttackDetails().isEmpty()) {
            TitledPane pane = new TitledPane("詳細", new VBox());
            pane.setAnimated(false);
            pane.setExpanded(false);
            pane.expandedProperty().addListener((ChangeListener<Boolean>) this::initializeDetail);

            this.detail.getChildren().add(pane);
        }
    }

    private void initializeDetail(ObservableValue<? extends Boolean> ob, Boolean o, Boolean n) {
        if (!n)
            return;
        for (Node node : this.detail.getChildren()) {
            if (node instanceof TitledPane) {
                VBox content = new VBox();
                for (PhaseState.AttackDetail detail : this.phase.getAttackDetails()) {
                    Chara attacker = detail.getAttacker();
                    Chara defender = detail.getDefender();

                    StringBuilder sb = new StringBuilder();
                    if (attacker != null)
                        sb.append(Ships.toName(attacker)).append("が");
                    sb.append(Ships.toName(defender)).append("に");
                    sb.append(detail.getDamage()).append("ダメージ");
                    sb.append("(").append(detail.getAtType()).append(")");
                    content.getChildren().add(new Label(sb.toString()));

                    HBox graphic = new HBox();
                    graphic.getChildren().add(new BattleDetailPhaseShip(attacker,
                            this.phase.getItemMap(), this.phase.getEscape()));
                    graphic.getChildren().add(new BattleDetailPhaseShip(defender,
                            this.phase.getItemMap(), this.phase.getEscape()));
                    content.getChildren().add(graphic);
                    content.getChildren().add(new Separator());
                }
                ((TitledPane) node).setContent(content);
            }
        }
    }
}
