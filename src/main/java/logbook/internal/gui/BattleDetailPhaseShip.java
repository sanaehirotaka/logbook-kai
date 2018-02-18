package logbook.internal.gui;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import logbook.bean.Chara;
import logbook.bean.SlotItem;
import logbook.internal.LoggerHolder;
import logbook.internal.Ships;

/**
 * 戦闘ログ詳細のフェーズの艦船
 *
 */
public class BattleDetailPhaseShip extends HBox {

    /** キャラクター */
    private Chara chara;

    /** 装備Map */
    private Map<Integer, SlotItem> itemMap;

    /** 退避艦IDスナップショット */
    private Set<Integer> escape;

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
    * @param itemMap 装備
    * @param escape 退避艦ID
    */
    public BattleDetailPhaseShip(Chara chara, Map<Integer, SlotItem> itemMap, Set<Integer> escape) {
        this.chara = chara;
        this.itemMap = itemMap;
        this.escape = escape;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/battle_detail_phase_ship.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        this.img.setImage(Ships.shipWithItemWithoutStateBannerImage(this.chara, this.itemMap, this.escape));
        this.name.setText(Ships.toName(this.chara));
        this.hp.setText(this.chara.getNowhp() + "/" + this.chara.getMaxhp());
    }
}
