package application.model.modelobjects;

import application.model.ModelConfigObject;
import application.utils.kafka.KafkaBrokerHostInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement(name = "KafkaBrokerConfig")
public class KafkaBrokerConfig implements ModelConfigObject, ToolTipInfoProvider {
    private static final int DEFAULT_KAFKA_PORT = 9092;
    private static final String DEFAULT_KAFKA_HOSTNAME = "localhost";

    private final KafkaBrokerHostInfo hostInfo = new KafkaBrokerHostInfo(DEFAULT_KAFKA_HOSTNAME,
                                                                         DEFAULT_KAFKA_PORT);
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty port = hostInfo.portProperty();
    private final StringProperty hostname = hostInfo.hostnameProperty();
    private final StringProperty uuid = new SimpleStringProperty(UUID.randomUUID().toString());
    private final StringProperty toolTipInfo = new SimpleStringProperty();

    public KafkaBrokerConfig(String brokerConfigName) {
        this();
        this.name.set(brokerConfigName);
    }

    public KafkaBrokerConfig() {
        bindShortInfoProperty();
    }

    @Override
    public StringProperty toolTipInfoProperty() {
        return toolTipInfo;
    }

    public StringProperty portProperty() {
        return port;
    }

    public StringProperty hostNameProperty() {
        return hostname;
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
        return "Broker configuration";
    }

    @XmlElement(name = "Uuid")
    @Override
    public String getUuid() {
        return uuid.get();
    }


    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    @XmlElement(name = "BrokerConfigName")
    public String getName() {
        return name.get();
    }

    public void setName(String configName) {
        if (StringUtils.isBlank(configName)) {
            return;
        }
        this.name.set(configName);
    }

    @XmlElement(name = "KafkaBrokerPort")
    public String getPort() {
        return port.get();
    }

    public void setPort(String port) {
        if (StringUtils.isBlank(port)) {
            return;
        }
        this.port.set(port);
    }

    @XmlElement(name = "KafkaBrokerHostname")
    public String getHostname() {
        return hostname.get();
    }

    public void setHostname(String hostname) {
        if (StringUtils.isBlank(hostname)) {
            return;
        }
        this.hostname.set(hostname);
    }

    @Override
    public String toString() {
        return name.get();
    }

    public KafkaBrokerHostInfo getHostInfo() {
        return hostInfo;
    }

    private void bindShortInfoProperty() {
        toolTipInfo.bind(hostname.concat(":").concat(port));
    }
}
