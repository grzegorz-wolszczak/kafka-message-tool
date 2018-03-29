package application.model.pojos;

import application.model.XmlElementNames;
import lombok.EqualsAndHashCode;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@EqualsAndHashCode
@XmlRootElement(name = XmlElementNames.BROKER_CONFIG)
public class BrokerConfigPojo implements Copyable<BrokerConfigPojo>{

    private String uuid;
    private String configName;
    private String kafkaBrokerPort;
    private String kafkaBrokerHostName;

    @XmlElement(name = XmlElementNames.CONFIG_NAME)
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    @XmlElement(name = XmlElementNames.BROKER_PORT)
    public String getKafkaBrokerPort() {
        return kafkaBrokerPort;
    }

    public void setKafkaBrokerPort(String kafkaBrokerPort) {
        this.kafkaBrokerPort = kafkaBrokerPort;
    }

    @XmlElement(name = XmlElementNames.BROKER_HOSTNAME)
    public String getKafkaBrokerHostName() {
        return kafkaBrokerHostName;
    }

    public void setKafkaBrokerHostName(String kafkaBrokerHostName) {
        this.kafkaBrokerHostName = kafkaBrokerHostName;
    }

    @XmlElement(name = XmlElementNames.UUID)
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Override
    public BrokerConfigPojo copyOfMine() {
        final BrokerConfigPojo p = new BrokerConfigPojo();
        p.setConfigName(getConfigName());
        p.setKafkaBrokerHostName(getKafkaBrokerHostName());
        p.setKafkaBrokerPort(getKafkaBrokerPort());
        p.setUuid(getUuid());
        return p;
    }
}
