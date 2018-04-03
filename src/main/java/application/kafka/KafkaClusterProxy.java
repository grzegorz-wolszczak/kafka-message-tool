package application.kafka;

import application.kafka.dto.AssignedConsumerInfo;
import application.kafka.dto.ClusterNodeInfo;
import application.kafka.dto.TopicAggregatedSummary;
import application.kafka.dto.UnassignedConsumerInfo;
import org.apache.kafka.clients.admin.ConfigEntry;

import java.util.Set;
import java.util.function.Consumer;

public interface KafkaClusterProxy {

    //ClusterStateSummary getClusterSummary();

    void reportInvalidClusterConfigurationTo(Consumer<String> problemReporter);

    void createTopic(String topicName, int partitionNumber, int replicationFactor) throws Exception;

    void deleteTopic(String topicName) throws Exception;

    TriStateConfigEntryValue isTopicAutoCreationEnabled();

    TriStateConfigEntryValue isTopicDeletionEnabled();

    boolean hasTopic(String topicName);

    Set<ConfigEntry> getTopicProperties(String topicName);

    void close();

    Set<AssignedConsumerInfo> getConsumersForTopic(String topicName);

    Set<UnassignedConsumerInfo> getUnassignedConsumersInfo();

    Set<ClusterNodeInfo> getNodesInfo();

    Set<TopicAggregatedSummary> getAggregatedTopicSummary();

    int partitionsForTopic(String topicName);
}
