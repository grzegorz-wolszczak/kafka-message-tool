import application.model.FromPojoConverter;
import application.model.KafkaOffsetResetType;
import application.model.ModelDataProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FromPojoConverterSpecification {

    @Mock
    private ModelDataProxy proxy;

    @Test
    public void shouldConvertFromEmptyBrokerConfigPojoToObjectModel() {
        // GIVEN
        FromPojoConverter c = getConverter();
        BrokerConfigPojo emptyPojo = new BrokerConfigPojo();

        // WHEN
        KafkaBrokerConfig converted = c.fromPojo(emptyPojo);

        // THEN
        assertThat(converted.getHostname()).isEqualTo("localhost");
        assertThat(converted.getPort()).isEqualTo("9092");
        assertThat(converted.getName()).isEqualTo("<new broker config>");
        assertThat(converted.getUuid()).isNotBlank();
    }

    @Test
    public void shouldConvertFromBrokerConfigPojoToObjectModel() {
        // GIVEN
        FromPojoConverter c = getConverter();
        BrokerConfigPojo pojo = new BrokerConfigPojo();
        pojo.setConfigName("some_name");
        pojo.setKafkaBrokerHostName("some_hostname");
        pojo.setKafkaBrokerPort("1");
        pojo.setUuid("some_uuid");

        // WHEN
        KafkaBrokerConfig converted = c.fromPojo(pojo);

        // THEN
        assertThat(converted.getHostname()).isEqualTo("some_hostname");
        assertThat(converted.getPort()).isEqualTo("1");
        assertThat(converted.getName()).isEqualTo("some_name");
        assertThat(converted.getUuid()).isEqualTo("some_uuid");
    }

    @Test
    public void shouldConvertFromEmptyTopicConfigPojoToObjectModel() {
        // GIVEN
        FromPojoConverter c = getConverter();
        TopicConfigPojo pojo = new TopicConfigPojo();

        // WHEN
        KafkaTopicConfig converted = c.fromPojo(pojo);

        // THEN
        assertThat(converted.getRelatedConfig()).isNull();
        assertThat(converted.getBrokerUuid()).isEmpty();
        assertThat(converted.getTopicName()).isEqualTo("test");
        assertThat(converted.getUuid()).isNotBlank();
        assertThat(converted.getName()).isEqualTo("<new topic config>");
    }

    @Test
    public void shouldConvertFromEmptySenderConfigPojoToObjectModel() {
        // GIVEN
        SenderConfigPojo pojo = new SenderConfigPojo();
        FromPojoConverter c = getConverter();

        // WHEN
        KafkaSenderConfig config = c.fromPojo(pojo);

        // THEN
        assertThat(config.getMsgContentTemplate()).isEmpty();
        assertThat(config.getMessageKeyEnabled()).isFalse();
        assertThat(config.getMessageKey()).isEmpty();
        assertThat(config.getRelatedConfig()).isNull();
        assertThat(config.getName()).isEqualTo("<new message sender config>");
        assertThat(config.getUuid()).isNotBlank();
        assertThat(config.getRunBeforeAllMessagesScript()).isEqualTo("");
        assertThat(config.getRunBeforeEachMessageScript()).isEqualTo("");
        assertThat(config.getSendingSimulationModeEnabled()).isFalse();
    }

    @Test
    public void shouldConvertFromTopicConfigToObjectModel() {
        // GIVEN

        FromPojoConverter c = getConverter();
        TopicConfigPojo pojo = new TopicConfigPojo();
        final String topicConfigName = "<empty_topic_config_name>";
        final String relatedUuid = "broker_uuid";
        final String topicName = "topic_name";
        final String someTopicConfigUuuid = "someTopicConfigUuuid";
        pojo.setBrokerUuid(relatedUuid);
        pojo.setConfigName(topicConfigName);
        pojo.setTopicName(topicName);
        pojo.setUuid(someTopicConfigUuuid);
        KafkaBrokerConfig relatedBrokerConfig = new KafkaBrokerConfig();
        relatedBrokerConfig.setUuid(relatedUuid);

        when(proxy.getBrokerConfigByUuid(relatedUuid)).thenReturn(Optional.of(relatedBrokerConfig));


        // WHEN
        KafkaTopicConfig converted = c.fromPojo(pojo);

        // THEN
        assertThat(converted.getTopicName()).isEqualTo(topicName);
        assertThat(converted.getName()).isEqualTo(topicConfigName);
        assertThat(converted.getUuid()).isEqualTo(someTopicConfigUuuid);
        assertThat(converted.getRelatedConfig()).isEqualTo(relatedBrokerConfig);
        assertThat(converted.getBrokerUuid()).isEqualTo(relatedUuid);
    }

    @Test
    public void shouldConvertFromSenderConfigPojoToObjectModel() {
        // GIVEN
        SenderConfigPojo pojo = new SenderConfigPojo();
        FromPojoConverter c = getConverter();
        final String relatedUuid = "SomeUUid";

        pojo.setContent("some_content");
        pojo.setMessageKeyEnabled(true);
        pojo.setMessageKey("Some_message_key");
        pojo.setConfigName("some_name");
        pojo.setRepeatCount("33");
        pojo.setRunBeforeAllMessagesScript("zzz");
        pojo.setRunBeforeEachMessageScript("DDD");
        pojo.setTopicConfigUuid(relatedUuid);
        pojo.setSendingSimulationModeEnabled(true);
        KafkaTopicConfig relatedConfig = mock(KafkaTopicConfig.class);

        when(proxy.getTopicConfigByUuid(relatedUuid)).thenReturn(Optional.of(relatedConfig));
        when(relatedConfig.getUuid()).thenReturn(relatedUuid);

        // WHEN
        KafkaSenderConfig config = c.fromPojo(pojo);
        // THEN
        assertThat(config.getMsgContentTemplate()).isEqualTo("some_content");
        assertThat(config.getMessageKeyEnabled()).isTrue();
        assertThat(config.getMessageKey()).isEqualTo("Some_message_key");
        assertThat(config.getTopicConfigUuid()).isEqualTo(relatedUuid);
        final int expectedRepeatCount = 33;
        assertThat(config.getRepeatCount()).isEqualTo(expectedRepeatCount);
        assertThat(config.getName()).isEqualTo("some_name");
        assertThat(config.getRunBeforeAllMessagesScript()).isEqualTo("zzz");
        assertThat(config.getRunBeforeEachMessageScript()).isEqualTo("DDD");
        assertThat(config.getSendingSimulationModeEnabled()).isTrue();
    }

    @Test
    public void shouldConvertFromEmptyListenerPojoToObjectModel() {
        // GIVEN
        ListenerConfigPojo pojo = new ListenerConfigPojo();
        FromPojoConverter c = getConverter();

        // WHEN
        KafkaListenerConfig config = c.fromPojo(pojo);

        // THEN
        assertThat(config.getConsumerGroup()).isNotBlank();
        assertThat(config.getPollTimeout()).isEqualTo("2000");
        assertThat(config.getOffsetResetConfig()).isEqualTo(KafkaOffsetResetType.EARLIEST);
        assertThat(config.getTopicConfigUuid()).isNull();
        assertThat(config.getName()).isEqualTo("<new message listener config>");
        assertThat(config.getUuid()).isNotBlank();
    }

    @Test
    public void shouldConvertFromListenerPojoToObjectModel() {
        // GIVEN
        ListenerConfigPojo pojo = new ListenerConfigPojo();
        FromPojoConverter c = getConverter();
        KafkaTopicConfig relatedConfig = mock(KafkaTopicConfig.class);
        final String relatedUuid = "SomeUUid";
        when(proxy.getTopicConfigByUuid(relatedUuid)).thenReturn(Optional.of(relatedConfig));
        when(relatedConfig.getUuid()).thenReturn(relatedUuid);

        pojo.setConsumerGroup("some_consumer_group");
        pojo.setConfigName("<empty_listener_config_name>");
        pojo.setOffsetResetConfig("latest");
        pojo.setPollTimeout("3000");
        pojo.setTopicConfigUuid(relatedUuid);
        pojo.setUuid("some_uuid");
        // WHEN
        KafkaListenerConfig config = c.fromPojo(pojo);

        // THEN
        assertThat(config.getConsumerGroup()).isEqualTo("some_consumer_group");
        assertThat(config.getPollTimeout()).isEqualTo("3000");
        assertThat(config.getOffsetResetConfig()).isEqualTo(KafkaOffsetResetType.LATEST);
        assertThat(config.getTopicConfigUuid()).isEqualTo(relatedUuid);
        assertThat(config.getRelatedConfig()).isEqualTo(relatedConfig);
        assertThat(config.getName()).isEqualTo("<empty_listener_config_name>");
        assertThat(config.getUuid()).isNotBlank();
    }

    @Test
    public void shouldConvertFromListenerPojoToObjectModelWhenPojoHasInvalidValues() {
        // GIVEN
        ListenerConfigPojo pojo = new ListenerConfigPojo();
        FromPojoConverter c = getConverter();
        pojo.setPollTimeout("invalid_value");
        pojo.setOffsetResetConfig("invalid_value");

        // WHEN
        KafkaListenerConfig config = c.fromPojo(pojo);

        // THEN
        assertThat(config.getPollTimeout()).isEqualTo("2000");
        assertThat(config.getOffsetResetConfig()).isEqualTo(KafkaOffsetResetType.EARLIEST);
    }

    @Test
    public void shouldConvertFromSenderPojoToObjectModelWhenPojoHasInvalidValues() {
        // GIVEN
        SenderConfigPojo pojo = new SenderConfigPojo();
        FromPojoConverter c = getConverter();
        pojo.setRepeatCount("invalid_value");

        // WHEN
        KafkaSenderConfig config = c.fromPojo(pojo);

        // THEN
        final int expectedDefaultValue = 1;
        assertThat(config.getRepeatCount()).isEqualTo(expectedDefaultValue);
    }


    @BeforeMethod
    private void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private FromPojoConverter getConverter() {
        return new FromPojoConverter(proxy);
    }

}
