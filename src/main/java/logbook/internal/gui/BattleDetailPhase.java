package logbook.internal.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logbook.bean.Chara;
import logbook.bean.Enemy;
import logbook.bean.Friend;
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

    /** 詳細 */
    private List<PhaseState.AttackDetail> attackDetails;

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

    /** 友軍艦隊フラグ */
    private boolean isFriendlyBattle;

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
        this(phase, infomation, false);
    }

    /**
    * 戦闘ログ詳細のフェーズのコンストラクタ
    *
    * @param phase フェイズ
    * @param infomation 付加情報
    * @param isFriendlyBattle 友軍艦隊フラグ
    */
    public BattleDetailPhase(PhaseState phase, List<? extends Node> infomation, boolean isFriendlyBattle) {
        this.phase = new PhaseState(phase);
        this.attackDetails = new ArrayList<>(phase.getAttackDetails());
        this.nodes = infomation;
        this.isFriendlyBattle = isFriendlyBattle;
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

        PopOver<Chara> popover = new PopOver<>((node, chara) -> {
            VBox child = new VBox();
            child.getChildren().add(new FleetTabShipPopup(chara));
            List<PhaseState.AttackDetail> details = this.attackDetails.stream()
                    .filter(e -> {
                        if (chara.getClass() != e.getAttacker().getClass()) {
                            return false;
                        }
                        if (chara instanceof Ship) {
                            return ((Ship) chara).getId() == ((Ship) e.getAttacker()).getId();
                        }
                        if (chara instanceof Friend) {
                            return chara.getShipId() == e.getAttacker().getShipId();
                        }
                        if (chara instanceof Enemy) {
                            return ((Enemy) chara).getOrder() == ((Enemy) e.getAttacker()).getOrder();
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            if (!details.isEmpty()) {
                Label text = new Label("与ダメージ");
                text.getStyleClass().add("title");
                child.getChildren().add(text);
                child.getChildren().add(this.detailNode(details));
            }
            return new PopOverPane(Ships.toName(chara), child);
        });

        if (this.isFriendlyBattle) {
            for (Friend friend : this.phase.getAfterFriendly()) {
                if (friend != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(friend, null, null);
                    this.afterFriend.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, friend);
                }
            }
        } else {
            for (Ship ship : this.phase.getAfterFriend()) {
                if (ship != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(ship,
                            this.phase.getItemMap(), this.phase.getEscape());
                    this.afterFriend.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, ship);
                }
            }
            for (Ship ship : this.phase.getAfterFriendCombined()) {
                if (ship != null) {
                    BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(ship,
                            this.phase.getItemMap(), this.phase.getEscape());
                    this.afterFriendCombined.getChildren().add(phaseShip);
                    // マウスオーバーでのポップアップ
                    popover.install(phaseShip, ship);
                }
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemyCombined()) {
            if (enemy != null) {
                BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(enemy, null, null);
                this.afterEnemyCombined.getChildren().add(phaseShip);
                // マウスオーバーでのポップアップ
                popover.install(phaseShip, enemy);
            }
        }
        for (Enemy enemy : this.phase.getAfterEnemy()) {
            if (enemy != null) {
                BattleDetailPhaseShip phaseShip = new BattleDetailPhaseShip(enemy, null, null);
                this.afterEnemy.getChildren().add(phaseShip);
                // マウスオーバーでのポップアップ
                popover.install(phaseShip, enemy);
            }
        }

        if (!this.attackDetails.isEmpty()) {
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
                Parent content = this.detailNode(this.attackDetails);
                ((TitledPane) node).setContent(content);
            }
        }
    }

    private Parent detailNode(List<PhaseState.AttackDetail> details) {
        VBox content = new VBox();
        for (PhaseState.AttackDetail detail : details) {
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
        return content;
    }
}
