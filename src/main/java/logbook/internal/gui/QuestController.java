package logbook.internal.gui;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import logbook.bean.Quest;
import logbook.bean.QuestCollection;

/**
 * 任務一覧のコントローラ
 *
 */
public class QuestController extends WindowController {

    @FXML
    private TabPane tab;

    private Timeline timeline;

    @FXML
    void initialize() {
        try {
            Collection<Quest> quests = QuestCollection.get().getQuestMap().values();

            QuestTablePane allPane = new QuestTablePane(() -> {
                return quests.stream()
                        // TODO ここでのソートは無意味?
                        .sorted(Comparator.comparing(Quest::getNo))
                        .sorted(Comparator.comparing(Quest::getState).reversed())
                        .collect(Collectors.toList());
            });
            this.tab.getTabs().add(new Tab("全て", allPane));
            IntStream.of(1, 2, 3, 6).forEach(idx -> {
                QuestTablePane pane = new QuestTablePane(() -> {
                    return quests.stream()
                            .sorted(Comparator.comparing(Quest::getNo))
                            .sorted(Comparator.comparing(Quest::getState).reversed())
                            .filter(q -> q.getType() == idx)
                            .collect(Collectors.toList());
                });
                this.tab.getTabs().add(new Tab(Quest.getTypeString(idx), pane));
            });

            // TODO 無限生成TimeLine
            this.timeline = new Timeline();
            this.timeline.setCycleCount(Timeline.INDEFINITE);
            this.timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(1),
                    this::update));
            this.timeline.play();

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
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
            if (content instanceof QuestTablePane) {
                ((QuestTablePane) content).update();
            }
        }
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        window.setOnCloseRequest(e -> this.timeline.stop());
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(QuestController.class);
    }
}
