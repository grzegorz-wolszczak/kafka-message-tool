package application.utils.gui;

import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;

public class ColorChangableButtonWrapper implements ColorChangable {
    private static final int COLOR_MAX = 255;
    private final String originalStyle;
    private ToggleButton button;

    public ColorChangableButtonWrapper(ToggleButton button) {
        this.button = button;
        originalStyle = button.getStyle();
    }


    @Override
    public void changeColor(Color color) {
        String blinkStyle = String.format("-fx-base: rgb(%s, %s, %s);",
                COLOR_MAX * color.getRed(),
                COLOR_MAX * color.getGreen(),
                COLOR_MAX * color.getBlue());
        button.setStyle(blinkStyle);
    }

    @Override
    public void restoreOriginalColor() {
        button.setStyle(originalStyle);
    }
}
