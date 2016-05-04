package logbook.internal.gui;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.IAirbattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.Stage1;
import logbook.bean.Ship;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.PhaseState;
import logbook.internal.Ships;

/**
 * 戦況表示
 *
 */
public class BattleDetail extends WindowController {

    /** 連合艦隊 */
    private CombinedType combinedType;

    /** 艦隊スナップショット */
    private Map<Integer, List<Ship>> deckMap;

    /** 戦闘 */
    private IFormation battle;

    /** 夜戦 */
    private IMidnightBattle midnight;

    /** 情報 */
    @FXML
    private Label info;

    /** フェーズ */
    @FXML
    private VBox phase;

    /**
     * 戦況表示
     * @param combinedType 連合艦隊
     * @param deckMap 艦隊スナップショット
     * @param battle 戦闘
     * @param midnight 夜戦
     */
    void setData(CombinedType combinedType, Map<Integer, List<Ship>> deckMap, IFormation battle,
            IMidnightBattle midnight) {
        this.combinedType = combinedType;
        this.deckMap = deckMap;
        this.battle = battle;
        this.midnight = midnight;
        this.update();
    }

    private void update() {
        this.setInfo();
        this.setPhase();
    }

    private void setInfo() {
        PhaseState ps = new PhaseState(this.combinedType, this.battle, this.deckMap);

        StringBuilder info = new StringBuilder()
                // 艦隊行動
                .append("艦隊行動:")
                .append(BattleTypes.Intercept.toIntercept(this.battle.getFormation().get(2)).toString())
                .append("\n")
                // 味方陣形
                .append("味方陣形:")
                .append(BattleTypes.Formation.toFormation(this.battle.getFormation().get(0)).toString())
                // 敵陣形
                .append("/")
                .append("敵陣形:")
                .append(BattleTypes.Formation.toFormation(this.battle.getFormation().get(1)).toString())
                .append("\n")
                // 制空値計
                .append("制空値計: " + ps.getAfterFriend().stream()
                        .filter(Objects::nonNull)
                        .mapToInt(Ships::airSuperiority)
                        .sum());

        if (this.battle instanceof IKouku) {
            Kouku kouku = ((IKouku) this.battle).getKouku();
            Stage1 stage1 = kouku.getStage1();

            if (stage1 != null) {
                Map<Integer, SlotitemMst> slotitemMst = SlotitemMstCollection.get()
                        .getSlotitemMap();
                info.append("(")
                        // 制空権
                        .append(BattleTypes.DispSeiku.toDispSeiku(stage1.getDispSeiku()).toString())
                        .append(")")
                        .append("\n")
                        // 触接
                        .append("味方触接:")
                        .append(Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(0)))
                                .map(SlotitemMst::getName)
                                .orElse("なし"))
                        .append("/").append("敵触接:")
                        .append(Optional.ofNullable(slotitemMst.get(stage1.getTouchPlane().get(1)))
                                .map(SlotitemMst::getName)
                                .orElse("なし"));
            }
        }
        this.info.setText(info.toString());
    }

    private void setPhase() {
        PhaseState ps = new PhaseState(this.combinedType, this.battle, this.deckMap);

        List<Node> phases = this.phase.getChildren();

        BattleDetailPhase phaseBeforePane = new BattleDetailPhase(ps);
        phaseBeforePane.setText("戦闘前");
        phases.add(phaseBeforePane);

        // 基地航空隊戦フェイズ
        if (this.battle instanceof IAirBaseAttack) {
            if (((IAirBaseAttack) this.battle).getAirBaseAttack() != null) {
                // 基地航空隊戦フェイズ適用
                ps.applyAirBaseAttack((IAirBaseAttack) this.battle);

                BattleDetailPhase phasePane = new BattleDetailPhase(ps);
                phasePane.setText("基地航空隊戦フェイズ");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
        // 航空戦フェイズ
        if (this.battle instanceof IKouku) {
            if (((IKouku) this.battle).getKouku() != null) {
                // 航空戦フェイズ適用
                ps.applyKouku((IKouku) this.battle);

                BattleDetailPhase phasePane = new BattleDetailPhase(ps);
                phasePane.setText("航空戦フェイズ");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
        // 支援フェイズ
        if (this.battle instanceof ISupport) {
            if (((ISupport) this.battle).getSupportInfo() != null) {
                // 支援フェイズ適用
                ps.applySupport((ISupport) this.battle);

                BattleDetailPhase phasePane = new BattleDetailPhase(ps);
                phasePane.setText("支援フェイズ");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
        // 砲雷撃戦フェイズ
        if (this.battle instanceof ISortieHougeki) {
            // 砲雷撃戦フェイズ適用
            ps.applySortieHougeki((ISortieHougeki) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("砲雷撃戦フェイズ");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
        // 航空戦
        if (this.battle instanceof IAirbattle) {
            // 航空戦適用
            ps.applyAirbattle((IAirbattle) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("航空戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
        // 特殊夜戦
        if (this.battle instanceof IMidnightBattle) {
            // 特殊夜戦適用
            ps.applyMidnightBattle((IMidnightBattle) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("特殊夜戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
        // 夜戦
        if (this.midnight != null) {
            PhaseState phaseMidnight = new PhaseState(this.combinedType, this.midnight, this.deckMap);
            // 夜戦適用
            phaseMidnight.applyMidnightBattle(this.midnight);

            BattleDetailPhase phasePane = new BattleDetailPhase(phaseMidnight);
            phasePane.setText("夜戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }

        ((BattleDetailPhase) phases.get(phases.size() - 1)).setExpanded(true);
    }
}
