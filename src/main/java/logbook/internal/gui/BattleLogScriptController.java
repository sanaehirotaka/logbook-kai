package logbook.internal.gui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jdk.nashorn.api.scripting.JSObject;
import logbook.bean.BattleLog;
import logbook.bean.BattleLogScriptCollection;
import logbook.bean.BattleLogScriptCollection.BattleLogScript;
import logbook.internal.BattleLogs;
import logbook.internal.LoggerHolder;
import logbook.plugin.PluginServices;

/**
 * 高度な集計
 *
 */
public class BattleLogScriptController extends WindowController {

    @FXML
    private ListView<BattleLogScript> list;

    @FXML
    private Button save;

    @FXML
    private TextField name;

    @FXML
    private Button run;

    @FXML
    private VBox editbox;

    @FXML
    private TextArea result;

    private TextEditorPane editor;

    private List<BattleLogDetail> details = Collections.emptyList();

    private BattleLogScript current;

    @FXML
    void initialize() {
        this.editor = new TextEditorPane();
        this.editor.start("javascript");
        this.editbox.getChildren().add(this.editor);
        VBox.setVgrow(this.editor, Priority.ALWAYS);
        this.list.getItems().addAll(BattleLogScriptCollection.get().getScripts());
        this.list.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> {
            this.setCurrent(n);
        });
        this.setCurrent(null);
    }

    @FXML
    void create() {
        BattleLogScript script = new BattleLogScript();
        script.setName("新しい集計");

        StringBuilder sb = new StringBuilder();
        URL sample = PluginServices.getResource("logbook/gui/battlelog_script_sample.js");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(sample.openStream(), StandardCharsets.UTF_8))) {
            int len = 0;
            char[] buf = new char[1024];
            while ((len = reader.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
        } catch (Exception e) {
            LoggerHolder.get().warn("高度な集計のサンプルスクリプトを読み込めませんでした", e);
        }
        script.setScript(sb.toString());
        this.list.getItems().add(script);
        BattleLogScriptCollection.get().getScripts().add(script);
    }

    @FXML
    void remove() {
        ButtonType result = Tools.Conrtols.alert(AlertType.CONFIRMATION,
                "スクリプトの削除", "このスクリプトを削除しますか？", this.getWindow())
                .orElse(null);
        if (!ButtonType.OK.equals(result)) {
            return;
        }

        BattleLogScript selected = this.list.getSelectionModel().getSelectedItem();
        if (selected != null) {
            this.list.getItems().remove(selected);
            BattleLogScriptCollection.get().getScripts().remove(selected);
        }
        if (this.current == selected) {
            this.setCurrent(null);
        }
    }

    @FXML
    void save() {
        if (this.current != null) {
            this.current.setName(this.name.getText());
            this.current.setScript(this.editor.get());
        }
        this.list.refresh();
    }

    @FXML
    void run() {
        if (this.current == null) {
            return;
        }
        if (!this.current.getScript().equals(this.editor.get())) {
            ButtonType result = Tools.Conrtols.alert(AlertType.CONFIRMATION,
                    "スクリプトの保存", "スクリプトは保存されていません。\n保存せず実行しますか？", this.getWindow())
                    .orElse(null);
            if (!ButtonType.OK.equals(result)) {
                return;
            }
        }
        StringWriter writer = new StringWriter();
        PrintWriter out = new PrintWriter(writer, true);
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");

            JSObject obj = (JSObject) engine.eval("this");
            obj.setMember("print", (Consumer<Object>) out::println);

            LogCollector collector = new LogCollector(engine, this.details);
            obj.setMember("LogCollector", collector);

            engine.getContext().setAttribute(ScriptEngine.FILENAME, this.current.getName(), ScriptContext.ENGINE_SCOPE);
            engine.eval(this.current.getScript());
        } catch (ScriptException e) {
            out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace(out);
        }
        this.result.setText(writer.toString());
    }

    void setData(List<BattleLogDetail> details) {
        this.details = details;
    }

    private void setCurrent(BattleLogScript script) {
        this.current = script;
        if (script != null) {
            this.save.setDisable(false);
            this.run.setDisable(false);
            this.name.setText(script.getName());
            this.name.setDisable(false);
            this.editor.set(script.getScript());
            this.editor.setReadOnly(false);
        } else {
            this.save.setDisable(true);
            this.run.setDisable(true);
            this.name.setText("");
            this.name.setDisable(true);
            this.editor.set("");
            this.editor.setReadOnly(true);
        }
    }

    public static class LogCollector {

        private ScriptEngine engine;

        private List<BattleLogDetail> details;

        public LogCollector(ScriptEngine engine, List<BattleLogDetail> details) {
            this.engine = engine;
            this.details = details;
        }

        public Object reduce(Object callback, Object initialValue) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            Invocable invocable = (Invocable) this.engine;

            Object parser = this.engine.eval("JSON");

            Object result = initialValue;
            for (BattleLogDetail log : this.details) {
                BattleLog battleLog = BattleLogs.read(log.getDate());
                String json = mapper.writeValueAsString(battleLog);
                Object obj = invocable.invokeMethod(parser, "parse", json);
                result = ((JSObject) callback).call(null, result, obj);
            }
            return result;
        }
    }
}
