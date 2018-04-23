package application.newview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MessagesPane extends AnchorPane {
    private static final String FXML_FILE = "new/MessagesNotificationsView.fxml";


    public MessagesPane() throws IOException {
        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);
    }

    @FXML
    private void initialize(){

    }


}
