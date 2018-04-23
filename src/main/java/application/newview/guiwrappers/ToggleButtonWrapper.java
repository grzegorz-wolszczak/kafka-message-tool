package application.newview.guiwrappers;

import javafx.scene.control.ToggleButton;

public class ToggleButtonWrapper implements FXWrapper<ToggleButton> {
    private ToggleButton backingObject;

    public ToggleButtonWrapper(ToggleButton backingObject) {
        this.backingObject = backingObject;
    }

    public boolean isSelected() {
        return backingObject.isSelected();
    }

    @Override
    public ToggleButton getWrappedObject() {
        return backingObject;
    }
}
