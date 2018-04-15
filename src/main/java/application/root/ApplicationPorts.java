package application.root;

import application.kafka.sender.KafkaMessageSender;
import application.kafka.listener.Listeners;

public interface ApplicationPorts extends Restartable {
    KafkaMessageSender getSender();
    Listeners getListeners();

}
