package application.model.pojos;

import application.model.XmlElementNames;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@XmlRootElement(name = XmlElementNames.LISTENER_CONFIG)
public class ListenerConfigPojo implements Copyable<ListenerConfigPojo>{
    private String configName;
    private String uuid;
    private String topicConfigUuid;
    private String consumerGroup;
    private String pollTimeout;
    private String offsetResetConfig;
    private String receivedMsgLimitCount;
    private boolean receivedMsgLimitEnabled;

    @XmlElement(name = XmlElementNames.CONFIG_NAME)
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @XmlElement(name = XmlElementNames.TOPIC_CONFIG_UUID)
    public String getTopicConfigUuid() {
        return topicConfigUuid;
    }

    public void setTopicConfigUuid(String topicConfigUuid) {
        this.topicConfigUuid = topicConfigUuid;
    }

    @XmlElement(name = XmlElementNames.UUID)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @XmlElement(name = XmlElementNames.CONSUMER_GROUP)
    public String getConsumerGroup() {
        return consumerGroup;
    }


    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    @XmlElement(name = XmlElementNames.POLL_TIMEOUT)
    public String getPollTimeout() {
        return pollTimeout;
    }


    public void setPollTimeout(String pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    public String getOffsetResetConfig() {
        return offsetResetConfig;
    }

    @XmlElement(name = XmlElementNames.OFFSET_CONFIG_PROPERTY)
    public void setOffsetResetConfig(String offsetResetConfig) {
        this.offsetResetConfig = offsetResetConfig;
    }

    @Override
    public ListenerConfigPojo copyOfMine() {
        final ListenerConfigPojo p = new ListenerConfigPojo();

        p.setConfigName(getConfigName());
        p.setUuid(getUuid());
        p.setTopicConfigUuid(getTopicConfigUuid());
        p.setConsumerGroup(getConsumerGroup());
        p.setPollTimeout(getPollTimeout());
        p.setOffsetResetConfig(getOffsetResetConfig());
        p.setReceivedMsgLimitCount(getReceivedMsgLimitCount());
        p.setReceivedMsgLimitEnabled(getReceivedMsgLimitEnabled());
        return p;
    }


    @XmlElement(name= XmlElementNames.RECEIVED_MSG_LIMIT_COUNT)
    public void setReceivedMsgLimitCount(String receivedMsgLimitCount) {
        this.receivedMsgLimitCount = receivedMsgLimitCount;
    }

    @XmlElement(name= XmlElementNames.RECEIVED_MSG_LIMIT_ENABLED)
    public void setReceivedMsgLimitEnabled(boolean receivedMsgLimitEnabled) {
        this.receivedMsgLimitEnabled = receivedMsgLimitEnabled;
    }

    public boolean getReceivedMsgLimitEnabled() {
        return receivedMsgLimitEnabled;
    }

    public String getReceivedMsgLimitCount() {
        return receivedMsgLimitCount;
    }
}
