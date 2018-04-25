package application.kafka.cluster;

import application.exceptions.ClusterConfigurationError;
import application.utils.HostInfo;
import application.utils.HostPortValue;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class KafkaClusterProxies {
    private final Map<HostPortValue, KafkaClusterProxy> hostPortToProxy = new HashMap<>();
    private final Map<HostInfo, ObjectProperty<KafkaClusterProxy>> hostInfoToBrokerProperty = new HashMap<>();

    public KafkaClusterProxies() {
    }

    public KafkaClusterProxy getRefreshed(HostInfo hostInfo) throws InterruptedException,
                                                                    ExecutionException,
                                                                    TimeoutException,
                                                                    ClusterConfigurationError {
        // setting to 'null' first because getting new object might result in exception
        // in that case we want getAsProperty() return empty proxy (null)
        getAsProperty(hostInfo).set(null);
        final KafkaClusterProxy newProxy = getNewOrRefreshed(hostInfo);
        getAsProperty(hostInfo).set(newProxy);
        return newProxy;
    }

    public KafkaClusterProxy get(HostInfo hostInfo) {
        return getAsProperty(hostInfo).get();
    }

    public ObjectProperty<KafkaClusterProxy> getAsProperty(HostInfo hostInfo) {
        if (!hostInfoToBrokerProperty.containsKey(hostInfo)) {
            hostInfoToBrokerProperty.put(hostInfo, new SimpleObjectProperty<>());
        }
        return hostInfoToBrokerProperty.get(hostInfo);
    }

    private KafkaClusterProxy getNewOrRefreshed(HostInfo hostInfo) throws ClusterConfigurationError,
                                                                          ExecutionException,
                                                                          TimeoutException,
                                                                          InterruptedException {
        final HostPortValue hostPort = HostPortValue.from(hostInfo);

        closeOldProxyIfExistsFor(hostPort);
        final KafkaClusterProxy newProxy = KafkaClusterProxyFactory.create(hostPort, hostPortToProxy.getOrDefault(hostPort, null));
        hostPortToProxy.put(hostPort, newProxy);
        return newProxy;
    }

    private void closeOldProxyIfExistsFor(HostPortValue hostPort) {
        if (hostPortToProxy.containsKey(hostPort)) {
            KafkaClusterProxy oldProxy = hostPortToProxy.get(hostPort);
            oldProxy.close();
        }
    }

}
