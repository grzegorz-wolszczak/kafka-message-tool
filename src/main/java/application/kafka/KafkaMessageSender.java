
package application.kafka;

import application.model.MessageOnTopicDto;

public interface KafkaMessageSender {
    void stop();
    void start();
    void sendMessages(MessageOnTopicDto msgToTopic);
}
