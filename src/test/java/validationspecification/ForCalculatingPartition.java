package validationspecification;

import application.model.modelobjects.KafkaSenderConfig;
import application.utils.ValidationStatus;
import application.utils.Validations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ForCalculatingPartition extends ValidationTestBase {


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


    @BeforeMethod
    private void setUpAllConfigs() {
        configureValidSenderConfigWithBrokerProxy();
    }


    protected KafkaSenderConfig getFullyConfiguredSenderConfig() {
        return senderConfig;

    }

    private KafkaSenderConfig configWithDisabledMessageKey() {
        final KafkaSenderConfig config = getFullyConfiguredSenderConfig();
        config.messageKeyEnabledProperty().set(false);
        return config;
    }

    private KafkaSenderConfig configWithBlankMessageKey() {
        final KafkaSenderConfig config = getFullyConfiguredSenderConfig();
        config.messageKeyEnabledProperty().set(true);
        config.messageKeyProperty().set("");
        return config;
    }
}
