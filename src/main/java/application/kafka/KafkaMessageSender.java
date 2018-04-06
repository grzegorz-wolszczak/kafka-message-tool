
package application.kafka;

import application.model.MessageOnTopicDto;

public interface KafkaMessageSender {
    void sendMessages(MessageOnTopicDto msgToTopic);
}
