package application.customfxwidgets.consumergroupview;

public class ConsumerGroupDetailRecord {
    private final String topicName;
    private final String partitionNum;
    private final String currentOffset;
    private final String logEndOffset;
    private final String lag;
    private final String consumerId;
    private final String host;
    private final String clientId;
    private final String consumerGroupId;
    public ConsumerGroupDetailRecord(String topicName,
                                     String partitionNum,
                                     String currentOffset,
                                     String logEndOffset,
                                     String lag,
                                     String consumerId,
                                     String host,
                                     String clientId, String consumerGroupId) {
        this.topicName = topicName;
        this.partitionNum = partitionNum;
        this.currentOffset = currentOffset;
        this.logEndOffset = logEndOffset;
        this.lag = lag;
        this.consumerId = consumerId;
        this.host = host;
        this.clientId = clientId;
        this.consumerGroupId = consumerGroupId;
    }

    public String getLag() {
        return lag;
    }

    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getPartitionNum() {
        return partitionNum;
    }

    public String getCurrentOffset() {
        return currentOffset;
    }

    public String getLogEndOffset() {
        return logEndOffset;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public String getHost() {
        return host;
    }

    public String getClientId() {
        return clientId;
    }
}
