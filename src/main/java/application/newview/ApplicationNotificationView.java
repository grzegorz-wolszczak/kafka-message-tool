package application.newview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ApplicationNotificationView extends AnchorPane {
    private static final String FXML_FILE = "ApplicationNotificationView.fxml";

    public ApplicationNotificationView() throws IOException {
        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);
    }
}
