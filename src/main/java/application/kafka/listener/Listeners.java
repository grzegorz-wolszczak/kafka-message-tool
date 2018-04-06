
package application.kafka.listener;

import application.model.modelobjects.KafkaListenerConfig;

public interface Listeners {
    Listener getListener(KafkaListenerConfig listenerConfig);

    void stopAll();
}
