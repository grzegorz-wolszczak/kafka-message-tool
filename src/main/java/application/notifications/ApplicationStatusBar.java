package application.notifications;

import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.paint.Color;
import org.controlsfx.control.StatusBar;

public class ApplicationStatusBar {
    private int warningsCount = 0;
    private int errorCount = 0;
    private LabelBlinker warningBlinker;
    private LabelBlinker errorBlinker;
    private Label errorLabel = new Label();
    private Label warningLabel = new Label();

    public ApplicationStatusBar(StatusBar statusBar) {
        warningBlinker = new LabelBlinker(warningLabel, Color.BLUE);
        errorBlinker = new LabelBlinker(errorLabel, Color.RED);
        statusBar.getRightItems().addAll(new Separator(Orientation.VERTICAL),
                                         warningLabel,
                                         new Separator(Orientation.VERTICAL),
                                         errorLabel);
        updateErrorLabel();
        updateWarningLabel();

    }

    public void notifyAboutWarning() {
        warningsCount++;
        updateWarningLabel();
        warningBlinker.blink();
    }

    public void notifyAboutError() {
        errorCount++;
        updateErrorLabel();
        errorBlinker.blink();
    }

    private void updateWarningLabel() {
        warningLabel.setText(String.format("W:%d", warningsCount));
    }

    private void updateErrorLabel() {
        errorLabel.setText(String.format("E:%d", errorCount));
    }
}
