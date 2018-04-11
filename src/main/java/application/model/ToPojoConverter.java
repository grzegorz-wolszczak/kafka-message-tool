package application.model;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;

public class ToPojoConverter {
    public static BrokerConfigPojo toPojoFrom(KafkaBrokerConfig config) {
        BrokerConfigPojo pojo = new BrokerConfigPojo();
        pojo.setConfigName(config.getName());
        pojo.setKafkaBrokerHostName(config.getHostname());
        pojo.setKafkaBrokerPort(config.getPort());
        pojo.setUuid(config.getUuid());
        return pojo;
    }

    public static SenderConfigPojo toPojoFrom(KafkaSenderConfig config) {
        final SenderConfigPojo pojo = new SenderConfigPojo();
        pojo.setContent(config.getMsgContentTemplate());
        pojo.setMessageKeyEnabled(config.isMessageKeyEnabled());
        pojo.setMessageKey(config.getMessageKey());
        pojo.setConfigName(config.getName());
        pojo.setTopicConfigUuid(config.getTopicConfigUuid());
        pojo.setRepeatCount(config.getRepeatCount().toString());
        pojo.setRunBeforeEachMessageScript(config.getRunBeforeEachMessageScript());
        pojo.setRunBeforeAllMessagesScript(config.getRunBeforeAllMessagesScript());
        pojo.setUuid(config.getUuid());
        pojo.setSendingSimulationModeEnabled(config.getSendingSimulationModeEnabled());
        return pojo;
    }

    public static TopicConfigPojo toPojoFrom(KafkaTopicConfig config) {
        TopicConfigPojo pojo = new TopicConfigPojo();
        pojo.setBrokerUuid(config.getBrokerUuid());
        pojo.setConfigName(config.getName());
        pojo.setTopicName(config.getTopicName());
        pojo.setUuid(config.getUuid());
        return pojo;
    }

    public static ListenerConfigPojo toPojoFrom(KafkaListenerConfig config) {
        ListenerConfigPojo pojo = new ListenerConfigPojo();
        pojo.setOffsetResetConfig(config.getOffsetResetConfig().name().toLowerCase());
        pojo.setPollTimeout(config.getPollTimeout());
        pojo.setConsumerGroup(config.getConsumerGroup());
        pojo.setTopicConfigUuid(config.getTopicConfigUuid());
        pojo.setConfigName(config.getName());
        pojo.setUuid(config.getUuid());
        pojo.setReceivedMsgLimitEnabled(config.getReceivedMsgLimitEnabled());
        pojo.setReceivedMsgLimitCount(config.getReceivedMsgLimitCount());
        return pojo;
    }
}
