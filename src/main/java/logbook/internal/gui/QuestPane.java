package logbook.internal.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import logbook.bean.AppQuest;
import logbook.bean.AppQuestCollection;
import logbook.bean.QuestList.Quest;

/**
 * 任務
 *
 */
public class QuestPane extends AnchorPane {

    private AppQuest quest;

    @FXML
    private Label type;

    @FXML
    private Label name;

    @FXML
    private Label progress;

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
            LoggerHolder.LOG.error("FXMLのロードに失敗しました", e);
        }
    }

    @FXML
    void initialize() {
        try {
            Quest quest = this.quest.getQuest();

            switch (quest.getCategory()) {
            case 1:
                this.type.setText("編成");
                this.getStyleClass().add("deck");
                break;
            case 2:
                this.type.setText("出撃");
                this.getStyleClass().add("sortie");
                break;
            case 3:
                this.type.setText("演習");
                this.getStyleClass().add("practice");
                break;
            case 4:
                this.type.setText("遠征");
                this.getStyleClass().add("mission");
                break;
            case 5:
                this.type.setText("補給/入渠");
                this.getStyleClass().add("supply");
                break;
            case 6:
                this.type.setText("工廠");
                this.getStyleClass().add("kousyou");
                break;
            case 7:
                this.type.setText("改装");
                this.getStyleClass().add("kaisou");
                break;
            case 8:
                this.type.setText("出撃");
                this.getStyleClass().add("sortie");
                break;
            default:
                this.type.setText("その他");
                break;
            }

            switch (quest.getProgressFlag()) {
            case 1:
                this.progress.setText("50%");
                break;
            case 2:
                this.progress.setText("80%");
                break;
            default:
                break;
            }

            this.name.setText(quest.getTitle());

            // マウスオーバーでのポップアップ
            this.setOnMouseEntered(e -> {
                this.name.setText(this.quest.getQuest().getDetail());
            });
            this.setOnMouseExited(e -> {
                this.name.setText(this.quest.getQuest().getTitle());
            });
        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    @FXML
    void remove(ActionEvent event) {
        AppQuestCollection.get()
                .getQuest()
                .remove(this.quest.getNo());
    }

    @FXML
    void search(ActionEvent event) {
        try {
            Desktop.getDesktop()
                    .browse(URI.create("https://www.google.co.jp/search?q="
                            + URLEncoder.encode(this.quest.getQuest().getTitle(), "UTF-8")));
        } catch (Exception e) {
            LoggerHolder.LOG.warn("ブラウザを開けませんでした", e);
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(QuestPane.class);
    }
}
