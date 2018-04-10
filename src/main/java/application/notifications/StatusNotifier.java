package application.notifications;

import javafx.application.Platform;
import org.controlsfx.control.StatusBar;

public class StatusNotifier {
    private ApplicationStatusBar appStatusBar;

    public void configureStatusBar(StatusBar statusBar) {
        appStatusBar = new ApplicationStatusBar(statusBar);
    }

    public void notifyAboutError(){
        Platform.runLater(()->appStatusBar.notifyAboutError());
    }
}
