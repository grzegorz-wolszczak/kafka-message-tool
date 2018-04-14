package application.kafka.listener;

import javafx.beans.property.ObjectProperty;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConsumerPartitionsRebalanceListener implements ConsumerRebalanceListener {

    private final String topicName;
    private ObjectProperty<AssignedPartitionsInfo> partitionsProperty;


    public ConsumerPartitionsRebalanceListener(String topicName,
                                               ObjectProperty<AssignedPartitionsInfo> partitionsProperty) {
        this.topicName = topicName;

        this.partitionsProperty = partitionsProperty;
    }

    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        updatePartitionList(partitions);

    }

    private void updatePartitionList(Collection<TopicPartition> partitions) {
        List<Integer> partitionForTopic = new ArrayList<>();
        for (TopicPartition partition : partitions) {

            if (partition.topic().equals(topicName)) {
                partitionForTopic.add(partition.partition());
            }
        }
        partitionsProperty.set(AssignedPartitionsInfo.fromPartitionList(partitionForTopic));
    }

    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        updatePartitionList(partitions);
    }
}
