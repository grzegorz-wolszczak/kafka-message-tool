package application.model;

import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.HostInfo;

public final class MessageOnTopicDto {
    private final HostInfo brokerHostInfo;
    private final String msgKey;
    private final String topicName;
    private final String message;
    private final boolean shouldSimulateSending;
    private final int msgNum;

    public int getMsgNum() {
        return msgNum;
    }

    public int getTotalMsgCount() {
        return totalMsgCount;
    }

    private final int totalMsgCount;


    private MessageOnTopicDto(HostInfo brokerHostInfo,
                              String msgKey,
                              String topicName,
                              String message,
                              boolean shouldSimulateSending,
                              int msgNum,
                              int totalMsgCount) {

        this.brokerHostInfo = brokerHostInfo;
        this.msgKey = msgKey;
        this.topicName = topicName;
        this.message = message;
        this.shouldSimulateSending = shouldSimulateSending;
        this.msgNum = msgNum;
        this.totalMsgCount = totalMsgCount;
    }

    public boolean shouldSimulateSending() {
        return shouldSimulateSending;
    }

    public static MessageOnTopicDto from(KafkaSenderConfig config,
                                         String message,
                                         boolean shouldSimulateSending,
                                         int msgNum,
                                         int totalMsgCount) {

        final KafkaTopicConfig topicConfig = config.getRelatedConfig();
        final KafkaBrokerConfig brokerConfig = topicConfig.getRelatedConfig();
        String msgKey = config.isMessageKeyEnabled() ? config.getMessageKey() : null;

        return new MessageOnTopicDto(brokerConfig.getHostInfo(),
                                     msgKey,
                                     topicConfig.getTopicName(),
                                     message,
                                     shouldSimulateSending,
                                     msgNum,
                                     totalMsgCount);
    }

    public HostInfo getBrokerHostInfo() {
        return brokerHostInfo;
    }

    public String getMessageKey() {
        return msgKey;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getMessage() {
        return message;
    }
}
