package logbook.internal.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import logbook.bean.BattleTypes;
import logbook.bean.BattleTypes.AirBaseAttack;
import logbook.bean.BattleTypes.CombinedType;
import logbook.bean.BattleTypes.IAirBaseAttack;
import logbook.bean.BattleTypes.IAirbattle;
import logbook.bean.BattleTypes.IFormation;
import logbook.bean.BattleTypes.IKouku;
import logbook.bean.BattleTypes.IMidnightBattle;
import logbook.bean.BattleTypes.INSupport;
import logbook.bean.BattleTypes.INightToDayBattle;
import logbook.bean.BattleTypes.ISortieHougeki;
import logbook.bean.BattleTypes.ISupport;
import logbook.bean.BattleTypes.Kouku;
import logbook.bean.BattleTypes.Stage1;
import logbook.bean.BattleTypes.Stage2;
import logbook.bean.BattleTypes.SupportAiratack;
import logbook.bean.BattleTypes.SupportInfo;
import logbook.bean.MapStartNext;
import logbook.bean.Ship;
import logbook.bean.SlotItem;
import logbook.bean.SlotitemMst;
import logbook.bean.SlotitemMstCollection;
import logbook.internal.Items;
import logbook.internal.PhaseState;
import logbook.internal.Ships;

/**
 * 戦況表示
 *
 */
public class BattleDetail extends WindowController {

    /** 出撃/進撃 */
    private MapStartNext last;

    /** 連合艦隊 */
    private CombinedType combinedType;

    /** 艦隊スナップショット */
    private Map<Integer, List<Ship>> deckMap;

    /** 装備 */
    private Map<Integer, SlotItem> itemMap;

    /** 戦闘 */
    private IFormation battle;

    /** 夜戦 */
    private IMidnightBattle midnight;

    /** フェーズ */
    @FXML
    private VBox phase;

    /** マス */
    @FXML
    private Label mapcell;

    /** 艦隊行動: */
    @FXML
    private Label intercept;

    /** 味方陣形 */
    @FXML
    private Label fFormation;

    /** 味方陣形 */
    @FXML
    private Label eFormation;

    /** 制空値計 */
    @FXML
    private Label seiku;

    /** 味方触接 */
    @FXML
    private Label dispSeiku;

    /** 味方触接アイコン */
    @FXML
    private ImageView fTouchPlaneImage;

    /** 味方触接 */
    @FXML
    private Label fTouchPlane;

    /** 敵触接アイコン */
    @FXML
    private ImageView eTouchPlaneImage;

    /** 敵触接 */
    @FXML
    private Label eTouchPlane;

    /** 対空CI */
    @FXML
    private Label tykuCI;

    /**
     * 戦況表示
     * @param last 出撃/進撃
     * @param combinedType 連合艦隊
     * @param deckMap 艦隊スナップショット
     * @param itemMap 装備
     * @param battle 戦闘
     * @param midnight 夜戦
     */
    void setData(MapStartNext last, CombinedType combinedType, Map<Integer, List<Ship>> deckMap,
            Map<Integer, SlotItem> itemMap, IFormation battle, IMidnightBattle midnight) {
        this.last = last;
        this.combinedType = combinedType;
        this.deckMap = deckMap;
        this.itemMap = itemMap;
        this.battle = battle;
        this.midnight = midnight;
        this.update();
    }

    /**
     * 画像ファイルに保存
     *
     * @param event ActionEvent
     */
    @FXML
    void storeImageAction(ActionEvent event) {
        Tools.Conrtols.storeSnapshot(this.phase, "戦闘ログのスナップショット", this.getWindow());
    }

    private void update() {
        this.setInfo();
        this.setPhase();
    }

