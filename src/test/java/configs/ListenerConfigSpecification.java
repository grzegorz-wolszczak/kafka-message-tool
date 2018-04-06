package configs;

import application.model.modelobjects.KafkaListenerConfig;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListenerConfigSpecification {

    @Test
    public void shouldAssignNewUuid() {
        final KafkaListenerConfig config = new KafkaListenerConfig();
        final String oldUuid = config.getUuid();
        config.assignNewUuid();
        assertThat(oldUuid).isNotEqualTo(config.getUuid());
    }

    @Test
    public void shouldReturnObjectTypeName() {
        assertThat(new KafkaListenerConfig().getObjectTypeName()).isEqualTo("Message listener configuration");
    }

}
