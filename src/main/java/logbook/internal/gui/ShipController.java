package logbook.internal.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logbook.bean.AppConfig;
import logbook.bean.AppShipTableConfig;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.ShipLabelCollection;
import logbook.bean.ShipMst;
import logbook.internal.LoggerHolder;
import logbook.internal.SeaArea;
import logbook.internal.Ships;

/**
 * 所有艦娘一覧のコントローラ
 *
 */
public class ShipController extends WindowController {

    @FXML
    private TabPane tab;

    private Timeline timeline;

    @FXML
    void initialize() {
        try {
            this.tab.getSelectionModel().selectedItemProperty().addListener((ov, o, n) -> {
                if (o instanceof Tab) {
                    Node node = o.getContent();
                    if (node instanceof ShipTablePane) {
                        ((ShipTablePane) node).disable();
                    }
                }
                if (n instanceof Tab) {
                    Node node = n.getContent();
                    if (node instanceof ShipTablePane) {
                        ((ShipTablePane) node).enable();
                        ((ShipTablePane) node).update();
                    }
                    if (node instanceof StatisticsPane) {
                        ((StatisticsPane) node).update();
                    }
                }
            });

            ShipTablePane allPane = new ShipTablePane(() -> {
                Map<Integer, Ship> shipMap = ShipCollection.get()
                        .getShipMap();
                return shipMap.values().stream()
//                      .sorted(Comparator.comparing(Ship::getLv).reversed()
//                      .thenComparing(Comparator.comparing(Ship::getShipId)))
                        .sorted(Comparator.comparingInt((Ship s) -> Ships.shipMst(s).map(ShipMst::getSortId).orElse(Integer.MAX_VALUE))
                                .thenComparingInt(Ship::getId))
                        .collect(Collectors.toList());
            }, "全員");

            this.tab.getTabs().add(new Tab("全員", allPane));

            // 全艦隊のタブ
            if (AppConfig.get().isAllDecksTab()) {
                this.addAllDecksTab();
            }
            // 艦隊単位のタブ
            if (AppConfig.get().isDeckTabs()) {
                this.addDeckTabs();
            }
            // ラベル単位のタブ
            if (AppConfig.get().isLabelTabs()) {
                this.addLabelTabs();
            }
            int index = AppShipTableConfig.get().getTabIndex();
            if (index < this.tab.getTabs().size()) {
                this.tab.getSelectionModel().select(index);
            }
            this.tab.getSelectionModel().selectedIndexProperty().addListener((ob, o, n) -> {
                if (n != null) {
                    AppShipTableConfig.get().setTabIndex(n.intValue());
                }
            });

            this.addStatistics();

            this.timeline = new Timeline();
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(5),
                    this::update));
            this.timeline.play();

        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 全艦隊のタブ
     */
    private void addAllDecksTab() {
        ShipTablePane deckPane = new ShipTablePane(() -> {
            Map<Integer, Ship> shipMap = ShipCollection.get()
                    .getShipMap();
            return DeckPortCollection.get().getDeckPortMap().values().stream()
                .map(DeckPort::getShip)
                .flatMap(List::stream)
                .map(shipMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }, "全艦隊");
        this.tab.getTabs().add(new Tab("全艦隊", deckPane));
    }

    /**
     * 艦隊単位のタブ
     */
    private void addDeckTabs() {
        for (DeckPort deck : DeckPortCollection.get().getDeckPortMap().values()) {
            ShipTablePane deckPane = new ShipTablePane(deck);
            this.tab.getTabs().add(new Tab(deck.getName(), deckPane));
        }
    }

    /**
     * ラベル単位のタブ
     */
    private void addLabelTabs() {
        Map<Integer, Set<String>> labelMap = ShipLabelCollection.get().getLabels();
        ShipCollection.get().getShipMap().values().stream()
                .flatMap(ship -> {
                    List<String> label = new ArrayList<>();
                    SeaArea area = SeaArea.fromArea(ship.getSallyArea());
                    if (area != null) {
                        label.add(area.toString());
                    }
                    Set<String> labels = labelMap.get(ship.getId());
                    if (labels != null) {
                        label.addAll(labels);
                    }
                    return label.stream();
                })
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparing(ShipController::labelSortOrder))
                .forEach(label -> {
                    ShipTablePane labelPane = new ShipTablePane(() -> {
                        Map<Integer, Ship> shipMap = ShipCollection.get()
                                .getShipMap();
                        return shipMap.values().stream()
                                .filter(ship -> {
                                    SeaArea area = SeaArea.fromArea(ship.getSallyArea());
                                    if (area != null && label.equals(area.toString()))
                                        return true;
                                    Set<String> labels = ShipLabelCollection.get().getLabels().get(ship.getId());
                                    if (labels != null && labels.contains(label))
                                        return true;
                                    return false;
                                })
                                .sorted(Comparator.comparing(Ship::getLv).reversed()
                                        .thenComparing(Comparator.comparing(Ship::getShipId)))
                                .collect(Collectors.toList());
                    }, label);
                    this.tab.getTabs().add(new Tab(label, labelPane));
                });
    }

    /**
     * ラベルのソート順を返します
     * @param label ラベル文字列
     * @return ソート順、もし定義にないものの場合は Integer.MAX_VALUE
     */
    private static int labelSortOrder(String label) {
        return Stream.of(SeaArea.values())
                .filter(s -> s.getName().equals(label))
                .findAny()
                .map(SeaArea::getArea)
                .orElse(Integer.MAX_VALUE);
    }

    /**
     * 統計タブ
     */
    private void addStatistics() {
        this.tab.getTabs().add(new Tab("統計", new StatisticsPane()));
    }

    /**
     * 画面の更新
     *
     * @param e ActionEvent
     */
    void update(ActionEvent e) {
        Tab selectedTab = this.tab.getSelectionModel()
                .getSelectedItem();
        if (selectedTab != null) {
            Node content = selectedTab.getContent();
            if (content instanceof ShipTablePane) {
                ((ShipTablePane) content).update();
            }
        }
    }

    @Override
    protected void onWindowHidden(WindowEvent e) {
        if (this.timeline != null) {
            this.timeline.stop();
        }
    }
}
