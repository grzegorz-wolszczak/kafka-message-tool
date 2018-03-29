package application.root;

import application.kafka.KafkaMessageSender;
import application.kafka.Listeners;

public interface ApplicationPorts  {
    KafkaMessageSender getSender();
    Listeners getListeners();

    void stop();
    void start();
}
