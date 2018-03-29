package application.kafka.dto;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.common.TopicPartitionInfo;

import java.util.List;
import java.util.Set;

public class ClusterTopicInfo {

    private final String topicName;
    private final List<TopicPartitionInfo> partitions;
    private final Set<ConfigEntry> configEntries;

    public ClusterTopicInfo(String topicName,
                            List<TopicPartitionInfo> partitions,
                            Set<ConfigEntry> configEntries) {

        this.topicName = topicName;
        this.partitions = partitions;
        this.configEntries = configEntries;
    }

    public Set<ConfigEntry> getConfigEntries() {
        return configEntries;
    }

    public List<TopicPartitionInfo> getPartitions() {
        return partitions;
    }

    public String getTopicName() {
        return topicName;
    }

}
