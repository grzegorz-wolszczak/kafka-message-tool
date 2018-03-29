
package application.kafka.dto;

public final class TopicAggregatedSummary {
    public String getTopicName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPartitionsCount(int partitionsCount) {
        this.partitionsCount = partitionsCount;
    }

    public int getConsumerGroupsCount() {
        return consumerGroupsCount;
    }

    public void setConsumerGroupsCount(int consumerGroupsCount) {
        this.consumerGroupsCount = consumerGroupsCount;
    }

    public int getConsumersCount() {
        return consumersCount;
    }

    public void setConsumersCount(int consumersCount) {
        this.consumersCount = consumersCount;
    }

    private String name;
    private int partitionsCount;
    private int consumerGroupsCount;
    private int consumersCount;

    public int getPartitionsCount() {
        return partitionsCount;
    }
}
