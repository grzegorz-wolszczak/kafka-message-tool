
package application.kafka;

import application.model.MessageOnTopicDto;
import application.utils.HostInfo;

public interface KafkaMessageSender {
    void initiateFreshConnection(HostInfo info);

    void sendMessages(MessageOnTopicDto msgToTopic);
}
