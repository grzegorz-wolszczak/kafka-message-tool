package application.model;

import javafx.beans.property.StringProperty;

public interface ModelConfigObject {
    String getUuid();
    String getName();
    StringProperty nameProperty();
    void assignNewUuid();
    String getObjectTypeName();
}
