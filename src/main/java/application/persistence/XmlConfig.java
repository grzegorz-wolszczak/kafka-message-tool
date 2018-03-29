package application.persistence;

import application.model.XmlElementNames;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = XmlElementNames.APPLICATION_CONFIG)
public class XmlConfig {
    private List<SenderConfigPojo> messagesConfigs;
    private List<TopicConfigPojo> topicConfigs;
    private List<ListenerConfigPojo> listenerConfigs;
    private List<BrokerConfigPojo> brokerConfigs;
    private GuiSettings guiSettings;
    private GlobalSettings globalSettings;

    // no arg default constructor is needed for xml serialization
    public XmlConfig() {
    }

    @XmlElement(name = XmlElementNames.GLOBAL_SETTINGS)
    public GlobalSettings getGlobalSettings() {
        return globalSettings;
    }

    public void setGlobalSettings(GlobalSettings globalSettings) {
        this.globalSettings = globalSettings;
    }

    @XmlElement(name = XmlElementNames.GUI_SETTINGS)
    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    @XmlElementWrapper(name = XmlElementNames.LISTENER_CONFIGS)
    @XmlElement(name = XmlElementNames.LISTENER_CONFIG)
    public List<ListenerConfigPojo> getListenerConfigs() {
        return listenerConfigs;
    }

    public void setListenerConfigs(List<ListenerConfigPojo> configs) {
        this.listenerConfigs = configs;
    }

    @XmlElementWrapper(name = XmlElementNames.TOPIC_CONFIGS)
    @XmlElement(name = XmlElementNames.TOPIC_CONFIG)
    public List<TopicConfigPojo> getTopicConfigs() {
        return topicConfigs;
    }

    public void setTopicConfigs(List<TopicConfigPojo> configs) {
        this.topicConfigs = configs;
    }


    @XmlElementWrapper(name = XmlElementNames.SENDER_CONFIGS)
    @XmlElement(name = XmlElementNames.SENDER_CONFIG)
    public List<SenderConfigPojo> getMessagesConfigs() {
        return messagesConfigs;
    }

    public void setMessagesConfigs(List<SenderConfigPojo> configs) {
        this.messagesConfigs = configs;
    }

    @XmlElementWrapper(name = XmlElementNames.BROKER_CONFIGS)
    @XmlElement(name = XmlElementNames.BROKER_CONFIG)
    public List<BrokerConfigPojo> getBrokerConfigs() {
        return brokerConfigs;
    }

    public void setBrokerConfigs(List<BrokerConfigPojo> configs) {
        this.brokerConfigs = configs;
    }

}
