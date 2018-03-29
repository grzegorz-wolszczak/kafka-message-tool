package application.model;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;

import java.util.List;
import java.util.Optional;

public interface ModelDataProxy {
    void addConfig(KafkaBrokerConfig kafkaBrokerConfig);

    void addConfig(KafkaTopicConfig config);

    void addConfig(KafkaSenderConfig message);

    void addConfig(KafkaListenerConfig config);


    Optional<KafkaTopicConfig> getTopicConfigByUuid(String uuid);

    Optional<KafkaBrokerConfig> getBrokerConfigByUuid(String brokerUuid);

    List<BrokerConfigPojo> brokerConfigPojos();

    List<TopicConfigPojo> topicConfigPojos();

    List<ListenerConfigPojo> listenerConfigPojos();

    List<SenderConfigPojo> messagesConfigPojos();
}
