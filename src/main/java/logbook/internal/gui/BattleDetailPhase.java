package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    /** 第1艦隊 */
    @FXML
    private VBox afterFriend;

    /** 第2艦隊 */
    @FXML
    private VBox afterFriendCombined;

    /** 敵 */
    @FXML
    private VBox afterEnemy;

    /**
    * 戦闘ログ詳細のフェーズのコンストラクタ
    *
    * @param phase フェイズ
    */
    public BattleDetailPhase(PhaseState phase) {
        this.phase = phase;
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
