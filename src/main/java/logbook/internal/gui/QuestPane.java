package logbook.internal.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.stage.Stage;
import logbook.bean.AppQuest;
import logbook.bean.AppQuestCollection;
import logbook.bean.QuestList.Quest;
import logbook.internal.LoggerHolder;
import logbook.internal.ThreadManager;
import logbook.plugin.PluginServices;

/**
 * 任務
 *
 */
public class QuestPane extends HBox {

    private AppQuest quest;

    @FXML
    private Arc progress;

    @FXML
    private Hyperlink name;

    @FXML
    private Hyperlink condition;

    @FXML
    private VBox detailView;

    @FXML
    private VBox infomation;

    @FXML
    private Label detail;

    /**
     * 任務のコンストラクタ
     *
     * @param quest 任務
     */
    public QuestPane(AppQuest quest) {
        this.quest = quest;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/quest.fxml");
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            LoggerHolder.get().error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            // 詳細は初期表示しない
            this.detailView.managedProperty().bind(this.detailView.visibleProperty());
            this.detailView.setVisible(false);

            Quest quest = this.quest.getQuest();

            switch (quest.getCategory()) {
            case 1:
                this.getStyleClass().add("deck");
                break;
            case 2:
            case 8:
            case 9:
                this.getStyleClass().add("sortie");
                break;
            case 3:
                this.getStyleClass().add("practice");
                break;
            case 4:
                this.getStyleClass().add("mission");
                break;
            case 5:
                this.getStyleClass().add("supply");
                break;
            case 6:
                this.getStyleClass().add("kousyou");
                break;
            case 7:
                this.getStyleClass().add("kaisou");
                break;
            default:
                break;
            }
            if (!this.quest.isActive()) {
                this.getStyleClass().add("inactive");
            }
            switch (quest.getProgressFlag()) {
            case 1:
                this.progress.setStartAngle(90);
                this.progress.setRadiusX(5);
                this.progress.setRadiusY(5);
                this.progress.setLength(360 * 0.5);
                break;
            case 2:
                this.progress.setStartAngle(45);
                this.progress.setRadiusX(5);
                this.progress.setRadiusY(5);
                this.progress.setLength(360 * 0.8);
                break;
            default:
                if (quest.getState() == 3) {
                    this.progress.setRadiusX(5);
                    this.progress.setRadiusY(5);
                } else {
                    this.progress.setRadiusX(2);
                    this.progress.setRadiusY(2);
                }
                this.progress.setLength(360);
                break;
            }
            this.name.setText(quest.getTitle());
            this.detail.setText(quest.getDetail().replaceAll("<br>", ""));
            this.setOnContextMenuRequested(this::showContextMenu);

            if (PluginServices.getResource("logbook/quest/" + quest.getNo() + ".json") == null) {
                this.condition.setVisible(false);
            }
        } catch (Exception e) {
            LoggerHolder.get().error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void remove(ActionEvent event) {
        AppQuestCollection.get()
                .getQuest()
                .remove(this.quest.getNo());
    }

    @FXML
    void removeAll(ActionEvent event) {
        AppQuestCollection.get()
                .getQuest()
                .clear();
    }

    @FXML
    void search(ActionEvent event) {
        try {
            ThreadManager.getExecutorService()
                    .submit(() -> {
                        Desktop.getDesktop()
                                .browse(URI.create("https://www.google.co.jp/search?q="
                                        + URLEncoder.encode(this.quest.getQuest().getTitle(), "UTF-8")));
                        return null;
                    });
        } catch (Exception e) {
            LoggerHolder.get().warn("ブラウザを開けませんでした", e);
        }
    }

    @FXML
    void expand(ActionEvent event) {
        boolean expanded = !this.detailView.isVisible();
        this.detailView.setVisible(expanded);
        if (expanded) {
            this.getStyleClass().add("expanded");
        } else {
            this.getStyleClass().remove("expanded");
        }
    }

    @FXML
    void condition(ActionEvent event) {
        try {
            InternalFXMLLoader.showWindow("logbook/gui/quest_progress.fxml", (Stage) this.getScene().getWindow(),
                    this.quest.getQuest().getTitle(), c -> {
                        ((QuestProgress) c).setQuest(this.quest);
                    }, null);
        } catch (IOException e) {
            LoggerHolder.get().warn("進捗を開けませんでした", e);
        }
    }

    private void showContextMenu(ContextMenuEvent event) {
        MenuItem item = new MenuItem("全て除去");
        item.setOnAction(this::removeAll);
        ContextMenu contextMenu = new ContextMenu(item);
        contextMenu.show(this.getScene().getWindow(), event.getScreenX(), event.getScreenY());
    }
}
