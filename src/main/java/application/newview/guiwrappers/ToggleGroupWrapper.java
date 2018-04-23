package application.newview.guiwrappers;

import javafx.scene.control.ToggleGroup;

public class ToggleGroupWrapper implements FXWrapper<ToggleGroup> {

    private final ToggleGroup backingObject;

    public ToggleGroupWrapper(ToggleGroup backingObject) {
        this.backingObject = backingObject;
    }

    @Override
    public ToggleGroup getWrappedObject() {
        return backingObject;
    }

    public boolean isSelected() {
        return backingObject.getSelectedToggle() != null;

    }
}
