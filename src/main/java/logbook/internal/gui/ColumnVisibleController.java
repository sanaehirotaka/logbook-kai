package logbook.internal.gui;

import java.util.Set;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import logbook.bean.AppConfig;

/**
 * テーブル列の表示・非表示の設定ダイアログを表示する
 *
 */
public class ColumnVisibleController extends WindowController {

    /** リスト */
    @FXML
    private ListView<TableColumn<?, ?>> listView;

    @FXML
    void initialize() {
        this.listView.setCellFactory(
                CheckBoxListCell.forListView(t -> t.visibleProperty(),
                        new StringConverter<TableColumn<?, ?>>() {
                            @Override
                            public String toString(TableColumn<?, ?> table) {
                                return table.getText();
                            }

                            @Override
                            public TableColumn<? extends Object, ?> fromString(String string) {
                                throw new IllegalStateException();
                            }
                        }));
    }

    /**
     * 全てを選択
     */
    @FXML
    void selectAll() {
        this.listView.getItems().forEach(e -> e.setVisible(true));
    }

    /**
     * 全てを非選択
     */
    @FXML
    void deselectAll() {
        this.listView.getItems().forEach(e -> e.setVisible(false));
    }

    /**
     * リストにアイテムを設定する
     *
     * @param table テーブル
     * @param key テーブルのキー名
     */
    public void setData(TableView<?> table, String key) {
        this.listView.getItems().addAll(table.getColumns());
        // 閉じるときに設定を保存する
        this.getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            // 非表示にした列のSet
            Set<String> setting = this.listView.getItems()
                    .stream()
                    .filter(c -> !c.isVisible())
                    .map(TableColumn::getText)
                    .collect(Collectors.toSet());
            if (setting.isEmpty()) {
                AppConfig.get().getColumnVisibleMap().remove(key);
            } else {
                AppConfig.get().getColumnVisibleMap().put(key, setting);
            }
        });
    }
}
