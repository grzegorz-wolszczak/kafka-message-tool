package validationspecification;

import application.kafka.cluster.KafkaClusterProxies;
import application.kafka.cluster.KafkaClusterProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import autofixture.publicinterface.Any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationTestBase {
    protected final KafkaBrokerConfig brokerConfig = new KafkaBrokerConfig();
    protected final KafkaTopicConfig topicConfig = new KafkaTopicConfig();
    protected final KafkaSenderConfig senderConfig = new KafkaSenderConfig();
    protected final KafkaClusterProxies clusterProxies = mock(KafkaClusterProxies.class);
    protected final KafkaClusterProxy clusterProxy = mock(KafkaClusterProxy.class);
    protected String topicName;

    protected void configureValidSenderConfigWithBrokerProxy() {
        topicName = Any.string();

        senderConfig.setMessageKey("abc");
        senderConfig.setMessageKeyEnabled(true);
        senderConfig.setMsgContentTemplate("abc");
        senderConfig.setName("some senderConfig name");
        senderConfig.setRunBeforeAllMessagesScript("aaa");
        senderConfig.setRunBeforeEachMessageScript("bbb");
        senderConfig.setUuid("uuid");
        senderConfig.setRelatedConfig(topicConfig);

        topicConfig.setName("abc");
        topicConfig.setTopicName(topicName);
        topicConfig.setRelatedConfig(brokerConfig);

        brokerConfig.setHostname("hostname");
        brokerConfig.setName("some name");
        brokerConfig.setPort("124");

        when(clusterProxies.get(brokerConfig.getHostInfo())).thenReturn(clusterProxy);
        when(clusterProxy.hasTopic(topicName)).thenReturn(true);
        when(clusterProxy.areAdvertisedListenersValid()).thenReturn(true);
    }
}
