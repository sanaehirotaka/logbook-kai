package logbook.internal.gui;

import java.io.InputStream;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
import logbook.internal.LoggerHolder;
import logbook.internal.ToStringConverter;
import logbook.plugin.PluginServices;

/**
 * 遠征確認画面
 *
 */
public class MissionCheck extends WindowController {

    @FXML
    private ChoiceBox<DeckPort> fleet;

    @FXML
    private TreeView<String> conditionTree;

    private ObjectMapper mapper = new ObjectMapper();

    public MissionCheck() {
        this.mapper.configure(Feature.ALLOW_COMMENTS, true);
    }

    @FXML
    void initialize() {
        this.fleet.getSelectionModel().selectedItemProperty().addListener((ChangeListener<DeckPort>) this::buildTree);
        this.fleet.getItems().addAll(DeckPortCollection.get().getDeckPortMap().values());
        this.fleet.getSelectionModel().select(1);
        this.fleet.setConverter(ToStringConverter.of(DeckPort::getName));
    }

    @FXML
    void update(ActionEvent event) {
        this.buildTree(this.fleet.getSelectionModel().getSelectedItem());
    }

    private void buildTree(ObservableValue<? extends DeckPort> observable, DeckPort oldValue, DeckPort newValue) {
        this.buildTree(newValue);
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
            Integer missionId = mission.getId();
            if ("前衛支援任務".equals(mission.getName())) {
                missionId = 33;
            } else if ("艦隊決戦支援任務".equals(mission.getName())) {
                missionId = 34;
            }

            InputStream is = PluginServices.getResourceAsStream("logbook/mission/" + missionId + ".json");
            if (is == null) {
                return null;
            }
            MissionCondition condition;
            TreeItem<String> item;
            try {
                condition = this.mapper.readValue(is, MissionCondition.class);
                condition.test(fleet);
                item = this.buildLeaf(condition);
                item.setValue(mission.getName());
            } finally {
                is.close();
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
