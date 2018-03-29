package application.model;

import javafx.beans.property.ObjectProperty;

public interface RelatedConfigHolder<T>{
    T getRelatedConfig();
    void setRelatedConfig(T config);
    ObjectProperty<T> relatedConfigProperty();
}
