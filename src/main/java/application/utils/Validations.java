package application.utils;

import application.kafka.KafkaClusterProxies;
import application.kafka.KafkaClusterProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import org.apache.commons.lang3.StringUtils;

public class Validations {
    public static ValidationStatus validateForCalculatingPartition(KafkaSenderConfig config,
                                                                   KafkaClusterProxies clusterProxies) {
        if (!config.getMessageKeyEnabled()) {
            return ValidationStatus.failure("Message key is not set");
        }

        if (StringUtils.isBlank(config.getMessageKey())) {
            return ValidationStatus.failure("Message key is blank");
        }

        final KafkaTopicConfig topicConfig = config.getRelatedConfig();
        if (topicConfig == null) {
            return ValidationStatus.failure("Topic config not set");
        }

        final KafkaBrokerConfig brokerConfig = topicConfig.getRelatedConfig();
        if (brokerConfig == null) {
            return ValidationStatus.failure("Broker config not set");
        }

        final KafkaClusterProxy kafkaClusterProxy = clusterProxies.get(brokerConfig.getHostInfo());
        if (kafkaClusterProxy == null) {
            return ValidationStatus.failure("Broker status unknown");
        }

        final String topicName = topicConfig.getTopicName();
        if (!kafkaClusterProxy.hasTopic(topicName)) {
            return ValidationStatus.failure(
                String.format("Topic '%s' does not exist on broker", topicName)
            );
        }
        return ValidationStatus.success();
    }
}
