package application.utils.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;

public class BlinkableToggleButton implements BlinkableNode {
    private final String style;
    private final Node graphic;
    private final ObservableList<String> stylesheets;
    private ToggleButton button;

    public BlinkableToggleButton(ToggleButton button) {
        this.button = button;
        style = this.button.getStyle();
        graphic = this.button.getGraphic();
        stylesheets = FXCollections.observableArrayList(button.getStylesheets());
    }


    @Override
    public String getStyle() {
        return button.styleProperty().get();
    }

    @Override
    public void setStyle(String style) {
        button.styleProperty().set(style);

    }
}
