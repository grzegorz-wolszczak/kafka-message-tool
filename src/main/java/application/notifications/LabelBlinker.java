package application.notifications;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class LabelBlinker {

    private Label label;
    private Color blinkColor;
    private Timeline timeline;
    public LabelBlinker(Label label, Color blinkColor) {
        this.label = label;
        this.blinkColor = blinkColor;

        createKeyFrames();
    }

    public void setLabel(Label label) {
        this.label = label;
        createKeyFrames();
    }

    public void setBlinkColor(Color blinkColor) {
        this.blinkColor = blinkColor;
        createKeyFrames();
    }

    public void blink() {
        timeline.play();
    }

    private void createKeyFrames() {
        final Background originalBackground = label.getBackground();
        final Paint originalFill = label.getTextFill();
        final Background blinkBackground = new Background(new BackgroundFill(blinkColor,
                                                                             new CornerRadii(0),
                                                                             new Insets(0)));

        final KeyFrame firstKeyFrame = new KeyFrame(Duration.seconds(0.01), evt -> {
            label.setBackground(blinkBackground);
            label.setTextFill(Color.WHITE);
        });
        final KeyFrame secondKeyFrame = new KeyFrame(Duration.seconds(0.25), e -> {
            label.setBackground(originalBackground);
            label.setTextFill(originalFill);
        });
        timeline = new Timeline(firstKeyFrame,
                                secondKeyFrame);

    }
}

