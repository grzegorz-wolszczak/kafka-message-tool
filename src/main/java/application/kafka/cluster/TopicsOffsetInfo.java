package application.kafka.cluster;


import lombok.ToString;

@ToString
public class TopicsOffsetInfo {
    private final String consumerGroup;
    private final String topicName;
    private final String partition;
    private final String beginOffset;
    private final String endOffset;
    private final String topicPartitionMsgCount;
    private final String currentOffset;
    private final String lag;

    public TopicsOffsetInfo(String topicName,
                            String beginOffset,
                            String endOffset,
                            String consumerGroup,
                            String partition,
                            String topicPartitionMsgCount,
                            String currentOffset,
                            String lag) {
        this.topicName = topicName;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.consumerGroup = consumerGroup;
        this.partition = partition;
        this.topicPartitionMsgCount = topicPartitionMsgCount;
        this.currentOffset = currentOffset;
        this.lag = lag;
    }

    public String getCurrentOffset() {
        return currentOffset;
    }

    public String getLag() {
        return lag;
    }

    public String getTopicPartitionMsgCount() {
        return topicPartitionMsgCount;
    }

    public String getTopicName() {
        return topicName;
    }

    public String getBeginOffset() {
        return beginOffset;
    }


    public String getEndOffset() {
        return endOffset;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public String getPartition() {
        return this.partition;
    }

}
