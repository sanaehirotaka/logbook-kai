package logbook.internal.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import logbook.internal.LogWriter;

/**
 * TableViewに関係するメソッドを集めたクラス
 *
 */
class TableTool {

    private static final String SEPARATOR = "\t"; //$NON-NLS-1$

    private static final String NL = "\n"; //$NON-NLS-1$

    private static final String CSV_SEPARATOR = ","; //$NON-NLS-1$

    private static final String CSV_NL = "\r\n"; //$NON-NLS-1$

    /**
     * 行をヘッダ付きで文字列にします
     *
     * @param table テーブル
     * @param rows 行
     * @return ヘッダ付きの文字列
     */
    static <T> String toString(TableView<?> table, List<?> rows) {
        String body = rows.stream()
                .map(Object::toString)
                .collect(Collectors.joining(NL));
        return new StringJoiner(NL).add(tableHeader(table, SEPARATOR))
                .add(body)
                .toString();
    }

    /**
     * 選択行をヘッダ付きで文字列にします
     *
     * @param table テーブル
     * @return ヘッダ付きの文字列
     */
    static String selectionToString(TableView<?> table) {
        return toString(table, table.getSelectionModel()
                .getSelectedItems());
    }

    /**
     * 選択行をヘッダ付きでクリップボードにコピーします
     *
     * @param table テーブル
     */
    static void selectionCopy(TableView<?> table) {
        ClipboardContent content = new ClipboardContent();
        content.putString(selectionToString(table));
        Clipboard.getSystemClipboard()
                .setContent(content);
    }

    /**
     * テーブルの行をすべて選択します
     *
     * @param table テーブル
     */
    static void selectAll(TableView<?> table) {
        // Selection All
        table.getSelectionModel()
                .selectAll();
    }

    /**
     * キーボードイベントのハンドラー(Ctrl+Cを実装)
     *
     * @param event キーボードイベント
     */
    static void defaultOnKeyPressedHandler(KeyEvent event) {
        if (event.getSource() instanceof TableView<?>) {
            TableView<?> table = (TableView<?>) event.getSource();
            // Copy
            if (event.isControlDown() && event.getCode() == KeyCode.C) {
                selectionCopy(table);
            }
        }
    }

    /**
     * テーブルの内容をCSVファイルとして出力します
     *
     * @param table テーブル
     * @param title タイトル及びファイル名
     * @param own 親ウインドウ
     */
    static void store(TableView<?> table, String title, Window own) throws IOException {
        String body = table.getItems()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(CSV_NL))
                .replaceAll(SEPARATOR, CSV_SEPARATOR);
        String content = new StringJoiner(CSV_NL)
                .add(tableHeader(table, CSV_SEPARATOR))
                .add(body)
                .toString();

        FileChooser chooser = new FileChooser();
        chooser.setTitle(title + "の保存");
        chooser.setInitialFileName(title + ".csv");
        chooser.getExtensionFilters().addAll(
                new ExtensionFilter("CSV Files", "*.csv"));
        File f = chooser.showSaveDialog(own);
        if (f != null) {
            Files.write(f.toPath(), content.getBytes(LogWriter.DEFAULT_CHARSET));
        }
    }

    private static String tableHeader(TableView<?> table, String separator) {
        return table.getColumns()
                .stream()
                .map(TableColumn::getText)
                .collect(Collectors.joining(separator));
    }
}
