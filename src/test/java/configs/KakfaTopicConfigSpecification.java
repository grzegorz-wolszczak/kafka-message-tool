package configs;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class KakfaTopicConfigSpecification {

    @Test
    public void shoulNotAllowSettingBlankTopicConfigName() {
        // GIVEN
        final KafkaTopicConfig config = new KafkaTopicConfig();
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
    public void shoulNotAllowSettingBlankTopicName() {
        // GIVEN
        final KafkaTopicConfig config = new KafkaTopicConfig();
        final String topicName = config.getTopicName();
        assertThat(topicName).isNotBlank();
        // WHEN/THEN
        config.setTopicName(null);
        assertThat(config.getTopicName()).isEqualTo(topicName);

        // WHEN/THEN
        config.setTopicName("");
        assertThat(config.getTopicName()).isEqualTo(topicName);
    }

    @Test
    public void shouldUseNullValuesForBrokerHostnameAndPortForUnassignedBrokerConfig() {
        // GIVEN
        final KafkaTopicConfig config = new KafkaTopicConfig();

        // WHEN/THEN
        assertThat(config.getBrokerHostName()).isNull();
        assertThat(config.getBrokerPort()).isNull();
    }

    @Test
    public void shouldBindTopicConfigHostnamePropertyAndPortFromDefaultValuesOfEmptyBrokerConfigHostName() {
        // GIVEN
        final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
        final KafkaTopicConfig topicConfig = new KafkaTopicConfig();

        // WHEN
        topicConfig.setRelatedConfig(brokerConfig);

        // THEN
        assertThat(topicConfig.getBrokerHostName()).isEqualTo("localhost");
        assertThat(topicConfig.getBrokerPort()).isEqualTo("9092");
    }

    @Test
    public void shouldBindTopicConfigHostnamePropertyAndPortFromNotEmptyBrokerConfigHostName() {
        // GIVEN
        final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
        final KafkaTopicConfig topicConfig = new KafkaTopicConfig();

        // WHEN
        brokerConfig.setHostname("aHost");
        brokerConfig.setPort("aPort");
        topicConfig.setRelatedConfig(brokerConfig);

        // THEN
        assertThat(topicConfig.getBrokerHostName()).isEqualTo("aHost");
        assertThat(topicConfig.getBrokerPort()).isEqualTo("aPort");
    }

    @Test
    public void shouldUseNullValuesForBrokerHostnameAndPortForBrokerConfigResettedToNull() {
        // GIVEN
        final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
        final KafkaTopicConfig topicConfig = new KafkaTopicConfig();

        // WHEN
        topicConfig.setRelatedConfig(brokerConfig);
        topicConfig.setRelatedConfig(null);

        // THEN
        assertThat(topicConfig.getBrokerHostName()).isNull();
        assertThat(topicConfig.getBrokerPort()).isNull();
    }

    @Test
    public void shouldGenerateTooltipInfoFromDefaultTopicConfig() {
        // GIVEN
        final KafkaTopicConfig topicConfig = new KafkaTopicConfig();
        //topicConfig.setTopicName("t");

        // WHEN
        final String toolTip = topicConfig.toolTipInfoProperty().get();

        // THEN
        assertThat(toolTip).isEqualTo("topic       :'test'");
    }
    @Test
    public void shouldGenerateTooltipInfoFromTopicConfigWithDefautBrokerConfigConfigured() {
        // GIVEN
        final KafkaTopicConfig topicConfig = new KafkaTopicConfig();
        final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
        topicConfig.setRelatedConfig(brokerConfig);

        // WHEN
        final String toolTip = topicConfig.toolTipInfoProperty().get();

        // THEN
        assertThat(toolTip).isEqualTo("topic       :'test'\n" +
                                      "broker host : localhost:9092");
    }

    @Test
    public void shouldAssignNewUuid() {
        final KafkaTopicConfig config = new KafkaTopicConfig();
        final String oldUuid = config.getUuid();
        config.assignNewUuid();
        assertThat(oldUuid).isNotEqualTo(config.getUuid());
    }

    @Test
    public void shouldReturnObjectTypeName() {
        assertThat(new KafkaTopicConfig().getObjectTypeName()).isEqualTo("Topic configuration");
    }
}
