package logbook.internal.gui;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.PropertySheet.Item;
import org.controlsfx.property.BeanProperty;
import org.controlsfx.property.BeanPropertyUtils;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

/**
 * Beanプロパティーを編集するダイアログ
 *
 * @param <T> Bean
 */
public class PropertyDialog<T> {

    private T bean;
    private Alert alert;

    public PropertyDialog(Window owner, T bean, String title) {
        this.bean = bean;
        PropertySheet sheet = new PropertySheet(this.getProperties(bean));
        sheet.setModeSwitcherVisible(false);
        sheet.setSearchBoxVisible(sheet.getItems().size() > 10);

        this.alert = new Alert(AlertType.NONE);
        this.alert.getDialogPane().getStylesheets().add("logbook/gui/application.css");
        InternalFXMLLoader.setGlobal(this.alert.getDialogPane());
        this.alert.initOwner(owner);
        this.alert.setTitle(title);
        this.alert.getDialogPane().setContent(new BorderPane(sheet));
        this.alert.getButtonTypes().add(ButtonType.APPLY);
    }

    public T showAndWait() {
        this.alert.showAndWait();
        return this.bean;
    }

    private ObservableList<Item> getProperties(T bean) {
        ObservableList<Item> properties = BeanPropertyUtils.getProperties(bean);
        List<String> fields = Arrays.stream(bean.getClass().getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        properties.sort(Comparator.comparing(p -> fields.indexOf(((BeanProperty) p).getName())));
        return properties;
    }
}
