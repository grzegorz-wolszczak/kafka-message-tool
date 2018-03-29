package application.displaybehaviour;

import application.model.ModelConfigObject;
import javafx.beans.property.ObjectProperty;

public interface ModelConfigObjectsGuiInformer {
    ObjectProperty<ModelConfigObject> lastRemovedObjectProperty();

    ModelConfigObject selectedObject();
}
