package application.customfxwidgets;

import application.utils.GuiUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class CustomFxWidgetsLoader {
    public static void loadAnchorPane(AnchorPane node, String fxmlFile ) throws IOException {
        load(node, fxmlFile);
        GuiUtils.expandNodeToAnchorPaneBorders(node);
    }

    public static void load(Node node, String fxmlFile ) throws IOException {
        FXMLLoader loader = new FXMLLoader(node.getClass().getResource(fxmlFile));
        loader.setRoot(node);
        loader.setController(node);
        loader.load();
        GuiUtils.expandNodeToAnchorPaneBorders(node);
    }



}
