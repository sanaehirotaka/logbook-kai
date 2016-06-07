package logbook.internal.gui;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import logbook.bean.Quest;

/**
 * 所有艦娘一覧のテーブル
 *
 */
public class QuestTablePane extends VBox {

    /** テーブル */
    @FXML
    private TableView<QuestItem> table;

    /** No */
    @FXML
    private TableColumn<QuestItem, Integer> no;

    /** 任務 */
    @FXML
    private TableColumn<QuestItem, Quest> quest;

    /** 遂行 */
    @FXML
    private TableColumn<QuestItem, String> exec;

    /** 期限 */
    @FXML
    private TableColumn<QuestItem, String> type;

    /** 燃料 */
    @FXML
    private TableColumn<QuestItem, Integer> fuel;

    /** 弾薬 */
    @FXML
    private TableColumn<QuestItem, Integer> ammo;

    /** 鋼材 */
    @FXML
    private TableColumn<QuestItem, Integer> metal;

    /** ボーキサイト */
    @FXML
    private TableColumn<QuestItem, Integer> boux;

    /** 任務取得関数 */
    private Supplier<List<Quest>> questSupplier;

    /** 任務 */
    private final ObservableList<QuestItem> questItems = FXCollections.observableArrayList();

    /** 任務一覧のハッシュ・コード */
    private int questHashCode;

    /**
     * 任務一覧のテーブルのコンストラクタ
     *
     * @param shipSupplier 艦娘達
     */
    public QuestTablePane(Supplier<List<Quest>> questSupplier) {
        this.questSupplier = questSupplier;
        try {
            FXMLLoader loader = InternalFXMLLoader.load("logbook/gui/quest_table.fxml");
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
            // カラムとオブジェクトのバインド
            this.no.setCellValueFactory(new PropertyValueFactory<>("no"));
            this.quest.setCellValueFactory(new PropertyValueFactory<>("title"));
//            this.quest.setCellFactory(p -> new QuestImageCell());
            this.exec.setCellValueFactory(new PropertyValueFactory<>("state"));
            this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
            this.fuel.setCellValueFactory(new PropertyValueFactory<>("fuel"));
            this.ammo.setCellValueFactory(new PropertyValueFactory<>("ammo"));
            this.metal.setCellValueFactory(new PropertyValueFactory<>("metal"));
            this.boux.setCellValueFactory(new PropertyValueFactory<>("boux"));

            this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.table.setItems(this.questItems);
            this.table.setOnKeyPressed(TableTool::defaultOnKeyPressedHandler);
            this.update();

        } catch (Exception e) {
            LoggerHolder.LOG.error("FXMLの初期化に失敗しました", e);
        }
    }

    /**
     * 画面を更新する
     *
     */
    public void update() {
        try {
            List<Quest> quest = this.questSupplier.get();

            if (this.questHashCode != quest.hashCode()) {
                // ハッシュ・コードが変わっている場合画面の更新
                this.questHashCode = quest.hashCode();

                this.questItems.clear();
                this.questItems.addAll(quest.stream()
                        .map(QuestItem::new)
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerHolder.LOG.error("画面の更新に失敗しました", e);
        }
    }

    /**
     * 任務画像のセル
     *
     */
    private static class QuestImageCell extends TableCell<QuestItem, Quest> {
        @Override
        protected void updateItem(Quest quest, boolean empty) {
            super.updateItem(quest, empty);

            if (!empty) {
//                this.setGraphic(new ImageView(Ships.shipWithItemImage(ship)));
                this.setText(quest.getTitle());
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }
    }

    private static class LoggerHolder {
        /** ロガー */
        private static final Logger LOG = LogManager.getLogger(QuestTablePane.class);
    }
}
