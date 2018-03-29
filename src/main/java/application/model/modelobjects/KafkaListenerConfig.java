package application.model.modelobjects;

import application.model.KafkaOffsetResetType;
import application.model.ModelConfigObject;
import application.model.RelatedConfigHolder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

import static application.constants.ApplicationConstants.DEFAULT_CONSUMER_GROUP_ID;
import static application.constants.ApplicationConstants.DEFAULT_FETCH_TIMEOUT;


public class KafkaListenerConfig implements ModelConfigObject, RelatedConfigHolder<KafkaTopicConfig> {
    private final StringProperty name = new SimpleStringProperty();
    private final ObjectProperty<KafkaTopicConfig> topicConfig = new SimpleObjectProperty<>();
    private final StringProperty uuid = new SimpleStringProperty(UUID.randomUUID().toString());
    private final StringProperty consumerGroup = new SimpleStringProperty(DEFAULT_CONSUMER_GROUP_ID);
    private final StringProperty pollTimeout = new SimpleStringProperty(DEFAULT_FETCH_TIMEOUT);
    private final ObjectProperty<KafkaOffsetResetType> offsetResetConfig = new SimpleObjectProperty<>(KafkaOffsetResetType.EARLIEST);
    public KafkaListenerConfig(String s) {
        name.setValue(s);
    }

    // needed for xml serialization
    public KafkaListenerConfig() {
    }

    public ObjectProperty<KafkaTopicConfig> relatedConfigProperty() {
        return topicConfig;
    }

    public KafkaOffsetResetType getOffsetResetConfig() {
        return offsetResetConfig.get();
    }

    public void setOffsetResetConfig(KafkaOffsetResetType offsetResetConfig) {
        this.offsetResetConfig.set(offsetResetConfig);
    }

    public ObjectProperty<KafkaOffsetResetType> offsetResetConfigProperty() {
        return offsetResetConfig;
    }

    public String getConsumerGroup() {
        return consumerGroup.get();
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup.set(consumerGroup);
    }


    public String getPollTimeout() {
        return pollTimeout.get();
    }

    public void setPollTimeout(String pollTimeout) {
        this.pollTimeout.set(pollTimeout);
    }


    @Override
    public String getUuid() {
        return uuid.get();
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }


    public String getName() {
        return name.get();
    }

    @Override
    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public void assignNewUuid() {
        uuid.set(UUID.randomUUID().toString());
    }

    @Override
    public String getObjectTypeName() {
        return "Message listener configuration";
    }

    public void setName(String configName) {
        this.name.set(configName);
    }


    public String getTopicConfigUuid() {
        if (topicConfig.get() != null) {
            return topicConfig.get().getUuid();
        }
        return null;
    }

    public KafkaTopicConfig getRelatedConfig() {
        return topicConfig.get();
    }

    public void setRelatedConfig(KafkaTopicConfig topicConfig) {
        this.topicConfig.set(topicConfig);
    }


    public String toString() {
        return name.getValue();
    }

}
