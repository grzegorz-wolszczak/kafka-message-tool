package application.notifications;

import org.controlsfx.control.StatusBar;

public class AppNotifier {
    private ApplicationStatusBar appStatusBar;

    public void configureStatusBar(StatusBar statusBar) {
        appStatusBar = new ApplicationStatusBar(statusBar);
    }
}
