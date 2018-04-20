package application.customfxwidgets.configwindow;

import application.constants.ApplicationConstants;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static application.customfxwidgets.CustomFxWidgetsLoader.loadAnchorPane;

public class SettingsWindow extends AnchorPane {
    private static final String FXML_FILE = "SettingsWindow.fxml";
    private final Stage stage = new Stage();
    private final Application fxApplication;

    public SettingsWindow(Window owner, Application fxApplication) throws IOException {
        this.fxApplication = fxApplication;

        loadAnchorPane(this, FXML_FILE);
        setupStage(owner);
        //configureCloseButton();
    }

    private void setupStage(Window owner) {
        final Scene scene = new Scene(this);
        scene.getStylesheets().add(getClass().getResource(ApplicationConstants.GLOBAL_CSS_FILE_NAME).toExternalForm());
        scene.setRoot(this);
        stage.setScene(scene);
        stage.setTitle("Settings");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
    }
}
