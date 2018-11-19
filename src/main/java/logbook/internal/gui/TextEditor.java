package logbook.internal.gui;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TextEditor extends WindowController {

    @FXML
    private VBox box;

    @FXML
    private Button save;

    @FXML
    private TextField name;

    private TextEditorPane editor;

    private Supplier<String> lang;

    private Supplier<String> nameSup;

    private Supplier<String> source;

    private Supplier<Boolean> readOnly;

    private BiConsumer<String, String> saveAction;

    @FXML
    void initialize() {
        this.editor = new TextEditorPane();
        this.box.getChildren().add(this.editor);
        VBox.setVgrow(this.editor, Priority.ALWAYS);
        if (this.lang != null) {
            this.editor.start(this.lang.get());
        }
        if (this.nameSup != null) {
            this.name.setText(this.nameSup.get());
        }
        if (this.source != null) {
            this.editor.set(this.source.get());
        }
        if (this.readOnly != null) {
            this.editor.setReadOnly(this.readOnly.get());
            this.name.setEditable(this.readOnly.get());
        }
    }

    @FXML
    void save(ActionEvent event) {
        if (this.saveAction != null) {
            this.saveAction.accept(this.name.getText(), this.editor.get());
        }
    }

    /**
     * 言語を設定します
     *
     * @param lang 言語
     */
    public void setLanguage(String lang) {
        this.lang = () -> lang;
        if (this.editor != null) {
            this.editor.start(lang);
        }
    }

    /**
     * 名前を設定します
     *
     * @param name 名前
     */
    public void setName(String name) {
        this.nameSup = () -> name;
        if (this.name != null) {
            this.name.setText(name);
        }
    }

    /**
     * エディタの内容を設定します
     * @param source エディタの内容
     */
    public void setSource(Supplier<String> source) {
        this.source = source;
        if (this.editor != null) {
            this.editor.set(source.get());
        }
    }

    /**
     * 保存が押されたときのアクション
     * @param saveAction アクション
     */
    public void setSaveAction(BiConsumer<String, String> saveAction) {
        this.saveAction = saveAction;
    }

    /**
     * エディタを読み取り専用に設定します
     * @param readOnly 読み取り専用
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = () -> readOnly;
        if (this.editor != null) {
            this.editor.setReadOnly(readOnly);
        }
        if (this.name != null) {
            this.name.setEditable(!readOnly);
        }
    }
}
