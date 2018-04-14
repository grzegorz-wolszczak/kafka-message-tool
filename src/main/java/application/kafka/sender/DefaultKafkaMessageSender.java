package application.kafka.sender;

import application.logging.Logger;
import application.model.MessageOnTopicDto;
import application.utils.HostInfo;
import application.utils.kafka.KafkaProducers;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class DefaultKafkaMessageSender implements KafkaMessageSender {
    public static final String KAFKA_STRING_SERIALIZER_CLASS_NAME = "org.apache.kafka.common.serialization.StringSerializer";
    private static final int KAFKA_SENDER_SEND_TIMEOUT_MS = 3000;
    private static final int KAFKA_PRODUCER_MAX_BLOCK_MS = 1501;
    private KafkaProducer<String, String> producer;


    public DefaultKafkaMessageSender() {
    }

    @Override
    public void initiateFreshConnection(HostInfo info,
                                        boolean isSimulationModeEnabled) {
        if (isSimulationModeEnabled) {
            return;
        }
        producer = getProducer(info);
    }


    @Override
    public void sendMessages(MessageOnTopicDto msgsToTopic) {
        trySendMessages(msgsToTopic);
    }


    private void trySendMessages(MessageOnTopicDto msgOnTopicToBeSent) {

        try {
            refreshProducerIfNeeded(msgOnTopicToBeSent.getBrokerHostInfo(),
                                    msgOnTopicToBeSent.shouldSimulateSending());
            sendMessagesToTopic(msgOnTopicToBeSent);
            Logger.info("Ok. Message(s) sent.");
        } catch (Exception e) {
            printMostAppropriateDebugBasedOnExcepionType(e);
        } catch (Throwable e) {
            Logger.error("Sending thread exception", e);
        }

    }

    private void printMostAppropriateDebugBasedOnExcepionType(Exception e) {
        final Throwable cause = e.getCause();
        if (cause instanceof org.apache.kafka.common.errors.TimeoutException) {
            Logger.error("Sending failed: " + e.getLocalizedMessage() + " (maybe invalid broker port?)");
        } else if (cause instanceof InterruptedException) {
            Logger.warn("Sending stopped by user.");
        } else if (cause instanceof org.apache.kafka.common.errors.CorruptRecordException) {
            Logger.error("Sending failed: " + e.getLocalizedMessage() +
                             "\n(Probable reason: You forgot to set key for message while sending to compacted topic).");
        } else {
            Logger.error("Sending failed: " + e.getLocalizedMessage());
        }
    }

    private void sendMessagesToTopic(MessageOnTopicDto messageOnTopic)
        throws InterruptedException,
        ExecutionException,
        TimeoutException {


        final String message = messageOnTopic.getMessage();
        final int msgCount = messageOnTopic.getMsgNum();
        final int totalMsgCount = messageOnTopic.getTotalMsgCount();
        final String topicName = messageOnTopic.getTopicName();
        final String key = messageOnTopic.getMessageKey();


        final ProducerRecord<String, String> record = createRecord(topicName, key, message);
        Logger.info(String.format("%sSending record %d/%d (timeout ms: %d)%nmessage content= '%s'",
                                  messageOnTopic.shouldSimulateSending() ? "(simulation) " : "",
                                  msgCount,
                                  totalMsgCount,
                                  KAFKA_SENDER_SEND_TIMEOUT_MS,
                                  message));
        Logger.trace(String.format("Sending record %s", record));
        if (!messageOnTopic.shouldSimulateSending()) {
            final Future<RecordMetadata> futureResult = producer.send(record);
            final RecordMetadata recordMetadata = futureResult.get(KAFKA_SENDER_SEND_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            Logger.trace(String.format("Record sent, metadata %s", recordMetadata));
        }
    }

    private void refreshProducerIfNeeded(HostInfo brokerHostInfo,
                                         boolean isSimulationModeEnabled) {
        if (producer == null) {
            initiateFreshConnection(brokerHostInfo, isSimulationModeEnabled);
        }
    }

    private ProducerRecord<String, String> createRecord(String topicName,
                                                        String key,
                                                        String content) {
        return new ProducerRecord<>(topicName,
                                    key,
                                    content);
    }

    private Properties getKafkaProducerConfig(HostInfo hostInfo) {
        final Properties properties = new Properties();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, hostInfo.toHostPortString());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KAFKA_STRING_SERIALIZER_CLASS_NAME);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KAFKA_STRING_SERIALIZER_CLASS_NAME);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, String.valueOf(KAFKA_PRODUCER_MAX_BLOCK_MS));
        return properties;
    }

    private KafkaProducer<String, String> getProducer(HostInfo hostInfo) {
        final Properties props = getKafkaProducerConfig(hostInfo);
        return KafkaProducers.getProducerForProperties(props);
    }

}
