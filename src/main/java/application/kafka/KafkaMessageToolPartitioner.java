package application.kafka;

import org.apache.kafka.common.utils.Utils;

import java.nio.charset.Charset;

public class KafkaMessageToolPartitioner {

    public static int partition(String key, int numPartitions) {
        if (key == null) {
            return -1;
        }

        return Utils.toPositive(Utils.murmur2(key.getBytes(Charset.defaultCharset()))) % numPartitions;
    }
}
