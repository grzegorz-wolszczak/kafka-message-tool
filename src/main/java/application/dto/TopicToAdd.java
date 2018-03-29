package application.dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class TopicToAdd {

    private StringProperty topicName = new SimpleStringProperty("test");
    private IntegerProperty partitions = new SimpleIntegerProperty(1);
    private IntegerProperty replicationFactor = new SimpleIntegerProperty(1);


    public TopicToAdd() {
    }

    public StringProperty topicNameProperty() {
        return topicName;
    }

    public IntegerProperty partitionsProperty() {
        return partitions;
    }

    public IntegerProperty replicationFactorProperty() {
        return replicationFactor;
    }

    @Override
    public String toString() {
        return "TopicToAdd{" +
            "topicName=" + topicName.get() +
            ", partitions=" + partitions.get() +
            ", replicationFactor=" + replicationFactor.get() +
            '}';
    }
}
