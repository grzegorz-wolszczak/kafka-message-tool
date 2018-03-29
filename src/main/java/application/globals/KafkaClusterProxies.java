package application.globals;

import application.exceptions.ClusterConfigurationError;
import application.kafka.KafkaClusterProxy;
import application.kafka.KafkaClusterProxyFactory;
import application.logging.Logger;
import application.utils.HostInfo;
import application.utils.HostPortValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


public class KafkaClusterProxies {
    private static final Map<HostPortValue, KafkaClusterProxy> REPO = new HashMap<>();

    public static KafkaClusterProxy getFreshFor(HostInfo hostInfo) throws InterruptedException,
                                                                          ExecutionException,
                                                                          TimeoutException,
                                                                          ClusterConfigurationError {
        // setting to 'null' first because getting new object might result in exception
        // in that case we want KafkaClusterProxiesProperties.get() stay empty (null)
        KafkaClusterProxiesProperties.get(hostInfo).set(null);
        final KafkaClusterProxy newProxy = getNew(hostInfo);
        KafkaClusterProxiesProperties.get(hostInfo).set(newProxy);
        return newProxy;
    }

    private static KafkaClusterProxy getNew(HostInfo hostInfo) throws ClusterConfigurationError,
                                                                      ExecutionException,
                                                                      TimeoutException,
                                                                      InterruptedException {
        final HostPortValue hostPort = HostPortValue.from(hostInfo);
        Logger.trace(String.format("Creating new broker proxy for '%s'", hostPort.toHostString()));
        closeOldProxyIfExistsFor(hostPort);
        final KafkaClusterProxy newProxy = KafkaClusterProxyFactory.create(hostPort);
        REPO.put(hostPort, newProxy);
        return newProxy;
    }

    private static void closeOldProxyIfExistsFor(HostPortValue hostPort) {
        if (REPO.containsKey(hostPort)) {
            KafkaClusterProxy oldProxy = REPO.get(hostPort);
            oldProxy.close();
            REPO.remove(hostPort);
        }
    }


}
