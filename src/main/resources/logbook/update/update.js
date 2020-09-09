/*
 * 航海日誌 更新スクリプト
 */
load("fx:base.js");
load("fx:controls.js");
load("fx:graphics.js");
load("fx:web.js");
// import
var System = java.lang.System;
var Boolean = java.lang.Boolean;
var BufferedReader = java.io.BufferedReader;
var InputStream = java.io.InputStream;
var InputStreamReader = java.io.InputStreamReader;
var URI = java.net.URI;
var FileChannel = java.nio.channels.FileChannel;
var Files = java.nio.file.Files;
var Paths = java.nio.file.Paths;
var StandardCopyOption = java.nio.file.StandardCopyOption;
var StandardOpenOption = java.nio.file.StandardOpenOption;
var StandardCharsets = java.nio.charset.StandardCharsets;
var Collectors = java.util.stream.Collectors;
var ZipFile = java.util.zip.ZipFile;
var EventListener = org.w3c.dom.events.EventListener;

// 更新スクリプト
var scriptFile = System.getProperty("update_script");
// 航海日誌ディレクトリ
var targetDir = System.getProperty("install_target");
// インストールバージョン
var version = System.getProperty("install_version");
// Prerelease を使うかどうか
var usePrerelease = System.getProperty("use_prerelease");
// GitHub Releases API
var releaseURL = "https://api.github.com/repos/Sdk0815/logbook-kai/releases/tags/v" + version;
var release = {};
var asset = {};

/**
 * 更新のバックグラウンドタスク
 */
function UpdateTask() {
    this.threads = [];
}
UpdateTask.prototype.start = function(task) {
    var thread = new java.lang.Thread(task);
    thread.start();
    this.threads.push(thread);
}
UpdateTask.prototype.cancel = function() {
    this.threads.forEach(function(t) {
        t.interrupt();
    });
}
UpdateTask.prototype.getReleaseJson = function() {
    var _this = this;
    var task = new Task() {
        call: function() {
            var is = URI.create(releaseURL).toURL().openStream();
            var jsonText;
            try {
                jsonText = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
            } finally {
                is.close();
            }
            release = JSON.parse(jsonText);

            if (release["assets"]) {
                if ((!Boolean.parseBoolean(usePrerelease) && release["prerelease"]) || release["draft"]) {
                    var msg = release["name"] + "は試験的なバージョンであるため更新されませんでした。手動での更新をお願いします。";
                    throw msg;
                }
                for (var i = 0; i < release["assets"].length; i++) {
                    var name = release["assets"][i]["name"];
                    var prefix = "11".equals(System.getProperty("target_java_version")) ? "logbook-kai-java11_" : "logbook-kai_";
                    if (name.startsWith(prefix) && name.endsWith(".zip")) {
                        asset = release["assets"][i];
                        break;
                    }
                }
            }
            if (!asset) {
                throw "リリース情報はありません";
            }
        }
    }
    return task;
}
UpdateTask.prototype.updatecheck = function() {
    var _this = this;
    var task = new Task() {
        call: function() {
            var tempDir = Files.createTempDirectory("logbook-kai");
            var tempZip = tempDir.resolve(asset["name"]);

            for (var i = 0; i < 3; i++) {
                var downloadUrl = asset["browser_download_url"];
                task.updateMessage(downloadUrl + " をダウンロードしています");

                var is = URI.create(downloadUrl).toURL().openStream();
                try {
                    Files.copy(is, tempZip, StandardCopyOption.REPLACE_EXISTING);
                    if (Files.size(tempZip) == asset["size"])
                        break;

                    task.updateMessage("ファイルサイズが一致しませんでした。再ダウンロードします(最大3回)");
                } finally {
                    is.close();
                }
            }
            task.updateMessage("ファイルを更新しています");

            var files = "";
            var zipFile = new ZipFile(tempZip.toFile());
            try {
                zipFile.stream()
                    .filter(function (e) {
                        return !e.isDirectory();
                    })
                    .filter(function (e) {
                        return e.getName().endsWith(".jar");
                    })
                    .forEach(function (e) {
                        var to = Paths.get(targetDir, e.getName());
                        if (to.getParent()) {
                            if (!Files.exists(to.getParent())) {
                                Files.createDirectories(to.getParent());
                            }
                        }
                        task.updateMessage("[更新]" + to.toAbsolutePath());
                        Files.copy(zipFile.getInputStream(e), to, StandardCopyOption.REPLACE_EXISTING);
                    });
            } finally {
                zipFile.close();
                task.updateMessage("一時ファイルを削除しています");
                Files.deleteIfExists(tempZip);
                Files.deleteIfExists(tempDir);
            }
            return "更新が完了しました";
        }
    }
    return task;
}

