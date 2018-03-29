package application.globals;

import application.logging.Logger;
import javafx.scene.image.Image;

import java.net.URL;

public class AppGlobals {
    private static final String APPLICATION_ICON_PNG = "/kafka-logo.png";
    private Image applicationIcon;
    private final static AppGlobals INSTANCE = new AppGlobals();

    public static Image getApplicationIcon() {
        return INSTANCE.applicationIcon;
    }

    public static void initialize() {
        INSTANCE.loadAppIconIfPossible();
    }

    private void loadAppIconIfPossible() {
        final URL fileUrl = getClass().getResource(APPLICATION_ICON_PNG);
        if (fileUrl == null) {
            Logger.error(String.format("Could not set application action from file '%s'", APPLICATION_ICON_PNG));
            return;
        }
        final String url = fileUrl.toExternalForm();
        try {
            applicationIcon = new Image(url);
        } catch (Exception ex) {
            Logger.error(
                String.format(
                    "Could not set application action from url '%s'", fileUrl), ex);
        }
    }
}
