
package application.kafka;

import application.model.MessageOnTopicDto;
import application.utils.HostInfo;

public interface KafkaMessageSender {
    void initiateFreshConnection(HostInfo info);

    void stop();
    void start();
    void sendMessages(MessageOnTopicDto msgToTopic);
}
