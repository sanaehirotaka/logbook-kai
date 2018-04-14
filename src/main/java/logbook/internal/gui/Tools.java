package logbook.internal.gui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.controlsfx.control.Notifications;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logbook.bean.AppConfig;
import logbook.bean.WindowLocation;
import logbook.internal.log.LogWriter;
import logbook.plugin.PluginServices;

/**
 * JavaFx Tools
 *
 */
public class Tools {

    /**
     * window
     *
     */
    public static class Windows {

        /**
         * ウインドウの設定
         * @param stage Stage
         * @throws IOException 入出力例外が発生した場合
         */
        public static void setIcon(Stage stage) throws IOException {
            // アイコン
            String[] uris = {
                    "logbook/gui/icon_256x256.png",
                    "logbook/gui/icon_128x128.png",
                    "logbook/gui/icon_64x64.png",
                    "logbook/gui/icon_48x48.png",
                    "logbook/gui/icon_32x32.png",
                    "logbook/gui/icon_16x16.png" };

            for (String uri : uris) {
                try (InputStream is = PluginServices.getResourceAsStream(uri)) {
                    stage.getIcons().add(new Image(is));
                }
            }
        }

        /**
         * デフォルトの閉じるアクション
         * @param controller WindowController
         */
        public static void defaultCloseAction(WindowController controller) {
            if (controller.getWindow() != null) {
                EventHandler<WindowEvent> action = e -> {
                    String key = controller.getClass().getCanonicalName();
                    AppConfig.get()
                            .getWindowLocationMap()
                            .put(key, controller.getWindowLocation());
                };
                controller.getWindow()
                        .addEventHandler(WindowEvent.WINDOW_HIDDEN, action);
            }
        }

        /**
         * デフォルトのウインドウ設定
         * @param controller WindowController
         */
        public static void defaultOpenAction(WindowController controller) {
            String key = controller.getClass().getCanonicalName();
            WindowLocation location = AppConfig.get()
                    .getWindowLocationMap()
                    .get(key);
            controller.setWindowLocation(location);
        }
    }

    /**
     * misc
     *
     */
    public static class Conrtols {

