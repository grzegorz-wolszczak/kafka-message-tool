package application.kafka.cluster;


import lombok.ToString;

@ToString
public class TopicsOffsetInfo {
    private String consumerGroup;
    private String topicName;
    private String partition;
    private String beginOffset;
    private String endOffset;

    public TopicsOffsetInfo(String topicName,
                            String beginOffset,
                            String endOffset,
                            String consumerGroup,
                            String partition) {
        this.topicName = topicName;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.consumerGroup = consumerGroup;
        this.partition = partition;
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
