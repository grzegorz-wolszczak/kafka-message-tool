package pojoconversions;

import autofixture.publicinterface.Any;
import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PojoCopyableSpecification {

    @Test
    public void shouldCreateTopicConfigPojoCopy() {
        // GIVEN
        TopicConfigPojo p1 = Any.anonymous(TopicConfigPojo.class);

        // WHEN
        TopicConfigPojo p2 = p1.copyOfMine();

        // THEN
        assertThat(p2).isEqualTo(p1);
    }


    @Test
    public void shouldCreateBrokerConfigPojoCopy() {
        // GIVEN
        BrokerConfigPojo p1 = Any.anonymous(BrokerConfigPojo.class);

        // WHEN
        BrokerConfigPojo p2 = p1.copyOfMine();

        // THEN
        assertThat(p2).isEqualTo(p1);
    }

    @Test
    public void shouldCreateListenerConfigPojoCopy() {
        // GIVEN
        ListenerConfigPojo p1 = Any.anonymous(ListenerConfigPojo.class);

        // WHEN
        ListenerConfigPojo p2 = p1.copyOfMine();

        // THEN
        assertThat(p2).isEqualTo(p1);
    }

    @Test
    public void shouldCreateSenderConfigPojoCopy() {
        // GIVEN
        SenderConfigPojo p1 = Any.anonymous(SenderConfigPojo.class);

        // WHEN
        SenderConfigPojo p2 = p1.copyOfMine();

        // THEN
        assertThat(p2).isEqualTo(p1);
    }

}
