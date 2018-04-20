package application.utils.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FXNodeBlinker {

    private ColorChangeable nodeToBlink;
    private Color blinkColor;
    private Timeline timeline;

    public FXNodeBlinker(Color blinkColor) {
        setBlinkColor(blinkColor);
    }

    public FXNodeBlinker(ColorChangeable nodeToBlink, Color blinkColor) {
        setBlinkColorWithNode(nodeToBlink, blinkColor);
    }

    public void setNodeToBlink(ColorChangeable newNodeToBlink) {
        setBlinkColorWithNode(newNodeToBlink, blinkColor);
    }

    public void setBlinkColor(Color newBlinkColor) {
        setBlinkColorWithNode(nodeToBlink, newBlinkColor);
    }

    public void blink() {
        if (timeline != null) {
            timeline.play();
        }
    }

    private void setBlinkColorWithNode(ColorChangeable nodeToBlink, Color blinkColor) {
        this.nodeToBlink = nodeToBlink;
        this.blinkColor = blinkColor;
        createKeyFrames();
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

