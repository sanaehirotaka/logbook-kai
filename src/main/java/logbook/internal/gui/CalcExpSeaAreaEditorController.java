package logbook.internal.gui;

import java.util.stream.Collectors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import logbook.bean.AppSeaAreaExp;
import logbook.bean.AppSeaAreaExpCollection;
import lombok.Setter;

/**
 * 海域編集
 *
 */
public class CalcExpSeaAreaEditorController extends WindowController {

    @FXML
    private TableView<SeaAreaExpItem> table;

    /** 海域 */
    @FXML
    private TableColumn<SeaAreaExpItem, String> name;

    /** 海域Exp */
    @FXML
    private TableColumn<SeaAreaExpItem, Integer> exp;

    /** 削除ボタン */
    @FXML
    private TableColumn<SeaAreaExpItem, String> cmd;

    @Setter
    private Runnable apply;

    @FXML
    void initialize() {
        // 海域
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.name.setCellFactory(TextFieldTableCell.forTableColumn());
        // 海域Exp
        this.exp.setCellValueFactory(new PropertyValueFactory<>("exp"));
        this.exp.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            public String toString(Integer val) {
                return Integer.toString(val);
            }
        }));
        // 削除ボタン
        this.cmd.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.cmd.setCellFactory(p -> new CmdCell());

        this.table.setItems(AppSeaAreaExpCollection.get()
                .getList()
                .stream()
                .map(SeaAreaExpItem::toItem)
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    @FXML
    void add(ActionEvent event) {
        SeaAreaExpItem item = new SeaAreaExpItem();
        item.setName("新しい海域");
        item.setExp(100);
        this.table.getItems().add(item);
        this.table.getSelectionModel().select(item);
        this.table.scrollTo(item);
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        window.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> {
            AppSeaAreaExpCollection.get()
                    .setList(this.table.getItems().stream()
                            .map(SeaAreaExpItem::toBean)
                            .collect(Collectors.toList()));
            this.apply.run();
        });
    }

    /**
     * ボタンのセル
     *
     */
    static class CmdCell extends TableCell<SeaAreaExpItem, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (!empty) {
                Button button = new Button("除去");
                button.setOnAction(this::handle);
                this.setGraphic(button);
                this.setText(null);
            } else {
                this.setGraphic(null);
                this.setText(null);
            }
        }

        private void handle(ActionEvent e) {
            this.getTableView().getItems().remove(this.getTableRow().getItem());
        }
    }

    /**
     * テーブル内容
     */
    public static class SeaAreaExpItem {

        /** 海域 */
        private StringProperty name = new SimpleStringProperty();

        /** 海域Exp */
        private IntegerProperty exp = new SimpleIntegerProperty();

        /**
         * 海域を取得します。
         * @return 海域
         */
        public StringProperty nameProperty() {
            return this.name;
        }

        /**
         * 海域を取得します。
         * @return 海域
         */
        public String getName() {
            return this.name.get();
        }

        /**
         * 海域を設定します。
         * @param name 海域
         */
        public void setName(String name) {
            this.name.set(name);
        }

        /**
         * 海域Expを取得します。
         * @return 海域Exp
         */
        public IntegerProperty expProperty() {
            return this.exp;
        }

        /**
         * 海域Expを取得します。
         * @return 海域Exp
         */
        public Integer getExp() {
            return this.exp.get();
        }

        /**
         * 海域Expを設定します。
         * @param exp 海域Exp
         */
        public void setExp(Integer exp) {
            this.exp.set(exp);
        }

        public static SeaAreaExpItem toItem(AppSeaAreaExp bean) {
            SeaAreaExpItem item = new SeaAreaExpItem();
            item.setName(bean.getName());
            item.setExp(bean.getExp());
            return item;
        }

        public AppSeaAreaExp toBean() {
            return new AppSeaAreaExp(this.getName(), this.getExp());
        }
    }
}
