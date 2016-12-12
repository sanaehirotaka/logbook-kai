package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logbook.bean.BattleTypes.Stage2;

/**
 * Stage2 詳細
 *
 */
public class BattleDetailPhaseStage2 extends VBox {

    private Stage2 stage2;

    private String friendName;

    @FXML
    private Label fName;

    @FXML
    private Label fCount;

    @FXML
    private Label fCountAfter;

    @FXML
    private Label fLostcount;

    @FXML
    private Label eName;

    @FXML
    private Label eCount;

    @FXML
    private Label eCountAfter;

    @FXML
    private Label eLostcount;

    /**
    * Stage2 詳細
    * @param stage2　Stage2
    * @param friendName 味方の名前(僚艦/基地航空隊 等)
    */
    public BattleDetailPhaseStage2(Stage2 stage2, String friendName) {
        this.stage2 = stage2;
        this.friendName = friendName;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/battle_detail_phase_stage2.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        if (this.stage2 != null) {
            this.fName.setText(this.friendName);

            if (this.stage2.getFCount() != null && this.stage2.getFLostcount() != null) {
                this.fCount.setText(Integer.toString(this.stage2.getFCount()));
                this.fCountAfter.setText(Integer.toString(this.stage2.getFCount() - this.stage2.getFLostcount()));
                this.fLostcount.setText(Integer.toString(this.stage2.getFLostcount()));
            }
            if (this.stage2.getECount() != null && this.stage2.getELostcount() != null) {
                this.eCount.setText(Integer.toString(this.stage2.getECount()));
                this.eCountAfter.setText(Integer.toString(this.stage2.getECount() - this.stage2.getELostcount()));
                this.eLostcount.setText(Integer.toString(this.stage2.getELostcount()));
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BattleDetailPhaseStage2.class);
    }
}
