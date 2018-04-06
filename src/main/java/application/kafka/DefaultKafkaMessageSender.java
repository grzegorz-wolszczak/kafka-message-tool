package application.kafka;

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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class DefaultKafkaMessageSender implements KafkaMessageSender {
    private static final int KAFKA_SENDER_SEND_TIMEOUT_MS = 3000;
    private static final int KAFKA_PRODUCER_MAX_BLOCK_MS = 1501;
    private static final long SENDING_THREAD_SLEEP_MILLISECONDS = 250;
    private final LinkedBlockingQueue<MessageOnTopicDto> sendMsgQueue = new LinkedBlockingQueue<>();
    private AtomicBoolean shouldSendThreadBeRunning = new AtomicBoolean(true);
    private KafkaProducer<String, String> producer;

    public DefaultKafkaMessageSender() {

    }

    @Override
    public void initiateFreshConnection(HostInfo info) {
        producer = getProducer(info);
    }

    @Override
    public void stop() {
        shouldSendThreadBeRunning.set(false);
    }

    @Override
    public void start() {
        getSendThread().start();
    }

    @Override
    public void sendMessages(MessageOnTopicDto msgsToTopic) {

        try {
            sendMsgQueue.put(msgsToTopic);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private Thread getSendThread() {

        return new Thread(() -> {
            while (shouldSendThreadBeRunning.get()) {
                try {
                    final MessageOnTopicDto messagesToTopic = sendMsgQueue.poll(SENDING_THREAD_SLEEP_MILLISECONDS,
                                                                                TimeUnit.MILLISECONDS);

                    if (null == messagesToTopic) {
                        continue;
                    }

                    trySendMessages(messagesToTopic);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Logger.warn("Sending thread : InterruptedException");
                } catch (Throwable e) {
                    Logger.error("Sending thread exception", e);
                }
            }
        });
    }

    private void trySendMessages(MessageOnTopicDto msgOnTopicToBeSent) {

        try {
            refreshProducerIfNeeded(msgOnTopicToBeSent.getBrokerHostInfo());
            sendMessagesToTopic(msgOnTopicToBeSent);
            Logger.info("Ok. Message(s) sent.");
        } catch (Exception e) {
            printMostAppropriateDebugBasedOnExcepionType(e);
        }
    }

    private void printMostAppropriateDebugBasedOnExcepionType(Exception e) {
        final Throwable cause = e.getCause();
        if (cause != null && cause instanceof org.apache.kafka.common.errors.TimeoutException) {
            Logger.error("Sending failed: " + e.getLocalizedMessage() + " (maybe invalid broker port?)");
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

    private void refreshProducerIfNeeded(HostInfo brokerHostInfo) {
        if (producer == null) {
            initiateFreshConnection(brokerHostInfo);
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
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, String.valueOf(KAFKA_PRODUCER_MAX_BLOCK_MS));
        return properties;
    }

    private KafkaProducer<String, String> getProducer(HostInfo hostInfo) {
        final Properties props = getKafkaProducerConfig(hostInfo);
        return KafkaProducers.getProducerForProperties(props);
    }

}
