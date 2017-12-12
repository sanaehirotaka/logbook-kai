package logbook.internal.gui;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.Messages;
import logbook.bean.Chara;
import logbook.bean.ShipMst;
import logbook.internal.Ships;

/**
 * 戦闘ログ詳細のフェーズの艦船
 *
 */
public class BattleDetailPhaseShip extends HBox {

    /** キャラクター */
    private Chara chara;

    /** キャラクター画像 */
    @FXML
    private ImageView img;

    /** 名前 */
    @FXML
    private Label name;

    /** HP */
    @FXML
    private Label hp;

    /**
    * 戦闘ログ詳細のフェーズの艦船のコンストラクタ
    *
    * @param chara キャラクター
    */
    public BattleDetailPhaseShip(Chara chara) {
        this.chara = chara;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/battle_detail_phase_ship.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.img.setImage(Ships.shipWithItemWithoutStateBannerImage(this.chara));
        this.name.setText(Messages.getString("ship.name", Ships.shipMst(this.chara)
                .map(ShipMst::getName)
                .orElse(""), this.chara.getLv()));
        this.hp.setText(this.chara.getNowhp() + "/" + this.chara.getMaxhp());
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(BattleDetailPhase.class);
    }
}
