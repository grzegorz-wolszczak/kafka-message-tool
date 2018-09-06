package application.kafka.cluster;

import application.exceptions.ClusterConfigurationError;
import application.utils.HostInfo;
import javafx.beans.property.ObjectProperty;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface KafkaClusterProxiesBase {
    KafkaClusterProxy getRefreshed(HostInfo hostInfo) throws InterruptedException,
            ExecutionException,
            TimeoutException,
            ClusterConfigurationError;

    KafkaClusterProxy get(HostInfo hostInfo);

    ObjectProperty<KafkaClusterProxy> getAsProperty(HostInfo hostInfo);
}
