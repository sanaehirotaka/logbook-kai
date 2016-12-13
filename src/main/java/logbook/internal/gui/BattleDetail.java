package logbook.internal.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.AirBaseAttack;
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
import logbook.bean.BattleTypes.Stage2;
import logbook.bean.BattleTypes.SupportAiratack;
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

        // 基地航空隊戦フェイズ(噴式強襲)
        if (this.battle instanceof IAirBaseAttack) {
            if (((IAirBaseAttack) this.battle).getAirBaseInjection() != null) {
                // 基地航空隊戦フェイズ適用
                ps.applyAirBaseInject((IAirBaseAttack) this.battle);

                List<Node> stage = new ArrayList<>();

                AirBaseAttack airBaseAttack = ((IAirBaseAttack) this.battle).getAirBaseInjection();
                if (airBaseAttack.getStage1() != null) {
                    Stage1 stage1 = airBaseAttack.getStage1();
                    stage.add(new BattleDetailPhaseStage1(stage1, "基地航空隊"));
                }
                if (airBaseAttack.getStage2() != null) {
                    Stage2 stage2 = airBaseAttack.getStage2();
                    stage.add(new BattleDetailPhaseStage2(stage2, "基地航空隊"));
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
                phasePane.setText("基地航空隊戦フェイズ(噴式強襲)");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
        // 航空戦フェイズ(噴式強襲)
        if (this.battle instanceof IKouku) {
            if (((IKouku) this.battle).getInjectionKouku() != null) {
                // 航空戦フェイズ適用
                ps.applyInjectionKouku((IKouku) this.battle);

                List<Node> stage = new ArrayList<>();

                Kouku kouku = ((IKouku) this.battle).getInjectionKouku();
                if (kouku.getStage1() != null) {
                    Stage1 stage1 = kouku.getStage1();
                    stage.add(new BattleDetailPhaseStage1(stage1, "僚艦"));
                }
                if (kouku.getStage2() != null) {
                    Stage2 stage2 = kouku.getStage2();
                    stage.add(new BattleDetailPhaseStage2(stage2, "僚艦"));
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps);
                phasePane = new BattleDetailPhase(ps);
                phasePane.setText("航空戦フェイズ(噴式強襲)");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
        // 基地航空隊戦フェイズ
        if (this.battle instanceof IAirBaseAttack) {
            if (((IAirBaseAttack) this.battle).getAirBaseAttack() != null) {
                // 基地航空隊戦フェイズ適用
                ps.applyAirBaseAttack((IAirBaseAttack) this.battle);

                List<Node> stage = new ArrayList<>();

                for (AirBaseAttack airBaseAttack : ((IAirBaseAttack) this.battle).getAirBaseAttack()) {
                    if (airBaseAttack.getStage1() != null) {
                        stage.add(new BattleDetailPhaseStage1(airBaseAttack.getStage1(), "基地航空隊"));
                    }
                    if (airBaseAttack.getStage2() != null) {
                        stage.add(new BattleDetailPhaseStage2(airBaseAttack.getStage2(), "基地航空隊"));
                    }
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
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

                List<Node> stage = new ArrayList<>();

                Kouku kouku = ((IKouku) this.battle).getKouku();
                if (kouku.getStage1() != null) {
                    Stage1 stage1 = kouku.getStage1();
                    stage.add(new BattleDetailPhaseStage1(stage1, "僚艦"));
                }
                if (kouku.getStage2() != null) {
                    Stage2 stage2 = kouku.getStage2();
                    stage.add(new BattleDetailPhaseStage2(stage2, "僚艦"));
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
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

                SupportAiratack supportAir = ((ISupport) this.battle).getSupportInfo()
                        .getSupportAiratack();

                List<Node> stage = new ArrayList<>();

                if (supportAir != null) {
                    if (supportAir.getStage1() != null) {
                        Stage1 stage1 = supportAir.getStage1();
                        stage.add(new BattleDetailPhaseStage1(stage1, "支援艦隊"));
                    }
                    if (supportAir.getStage2() != null) {
                        Stage2 stage2 = supportAir.getStage2();
                        stage.add(new BattleDetailPhaseStage2(stage2, "支援艦隊"));
                    }
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
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
            if (((IAirbattle) this.battle).getKouku2() != null) {
                // 航空戦適用
                ps.applyAirbattle((IAirbattle) this.battle);

                List<Node> stage = new ArrayList<>();

                Kouku kouku = ((IAirbattle) this.battle).getKouku2();
                if (kouku.getStage1() != null) {
                    Stage1 stage1 = kouku.getStage1();
                    stage.add(new BattleDetailPhaseStage1(stage1, "僚艦"));
                }
                if (kouku.getStage2() != null) {
                    Stage2 stage2 = kouku.getStage2();
                    stage.add(new BattleDetailPhaseStage2(stage2, "僚艦"));
                }
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
                phasePane.setText("航空戦");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
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
