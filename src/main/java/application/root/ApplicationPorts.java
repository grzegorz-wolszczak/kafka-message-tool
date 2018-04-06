package application.root;

import application.kafka.KafkaMessageSender;
import application.kafka.listener.Listeners;

public interface ApplicationPorts  {
    KafkaMessageSender getSender();
    Listeners getListeners();

    void stop();
    void start();
}
