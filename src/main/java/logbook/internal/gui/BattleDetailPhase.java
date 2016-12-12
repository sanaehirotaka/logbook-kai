package logbook.internal.gui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import logbook.bean.Enemy;
import logbook.bean.Ship;
import logbook.internal.PhaseState;

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
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {

        if (this.nodes != null) {
            this.infomation.getChildren().addAll(this.nodes);
        }

        for (Ship ship : this.phase.getAfterFriend()) {
            if (ship != null) {
                this.afterFriend.getChildren().add(new BattleDetailPhaseShip(ship));
            }
        }
        for (Ship ship : this.phase.getAfterFriendCombined()) {
            if (ship != null) {
                this.afterFriendCombined.getChildren().add(new BattleDetailPhaseShip(ship));
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemyCombined()) {
            if (enemy != null) {
                this.afterEnemyCombined.getChildren().add(new BattleDetailPhaseShip(enemy));
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemy()) {
            if (enemy != null) {
                this.afterEnemy.getChildren().add(new BattleDetailPhaseShip(enemy));
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BattleDetailPhase.class);
    }
}
