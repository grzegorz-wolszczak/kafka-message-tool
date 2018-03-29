package application.model.pojos;

import application.model.XmlElementNames;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static application.model.XmlElementNames.BROKER_UUID;
import static application.model.XmlElementNames.TOPIC_NAME;
import static application.model.XmlElementNames.UUID;

@EqualsAndHashCode
@XmlRootElement(name = XmlElementNames.TOPIC_CONFIG)
public class TopicConfigPojo implements Copyable<TopicConfigPojo>{

    private String configName;
    private String uuid;
    private String topicName;
    private String brokerUuid;

    @XmlElement(name = UUID)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @XmlElement(name = XmlElementNames.CONFIG_NAME)
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @XmlElement(name = TOPIC_NAME)
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @XmlElement(name = BROKER_UUID)
    public String getBrokerUuid() {
        return brokerUuid;
    }

    public void setBrokerUuid(String brokerUuid) {
        this.brokerUuid = brokerUuid;
    }

    @Override
    public TopicConfigPojo copyOfMine() {
        TopicConfigPojo copied = new TopicConfigPojo();
        copied.setUuid(getUuid());
        copied.setTopicName(getTopicName());
        copied.setBrokerUuid(getBrokerUuid());
        copied.setConfigName(getConfigName());
        return copied;
    }


}
