import application.kafka.KafkaMessageSender
import application.scripting.GroovyScriptEvaluator
import application.scripting.MessageTemplateSender
import org.mockito.Mockito
import org.testng.annotations.Test

class MessageTemplateSenderSpecification {

    @Test
    void shouldEvaluateStringExpressionAndNotThrowInTheProcess() {

        KafkaMessageSender sender = Mockito.mock(KafkaMessageSender.class)
        def evaluator = new MessageTemplateSender(sender,new GroovyScriptEvaluator());

        def inputs = ["",
                      '''"''',
                      '''""''',
                      '''"""''',
                      """'""",
                      """''""",
                      """'''""",
                      '''\"\"''',
                      '''\\\"\\\"'''

        ]

        inputs.each { evaluator.evaluateMessageContent(it) }

    }
}
