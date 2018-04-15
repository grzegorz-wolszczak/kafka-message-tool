package application.customfxwidgets.senderconfig;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.controlsfx.control.StatusBar;

import java.util.Locale;

public class SentMessagesProgressNotifier {
    public static final double PERCENTAGE_MAX = 100.0;
    private DoubleProperty doubleProperty;
    private StatusBar statusBar;


    public SentMessagesProgressNotifier(StatusBar statusBar) {
        this.statusBar = statusBar;
        resetStatusBarOnConstruction();
    }

    private void resetStatusBarOnConstruction() {
        statusBar.getLeftItems().clear();
        final ObservableList<Node> rightItems = statusBar.getRightItems();
        rightItems.clear();
        doubleProperty = statusBar.progressProperty();
    }

    private double percentage(int count, int total) {
        return (count * PERCENTAGE_MAX / total);
    }

    private void displayMessageToStatusBar(String message) {
        Platform.runLater(()->statusBar.textProperty().set(message));
    }

    private void displayProgressOnProgressBar(double value)
    {
        Platform.runLater(()->doubleProperty.set(value));
    }

    // count is 1-based
    public void setMsgSentProgress(int count, int total) {
        displayProgressOnProgressBar((float)count / (float)total);
        double percentage = percentage(count, total);
        displayMessageToStatusBar(String.format(Locale.ENGLISH, "Sent messages: %d/%d (%06.3f)%%", count, total, percentage));
    }

    public void clearMsgSentProgress() {
        displayProgressOnProgressBar(0.0);
        //displayMessageToStatusBar("");

    }
}
