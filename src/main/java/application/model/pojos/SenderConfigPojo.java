package application.model.pojos;

import application.model.XmlElementNames;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static application.model.XmlElementNames.CONFIG_NAME;
import static application.model.XmlElementNames.CONTENT;
import static application.model.XmlElementNames.SENDER_CONFIG;
import static application.model.XmlElementNames.TOPIC_CONFIG_UUID;

@EqualsAndHashCode
@ToString
@XmlRootElement(name = SENDER_CONFIG)
public class SenderConfigPojo implements Copyable<SenderConfigPojo> {
    private String configName;
    private String uuid;
    private String content;
    private String messageKey;
    private boolean messageKeyEnabled;
    private String topicConfigUuid;
    private String repeatCount;
    private String runBeforeAllMessagesScript;
    private String runBeforeEachMessageScript;
    private boolean sendingSimulationModeEnabled;

    public String getConfigName() {
        return configName;
    }

    @XmlElement(name = CONFIG_NAME)
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getContent() {
        return content;
    }

    @XmlElement(name = CONTENT)
    public void setContent(String content) {
        this.content = content;
    }

    @XmlElement(name = TOPIC_CONFIG_UUID)
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

    @XmlElement(name = XmlElementNames.MESSAGE_KEY_ENABLED)
    public boolean isMessageKeyEnabled() {
        return messageKeyEnabled;
    }

    public void setMessageKeyEnabled(boolean messageKeyEnabled) {
        this.messageKeyEnabled = messageKeyEnabled;
    }

    @XmlElement(name = XmlElementNames.MESSAGE_KEY)
    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    @XmlElement(name = XmlElementNames.REPEAT_COUNT)
    public String getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(String repeatCount) {
        this.repeatCount = repeatCount;
    }

    @XmlElement(name = XmlElementNames.RUN_BEFORE_ALL_MSGS_SCRIPT)
    public String getRunBeforeAllMessagesScript() {
        return runBeforeAllMessagesScript;
    }

    public void setRunBeforeAllMessagesScript(String script) {
        this.runBeforeAllMessagesScript = script;
    }

    @XmlElement(name = XmlElementNames.RUN_BEFORE_EACH_MSG_SCRIPT)
    public String getRunBeforeEachMessageScript() {
        return runBeforeEachMessageScript;
    }

    public void setRunBeforeEachMessageScript(String script) {
        this.runBeforeEachMessageScript = script;
    }

    @Override
    public SenderConfigPojo copyOfMine() {
        final SenderConfigPojo p = new SenderConfigPojo();

        p.setConfigName(getConfigName());
        p.setUuid(getUuid());
        p.setContent(getContent());
        p.setMessageKey(getMessageKey());
        p.setMessageKeyEnabled(isMessageKeyEnabled());
        p.setTopicConfigUuid(getTopicConfigUuid());
        p.setRunBeforeAllMessagesScript(getRunBeforeAllMessagesScript());
        p.setRunBeforeEachMessageScript(getRunBeforeEachMessageScript());
        p.setRepeatCount(getRepeatCount());
        p.setSendingSimulationModeEnabled(getSendingSimulationModeEnabled());

        return p;
    }


    @XmlElement(name = XmlElementNames.SENDING_SIMULATION_MODE_ENABLED)
    public boolean getSendingSimulationModeEnabled() {
        return sendingSimulationModeEnabled;
    }

    public void setSendingSimulationModeEnabled(boolean simulationModeEnabled) {
        sendingSimulationModeEnabled = simulationModeEnabled;
    }
}
