package application.kafka.cluster;



public class TopicsOffsetInfo {
    private String topicName;
    private String beginOffset;
    private String endOffset;
    private String consumerGroup;
    private int partition;

    public TopicsOffsetInfo(String topicName,
                            String beginOffset,
                            String endOffset,
                            String consumerGroup,
                            int partition) {
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

    public int getPartition() {
        return this.partition;
    }
}
