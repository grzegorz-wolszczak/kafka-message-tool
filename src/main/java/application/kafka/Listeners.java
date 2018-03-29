
package application.kafka;

import application.model.modelobjects.KafkaListenerConfig;

public interface Listeners {
    Listener getListener(KafkaListenerConfig listenerConfig);

    void stopAll();
}