    private void setInfo() {
        PhaseState ps = new PhaseState(this.combinedType, this.battle, this.deckMap, this.itemMap);

        // マス
        boolean boss = this.last.getNo().equals(this.last.getBosscellNo()) || this.last.getEventId() == 5;
        this.mapcell.setText(this.last.getMapareaId()
                + "-" + this.last.getMapinfoNo()
                + "-" + this.last.getNo()
                + (boss ? "(ボス)" : ""));

        // 艦隊行動
        this.intercept.setText(BattleTypes.Intercept.toIntercept(this.battle.getFormation().get(2)).toString());
        // 味方陣形
        this.fFormation.setText(BattleTypes.Formation.toFormation(this.battle.getFormation().get(0)).toString());
        // 敵陣形
        this.eFormation.setText(BattleTypes.Formation.toFormation(this.battle.getFormation().get(1)).toString());
        // 制空値計
        this.seiku.setText(Integer.toString(ps.getAfterFriend().stream()
                .filter(Objects::nonNull)
                .mapToInt(Ships::airSuperiority)
                .sum()));

        if (this.battle instanceof IKouku) {
            Kouku kouku = ((IKouku) this.battle).getKouku();
            if (kouku != null) {
                Stage1 stage1 = kouku.getStage1();
                Stage2 stage2 = kouku.getStage2();

                if (stage1 != null) {
                    Map<Integer, SlotitemMst> slotitemMst = SlotitemMstCollection.get()
                            .getSlotitemMap();
                    // 制空権
                    this.dispSeiku.setText(BattleTypes.DispSeiku.toDispSeiku(stage1.getDispSeiku()).toString());
                    this.dispSeiku.getStyleClass().add("dispseiku" + stage1.getDispSeiku());
                    // 味方触接
                    SlotitemMst fTouchPlaneItem = slotitemMst.get(stage1.getTouchPlane().get(0));
                    if (fTouchPlaneItem != null) {
                        Image image = Items.itemImage(fTouchPlaneItem);
                        if (image != null) {
                            this.fTouchPlaneImage.setImage(image);
                        }
                        this.fTouchPlane.setText(fTouchPlaneItem.getName()
                                + "(+" + (int) (Ships.touchPlaneAttackCompensation(fTouchPlaneItem) * 100) + "%)");
                    } else {
                        this.fTouchPlaneImage.setFitWidth(0);
                        this.fTouchPlaneImage.setFitHeight(0);
                        this.fTouchPlane.setText("なし");
                    }
                    // 敵触接
                    SlotitemMst eTouchPlaneItem = slotitemMst.get(stage1.getTouchPlane().get(1));
                    if (eTouchPlaneItem != null) {
                        Image image = Items.itemImage(eTouchPlaneItem);
                        if (image != null) {
                            this.eTouchPlaneImage.setImage(image);
                        }
                        this.eTouchPlane.setText(eTouchPlaneItem.getName()
                                + "(+" + (int) (Ships.touchPlaneAttackCompensation(eTouchPlaneItem) * 100) + "%)");
                    } else {
                        this.eTouchPlaneImage.setFitWidth(0);
                        this.eTouchPlaneImage.setFitHeight(0);
                        this.eTouchPlane.setText("なし");
                    }
                }
                if (stage2 != null) {
                    // 対空CI
                    if (stage2.getAirFire() != null && stage2.getAirFire().getIdx() != null) {
                        // インデックスは0始まり
                        int idx = stage2.getAirFire().getIdx();
                        Ship ship;
                        if (idx < 6) {
                            ship = ps.getAfterFriend().get(idx);
                        } else {
                            ship = ps.getAfterFriendCombined().get(idx - 6);
                        }
                        this.tykuCI.setText(Ships.toName(ship));
                    }
                }
            }
        }
    }

    /**
     * 各フェイズの表示
     */
    private void setPhase() {
        PhaseState ps = new PhaseState(this.combinedType, this.battle, this.deckMap, this.itemMap);

        List<Node> phases = this.phase.getChildren();

        BattleDetailPhase phaseBeforePane = new BattleDetailPhase(ps);
        phaseBeforePane.setText("戦闘前");
        phases.add(phaseBeforePane);

        if (!(this.battle instanceof INightToDayBattle)) {
            // 夜戦→昼戦以外

            // 基地航空隊戦フェイズ(噴式強襲)
            this.airBaseInjectionAttack(ps, phases);
            // 航空戦フェイズ(噴式強襲)
            this.injectionKouku(ps, phases);
            // 基地航空隊戦フェイズ
            this.airBaseAttack(ps, phases);
            // 航空戦フェイズ
            this.kouku(ps, phases);
            // 支援フェイズ
            this.support(ps, phases);
            // 砲雷撃戦フェイズ
            this.sortieHougeki(ps, phases);
            // 航空戦
            this.airbattle(ps, phases);
            // 夜戦支援
            this.nSupport(ps, phases);
            // 特殊夜戦
            this.midnightBattle(ps, phases);
            // 夜戦
            this.midnight(ps, phases);
        } else {
            // 夜戦→昼戦以外

            // 夜戦支援
            this.nSupport(ps, phases);
            // 特殊夜戦
            this.midnightBattle(ps, phases);
            // 基地航空隊戦フェイズ(噴式強襲)
            this.airBaseInjectionAttack(ps, phases);
            // 航空戦フェイズ(噴式強襲)
            this.injectionKouku(ps, phases);
            // 基地航空隊戦フェイズ
            this.airBaseAttack(ps, phases);
            // 航空戦フェイズ
            this.kouku(ps, phases);
            // 支援フェイズ
            this.support(ps, phases);
            // 砲雷撃戦フェイズ
            this.sortieHougeki(ps, phases);
        }

        ((BattleDetailPhase) phases.get(phases.size() - 1)).setExpanded(true);
    }

