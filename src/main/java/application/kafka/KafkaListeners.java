
package application.kafka;

import application.model.modelobjects.KafkaListenerConfig;

import java.util.HashSet;
import java.util.Set;

public class KafkaListeners implements Listeners {

    private final Set<Listener> listeners = new HashSet<>();


    private Listener getByListenerConfigUuid(String uuid) {
        for (Listener e : listeners) {
            if (e.getListenerConfig().getUuid().equals(uuid)) {
                return e;
            }
        }
        return null;
    }


    @Override
    public Listener getListener(KafkaListenerConfig listenerConfig) {
        Listener listener = getByListenerConfigUuid(listenerConfig.getUuid());
        if (null == listener) {
            listener = new DefaultKafkaListener(listenerConfig);
            listeners.add(listener);
        }
        return listener;
    }

    @Override
    public void stopAll() {
        listeners.forEach(Listener::stop);
        listeners.clear();
    }

}
