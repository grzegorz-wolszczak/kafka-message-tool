package application.model.modelobjects;

import application.constants.ApplicationConstants;
import application.model.ModelConfigObject;
import application.model.RelatedConfigHolder;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;


public class KafkaTopicConfig implements ModelConfigObject, ToolTipInfoProvider, RelatedConfigHolder<KafkaBrokerConfig> {
    private static final ReadOnlyStringProperty TOPIC = new ReadOnlyStringWrapper("topic");
    private final StringProperty topicName = new SimpleStringProperty(ApplicationConstants.DEFAULT_NEW_TOPIC_NAME);
    private final StringProperty name = new SimpleStringProperty(ApplicationConstants.DEFAULT_NEW_TOPIC_CONFIG_NAME);
    private final StringProperty uuid = new SimpleStringProperty(UUID.randomUUID().toString());
    private final ObjectProperty<KafkaBrokerConfig> brokerProperty = new SimpleObjectProperty<>();
    private final StringProperty toolTipInfoProperty = new SimpleStringProperty();
    private final StringExpression topicExpression = TOPIC.concat("       :'").concat(topicName).concat("'");
    private final StringProperty brokerHostnameProperty = new SimpleStringProperty();
    private final StringProperty brokerPortProperty = new SimpleStringProperty();

    public KafkaTopicConfig(String name) {
        this();
        this.name.set(name);
    }

    public KafkaTopicConfig() {
        toolTipInfoProperty.bind(topicExpression);
        bindToolTipInfoProperty();
    }

    @Override
    public ObjectProperty<KafkaBrokerConfig> relatedConfigProperty() {
        return brokerProperty;
    }

    public String getBrokerHostName() {
        return brokerHostnameProperty.get();
    }

    public String getBrokerPort() {
        return brokerPortProperty.get();
    }

    @Override
    public StringProperty toolTipInfoProperty() {
        return toolTipInfoProperty;
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public void assignNewUuid() {
        uuid.set(UUID.randomUUID().toString());
    }

    @Override
    public String getObjectTypeName() {
        return "Topic configuration";
    }

    public String getBrokerUuid() {
        if (brokerProperty.get() != null) {
            return brokerProperty.get().getUuid();
        }
        return "";
    }

    public String getUuid() {
        return uuid.get();
    }

    // this is needed for proper serialization from xml
    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String configName) {
        if (StringUtils.isBlank(configName)) {
            return;
        }
        this.name.set(configName);
    }

    public StringProperty topicNameProperty() {
        return topicName;
    }

    public String getTopicName() {
        return topicName.get();
    }

    public void setTopicName(String topicName) {
        if (StringUtils.isBlank(topicName)) {
            return;
        }
        this.topicName.set(topicName);
    }

    @Override
    public String toString() {
        return name.get();
    }

    @Override
    public KafkaBrokerConfig getRelatedConfig() {
        return brokerProperty.get();
    }

    @Override
    public void setRelatedConfig(KafkaBrokerConfig brokerConfig) {
        this.brokerProperty.set(brokerConfig);

        brokerHostnameProperty.unbind();
        brokerPortProperty.unbind();
        if (brokerConfig == null) {
            brokerHostnameProperty.set(null);
            brokerPortProperty.set(null);
            return;

        }
        brokerHostnameProperty.bind(brokerConfig.hostNameProperty());
        brokerPortProperty.bind(brokerConfig.portProperty());
    }

    private void bindToolTipInfoProperty() {
        brokerProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                toolTipInfoProperty.bind(topicExpression.concat("\nbroker host : ").concat(newValue.toolTipInfoProperty()));
            } else {
                toolTipInfoProperty.bind(topicExpression);
            }
        });
    }
}