        /**
         * ノードの内容をPNGファイルとして出力します
         *
         * @param node ノード
         * @param title タイトル及びファイル名
         * @param own 親ウインドウ
         */
        public static void storeSnapshot(Node node, String title, Window own) {
            try {
                FileChooser chooser = new FileChooser();
                chooser.setTitle(title + "の保存");
                chooser.setInitialFileName(title + ".png");
                chooser.getExtensionFilters().addAll(
                        new ExtensionFilter("PNG Files", "*.png"));
                File f = chooser.showSaveDialog(own);
                if (f != null) {
                    SnapshotParameters sp = new SnapshotParameters();
                    sp.setFill(Color.WHITESMOKE);
                    WritableImage image = node.snapshot(sp, null);
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
         * @return ボタンタイプ
         */
        public static Optional<ButtonType> alert(AlertType type, String title, String message, Window own) {
            return alert(type, title, message, (Node) null, own);
        }

        /**
         * ダイアログを表示する(スタックトレース付)
         *
         * @param type ダイアログタイプ
         * @param title メッセージタイトル
         * @param message タイトル
         * @param t 例外
         * @param own 親ウインドウ
         * @return ボタンタイプ
         */
        public static Optional<ButtonType> alert(AlertType type, String title, String message, Throwable t,
                Window own) {
            StringWriter w = new StringWriter();
            t.printStackTrace(new PrintWriter(w));
            String stackTrace = w.toString();
            TextArea textArea = new TextArea(stackTrace);

            return alert(type, title, message, textArea, own);
        }

        /**
         * ダイアログを表示する
         *
         * @param type ダイアログタイプ
         * @param title メッセージタイトル
         * @param message タイトル
         * @param content コンテンツ
         * @param own 親ウインドウ
         * @return ボタンタイプ
         */
        public static Optional<ButtonType> alert(AlertType type, String title, String message, Node content,
                Window own) {
            Alert alert = new Alert(type);
            alert.getDialogPane().getStylesheets().add("logbook/gui/application.css");
            alert.initOwner(own);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.getDialogPane().setExpandableContent(content);
            return alert.showAndWait();
        }

        /**
         * 通知を表示する
         *
         * @param node グラフィック
         * @param title タイトル
         * @param message メッセージ
         */
        public static void showNotify(Node node, String title, String message) {
            showNotify(node, title, message, Duration.seconds(5));
        }

        /**
         * 通知を表示する
         *
         * @param node グラフィック
         * @param title タイトル
         * @param message メッセージ
         * @param hide 消えるまでの秒数
         */
        public static void showNotify(Node node, String title, String message, Duration hide) {
            Notifications notifications = Notifications.create()
                    .graphic(node)
                    .title(title)
                    .text(message)
                    .hideAfter(hide)
                    .position(Pos.BOTTOM_RIGHT);
            if (node == null) {
                notifications.showInformation();
            } else {
                notifications.show();
            }
        }

        /**
         * 画像の拡大・縮小を行う
         * @param view 拡大・縮小を行う画像を保持した {@link ImageView}
         * @return 設定に従った拡大・縮小を行った画像を保持した {@link ImageView}
         */
        public static ImageView zoomImage(ImageView view) {
            int percent = AppConfig.get().getImageZoomRate();
            if (percent != 100 && percent > 0) {
                double rate = (double) percent / 100;
                view.setFitHeight(view.getImage().getHeight() * rate);
                view.setFitWidth(view.getImage().getWidth() * rate);
            }
            return view;
        }

    }

    /**
     * TableViewに関係するメソッドを集めたクラス
     *
     */
    public static class Tables {

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
        public static <T> String toString(TableView<?> table, List<?> rows) {
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
        public static String selectionToString(TableView<?> table) {
            return toString(table, table.getSelectionModel()
                    .getSelectedItems());
        }

        /**
         * 選択行をヘッダ付きでクリップボードにコピーします
         *
         * @param table テーブル
         */
        public static void selectionCopy(TableView<?> table) {
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
        public static void selectAll(TableView<?> table) {
            // Selection All
            table.getSelectionModel()
                    .selectAll();
        }

        /**
         * キーボードイベントのハンドラー(Ctrl+Cを実装)
         *
         * @param event キーボードイベント
         */
        public static void defaultOnKeyPressedHandler(KeyEvent event) {
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
        public static void store(TableView<?> table, String title, Window own) throws IOException {
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
        public static void showVisibleSetting(TableView<?> table, String key, Stage window) throws IOException {
            InternalFXMLLoader.showWindow("logbook/gui/column_visible.fxml", window, "テーブル列の表示・非表示", w -> {
                ((ColumnVisibleController) w).setData(table, key);
            }, null);
        }

        /**
         * テーブル列の表示・非表示の設定を行う
         * @param table テーブル
         * @param key テーブルのキー名
         */
        public static void setVisible(TableView<?> table, String key) {
            Set<String> setting = AppConfig.get()
                    .getColumnVisibleMap()
                    .get(key);
            if (setting != null) {
                getColumns(table).forEach(column -> {
                    if (setting.contains(getColumnName(column))) {
                        column.setVisible(false);
                    }
                });
            }
        }

        /**
         * テーブル列の幅の設定を行う
         * @param table テーブル
         * @param key テーブルのキー名
         */
        public static void setWidth(TableView<?> table, String key) {
            Map<String, Double> setting = AppConfig.get()
                    .getColumnWidthMap()
                    .get(key);
            if (setting != null) {
                // 初期設定
                getColumns(table).forEach(column -> {
                    Double width = setting.get(getColumnName(column));
                    if (width != null) {
                        column.setPrefWidth(width);
                    }
                });
            }
            // 幅が変更された時に設定を保存する
            getColumns(table).forEach(column -> {
                column.widthProperty().addListener((ob, o, n) -> {
                    Map<String, Double> map = AppConfig.get()
                            .getColumnWidthMap()
                            .computeIfAbsent(key, e -> new HashMap<>());
                    map.put(getColumnName(column), n.doubleValue());
                });
            });
        }

        /**
         * テーブルソート列の設定を行う
         * @param table テーブル
         * @param key テーブルのキー名
         */
        public static <S> void setSortOrder(TableView<S> table, String key) {
            Map<String, String> setting = AppConfig.get()
                    .getColumnSortOrderMap()
                    .get(key);
            ObservableList<TableColumn<S, ?>> sortOrder = table.getSortOrder();
            if (setting != null) {
                // 初期設定
                Map<String, TableColumn<S, ?>> columnsMap = getColumns(table)
                        .collect(Collectors.toMap(Tables::getColumnName, c -> c, (c1, c2) -> c1));
                setting.forEach((k, v) -> {
                    Optional.ofNullable(columnsMap.get(k)).ifPresent(col -> {
                        sortOrder.add(col);
                        col.setSortType(SortType.valueOf(v));
                    });
                });
            }
            // ソート列またはソートタイプが変更された時に設定を保存する
            sortOrder.addListener((ListChangeListener<TableColumn<S, ?>>) e -> storeSortOrder(table, key));
            getColumns(table).forEach(col -> {
                col.sortTypeProperty().addListener((ob, o, n) -> storeSortOrder(table, key));
            });
        }

        /**
         * TableColumnの名前を取得する
         * @param column TableColumn
         * @return TableColumnの名前
         */
        public static String getColumnName(TableColumn<?, ?> column) {
            LinkedList<String> names = null;
            TableColumnBase<?, ?> parent = column;
            while ((parent = parent.getParentColumn()) != null) {
                if (names == null) {
                    names = new LinkedList<>();
                }
                names.addFirst(parent.getText());
            }
            if (names != null) {
                return names.stream().collect(Collectors.joining(".")) + "." + column.getText();
            } else {
                return column.getText();
            }
        }

        /**
         * TableViewからTableColumnをストリームとして取得する
         * @param table TableView
         * @return TableColumn
         */
        public static <S> Stream<TableColumn<S, ?>> getColumns(TableView<S> table) {
            return table.getColumns()
                    .stream()
                    .flatMap(Tables::flatColumns);
        }

        private static <S> void storeSortOrder(TableView<S> table, String key) {
            ObservableList<TableColumn<S, ?>> sortOrder = table.getSortOrder();
            Map<String, String> setting = AppConfig.get()
                    .getColumnSortOrderMap()
                    .computeIfAbsent(key, e1 -> new LinkedHashMap<>());
            setting.clear();
            sortOrder.stream().forEach(col -> setting.put(getColumnName(col), col.getSortType().name()));
        }

        private static String tableHeader(TableView<?> table, String separator) {
            return getColumns(table)
                    .map(Tables::getColumnName)
                    .filter(name -> !name.equals("行番号"))
                    .collect(Collectors.joining(separator));
        }

        /**
         * ネスとされたTableColumnの終端になるTableColumnを取得する
         * @param column TableColumn
         * @return TableColumnのストリーム
         */
        private static <S> Stream<TableColumn<S, ?>> flatColumns(TableColumn<S, ?> column) {
            if (!column.getColumns().isEmpty()) {
                return column.getColumns().stream()
                        .flatMap(Tables::flatColumns);
            }
            return Stream.of(column);
        }
    }

}
