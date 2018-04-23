package application.kafka.cluster;

import application.exceptions.ClusterConfigurationError;
import application.utils.HostPortValue;
import kafka.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class KafkaClusterProxyFactory {

    public static DefaultKafkaClusterProxy create(HostPortValue hostPort) throws ClusterConfigurationError,
                                                                                 ExecutionException,
                                                                                 TimeoutException,
                                                                                 InterruptedException {
        final AdminClient kafkaAdminClient = createKafkaAdminClient(hostPort);
        final org.apache.kafka.clients.admin.AdminClient kafkaClientAdminClient = createKafkaClientAdminClient(hostPort);
        final TopicAdmin topicAdmin = new TopicAdmin(kafkaClientAdminClient);
        return new DefaultKafkaClusterProxy(hostPort, topicAdmin, kafkaClientAdminClient, kafkaAdminClient);
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
