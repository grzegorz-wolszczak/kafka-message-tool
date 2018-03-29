package application.kafka;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.kafka.KafkaBrokerHostInfo;

public class TopicBrokerConfig {
    private KafkaTopicConfig topicConfig;
    private KafkaBrokerConfig brokerConfig;

    public TopicBrokerConfig(KafkaTopicConfig topicConfig,
                             KafkaBrokerConfig brokerConfig) {
        this.topicConfig = topicConfig;
        this.brokerConfig = brokerConfig;
    }

    public KafkaBrokerHostInfo getKafkaBrokerHostInfo() {
        return brokerConfig.getHostInfo();
    }

    public String getTopicName() {
        return topicConfig.getTopicName();
    }
}
