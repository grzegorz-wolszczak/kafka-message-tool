package application.kafka.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignedConsumerInfo {
    private String consumerId; // internal to kafka
    private String topic;
    private String consumerGroupId;
    private String host;
    private String clientId;
    private String offset;
    private int partition;

}
