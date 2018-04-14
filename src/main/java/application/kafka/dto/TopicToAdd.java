package application.kafka.dto;

import application.kafka.cluster.TopicCleanupPolicy;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class TopicToAdd {

    private StringProperty topicName = new SimpleStringProperty("test");
    private IntegerProperty partitions = new SimpleIntegerProperty(1);
    private IntegerProperty replicationFactor = new SimpleIntegerProperty(1);
    private ObjectProperty<TopicCleanupPolicy> cleanupPolicy = new SimpleObjectProperty<>(TopicCleanupPolicy.DELETE);

    public TopicToAdd() {
    }

    public String getTopicName() {
        return topicName.get();
    }

    public int getPartitions() {
        return partitions.get();
    }

    public int getReplicationFactor() {
        return replicationFactor.get();
    }

    public TopicCleanupPolicy getCleanupPolicy() {
        return cleanupPolicy.get();
    }

    public ObjectProperty<TopicCleanupPolicy> cleanupPolicyProperty() {
        return cleanupPolicy;
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
