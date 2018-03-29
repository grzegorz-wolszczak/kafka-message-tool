
package application.model;

import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;

public interface ModelObjectCollection<ModelObject>{
    ObservableList<ModelObject> getObservables();
    boolean hasObjectWithUuid(String uuid);
    boolean add(ModelObject o);
    Optional<ModelObject> getByUuid(String uuid);
    List<String> getNames();
}
