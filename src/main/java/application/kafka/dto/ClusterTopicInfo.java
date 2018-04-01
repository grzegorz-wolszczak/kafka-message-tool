package application.kafka.dto;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.common.TopicPartitionInfo;
import org.apache.kafka.common.config.TopicConfig;

import java.util.List;
import java.util.Optional;
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

    private Optional<ConfigEntry> findByName(String configEntryName)
    {
        final Optional<ConfigEntry> first = configEntries.stream()
            .filter(e -> e.name().equals(configEntryName))
            .findFirst();
        return first;
    }

    public boolean isCompacted(){
        final Optional<ConfigEntry> byName = findByName(TopicConfig.CLEANUP_POLICY_CONFIG);
        return byName.map(configEntry -> configEntry.value().equalsIgnoreCase(TopicConfig.CLEANUP_POLICY_COMPACT)).orElse(false);
    }

}
