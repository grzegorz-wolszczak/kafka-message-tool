package application.newview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AppContentView extends AnchorPane {
    private static final String FXML_FILE = "AppContentView.fxml";

    public AppContentView() throws IOException {
        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);
    }

    @FXML
    void onAnyListViewClicked() {
        //  refreshRightPane();
    }

    @FXML
    private void leftViewTabPaneOnMouseClicked() {
        //refreshRightPane();
    }

    @FXML
    private void addButtonOnAction() {
        //guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::addNewConfig);
        //refreshRightPane();
    }

    @FXML
    private void deleteButtonOnAction() {
        //guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::deleteSelectedConfig);
        //refreshRightPane();
    }
}
