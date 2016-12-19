package logbook.internal.gui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import logbook.bean.AppConfig;
import logbook.internal.log.LogWriter;

/**
 * JavaFx Tools
 *
 */
class Tools {

    /**
     * misc
     *
     */
    static class Conrtol {

        /**
         * ノードの内容をPNGファイルとして出力します
         *
         * @param ndoe ノード
         * @param title タイトル及びファイル名
         * @param own 親ウインドウ
         */
        static void storeSnapshot(Node node, String title, Window own) {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle(title + "の保存");
                chooser.setInitialFileName(title + ".png");
                chooser.getExtensionFilters().addAll(
                        new ExtensionFilter("PNG Files", "*.png"));
                File f = chooser.showSaveDialog(own);
                if (f != null) {
                    WritableImage image = node.snapshot(null, null);
                    try (OutputStream out = Files.newOutputStream(f.toPath())) {
                        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", out);
                    }
                }
            } catch (Exception e) {
                alert(AlertType.WARNING, "画像ファイルに保存出来ませんでした", "指定された場所へ画像ファイルを書き込みできませんでした", e, own);
            }
        }

        /**
         * ダイアログを表示する
         *
         * @param type ダイアログタイプ
         * @param title メッセージタイトル
         * @param message タイトル
         * @param own 親ウインドウ
         */
        static void alert(AlertType type, String title, String message, Window own) {
            alert(type, title, message, (Node) null, own);
        }

        /**
         * ダイアログを表示する(スタックトレース付)
         *
         * @param type ダイアログタイプ
         * @param title メッセージタイトル
         * @param message タイトル
         * @param t 例外
         * @param own 親ウインドウ
         */
        static void alert(AlertType type, String title, String message, Throwable t, Window own) {
            StringWriter w = new StringWriter();
            t.printStackTrace(new PrintWriter(w));
            String stackTrace = w.toString();
            TextArea textArea = new TextArea(stackTrace);

            alert(type, title, message, textArea, own);
        }

        /**
         * ダイアログを表示する
         *
         * @param type ダイアログタイプ
         * @param title メッセージタイトル
         * @param message タイトル
         * @param content コンテンツ
         * @param own 親ウインドウ
         */
        static void alert(AlertType type, String title, String message, Node content, Window own) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(own);
            alert.setTitle(title);
            alert.setHeaderText(title);
            alert.setContentText(message);
            alert.getDialogPane().setExpandableContent(content);
            alert.showAndWait();
        }
    }

    /**
     * TableViewに関係するメソッドを集めたクラス
     *
     */
    static class Table {

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

        /**
         * テーブル列の表示・非表示の設定を行う
         * @param table テーブル
         * @param key テーブルのキー名
         * @param window 親ウインドウ
         * @throws IOException 入出力例外が発生した場合
         */
        static void showVisibleSetting(TableView<?> table, String key, Stage window) throws IOException {
            InternalFXMLLoader.showWindow("logbook/gui/column_visible.fxml", window, "テーブル列の表示・非表示", w -> {
                ((ColumnVisibleController) w).setData(table, key);
            }, null);
        }

        /**
         * テーブル列の表示・非表示の設定を行う
         * @param table テーブル
         * @param key テーブルのキー名
         */
        static void setVisible(TableView<?> table, String key) {
            Set<String> setting = AppConfig.get()
                    .getColumnVisibleMap()
                    .get(key);
            if (setting != null) {
                table.getColumns().forEach(column -> {
                    if (setting.contains(column.getText())) {
                        column.setVisible(false);
                    }
                });
            }
        }

        private static String tableHeader(TableView<?> table, String separator) {
            return table.getColumns()
                    .stream()
                    .map(TableColumn::getText)
                    .collect(Collectors.joining(separator));
        }
    }

}
