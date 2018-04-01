package pojoconversions;

import application.model.pojos.BrokerConfigPojo;
import application.model.pojos.ListenerConfigPojo;
import application.model.pojos.SenderConfigPojo;
import application.model.pojos.TopicConfigPojo;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class PojoSerializationSpecification {

    @Test
    public void shouldXmlSerializeAndDeserializeBrokerConfig() throws JAXBException {
        final Class pojoClass = BrokerConfigPojo.class;
        // GIVEN
        String xml =
            "<BrokerConfig>" +
                "<ConfigName>a</ConfigName>" +
                "<BrokerHostname>b</BrokerHostname>" +
                "<BrokerPort>1</BrokerPort>" +
                "<Uuid>d</Uuid>" +
                "</BrokerConfig>";

        // WHEN (unmarshalling)
        BrokerConfigPojo pojo = (BrokerConfigPojo) unmarshall(xml, pojoClass);

        // THEN
        assertThat(pojo.getConfigName()).isEqualTo("a");
        assertThat(pojo.getKafkaBrokerHostName()).isEqualTo("b");
        assertThat(pojo.getKafkaBrokerPort()).isEqualTo("1");
        assertThat(pojo.getUuid()).isEqualTo("d");


        // WHEN (marshalling)
        final String toXml = marshall(pojo, pojoClass);

        // THEN
        BrokerConfigPojo pojo2 = (BrokerConfigPojo) unmarshall(toXml, pojoClass);
        assertThat(pojo).isEqualTo(pojo2);
    }

    @Test
    public void shouldXmlSerializeAndDeserializeTopicConfig() throws JAXBException {
        final Class pojoClass = TopicConfigPojo.class;
        // GIVEN
        String xml =
            "<TopicConfig>" +
                "<ConfigName>zz</ConfigName>" +
                "<TopicName>pp</TopicName>" +
                "<BrokerUuid>12</BrokerUuid>" +
                "<Uuid>xx</Uuid>" +
                "</TopicConfig>";

        // WHEN (unmarshalling)
        TopicConfigPojo pojo = (TopicConfigPojo) unmarshall(xml, pojoClass);

        // THEN
        assertThat(pojo.getBrokerUuid()).isEqualTo("12");
        assertThat(pojo.getConfigName()).isEqualTo("zz");
        assertThat(pojo.getTopicName()).isEqualTo("pp");
        assertThat(pojo.getUuid()).isEqualTo("xx");


        // WHEN (marshalling)
        final String toXml = marshall(pojo, pojoClass);

        // THEN
        TopicConfigPojo pojo2 = (TopicConfigPojo) unmarshall(toXml, pojoClass);
        assertThat(pojo).isEqualTo(pojo2);
    }

    @Test
    public void shouldXmlSerializeAndDeserializeSenderConfig() throws JAXBException {
        final Class pojoClass = SenderConfigPojo.class;
        // GIVEN
        String xml =
            "<SenderConfig>" +
                "<ConfigName>zz</ConfigName>" +
                "<Uuid>xx</Uuid>" +
                "<Content>pp</Content>" +
                "<TopicConfigUuid>12</TopicConfigUuid>" +
                "<RepeatCount>2</RepeatCount>"+
                "<MessageKeyEnabled>true</MessageKeyEnabled>" +
                "<RunBeforeAllMessagesScript>aaa</RunBeforeAllMessagesScript>" +
                "<RunBeforeEachMessageScript>def</RunBeforeEachMessageScript>" +
                "<MessageKey>abba</MessageKey>" +
                "<SendingSimulationModeEnabled>true</SendingSimulationModeEnabled>" +
                "</SenderConfig>";

        // WHEN (unmarshalling)
        SenderConfigPojo pojo = (SenderConfigPojo) unmarshall(xml, pojoClass);

        // THEN

        assertThat(pojo.getConfigName()).isEqualTo("zz");
        assertThat(pojo.getUuid()).isEqualTo("xx");
        assertThat(pojo.getTopicConfigUuid()).isEqualTo("12");
        assertThat(pojo.getContent()).isEqualTo("pp");
        assertThat(pojo.isMessageKeyEnabled()).isTrue();
        assertThat(pojo.getRepeatCount()).isEqualTo("2");
        assertThat(pojo.getRunBeforeAllMessagesScript()).isEqualTo("aaa");
        assertThat(pojo.getRunBeforeEachMessageScript()).isEqualTo("def");
        assertThat(pojo.getMessageKey()).isEqualTo("abba");
        assertThat(pojo.getSendingSimulationModeEnabled()).isTrue();

        // WHEN (marshalling)
        final String toXml = marshall(pojo, pojoClass);

        // THEN
        SenderConfigPojo pojo2 = (SenderConfigPojo) unmarshall(toXml, pojoClass);
        assertThat(pojo).isEqualTo(pojo2);
    }

    @Test
    public void shouldXmlSerializeAndDeserializeListenerConfig() throws JAXBException {
        final Class pojoClass = ListenerConfigPojo.class;
        // GIVEN
        String xml =
            "<ListenerConfig>" +
                "<ConfigName>zz</ConfigName>" +
                "<Uuid>xx</Uuid>" +
                "<TopicConfigUuid>12</TopicConfigUuid>" +
                "<ConsumerGroup>pp</ConsumerGroup>" +
                "<PollTimeout>1234</PollTimeout>" +
                "<OffsetConfigProperty>none</OffsetConfigProperty>" +
                "</ListenerConfig>";

        // WHEN (unmarshalling)
        ListenerConfigPojo pojo = (ListenerConfigPojo) unmarshall(xml, pojoClass);

        // THEN

        assertThat(pojo.getConfigName()).isEqualTo("zz");
        assertThat(pojo.getUuid()).isEqualTo("xx");
        assertThat(pojo.getTopicConfigUuid()).isEqualTo("12");
        assertThat(pojo.getConsumerGroup()).isEqualTo("pp");
        assertThat(pojo.getPollTimeout()).isEqualTo("1234");
        assertThat(pojo.getOffsetResetConfig()).isEqualTo("none");



        // WHEN (marshalling)
        final String toXml = marshall(pojo, pojoClass);

        // THEN
        ListenerConfigPojo pojo2 = (ListenerConfigPojo) unmarshall(toXml, pojoClass);
        assertThat(pojo).isEqualTo(pojo2);
    }



    private Object unmarshall(String xml, Class configClass) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(configClass);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(xml);
        return unmarshaller.unmarshal(reader);
    }

    private String marshall(Object config, Class aClass) throws JAXBException {
        final JAXBContext jaxbContext = JAXBContext.newInstance(aClass);
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(config, writer);
        return writer.toString();
    }
}
