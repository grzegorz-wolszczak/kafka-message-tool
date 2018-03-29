package application.utils.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaProducers {

    private static final Map<Properties, KafkaProducer<String, String>> PRODUCERS_FOR_PROPERTIES = new HashMap<>();


    public static KafkaProducer<String, String> getProducerForProperties(Properties props) {

        if (PRODUCERS_FOR_PROPERTIES.containsKey(props)) {
            final KafkaProducer<String, String> producer = PRODUCERS_FOR_PROPERTIES.get(props);
            producer.close();
        }
        // create new producer each time
        final KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        PRODUCERS_FOR_PROPERTIES.put(props, producer);
        return producer;
    }

    public static void close(){
        PRODUCERS_FOR_PROPERTIES.forEach((key, producer)->{
            producer.close();
        });
    }

}
