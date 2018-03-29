package application.kafka;

import kafka.server.KafkaConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NodesConfigProperties {
    private static final Set<String> EMPTY_SET = Collections.emptySet();
    private static final Set<String> CLUSTER_PROPERTIES_THAT_CAN_DIFFER = getClusterPropertiesThatCanDifferBetweenNodes();
    private final Map<String, Set<String>> nodesConfig = new HashMap<>();

    public static Set<String> getClusterPropertiesThatCanDifferBetweenNodes() {

        return new HashSet<>(Arrays.asList(
            KafkaConfig.LogDirsProp(),
            KafkaConfig.ListenersProp(),
            KafkaConfig.BrokerIdProp(),
            // zookeeper.connect can differ if kafka cluster is connected do zookeeper cluster,
            // not just single zookeeper node

            KafkaConfig.ZkConnectProp()
        ));
    }

    boolean isEmpty() {
        return nodesConfig.isEmpty();
    }

    public void addConfigEntry(String key, String value) {
        if (!nodesConfig.containsKey(key)) {
            nodesConfig.put(key, new HashSet<>());
        }

        Set<String> configValues = nodesConfig.get(key);
        configValues.add(value);
    }

    public void clear() {
        nodesConfig.clear();
    }

    private boolean canPropertyDifferBetweenNodes(String propertyName) {
        for (String canDifferName : CLUSTER_PROPERTIES_THAT_CAN_DIFFER) {
            if (canDifferName.equalsIgnoreCase(propertyName)) {
                return true;
            }
        }
        return false;
    }

    private boolean doesPropertyDiffersBetweenNodes(String propertyName) {
        return nodesConfig.getOrDefault(propertyName, EMPTY_SET).size() > 1;
    }

    public Set<String> getAllPropertiesThatDiffersBetweenNodes() {
        final HashSet<String> result = new HashSet<>();
        nodesConfig.keySet().forEach(propertyName -> {
            if (!canPropertyDifferBetweenNodes(propertyName) && doesPropertyDiffersBetweenNodes(propertyName)) {
                result.add(propertyName);
            }
        });
        return result;
    }

    public ConfigEntryBooleanValue topicAutoCreationEnabled() {
        return getConfigEntryBooleanValue(nodesConfig.get(KafkaConfig.AutoCreateTopicsEnableProp()));
    }

    public ConfigEntryBooleanValue topicDeletionEnabled() {
        return getConfigEntryBooleanValue(nodesConfig.get(KafkaConfig.DeleteTopicEnableProp()));
    }

    private ConfigEntryBooleanValue getConfigEntryBooleanValue(Set<String> strings) {
        if (strings.size() == 0) {
            // if could not get value in settings it might happen that we could not fetch
            // from cluster, e.g. describeCluster was not supported by broker
            //throw new InternalError("Cluster configuration was not fetched yet but we are asking for specific config entry value");
        }
        if (strings.size() == 1) {
            return ConfigEntryBooleanValue.of(strings.stream().findFirst().get().equalsIgnoreCase("true"));
        } else {
            return ConfigEntryBooleanValue.Inconsistent;
        }
    }
}
