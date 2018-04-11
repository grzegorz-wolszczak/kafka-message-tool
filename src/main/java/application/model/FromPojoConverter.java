package application.model;

import application.constants.ApplicationConstants;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class FromPojoConverter {

    public static final String DEFAULT_POLL_TIMEOUT = "2000";
    public static final KafkaOffsetResetType DEFAULT_OFFSET_RESET_TYPE = KafkaOffsetResetType.EARLIEST;
    private static final int DEFAULT_REPEAT_COUNT = 1;
    private static final String EMPTY_STRING = "";
    private ModelDataProxy modelProxy;

    public FromPojoConverter(ModelDataProxy modelProxy) {
        this.modelProxy = modelProxy;
    }

    public KafkaBrokerConfig fromPojo(BrokerConfigPojo pojo) {
        KafkaBrokerConfig c = new KafkaBrokerConfig();
        setBrokerConfigDefaults(c);

        if (StringUtils.isNotBlank(pojo.getUuid())) {
            c.setUuid(pojo.getUuid());
        }

        if (StringUtils.isNotBlank(pojo.getConfigName())) {
            c.setName(pojo.getConfigName());
        }
        if (StringUtils.isNotBlank(pojo.getKafkaBrokerHostName())) {
            c.setHostname(pojo.getKafkaBrokerHostName());
        }

        if (StringUtils.isNotBlank(pojo.getKafkaBrokerPort())) {
            c.setPort(pojo.getKafkaBrokerPort());
        }

        return c;
    }

    public KafkaSenderConfig fromPojo(SenderConfigPojo pojo) {
        KafkaSenderConfig c = new KafkaSenderConfig();
        setSenderConfigDefaults(c);
        if (StringUtils.isNotBlank(pojo.getContent())) {
            c.setMsgContentTemplate(pojo.getContent());
        }

        if (StringUtils.isNotBlank(pojo.getMessageKey())) {
            c.setMessageKey(pojo.getMessageKey());
        }
        if (StringUtils.isNotBlank(pojo.getConfigName())) {
            c.setName(pojo.getConfigName());
        }
        if (StringUtils.isNotBlank(pojo.getUuid())) {
            c.setUuid(pojo.getUuid());
        }
        c.setMessageKeyEnabled(pojo.isMessageKeyEnabled());
        if (StringUtils.isNotBlank(pojo.getRepeatCount())) {
            c.setRepeatCount(getUnsignedIntegerAsStringOrDefault(pojo.getRepeatCount(),
                                                                 DEFAULT_REPEAT_COUNT));
        }
        if (StringUtils.isNotBlank(pojo.getTopicConfigUuid())) {
            final String topicConfigUuid = pojo.getTopicConfigUuid();
            modelProxy.getTopicConfigByUuid(topicConfigUuid)
                .ifPresent(c::setRelatedConfig);
        }
        if (StringUtils.isNotBlank(pojo.getRunBeforeAllMessagesScript())) {
            c.setRunBeforeAllMessagesScript(pojo.getRunBeforeAllMessagesScript());
        }

        if (StringUtils.isNotBlank(pojo.getRunBeforeEachMessageScript())) {
            c.setRunBeforeEachMessageScript(pojo.getRunBeforeEachMessageScript());
        }
        c.setSendingSimulationModeEnabled(pojo.getSendingSimulationModeEnabled());


        return c;
    }

    public KafkaListenerConfig fromPojo(ListenerConfigPojo pojo) {

        KafkaListenerConfig c = new KafkaListenerConfig();
        setListenerConfigDefaults(c);
        if (StringUtils.isNotBlank(pojo.getConsumerGroup())) {
            c.setConsumerGroup(pojo.getConsumerGroup());
        }
        if (StringUtils.isNotBlank(pojo.getConfigName())) {
            c.setName(pojo.getConfigName());
        }
        if (StringUtils.isNotBlank(pojo.getUuid())) {
            c.setUuid(pojo.getUuid());
        }
        if (StringUtils.isNotBlank(pojo.getTopicConfigUuid())) {
            final String topicConfigUuid = pojo.getTopicConfigUuid();
            modelProxy.getTopicConfigByUuid(topicConfigUuid)
                .ifPresent(c::setRelatedConfig);
        }
        if (StringUtils.isNotBlank(pojo.getPollTimeout())) {
            c.setPollTimeout(getUnsignedIntegerValueAsStringOrDefault(pojo.getPollTimeout(),
                                                                      DEFAULT_POLL_TIMEOUT));
        }

        if (StringUtils.isNotBlank(pojo.getOffsetResetConfig())) {
            c.setOffsetResetConfig(getResetTypeOfDefaultIfInvalidValue(pojo));
        }
        if (StringUtils.isNotBlank(pojo.getReceivedMsgLimitCount())) {
            c.setReceivedMsgLimitCount(pojo.getReceivedMsgLimitCount());
        }

        c.setReceivedMsgLimitEnabled(pojo.getReceivedMsgLimitEnabled());

        return c;
    }

    public KafkaTopicConfig fromPojo(TopicConfigPojo pojo) {
        final KafkaTopicConfig c = new KafkaTopicConfig();
        setKafkaTopicConfigDefaults(c);

        if (StringUtils.isNotBlank(pojo.getConfigName())) {
            c.setName(pojo.getConfigName());
        }

        if (StringUtils.isNotBlank(pojo.getTopicName())) {
            c.setTopicName(pojo.getTopicName());
        }

        if (StringUtils.isNotBlank(pojo.getUuid())) {
            c.setUuid(pojo.getUuid());
        }

        if (StringUtils.isNotBlank(pojo.getBrokerUuid())) {
            modelProxy.getBrokerConfigByUuid(pojo.getBrokerUuid())
                .ifPresent(c::setRelatedConfig);
        }
        return c;
    }

    private KafkaOffsetResetType getResetTypeOfDefaultIfInvalidValue(ListenerConfigPojo pojo) {
        try {
            return KafkaOffsetResetType.valueOf(pojo.getOffsetResetConfig().toUpperCase());
        } catch (Exception e) {
            return DEFAULT_OFFSET_RESET_TYPE;
        }

    }

    private String getUnsignedIntegerValueAsStringOrDefault(String value, String defaultValue) {
        try {
            return String.valueOf(Integer.parseUnsignedInt(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Integer getUnsignedIntegerAsStringOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void setKafkaTopicConfigDefaults(KafkaTopicConfig c) {
        c.setTopicName(ApplicationConstants.DEFAULT_NEW_TOPIC_NAME);
        c.setName(ApplicationConstants.DEFAULT_TOPIC_CONFIG_NAME);
        c.setUuid(UUID.randomUUID().toString());
    }

    private void setListenerConfigDefaults(KafkaListenerConfig c) {
        c.setConsumerGroup(ApplicationConstants.DEFAULT_CONSUMER_GROUP_ID);
        c.setPollTimeout(DEFAULT_POLL_TIMEOUT);
        c.setOffsetResetConfig(DEFAULT_OFFSET_RESET_TYPE);
        c.setName(ApplicationConstants.DEFAULT_LISTENER_CONFIG_NAME);
        c.setUuid(UUID.randomUUID().toString());
        c.setReceivedMsgLimitEnabled(false);
        c.setReceivedMsgLimitCount("1");
    }

    private void setSenderConfigDefaults(KafkaSenderConfig c) {
        c.setMessageKey(EMPTY_STRING);
        c.setMessageKeyEnabled(false);
        c.setMsgContentTemplate(EMPTY_STRING);
        c.setRepeatCount(DEFAULT_REPEAT_COUNT);
        c.setName(ApplicationConstants.DEFAULT_SENDER_CONFIG_NAME);
        c.setUuid(UUID.randomUUID().toString());
        c.setRunBeforeAllMessagesScript(EMPTY_STRING);
        c.setRunBeforeEachMessageScript(EMPTY_STRING);
        c.setSendingSimulationModeEnabled(false);
    }

    private void setBrokerConfigDefaults(KafkaBrokerConfig c) {
        c.setPort(ApplicationConstants.DEFAULT_PORT_AS_STRING);
        c.setHostname(ApplicationConstants.DEFAULT_HOSTNAME);
        c.setName(ApplicationConstants.DEFAULT_BROKER_CONFIG_NAME);
        c.setUuid(UUID.randomUUID().toString());
    }
}
