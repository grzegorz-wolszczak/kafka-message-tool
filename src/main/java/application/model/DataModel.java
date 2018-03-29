package application.model;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;

// not sure yet where to hold this objects
public class DataModel{

    private final ModelObjectCollection<KafkaBrokerConfig> brokerConfigs = new ModelConfigObjects<>("Brokers");
    private final ModelObjectCollection<KafkaTopicConfig> topicConfigs = new ModelConfigObjects<>("Topics");
    private final ModelObjectCollection<KafkaSenderConfig> senderConfigs = new ModelConfigObjects<>("Senders");
    private final ModelObjectCollection<KafkaListenerConfig> listenerConfigs = new ModelConfigObjects<>("Listeners");


    public ModelObjectCollection<KafkaSenderConfig> getSenderConfigs() {
        return senderConfigs;
    }

    public ModelObjectCollection<KafkaTopicConfig> getTopicConfigs() {
        return topicConfigs;
    }

    public ModelObjectCollection<KafkaListenerConfig> getListenerConfigs() {
        return listenerConfigs;
    }

    public ModelObjectCollection<KafkaBrokerConfig> getBrokerConfigs() {
        return brokerConfigs;
    }


}


//KafkaListenerConfig