package application.kafka.cluster;

import application.exceptions.ClusterConfigurationError;
import application.logging.Logger;
import application.utils.HostPortValue;
import kafka.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class KafkaClusterProxyFactory {

    public static KafkaClusterProxy create(HostPortValue hostPort,
                                                  KafkaClusterProxy previous) throws ClusterConfigurationError,
                                                                                            ExecutionException,
                                                                                            TimeoutException,
                                                                                            InterruptedException {
        KafkaClusterProxy proxy = null;
        if (previous != null) {
            Logger.trace(String.format("[Proxy create] Reusing already existing broker proxy for '%s'", hostPort.toHostString()));
            proxy = previous;
        } else {
            Logger.trace(String.format("[Proxy create] Creating new broker proxy for '%s'", hostPort.toHostString()));
            proxy = new DefaultKafkaClusterProxy(hostPort);
        }

        reinitialize(hostPort, proxy);
        return proxy;

    }

    public static void reinitialize(HostPortValue hostPort, KafkaClusterProxy proxy) throws ClusterConfigurationError,
                                                                                                   ExecutionException,
                                                                                                   TimeoutException,
                                                                                                   InterruptedException {
        final AdminClient kafkaAdminClient = createKafkaAdminClient(hostPort);
        final org.apache.kafka.clients.admin.AdminClient kafkaClientAdminClient = createKafkaClientAdminClient(hostPort);
        final TopicAdmin topicAdmin = new TopicAdmin(kafkaClientAdminClient);
        proxy.refresh(topicAdmin, kafkaClientAdminClient, kafkaAdminClient);
    }

    private static AdminClient createKafkaAdminClient(HostPortValue hostPort) {
        return AdminClient.createSimplePlaintext(hostPort.toHostString());
    }

    private static org.apache.kafka.clients.admin.AdminClient createKafkaClientAdminClient(HostPortValue hostPort) {
        final Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, hostPort.toHostString());
        return org.apache.kafka.clients.admin.AdminClient.create(props);
    }
}
