package application.scripting;

import application.constants.GroovyStringEscaper;
import application.exceptions.ExecutionStopRequested;
import application.kafka.KafkaMessageSender;
import application.logging.Logger;
import application.model.MessageOnTopicDto;
import application.model.modelobjects.KafkaSenderConfig;
import application.utils.ThrowableUtils;

public class MessageTemplateSender {

    private static final String MSG_CONTENT_VARIABLE_NAME = "msgContent";
    private final GroovyScriptEvaluator scriptEvaluator;
    private final KafkaMessageSender kafkaSender;

    public MessageTemplateSender(KafkaMessageSender kafkaSender,
                                 GroovyScriptEvaluator scriptEvaluator) {
        this.kafkaSender = kafkaSender;
        this.scriptEvaluator = scriptEvaluator;
    }

    public void send(KafkaSenderConfig config,
                     boolean isSimulationModeEnabled) {
        try {
            trySend(config, isSimulationModeEnabled);
        } catch (ExecutionStopRequested e) {
            Logger.warn("Sending stopped manually.");
        } catch (Exception e) {
            Logger.trace(ThrowableUtils.getFullStackTrace(e));
            Logger.error(ThrowableUtils.getMessageWithRootCause(e));
        }

    }

    public String evaluateMessageContent(String msgContentTemplate) throws Exception {
        final String msgContentToResolve = GroovyStringEscaper.escape(MSG_CONTENT_VARIABLE_NAME, msgContentTemplate);
        scriptEvaluator.runScript(msgContentToResolve);
        return returnEvaluatedMsgContent(MSG_CONTENT_VARIABLE_NAME);
    }

    private void trySend(KafkaSenderConfig config,
                         boolean isSimulationModeEnabled) throws Exception {

        final Integer totalMessageCount = config.getRepeatCount();

        Logger.info(String.format("Sending message [topic '%s', key '%s'], content template '%s', repeat count: %d",
                                  config.getRelatedConfig().getTopicName(),
                                  config.getMessageKey(),
                                  config.getMsgContentTemplate(),
                                  totalMessageCount
        ));

        resetScriptEngine();
        runScript(config.getRunBeforeAllMessagesScript());
        for (int i = 0; i < totalMessageCount; i++) {
            runScript(config.getRunBeforeEachMessageScript());
            final String evaluatedMessage = evaluateMessageContent(config.getMsgContentTemplate());

            kafkaSender.sendMessages(MessageOnTopicDto.from(config,
                                                            evaluatedMessage,
                                                            isSimulationModeEnabled,
                                                            i + 1,
                                                            totalMessageCount));

        }
    }

    private void resetScriptEngine() {
        scriptEvaluator.resetScriptContext();
    }

    private void runScript(String script) throws Exception {
        scriptEvaluator.runScript(script);
    }


    private String returnEvaluatedMsgContent(String var) {
        final Object evaluatedMsg = scriptEvaluator.getBinding(var);
        return String.format("%s", evaluatedMsg);
    }
}
