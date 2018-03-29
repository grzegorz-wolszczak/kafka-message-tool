package application.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ModelConfigObjects<T extends ModelConfigObject> implements ModelObjectCollection<T>{

    private final ObservableList<T> objects = FXCollections.observableArrayList();
    private final String name;

    ModelConfigObjects(String name) {
        this.name = name;
    }

    @Override
    public ObservableList<T> getObservables() {
        return objects;
    }

    @Override
    public boolean hasObjectWithUuid(String uuid) {
        if (uuid == null || uuid.isEmpty()) {
            return false;
        }
        return objects.stream().filter(e -> e.getUuid().equals(uuid)).collect(Collectors.toList()).size() > 0;
    }

    @Override
    public boolean add(T object) {
        if (hasObjectWithUuid(object.getUuid())) {
            return false;
        }
        return objects.add(object);
    }

    @Override
    public Optional<T> getByUuid(String uuid) {
        return objects.stream().filter(e -> e.getUuid().equals(uuid)).findFirst();
    }

    @Override
    public List<String> getNames() {
        return objects.stream().map(ModelConfigObject::getName).collect(Collectors.toList());
    }

}
