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
import java.util.stream.Collectors;

public class DefaultModelDataProxy implements ModelDataProxy {
    private final DataModel dataModel;


    public DefaultModelDataProxy(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    public void addConfig(KafkaSenderConfig message) {
        dataModel.getSenderConfigs().add(message);
    }


    @Override
    public void addConfig(KafkaTopicConfig config) {
        dataModel.getTopicConfigs().add(config);
    }

    @Override
    public Optional<KafkaTopicConfig> getTopicConfigByUuid(String uuid) {
        return dataModel.getTopicConfigs().getByUuid(uuid);
    }

    @Override
    public void addConfig(KafkaBrokerConfig config) {
        dataModel.getBrokerConfigs().add(config);
    }

    @Override
    public Optional<KafkaBrokerConfig> getBrokerConfigByUuid(String uuid) {
        return dataModel.getBrokerConfigs().getByUuid(uuid);
    }

    @Override
    public void addConfig(KafkaListenerConfig config) {
        dataModel.getListenerConfigs().add(config);
    }


    @Override
    public List<BrokerConfigPojo> brokerConfigPojos() {
        return dataModel.getBrokerConfigs().getObservables().stream().map(ToPojoConverter::toPojoFrom).collect(Collectors.toList());
    }

    @Override
    public List<TopicConfigPojo> topicConfigPojos() {
        return dataModel.getTopicConfigs().getObservables().stream().map(ToPojoConverter::toPojoFrom).collect(Collectors.toList());
    }

    @Override
    public List<ListenerConfigPojo> listenerConfigPojos() {
        return dataModel.getListenerConfigs().getObservables().stream().map(ToPojoConverter::toPojoFrom).collect(Collectors.toList());
    }

    @Override
    public List<SenderConfigPojo> messagesConfigPojos() {
        return dataModel.getSenderConfigs().getObservables().stream().map(ToPojoConverter::toPojoFrom).collect(Collectors.toList());
    }

}
