
package application.kafka.dto;

import org.apache.kafka.clients.admin.ConfigEntry;

import java.util.Set;

public class ClusterNodeInfo {

    private boolean isController;
    private String nodeId;
    private Set<ConfigEntry> entries;

    public ClusterNodeInfo(boolean isController, String nodeId, Set<ConfigEntry> entries) {
        this.isController = isController;
        this.nodeId = nodeId;
        this.entries = entries;
    }

    public Set<ConfigEntry> getEntries() {
        return entries;
    }

    public String getNodeId() {
        return nodeId;
    }

    public boolean isController() {
        return isController;
    }
}
