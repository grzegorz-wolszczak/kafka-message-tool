package pojoconversions;

import application.model.KafkaOffsetResetType;
import application.model.ToPojoConverter;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ToPojoConverterSpecification {


    @Test
    public void shouldConvertFromModelObjectToBrokerConfigPojo() {
        // GIVEN
        KafkaBrokerConfig config = new KafkaBrokerConfig();
        config.setName("a");
        config.setHostname("b");
        config.setPort("1");
        config.setUuid("d");

        // WHEN
        BrokerConfigPojo pojo = ToPojoConverter.toPojoFrom(config);

        // THEN
        assertThat(pojo.getConfigName()).isEqualTo("a");
        assertThat(pojo.getKafkaBrokerHostName()).isEqualTo("b");
        assertThat(pojo.getKafkaBrokerPort()).isEqualTo("1");
        assertThat(pojo.getUuid()).isEqualTo("d");

    }

    @Test
    public void shouldConvertFromModelObjectToTopicConfigPojo() {
        // GIVEN
        KafkaTopicConfig config = new KafkaTopicConfig();
        KafkaBrokerConfig relatedConfig = new KafkaBrokerConfig();

        config.setName("a");
        config.setUuid("d");
        config.setTopicName("c");
        relatedConfig.setUuid("related_uuid");
        config.setRelatedConfig(relatedConfig);


        // WHEN
        TopicConfigPojo pojo = ToPojoConverter.toPojoFrom(config);

        // THEN
        assertThat(pojo.getConfigName()).isEqualTo("a");
        assertThat(pojo.getUuid()).isEqualTo("d");
        assertThat(pojo.getTopicName()).isEqualTo("c");
        assertThat(pojo.getBrokerUuid()).isEqualTo("related_uuid");
    }

    @Test
    public void shouldConvertFromModelObjectToSenderConfigPojo() {
        // GIVEN
        KafkaSenderConfig config = new KafkaSenderConfig();
        KafkaTopicConfig relatedConfig = new KafkaTopicConfig();

        config.setName("a");
        config.setUuid("d");
        config.setMessageKeyEnabled(true);
        config.setMessageKey("message_key");
        config.setMsgContentTemplate("content");
        config.setRepeatCount(2);
        config.setRunBeforeAllMessagesScript("abc");
        config.setRunBeforeEachMessageScript("bcd");
        config.setSendingSimulationModeEnabled(true);
        relatedConfig.setUuid("related_uuid");
        config.setRelatedConfig(relatedConfig);


        // WHEN
        SenderConfigPojo pojo = ToPojoConverter.toPojoFrom(config);

        // THEN
        assertThat(pojo.getConfigName()).isEqualTo("a");
        assertThat(pojo.getUuid()).isEqualTo("d");
        assertThat(pojo.getContent()).isEqualTo("content");
        assertThat(pojo.getMessageKey()).isEqualTo("message_key");
        assertThat(pojo.isMessageKeyEnabled()).isTrue();
        assertThat(pojo.getRepeatCount()).isEqualTo("2");
        assertThat(pojo.getRunBeforeAllMessagesScript()).isEqualTo("abc");
        assertThat(pojo.getRunBeforeEachMessageScript()).isEqualTo("bcd");
        assertThat(pojo.getTopicConfigUuid()).isEqualTo("related_uuid");
        assertThat(pojo.getSendingSimulationModeEnabled()).isTrue();
    }

    @Test
    public void shouldConvertFromModelObjectToListenerConfigPojo() {
        // GIVEN
        KafkaListenerConfig config = new KafkaListenerConfig();
        KafkaTopicConfig relatedConfig = new KafkaTopicConfig();

        config.setName("a");
        config.setUuid("d");
        config.setPollTimeout("1234");
        config.setOffsetResetConfig(KafkaOffsetResetType.NONE);
        config.setConsumerGroup("ConsumerGroup");
        config.setReceivedMsgLimitCount("678");
        config.setReceivedMsgLimitEnabled(true);

        relatedConfig.setUuid("related_uuid");
        config.setRelatedConfig(relatedConfig);


        // WHEN
        ListenerConfigPojo pojo = ToPojoConverter.toPojoFrom(config);

        // THEN
        assertThat(pojo.getConfigName()).isEqualTo("a");
        assertThat(pojo.getUuid()).isEqualTo("d");
        assertThat(pojo.getTopicConfigUuid()).isEqualTo("related_uuid");
        assertThat(pojo.getConsumerGroup()).isEqualTo("ConsumerGroup");
        assertThat(pojo.getPollTimeout()).isEqualTo("1234");
        assertThat(pojo.getOffsetResetConfig()).isEqualTo("none");
        assertThat(pojo.getReceivedMsgLimitCount()).isEqualTo("678");
        assertThat(pojo.getReceivedMsgLimitEnabled()).isTrue();


    }
}
