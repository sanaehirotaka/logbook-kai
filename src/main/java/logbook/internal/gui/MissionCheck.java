package logbook.internal.gui;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.controlsfx.control.SegmentedButton;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logbook.bean.DeckPort;
import logbook.bean.DeckPortCollection;
import logbook.bean.Maparea;
import logbook.bean.MapareaCollection;
import logbook.bean.Mission;
import logbook.bean.MissionCollection;
import logbook.bean.MissionCondition;
import logbook.bean.Ship;
import logbook.bean.ShipCollection;
import logbook.bean.Stype;
import logbook.bean.StypeCollection;
import logbook.internal.LoggerHolder;
import logbook.internal.Missions;

/**
 * 遠征確認画面
 *
 */
public class MissionCheck extends WindowController {

    @FXML
    private SegmentedButton fleet;

    @FXML
    private TreeView<String> conditionTree;

    private ObjectMapper mapper = new ObjectMapper();

    private Set<Mission> expanded = new HashSet<>();

    public MissionCheck() {
        this.mapper.configure(Feature.ALLOW_COMMENTS, true);
    }

    @FXML
    void initialize() {
        for (DeckPort deck : DeckPortCollection.get().getDeckPortMap().values()) {
            ToggleButton button = new ToggleButton(deck.getName());
            button.setUserData(deck);
            this.fleet.getButtons().add(button);
        }
        this.fleet.getToggleGroup().selectedToggleProperty().addListener((ob, o, n) -> {
            DeckPort deck = null;
            if (n != null) {
                deck = (DeckPort) n.getUserData();
            }
            this.buildTree(deck);
        });
        this.fleet.getButtons().stream()
                .skip(1)
                .findFirst()
                .ifPresent(b -> b.setSelected(true));
    }

    @FXML
    void update(ActionEvent event) {
        Toggle toggle = this.fleet.getToggleGroup().getSelectedToggle();
        DeckPort deck = null;
        if (toggle != null) {
            deck = (DeckPort) toggle.getUserData();
        }
        this.buildTree(deck);
    }

    private void buildTree(DeckPort deck) {
        TreeItem<String> root = new TreeItem<>();
        if (deck != null) {
            Map<Integer, Ship> shipMap = ShipCollection.get()
                    .getShipMap();
            List<Ship> fleet = DeckPortCollection.get().getDeckPortMap().get(deck.getId()).getShip()
                    .stream()
                    .map(shipMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            Map<Integer, List<Mission>> missionMap = MissionCollection.get().getMissionMap().values().stream()
                    .sorted(Comparator.comparing(Mission::getMapareaId, Comparator.nullsLast(Comparator.naturalOrder()))
                            .thenComparing(Mission::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.groupingBy(Mission::getMapareaId, LinkedHashMap::new, Collectors.toList()));

            for (Map.Entry<Integer, List<Mission>> missionEntry : missionMap.entrySet()) {
                TreeItem<String> subTree = new TreeItem<>();
                subTree.setExpanded(true);

                List<Mission> missions = missionEntry.getValue();
                Integer mapareaId = missions.get(0).getMapareaId();

                String area = Optional.ofNullable(MapareaCollection.get().getMaparea().get(mapareaId))
                        .filter(map -> map.getId() != null && map.getId() <= 40)
                        .map(Maparea::getName)
                        .orElse("イベント海域");
                subTree.setValue(area);

                for (Mission mission : missions) {

                    TreeItem<String> sub = this.buildTree0(mission, fleet);
                    if (sub != null) {
                        sub.setExpanded(this.expanded.contains(mission));
                        sub.expandedProperty().addListener((ob, ov, nv) -> {
                            if (nv != null && nv) {
                                this.expanded.add(mission);
                            } else {
                                this.expanded.remove(mission);
                            }
                        });
                        subTree.getChildren().add(sub);
                    }
                }

                if (!subTree.getChildren().isEmpty()) {
                    root.getChildren().add(subTree);
                }
            }
        }
        this.conditionTree.setRoot(root);
    }

    private TreeItem<String> buildTree0(Mission mission, List<Ship> fleet) {
        try {
            TreeItem<String> item;
            Optional<MissionCondition> condition = Missions.getMissionCondition(mission.getId());
            if (condition.isPresent()) {
                MissionCondition cond = condition.get();
                cond.test(fleet);
                item = this.buildLeaf(cond);
            } else if (mission.getSampleFleet() != null) {
                item = new TreeItem<>();
                setIcon(item, null);
            } else {
                return null;
            }
            item.setValue(mission.toString());
            if (mission.getSampleFleet() != null) {
                TreeItem<String> sample = new TreeItem<>("サンプル編成");

                GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

                StackPane pane = new StackPane();
                pane.setPrefWidth(18);
                pane.getChildren().add(fontAwesome.create(FontAwesome.Glyph.INFO));
                sample.setGraphic(pane);

                for (Integer type : mission.getSampleFleet()) {
                    Optional.ofNullable(StypeCollection.get()
                            .getStypeMap()
                            .get(type))
                            .map(Stype::getName)
                            .ifPresent(name -> sample.getChildren().add(new TreeItem<>(name)));
                }
                item.getChildren().add(sample);
            }
            return item;
        } catch (Exception e) {
            LoggerHolder.get().error("遠征確認画面で例外", e);
        }
        return null;
    }

    private TreeItem<String> buildLeaf(MissionCondition condition) {
        TreeItem<String> item = new TreeItem<>(condition.toString());
        this.setIcon(item, condition.getResult());
        if (condition.getConditions() != null) {
            if (condition.getConditions().size() == 1 && !condition.getOperator().startsWith("N")) {
                item.setValue(condition.getConditions().get(0).toString());
            } else {
                for (MissionCondition subcondition : condition.getConditions()) {
                    item.getChildren().add(this.buildLeaf(subcondition));
                }
            }
        }
        return item;
    }

    private void setIcon(TreeItem<String> item, Boolean result) {
        GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");

        StackPane pane = new StackPane();
        pane.setPrefWidth(18);

        if (result != null) {
            if (result) {
                pane.getChildren().add(fontAwesome.create(FontAwesome.Glyph.CHECK).color(Color.GREEN));
            } else {
                pane.getChildren().add(fontAwesome.create(FontAwesome.Glyph.EXCLAMATION).color(Color.RED));
            }
        } else {
            pane.getChildren().add(fontAwesome.create(FontAwesome.Glyph.QUESTION).color(Color.GRAY));
        }
        item.setGraphic(pane);
    }
}
