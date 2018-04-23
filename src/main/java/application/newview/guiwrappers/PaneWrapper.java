package application.newview.guiwrappers;

import javafx.scene.layout.Pane;

public class PaneWrapper implements FXWrapper<Pane> {

    private Pane backingObject;

    public PaneWrapper(Pane backingObject) {
        this.backingObject = backingObject;
    }

    @Override
    public Pane getWrappedObject() {
        return backingObject;
    }
}
