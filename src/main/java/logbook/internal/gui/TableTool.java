package logbook.internal.gui;

import java.io.IOException;
import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * TableViewに関係するメソッドを集めたクラス
 *
 */
class TableTool {

    /**
     * 行をヘッダ付きで文字列にします
     *
     * @param table テーブル
     * @param rows 行
     * @return ヘッダ付きの文字列
     */
    static <T> String toString(TableView<?> table, List<?> rows) {
        return Tools.Table.toString(table, rows);
    }

    /**
     * 選択行をヘッダ付きで文字列にします
     *
     * @param table テーブル
     * @return ヘッダ付きの文字列
     */
    static String selectionToString(TableView<?> table) {
        return Tools.Table.selectionToString(table);
    }

    /**
     * 選択行をヘッダ付きでクリップボードにコピーします
     *
     * @param table テーブル
     */
    static void selectionCopy(TableView<?> table) {
        Tools.Table.selectionCopy(table);
    }

    /**
     * テーブルの行をすべて選択します
     *
     * @param table テーブル
     */
    static void selectAll(TableView<?> table) {
        Tools.Table.selectAll(table);
    }

    /**
     * キーボードイベントのハンドラー(Ctrl+Cを実装)
     *
     * @param event キーボードイベント
     */
    static void defaultOnKeyPressedHandler(KeyEvent event) {
        Tools.Table.defaultOnKeyPressedHandler(event);
    }

    /**
     * テーブルの内容をCSVファイルとして出力します
     *
     * @param table テーブル
     * @param title タイトル及びファイル名
     * @param own 親ウインドウ
     */
    static void store(TableView<?> table, String title, Window own) throws IOException {
        Tools.Table.store(table, title, own);
    }

    /**
     * テーブル列の表示・非表示の設定を行う
     * @param table テーブル
     * @param key テーブルのキー名
     * @param window 親ウインドウ
     * @throws IOException 入出力例外が発生した場合
     */
    static void showVisibleSetting(TableView<?> table, String key, Stage window) throws IOException {
        Tools.Table.showVisibleSetting(table, key, window);
    }

    /**
     * テーブル列の表示・非表示の設定を行う
     * @param table テーブル
     * @param key テーブルのキー名
     */
    static void setVisible(TableView<?> table, String key) {
        Tools.Table.setVisible(table, key);
    }

}
