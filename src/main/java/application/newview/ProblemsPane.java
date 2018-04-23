package application.newview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProblemsPane extends AnchorPane {
    private static final String FXML_FILE = "new/ProblemsNotificationsView.fxml";


    public ProblemsPane() throws IOException {
        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);
    }

    @FXML
    private void initialize(){

    }


}
