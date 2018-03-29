package application.kafka;

import application.kafka.dto.AssignedConsumerInfo;
import application.kafka.dto.ClusterNodeInfo;
import application.kafka.dto.ClusterTopicInfo;
import application.kafka.dto.TopicAggregatedSummary;
import application.kafka.dto.UnassignedConsumerInfo;
import org.apache.kafka.clients.admin.ConfigEntry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class ClusterStateSummary {

    private final Set<ClusterNodeInfo> nodesInfo = new HashSet<>();
    private final Set<ClusterTopicInfo> topicsInfo = new HashSet<>();
    private final Set<AssignedConsumerInfo> assignedConsumersInfo = new HashSet<>();
    private final Set<UnassignedConsumerInfo> unassignedConsumersInfo = new HashSet<>();
    private static final int INVALID_TOPIC = -1;
    private String clusterId;

    public Set<UnassignedConsumerInfo> getUnassignedConsumersInfo() {
        return new HashSet<>(unassignedConsumersInfo);
    }

    public void addUnassignedConsumerInfo(UnassignedConsumerInfo consumerInfo) {
        unassignedConsumersInfo.add(consumerInfo);
    }

    public Set<AssignedConsumerInfo> getConsumersForTopic(String topicName) {
        return assignedConsumersInfo.stream().filter(c -> c.getTopic().equals(topicName)).collect(Collectors.toSet());
    }

    public String getClusterId() {
        return clusterId;
    }

    public int partitionsForTopic(String topicName){
        for (ClusterTopicInfo topicInfo : topicsInfo) {
            if (topicInfo.getTopicName().equalsIgnoreCase(topicName)) {
                return topicInfo.getPartitions().size();
            }
        }
        return INVALID_TOPIC;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public Set<ClusterNodeInfo> getNodesInfo() {
        return nodesInfo;
    }

    public void clear() {
        clusterId = "";
        nodesInfo.clear();
        topicsInfo.clear();
        assignedConsumersInfo.clear();
    }

    public void addNodeInfo(ClusterNodeInfo clusterNodeInfo) {
        nodesInfo.add(clusterNodeInfo);
    }

    public void addTopicInfo(ClusterTopicInfo clusterTopicInfo) {
        topicsInfo.add(clusterTopicInfo);
    }

    public void addAssignedConsumerInfo(AssignedConsumerInfo assignedConsumerInfo) {
        assignedConsumersInfo.add(assignedConsumerInfo);
    }

    public boolean hasTopic(String topicName) {
        for (ClusterTopicInfo topicInfo : topicsInfo) {
            if (topicInfo.getTopicName().equalsIgnoreCase(topicName)) {
                return true;
            }
        }
        return false;
    }

    public Set<ConfigEntry> getTopicProperties(String topicName) {
        // just get first topicsInfo for first node,
        // it should be the same on rest of nodes any way
        for (ClusterTopicInfo clusterTopicInfo : topicsInfo) {
            if (clusterTopicInfo.getTopicName().equals(topicName)) {
                return clusterTopicInfo.getConfigEntries();
            }
        }
        return Collections.emptySet();
    }


    public Set<TopicAggregatedSummary> getAggregatedTopicSummary() {

        final HashSet<TopicAggregatedSummary> summaries = new HashSet<>();

        topicsInfo.forEach(topicInfo -> {
            final String topicName = topicInfo.getTopicName();

            final TopicSummary topicSummary = getTopicSummaryFor(topicName);

            final TopicAggregatedSummary summary = new TopicAggregatedSummary();
            summary.setName(topicName);
            summary.setConsumersCount(topicSummary.consumers.size());
            summary.setConsumerGroupsCount(topicSummary.consumerGroups.size());
            summary.setPartitionsCount(topicInfo.getPartitions().size());
            summaries.add(summary);
        });

        return summaries;
    }

    private TopicSummary getTopicSummaryFor(String topicName) {
        final TopicSummary topicSummary = new TopicSummary();

        assignedConsumersInfo.forEach(info -> {
            if (topicName.equals(info.getTopic())) {
                topicSummary.consumerGroups.add(info.getConsumerGroupId());
                topicSummary.consumers.add(info.getConsumerId());
            }
        });
        return topicSummary;
    }

    private static class TopicSummary {
        Set<String> consumers = new HashSet<>();
        Set<String> consumerGroups = new HashSet<>();
    }
}
