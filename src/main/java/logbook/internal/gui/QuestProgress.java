package logbook.internal.gui;

import java.io.InputStream;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import logbook.bean.AppQuest;
import logbook.bean.AppQuestCondition;
import logbook.bean.AppQuestCondition.Condition;
import logbook.internal.LoggerHolder;
import logbook.internal.QuestCollect;
import logbook.plugin.PluginServices;

/**
 * 任務進捗確認
 *
 */
public class QuestProgress extends WindowController {

    @FXML
    private Label name;

    @FXML
    private Label info;

    @FXML
    private TreeView<String> condition;

    void setQuest(AppQuest quest) {
        this.name.setText(quest.getQuest().getTitle());
        this.info.setText(quest.getQuest().getDetail().replaceAll("<br>", ""));
        try {
            InputStream is = PluginServices.getResourceAsStream("logbook/quest/" + quest.getNo() + ".json");
            if (is != null) {
                AppQuestCondition condition;
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    condition = mapper.readValue(is, AppQuestCondition.class);
                } finally {
                    is.close();
                }
                QuestCollect collect = QuestCollect.collect(quest, condition);
                condition.test(collect);

                TreeItem<String> root = new TreeItem<>(quest.getQuest().getTitle());

                for (Condition part : condition.getConditions()) {
                    TreeItem<String> leaf = new TreeItem<>(part.toString());
                    this.setIcon(leaf, part.getResult());
                }
                this.setIcon(root, condition.getResult());
                this.condition.setRoot(root);
            }
        } catch (Exception e) {
            LoggerHolder.get().error("任務確認画面で例外", e);
        }
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
