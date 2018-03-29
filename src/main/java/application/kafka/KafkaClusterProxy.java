package application.kafka;

import java.util.function.Consumer;

public interface KafkaClusterProxy {

    ClusterStateSummary getClusterSummary();

    void reportInvalidClusterConfigurationTo(Consumer<String> problemReporter);

    void createTopic(String topicName, int partitionNumber, int replicationFactor) throws Exception;

    void deleteTopic(String topicName) throws Exception;

    TriStateConfigEntryValue isTopicAutoCreationEnabled();

    TriStateConfigEntryValue isTopicDeletionEnabled();

    void close();
}
