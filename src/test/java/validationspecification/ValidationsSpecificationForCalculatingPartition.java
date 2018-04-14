package validationspecification;

import application.kafka.cluster.KafkaClusterProxies;
import application.kafka.cluster.KafkaClusterProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.ValidationStatus;
import application.utils.Validations;
import autofixture.publicinterface.Any;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationsSpecificationForCalculatingPartition {

    private final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
    private final KafkaTopicConfig topicConfig = new KafkaTopicConfig();
    private final KafkaSenderConfig senderConfig = new KafkaSenderConfig();
    private final KafkaClusterProxies clusterProxies = mock(KafkaClusterProxies.class);
    private final KafkaClusterProxy clusterProxy = mock(KafkaClusterProxy.class);

    @Test
    public void shouldReturnFailureDueToMessageKeyNotSetWhileValidationConfigForCalculatingPartition() {
        // GIVEN
        final KafkaSenderConfig config = configWithDisabledMessageKey();

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(config, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo("Message key is not set");

    }

    @Test
    public void shouldReturnFailureDueToBlankMessageKeyWhileValidationConfigForCalculatingParition() {
        // GIVEN
        final KafkaSenderConfig config = configWithBlankMessageKey();

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(config, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo("Message key is blank");

    }

    @Test
    public void shouldReturnFailureDueToTopicConfigNotSet() {

        // GIVEN
        senderConfig.setRelatedConfig(null);

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(senderConfig, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo("Topic config not set");
    }

    @Test
    public void shouldReturnFailureDueToBrokerConfigNotSet() {
        // GIVEN
        topicConfig.setRelatedConfig(null);

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(senderConfig, clusterProxies);

        // THEN

        assertThat(s.validationFailureMessage()).isEqualTo("Broker config not set");
    }

    @Test
    public void shouldReturnFailureDueToUnknownBrokerStatus() {
        // GIVEN
        when(clusterProxies.get(brokerConfig.getHostInfo())).thenReturn(null);

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(senderConfig, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo("Broker status unknown");
    }

    @Test
    public void shouldReturnFailureBecauseTopicDoesNotExistOnBroker() {
        // GIVEN
        final String topicName = Any.string();
        topicConfig.setTopicName(topicName);
        when(clusterProxies.get(brokerConfig.getHostInfo())).thenReturn(clusterProxy);
        when(clusterProxy.hasTopic(topicName)).thenReturn(false);

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(senderConfig, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo(String.format("Topic '%s' does not exist on broker", topicName));

    }



    @BeforeMethod
    private void setUpAllConfigs() {
        senderConfig.setMessageKey("abc");
        senderConfig.setMessageKeyEnabled(true);
        senderConfig.setMsgContentTemplate("abc");
        senderConfig.setName("some senderConfig name");
        senderConfig.setRunBeforeAllMessagesScript("aaa");
        senderConfig.setRunBeforeEachMessageScript("bbb");
        senderConfig.setUuid("uuid");
        senderConfig.setRelatedConfig(topicConfig);

        topicConfig.setName("abc");
        topicConfig.setTopicName("tc");
        topicConfig.setRelatedConfig(brokerConfig);

        brokerConfig.setHostname("hostname");
        brokerConfig.setName("some name");
        brokerConfig.setPort("124");

    }

    private KafkaSenderConfig getFullyConfiguredSenderConfig() {
        return senderConfig;

    }

    private KafkaSenderConfig configWithDisabledMessageKey() {
        final KafkaSenderConfig config = getFullyConfiguredSenderConfig();
        config.messageKeyEnabledProperty().set(false);
        return config;
    }

    private KafkaSenderConfig configWithBlankMessageKey() {
        final KafkaSenderConfig config = new KafkaSenderConfig();
        config.messageKeyEnabledProperty().set(true);
        config.messageKeyProperty().set("");
        return config;
    }
}
