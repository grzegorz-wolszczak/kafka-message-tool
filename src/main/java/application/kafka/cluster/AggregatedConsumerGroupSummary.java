package application.kafka.cluster;

import java.util.List;

public class AggregatedConsumerGroupSummary {
    public String getConsumerGroupId() {
        return consumerGroupId;
    }

    private final String consumerGroupId;
    private final List<TopicsOffsetInfo> topicOffsets;

    public AggregatedConsumerGroupSummary(String consumerGroupId, List<TopicsOffsetInfo> topicOffsets) {

        this.consumerGroupId = consumerGroupId;
        this.topicOffsets = topicOffsets;
    }
}
