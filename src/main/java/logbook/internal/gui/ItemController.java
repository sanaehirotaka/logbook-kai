package logbook.internal.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import logbook.bean.AppItemTableConfig;

/**
 * 所有装備一覧のUIコントローラー(親)
 *
 */
public class ItemController extends WindowController {
    @FXML
    private TabPane tab;
    
    @FXML
    private ItemItemController itemController;

    @FXML
    private ItemAirBaseController airbaseController;

    @FXML
    void initialize() {
        this.tab.getSelectionModel().select(AppItemTableConfig.get().getTabIndex());
        this.tab.getSelectionModel().selectedIndexProperty().addListener((ob, o, n) -> AppItemTableConfig.get().setTabIndex(n.intValue()));
    }

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        this.itemController.setWindow(window);
        this.airbaseController.setWindow(window);
    }
}
