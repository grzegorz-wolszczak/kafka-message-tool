package validationspecification;

import application.utils.ValidationStatus;
import application.utils.Validations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ForSendingMessage extends ValidationTestBase{
    @BeforeMethod
    private void setUpAllConfigs() {
        configureValidSenderConfigWithBrokerProxy();
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
        when(clusterProxy.hasTopic(topicName)).thenReturn(false);

        // WHEN
        ValidationStatus s = Validations.validateForCalculatingPartition(senderConfig, clusterProxies);

        // THEN
        assertThat(s.validationFailureMessage()).isEqualTo(String.format("Topic '%s' does not exist on broker", topicName));

    }

}
