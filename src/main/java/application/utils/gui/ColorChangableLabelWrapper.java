package application.utils.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class ColorChangableLabelWrapper implements ColorChangable {

    private Label label;
    private final Paint originalFill;
    private final Background originalBackground;

    public ColorChangableLabelWrapper(Label label) {
        this.label = label;
        originalFill = label.getTextFill();
        originalBackground = label.getBackground();
    }


    @Override
    public void changeColor(Color color) {
        label.setTextFill(Color.WHITE);
        label.setBackground(new Background(new BackgroundFill(color,
                CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public void restoreOriginalColor() {
        label.setTextFill(originalFill);
        label.setBackground(originalBackground);
    }
}
