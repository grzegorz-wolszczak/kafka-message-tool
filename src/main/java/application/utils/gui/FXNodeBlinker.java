package application.utils.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FXNodeBlinker {

    public static final int COLOR_MAX = 255;
    private ColorChangable nodeToBlink;
    private Color blinkColor;
    private Timeline timeline;
    private String originalStyle;

    public FXNodeBlinker(Color blinkColor) {
        setBlinkColor(blinkColor);
    }

    public FXNodeBlinker(ColorChangable nodeToBlink, Color blinkColor) {
        setBlinkColor(blinkColor);
        setNodeToBlink(nodeToBlink);
    }

    public void setNodeToBlink(ColorChangable nodeToBlink) {
        this.nodeToBlink = nodeToBlink;
        createKeyFrames();
    }

    public void setBlinkColor(Color blinkColor) {
        this.blinkColor = blinkColor;
        createKeyFrames();
    }

    public void blink() {
        if (timeline != null) {
            timeline.play();
        }
    }

    private void createKeyFrames() {
        if (nodeToBlink == null) {
            return;
        }



        final KeyFrame firstKeyFrame = new KeyFrame(Duration.seconds(0.01), evt -> {
            nodeToBlink.changeColor(blinkColor);
        });
        final KeyFrame secondKeyFrame = new KeyFrame(Duration.seconds(0.55), e -> {
            nodeToBlink.restoreOriginalColor();

        });
        timeline = new Timeline(firstKeyFrame,
                secondKeyFrame);

    }
}

