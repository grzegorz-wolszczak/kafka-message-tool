package application.utils.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class FXNodeBlinker {

    public static final int COLOR_MAX = 255;
    private BlinkableNode nodeToBlink;
    private Color blinkColor;
    private Timeline timeline;
    private String originalStyle;

    public FXNodeBlinker(BlinkableNode nodeToBlink, Color blinkColor) {
        this.nodeToBlink = nodeToBlink;
        this.blinkColor = blinkColor;

        createKeyFrames();
    }

    public void setNodeToBlink(BlinkableNode nodeToBlink) {
        this.nodeToBlink = nodeToBlink;
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
        originalStyle = nodeToBlink.getStyle();


        String blinkStyle = String.format("-fx-base: rgb(%s, %s, %s);",
                                          COLOR_MAX *blinkColor.getRed(),
                                          COLOR_MAX*blinkColor.getGreen(),
                                          COLOR_MAX*blinkColor.getBlue());



        final KeyFrame firstKeyFrame = new KeyFrame(Duration.seconds(0.01), evt -> {
            nodeToBlink.setStyle(blinkStyle);
        });
        final KeyFrame secondKeyFrame = new KeyFrame(Duration.seconds(0.25), e -> {
            nodeToBlink.setStyle(originalStyle);

        });
        timeline = new Timeline(firstKeyFrame,
                                secondKeyFrame);

    }
}