    /**
     * 基地航空隊戦フェイズ(噴式強襲)
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void airBaseInjectionAttack(PhaseState ps, List<Node> phases) {
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
    }

    /**
     * 航空戦フェイズ(噴式強襲)
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void injectionKouku(PhaseState ps, List<Node> phases) {
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
                BattleDetailPhase phasePane = new BattleDetailPhase(ps, stage);
                phasePane = new BattleDetailPhase(ps);
                phasePane.setText("航空戦フェイズ(噴式強襲)");
                phasePane.setExpanded(false);
                phases.add(phasePane);
            }
        }
    }

    /**
     * 基地航空隊戦フェイズ
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void airBaseAttack(PhaseState ps, List<Node> phases) {
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
    }

    /**
     * 航空戦フェイズ
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void kouku(PhaseState ps, List<Node> phases) {
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
    }

    /**
     * 支援フェイズ
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void support(PhaseState ps, List<Node> phases) {
        // 支援フェイズ
        if (this.battle instanceof ISupport) {
            SupportInfo support = ((ISupport) this.battle).getSupportInfo();
            if (support != null) {
                // 支援フェイズ適用
                ps.applySupport((ISupport) this.battle);
                this.setSupportPhase(ps, phases, support);
            }
        }
    }

    /**
     * 砲雷撃戦フェイズ
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void sortieHougeki(PhaseState ps, List<Node> phases) {
        // 砲雷撃戦フェイズ
        if (this.battle instanceof ISortieHougeki) {
            // 砲雷撃戦フェイズ適用
            ps.applySortieHougeki((ISortieHougeki) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("砲雷撃戦フェイズ");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
    }

    /**
     * 航空戦
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void airbattle(PhaseState ps, List<Node> phases) {
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
    }

    /**
     * 夜戦支援
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void nSupport(PhaseState ps, List<Node> phases) {
        // 夜戦支援
        if (this.battle instanceof INSupport) {
            SupportInfo support = ((INSupport) this.battle).getNSupportInfo();
            if (support != null) {
                // 支援フェイズ適用
                ps.applySupport((INSupport) this.battle);
                this.setSupportPhase(ps, phases, support);
            }
        }
    }

    /**
     * 特殊夜戦
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void midnightBattle(PhaseState ps, List<Node> phases) {
        // 特殊夜戦
        if (this.battle instanceof IMidnightBattle) {
            // 特殊夜戦適用
            ps.applyMidnightBattle((IMidnightBattle) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("特殊夜戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
        if (this.battle instanceof INightToDayBattle) {
            // 特殊夜戦適用
            ps.applyMidnightBattle((INightToDayBattle) this.battle);

            BattleDetailPhase phasePane = new BattleDetailPhase(ps);
            phasePane.setText("特殊夜戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
    }

    /**
     * 夜戦
     * @param ps フェーズ
     * @param phases 表示ノード
     */
    private void midnight(PhaseState ps, List<Node> phases) {
        // 夜戦
        if (this.midnight != null) {
            PhaseState phaseMidnight = new PhaseState(ps);
            // 夜戦適用
            phaseMidnight.applyMidnightBattle(this.midnight);

            BattleDetailPhase phasePane = new BattleDetailPhase(phaseMidnight);
            phasePane.setText("夜戦");
            phasePane.setExpanded(false);
            phases.add(phasePane);
        }
    }

    /**
     * 支援共通
     * @param ps フェーズ
     * @param phases 表示ノード
     * @param support 支援
     */
    private void setSupportPhase(PhaseState ps, List<Node> phases, SupportInfo support) {
        SupportAiratack supportAir = support.getSupportAiratack();
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
