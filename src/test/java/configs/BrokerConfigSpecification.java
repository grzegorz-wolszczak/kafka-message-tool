package configs;

import application.model.modelobjects.KafkaBrokerConfig;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BrokerConfigSpecification {
    @Test
    public void shouldNotAllowSettingBlankConfigName() {
        final KafkaBrokerConfig config = getConfig();
        final String name = config.getName();
        assertThat(name).isNotBlank();
        // WHEN/THEN
        config.setName(null);
        assertThat(config.getName()).isEqualTo(name);

        // WHEN/THEN
        config.setName("");
        assertThat(config.getName()).isEqualTo(name);
    }

    @Test
    public void shouldNotAllowSettingBlankHostName() {
        final KafkaBrokerConfig config = getConfig();
        final String hostname = config.getHostname();
        assertThat(hostname).isNotBlank();
        // WHEN/THEN
        config.setHostname(null);
        assertThat(config.getHostname()).isEqualTo(hostname);

        // WHEN/THEN
        config.setHostname("");
        assertThat(config.getHostname()).isEqualTo(hostname);
    }

    private KafkaBrokerConfig getConfig() {
        return new KafkaBrokerConfig();
    }

    @Test
    public void shouldNotAllowSettingBlankPort() {
        final KafkaBrokerConfig config = getConfig();
        final String port = config.getPort();
        assertThat(port).isNotBlank();
        // WHEN/THEN
        config.setPort(null);
        assertThat(config.getPort()).isEqualTo(port);

        // WHEN/THEN
        config.setPort("");
        assertThat(config.getPort()).isEqualTo(port);
    }

    @Test
    public void shouldAssignNewUuid() {
        final KafkaBrokerConfig config = getConfig();
        final String oldUuid = config.getUuid();
        config.assignNewUuid();
        assertThat(oldUuid).isNotEqualTo(config.getUuid());
    }

    @Test
    public void shouldReturnObjectTypeName() {
        assertThat(new KafkaBrokerConfig().getObjectTypeName()).isEqualTo("Broker configuration");
    }
}
