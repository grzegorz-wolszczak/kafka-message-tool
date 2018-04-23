package application.utils;


import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;

public class ConfigNameGenerator {
    public static String generateNewTopicConfigName(KafkaTopicConfig config) {
        final KafkaBrokerConfig brokerConfig = config.getRelatedConfig();
        String brokerName = "<unknown_broker_name>";
        if (brokerConfig != null) {
            brokerName = brokerConfig.getName();
        }
        return String.format("%s@%s", config.getTopicName(), brokerName);
    }

    public static String generateNewSenderConfigName(KafkaSenderConfig config) {
        final String configName = getTopicConfig(config.getRelatedConfig());
        return String.format("send-to:%s", configName);
    }

    private static String getTopicConfig(KafkaTopicConfig relatedConfig) {
        String configName = "<unknown_topic_name>";
        if (relatedConfig != null) {
            configName = generateNewTopicConfigName(relatedConfig);
        }
        return configName;
    }

    public static String generateNewListenerConfigName(KafkaListenerConfig config) {
        final String configName = getTopicConfig(config.getRelatedConfig());
        return String.format("listen-from:%s", configName);
    }
}
