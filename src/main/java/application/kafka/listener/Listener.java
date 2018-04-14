package application.kafka.listener;

import application.model.modelobjects.KafkaListenerConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public interface Listener {
    KafkaListenerConfig getListenerConfig();

    StringProperty loggedTextProperty();

    BooleanProperty isRunningProperty();

    ObjectProperty<AssignedPartitionsInfo> assignedPartitionsProperty();

    void start();

    void stop();
}
