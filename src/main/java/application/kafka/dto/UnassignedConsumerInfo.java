package application.kafka.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UnassignedConsumerInfo {
    private String consumerId;
    private String consumerGroupId;
    private String host;
    private String clientId;
}


