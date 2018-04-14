package logbook.internal.gui;

import javafx.fxml.FXML;
import javafx.stage.Stage;

/**
 * 所有装備一覧のUIコントローラー(親)
 *
 */
public class ItemController extends WindowController {

    @FXML
    private ItemItemController itemController;

    @FXML
    private ItemAirBaseController airbaseController;

    @Override
    public void setWindow(Stage window) {
        super.setWindow(window);
        this.itemController.setWindow(window);
        this.airbaseController.setWindow(window);
    }
}