/**
 * 更新のUI制御
 */
function UpdateUI(stage) {
    var _this = this;
    this.stage = stage;
    this.task = new UpdateTask();

    this.stage.title = "航海日誌の更新"
    this.stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, function() {
        try {
            // 全てのタスクをキャンセル
            _this.task.cancel();
            // 更新スクリプトの削除
            Files.deleteIfExists(Paths.get(scriptFile));
        } catch (e) {
        }
    });
}
UpdateUI.prototype.show = function() {
    this.stage.show();
}
UpdateUI.prototype.close = function() {
    this.stage.close();
}
UpdateUI.prototype.display = function(pane) {
    this.stage.setScene(new Scene(pane));
}

UpdateUI.prototype.startup = function() {
    var _this = this;

    var pane = new StackPane();
    pane.setPrefHeight(600);
    pane.setPrefWidth(700);
    pane.setPadding(new Insets(6));
    var vbox = new VBox();
    var label = new TextFlow(new Text("航海日誌 v" + version + "への更新を行います。\n"),
            new Text("必ず更新の前に航海日誌を終了させてください。 \n"),
            new Text("更新の準備が出来ましたら[更新]を押して更新を行ってください。"));
    label.setStyle("-fx-font-weight: bold");
    vbox.getChildren().add(label);
    var stackPane1 = new StackPane();
    VBox.setVgrow(stackPane1, Priority.ALWAYS);

    var webView = new WebView();
    var webEngine = webView.engine;
    webEngine.loadContent("<html>"
            + "<head>"
            + "<script src='https://cdnjs.cloudflare.com/ajax/libs/marked/0.3.6/marked.min.js'></script>"
            + "<link href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' rel='stylesheet'>"
            + "<style>body { font-family: 'Meiryo UI',Meiryo,'Segoe UI','Lucida Grande',Verdana,Arial,Helvetica,sans-serif; }</style>"
            + "</head>"
            + "<body class='container'></body>"
            + "</html>");
    stackPane1.getChildren().add(webView);

    var stackPane2 = new StackPane();
    var vbox2 = new VBox();
    vbox2.alignment = Pos.CENTER;
    var check = new CheckBox("航海日誌を終了しました");
    var button = new Button("更新");
    button.disable = true;
    check.onAction = function() {
        if (check.isSelected()) {
            button.disable = false;
        } else {
            button.disable = true;
        }
    }
    button.onAction = function() {
        _this.update();
    }
    vbox2.getChildren().addAll(check, button);
    stackPane2.getChildren().add(vbox2);

    vbox.getChildren().addAll(stackPane1, stackPane2);
    pane.getChildren().add(vbox);

    var linkListener = new EventListener() {
        handleEvent: function (e) {
            java.awt.Desktop.getDesktop()
                    .browse(URI.create(e.getTarget().getAttribute("href")));
            e.preventDefault();
        }
    }
    var releaseNote = function() {
        if (webEngine.executeScript("window.marked") && release.body) {
            var body = webEngine.executeScript("document.body");
            var window = webEngine.executeScript("window");
            body.setMember("innerHTML", window.call("marked", release.body));

            var nodes = webEngine.executeScript("document.getElementsByTagName('a')");
            for (var i = 0; i < nodes.length; i++) {
                nodes[i].addEventListener("click", linkListener, false);
            }
        }
    }

    webEngine.getLoadWorker().stateProperty().addListener(
        new ChangeListener() {
            changed: function (ov, oldState, newState) {
                if (newState === Worker.State.SUCCEEDED) {
                    releaseNote();
                }
            }
        }
    );
    // task
    {
        var task = this.task.getReleaseJson();
        task.onFailed = function() {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(_this.stage);
            alert.setTitle("更新情報を取得できませんでした");
            alert.setContentText(task.exception.message);
            alert.showAndWait();

           _this.failed();
       }
       task.onSucceeded = function() {
           releaseNote();
       }
       this.task.start(task);
    }

    this.display(pane);
}
UpdateUI.prototype.update = function() {
    var _this = this;
    var pane = new StackPane();
    pane.setPrefHeight(200);
    pane.setPrefWidth(700);
    pane.setPadding(new Insets(6));
    var vbox = new VBox();
    var label = new Label("更新を確認しています");
    var list = new ListView();
    list.setFocusTraversable(false);

    vbox.getChildren().addAll(label, list);
    pane.getChildren().add(vbox);

    {
        var task = this.task.updatecheck();
        task.messageProperty().addListener(new ChangeListener() {
            changed: function(ob, o, n) {
                list.getItems().add(n);
            }
        });
        task.onFailed = function() {
             var alert = new Alert(Alert.AlertType.INFORMATION);
             alert.initOwner(_this.stage);
             alert.setTitle("更新が出来ませんでした");
             alert.setContentText(task.exception.message);
             alert.showAndWait();

            _this.failed();
        }
        task.onSucceeded = function() {
            var alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(_this.stage);
            alert.setTitle("完了しました");
            alert.setContentText(task.value);
            alert.showAndWait();

            _this.succeeded();
        }

        this.task.start(task);
    }

    this.display(pane);
}
UpdateUI.prototype.succeeded = function() {
    var _this = this;

    var pane = new StackPane();
    pane.setPrefHeight(200);
    pane.setPrefWidth(700);
    pane.setPadding(new Insets(6));
    var vbox = new VBox();
    var stackPane1 = new StackPane();
    VBox.setVgrow(stackPane1, Priority.ALWAYS);
    var label = new Label("航海日誌は v" + version + "へ更新されました");
    label.setWrapText(true);
    label.setStyle("-fx-font-weight: bold;-fx-text-fill: green");
    stackPane1.getChildren().add(label);

    var stackPane2 = new StackPane();
    var button = new Button("閉じる");
    button.onAction = function() {
        _this.close();
    }
    stackPane2.getChildren().add(button);

    vbox.getChildren().addAll(stackPane1, stackPane2);
    pane.getChildren().add(vbox);

    this.display(pane);
}
UpdateUI.prototype.failed = function() {
    var _this = this;

    var pane = new StackPane();
    pane.setPrefHeight(200);
    pane.setPrefWidth(700);
    pane.setPadding(new Insets(6));
    var vbox = new VBox();
    var stackPane1 = new StackPane();
    VBox.setVgrow(stackPane1, Priority.ALWAYS);
    var label = new Label("航海日誌の更新が中断されました。");
    label.setWrapText(true);
    label.setStyle("-fx-font-weight: bold");
    stackPane1.getChildren().add(label);

    var stackPane2 = new StackPane();
    var button = new Button("閉じる");
    button.onAction = function() {
        _this.close();
    }
    stackPane2.getChildren().add(button);

    vbox.getChildren().addAll(stackPane1, stackPane2);
    pane.getChildren().add(vbox);

    this.display(pane);
}

var ui = new UpdateUI($STAGE);
ui.startup();
ui.show();
