package application.model.modelobjects;

import application.constants.ApplicationConstants;
import application.model.ModelConfigObject;
import application.model.RelatedConfigHolder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;


public class KafkaSenderConfig implements ModelConfigObject, RelatedConfigHolder<KafkaTopicConfig> {

    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty msgContentTemplate = new SimpleStringProperty("");
    private final ObjectProperty<KafkaTopicConfig> topicConfig = new SimpleObjectProperty<>();
    private final StringProperty uuid = new SimpleStringProperty(UUID.randomUUID().toString());
    private final BooleanProperty messageKeyEnabled = new SimpleBooleanProperty(false);
    private final StringProperty runBeforeAllMessagesScript = new SimpleStringProperty("");
    private final StringProperty runBeforeEachMessageScript = new SimpleStringProperty("");
    private final IntegerProperty repeatCount = new SimpleIntegerProperty(1);
    private final StringProperty messageKey = new SimpleStringProperty(ApplicationConstants.DEFAULT_MESSAGE_KEY);
    private final BooleanProperty sendingSimulationModeEnabledProperty = new SimpleBooleanProperty(false);


    public KafkaSenderConfig(String name) {
        this.name.set(name);
    }

    public KafkaSenderConfig() {
    }

    public boolean getSendingSimulationModeEnabled() {
        return sendingSimulationModeEnabledProperty.get();
    }

    public BooleanProperty sendingSimulationModeEnabledProperty() {
        return sendingSimulationModeEnabledProperty;
    }

    public void setSendingSimulationModeEnabled(boolean value) {
        sendingSimulationModeEnabledProperty.set(value);
    }

    public IntegerProperty repeatCountProperty() {
        return repeatCount;
    }

    public ObjectProperty<KafkaTopicConfig> relatedConfigProperty() {
        return topicConfig;
    }

    public boolean isMessageKeyEnabled() {
        return messageKeyEnabled.get();
    }

    public boolean getMessageKeyEnabled() {
        return this.messageKeyEnabled.get();
    }

    public void setMessageKeyEnabled(boolean messageKeyEnabled) {
        this.messageKeyEnabled.set(messageKeyEnabled);
    }

    public BooleanProperty messageKeyEnabledProperty() {
        return messageKeyEnabled;
    }

    public String getMessageKey() {
        return messageKey.get();
    }

    public void setMessageKey(String messgeKey) {
        this.messageKey.set(messgeKey);
    }

    public StringProperty messageKeyProperty() {
        return messageKey;
    }


    public String getUuid() {
        return uuid.get();
    }


    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public String getTopicConfigUuid() {
        final KafkaTopicConfig kafkaTopicConfig = topicConfig.get();
        if (kafkaTopicConfig != null) {
            return kafkaTopicConfig.getUuid();
        }
        return null;
    }


    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
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
        return "Message sender configuration";
    }

    public String getMsgContentTemplate() {
        return msgContentTemplate.get();
    }

    public void setMsgContentTemplate(String msgContentTemplate) {
        this.msgContentTemplate.set(msgContentTemplate);
    }

    @Override
    public String toString() {
        return name.get();
    }

    public KafkaTopicConfig getRelatedConfig() {
        return topicConfig.get();
    }

    public void setRelatedConfig(KafkaTopicConfig config) {
        topicConfig.set(config);
    }

    public Integer getRepeatCount() {
        return repeatCount.get();
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount.set(repeatCount);
    }

    public StringProperty runBeforeAllMessagesScriptProperty() {
        return runBeforeAllMessagesScript;
    }

    public StringProperty runBeforeEachMessageScriptProperty() {
        return runBeforeEachMessageScript;
    }

    public String getRunBeforeAllMessagesScript() {
        return runBeforeAllMessagesScript.get();
    }

    public void setRunBeforeAllMessagesScript(String script) {
        this.runBeforeAllMessagesScript.set(script);
    }

    public String getRunBeforeEachMessageScript() {
        return runBeforeEachMessageScript.get();
    }

    public void setRunBeforeEachMessageScript(String script) {
        this.runBeforeEachMessageScript.set(script);
    }
}

