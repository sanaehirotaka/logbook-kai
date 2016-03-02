package logbook.internal.gui;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import logbook.bean.SlotitemDescriptionCollection;

/**
 * 所有装備一覧のUIコントローラー
 *
 */
public class ItemController extends WindowController {

    /** 一覧 */
    @FXML
    private TableView<Item> table;

    /** 名称 */
    @FXML
    private TableColumn<Item, String> name;

    /** 種別 */
    @FXML
    private TableColumn<Item, String> type;

    /** 個数 */
    @FXML
    private TableColumn<Item, Integer> count;

    /** 火力 */
    @FXML
    private TableColumn<Item, Integer> houg;

    /** 命中 */
    @FXML
    private TableColumn<Item, Integer> houm;

    /** 射程 */
    @FXML
    private TableColumn<Item, Integer> leng;

    /** 運 */
    @FXML
    private TableColumn<Item, Integer> luck;

    /** 回避 */
    @FXML
    private TableColumn<Item, Integer> houk;

    /** 爆装 */
    @FXML
    private TableColumn<Item, Integer> baku;

    /** 雷装 */
    @FXML
    private TableColumn<Item, Integer> raig;

    /** 索敵 */
    @FXML
    private TableColumn<Item, Integer> saku;

    /** 対潜 */
    @FXML
    private TableColumn<Item, Integer> tais;

    /** 対空 */
    @FXML
    private TableColumn<Item, Integer> tyku;

    /** 装甲 */
    @FXML
    private TableColumn<Item, Integer> souk;

    /** 詳細・名前 */
    @FXML
    private Label dName;

    @FXML
    void initialize() {

        // カラムとオブジェクトのバインド
        this.name.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.type.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.count.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.houg.setCellValueFactory(new PropertyValueFactory<>("houg"));
        this.houm.setCellValueFactory(new PropertyValueFactory<>("houm"));
        this.leng.setCellValueFactory(new PropertyValueFactory<>("leng"));
        this.luck.setCellValueFactory(new PropertyValueFactory<>("luck"));
        this.houk.setCellValueFactory(new PropertyValueFactory<>("houk"));
        this.baku.setCellValueFactory(new PropertyValueFactory<>("baku"));
        this.raig.setCellValueFactory(new PropertyValueFactory<>("raig"));
        this.saku.setCellValueFactory(new PropertyValueFactory<>("saku"));
        this.tais.setCellValueFactory(new PropertyValueFactory<>("tais"));
        this.tyku.setCellValueFactory(new PropertyValueFactory<>("tyku"));
        this.souk.setCellValueFactory(new PropertyValueFactory<>("souk"));

        // 行を作る
        List<Item> items = SlotitemDescriptionCollection.get()
                .getSlotitemMap()
                .values()
                .stream()
                .map(Item::toItem)
                .filter(e -> e.getCount() > 0)
                .sorted(Comparator.comparing(Item::getName))
                .sorted(Comparator.comparing(Item::getType))
                .collect(Collectors.toList());
        this.table.setItems(FXCollections.observableArrayList(items));

        // 装備が選択された時のリスナーを設定
        this.table.getSelectionModel()
                .selectedItemProperty()
                .addListener(this::detail);
    }

    /**
     * 右ペインに詳細表示するリスナー
     *
     * @param observable 値が変更されたObservableValue
     * @param oldValue 古い値
     * @param value 新しい値
     */
    private void detail(ObservableValue<? extends Item> observable, Item oldValue, Item value) {
        //TODO:実装する
        if (value != null) {
            // 選択
            this.dName.setText(value.getName());
        } else {
            // 未選択
            this.dName.setText("");
        }
    }
}
