package application.globals;

import application.kafka.KafkaClusterProxy;
import application.utils.HostInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Map;

public class KafkaClusterProxiesProperties {
    private final static Map<String, ObjectProperty<KafkaClusterProxy>> REPO = new HashMap<>();
    public static ObjectProperty<KafkaClusterProxy> get(HostInfo hostInfo) {
        final String key = hostInfo.toHostPortString();
        if (!REPO.containsKey(key)) {
            REPO.put(key, new SimpleObjectProperty<>());
        }
        return REPO.get(key);
    }
}

