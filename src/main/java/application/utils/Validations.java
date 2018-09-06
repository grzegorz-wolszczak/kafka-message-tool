package application.utils;

import application.kafka.cluster.KafkaClusterProxiesBase;
import application.kafka.cluster.KafkaClusterProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import org.apache.commons.lang3.StringUtils;

public class Validations {
    public static ValidationStatus validateForCalculatingPartition(KafkaSenderConfig config,
                                                                   KafkaClusterProxiesBase clusterProxies) {

        final ValidationStatus forSending = validateForSendingMessage(config, clusterProxies);
        if (forSending.isFailure()) {
            return forSending;
        }

        final ValidationStatus x = validateMessageKey(config);
        if (x.isFailure()) {
            return x;
        }
        return ValidationStatus.success();
    }

    public static ValidationStatus validateForSendingMessage(KafkaSenderConfig config,
                                                             KafkaClusterProxiesBase clusterProxies) {

        final KafkaTopicConfig topicConfig = config.getRelatedConfig();
        if (topicConfig == null) {
            return ValidationStatus.failure("Topic config not set");
        }

        final ValidationStatus brokerValidationStatus = validateBrokerConfigValidAndStatusIsKnown(topicConfig, clusterProxies);
        if (brokerValidationStatus.isFailure()) {
            return brokerValidationStatus;
        }

        final KafkaBrokerConfig brokerConfig = topicConfig.getRelatedConfig();
        final KafkaClusterProxy kafkaClusterProxy = clusterProxies.get(brokerConfig.getHostInfo());

        final String topicName = topicConfig.getTopicName();
        if (!kafkaClusterProxy.hasTopic(topicName)) {
            return ValidationStatus.failure(
                String.format("Topic '%s' does not exist on broker", topicName)
            );
        }
        return ValidationStatus.success();
    }

    private static ValidationStatus validateBrokerConfigValidAndStatusIsKnown(KafkaTopicConfig topicConfig,
                                                                              KafkaClusterProxiesBase clusterProxies) {
        final KafkaBrokerConfig brokerConfig = topicConfig.getRelatedConfig();
        if (brokerConfig == null) {
            return ValidationStatus.failure("Broker config not set");
        }

        final KafkaClusterProxy kafkaClusterProxy = clusterProxies.get(brokerConfig.getHostInfo());
        if (kafkaClusterProxy == null) {
            return ValidationStatus.failure("Broker status unknown");
        }

        return ValidationStatus.success();
    }

    private static ValidationStatus validateMessageKey(KafkaSenderConfig config) {
        if (!config.getMessageKeyEnabled()) {
            return ValidationStatus.failure("Message key is not set");
        }

        if (StringUtils.isBlank(config.getMessageKey())) {
            return ValidationStatus.failure("Message key is blank");
        }
        return ValidationStatus.success();
    }
}
