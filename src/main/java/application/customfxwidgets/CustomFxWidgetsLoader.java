package application.customfxwidgets;

import application.utils.AppUtils;
import application.utils.GuiUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class CustomFxWidgetsLoader {
    public static void loadAnchorPane(AnchorPane node, String fxmlFile ) throws IOException {
        loadOnAnchorPane(node, fxmlFile);
    }

    public static void loadOnAnchorPane(Node node, String fxmlFile ) throws IOException {
        load(node, fxmlFile);
        GuiUtils.expandNodeToAnchorPaneBorders(node);
    }

    public static void load(Node node, String fxmlFile ) throws IOException {
        URL url = AppUtils.getFxmlResourceFile(fxmlFile);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setRoot(node);
        loader.setController(node);
        loader.load();
    }


}
