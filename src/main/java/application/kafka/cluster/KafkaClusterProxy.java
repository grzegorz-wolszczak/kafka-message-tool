package application.kafka.cluster;

import application.exceptions.ClusterConfigurationError;
import application.kafka.dto.AssignedConsumerInfo;
import application.kafka.dto.ClusterNodeInfo;
import application.kafka.dto.TopicAggregatedSummary;
import application.kafka.dto.TopicToAdd;
import application.kafka.dto.UnassignedConsumerInfo;
import javafx.collections.ObservableList;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConfigEntry;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public interface KafkaClusterProxy {

    void reportInvalidClusterConfigurationTo(Consumer<String> problemReporter);

    void createTopic(TopicToAdd topicToAdd) throws Exception;

    void deleteTopic(String topicName) throws Exception;

    TriStateConfigEntryValue isTopicAutoCreationEnabled();

    TriStateConfigEntryValue isTopicDeletionEnabled();

    boolean hasTopic(String topicName);

    Set<ConfigEntry> getTopicProperties(String topicName);

    void close();

    Set<AssignedConsumerInfo> getConsumersForTopic(String topicName);

    Set<UnassignedConsumerInfo> getUnassignedConsumersInfo();

    Set<ClusterNodeInfo> getNodesInfo();

    ObservableList<TopicsOffsetInfo> getTopicOffsetsInfo();

    Set<TopicAggregatedSummary> getAggregatedTopicSummary();

    int partitionsForTopic(String topicName);


    void refresh(TopicAdmin topicAdmin, AdminClient kafkaClientAdminClient, kafka.admin.AdminClient kafkaAdminClient) throws ClusterConfigurationError, InterruptedException, ExecutionException, TimeoutException;
}
