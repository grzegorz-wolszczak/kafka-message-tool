package application.scripting;

import application.constants.GroovyStringEscaper;
import application.customfxwidgets.senderconfig.StatusBarNotifier;
import application.exceptions.ExecutionStopRequested;
import application.kafka.sender.KafkaMessageSender;
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
                     StatusBarNotifier sentMessagesNotifier,
                     String sharedScriptContent,
                     boolean isSimulationModeEnabled) {
        try {
            trySend(config, sentMessagesNotifier, sharedScriptContent, isSimulationModeEnabled);
        } catch (ExecutionStopRequested e) {
            Logger.warn("Sending stopped by user.");
        } catch (Exception e) {
            Logger.trace(ThrowableUtils.getFullStackTrace(e));
            Logger.error(ThrowableUtils.getMessageWithRootCause(e));
        } finally {
            sentMessagesNotifier.clearMsgSentProgress();
        }

    }

    public String evaluateMessageContent(String msgContentTemplate) throws Exception {
        final String msgContentToResolve = GroovyStringEscaper.escape(MSG_CONTENT_VARIABLE_NAME, msgContentTemplate);
        scriptEvaluator.runScript(msgContentToResolve);
        return returnEvaluatedMsgContent(MSG_CONTENT_VARIABLE_NAME);
    }

    private void trySend(KafkaSenderConfig config,
                         StatusBarNotifier sentMessagesNotifier,
                         String sharedScriptContent,
                         boolean isSimulationModeEnabled) throws Exception {

        final Integer totalMessageCount = config.getRepeatCount();

        Logger.info(String.format("Sending message [topic '%s', key '%s'], content template '%s', repeat count: %d",
                                  config.getRelatedConfig().getTopicName(),
                                  config.getMessageKey(),
                                  config.getMsgContentTemplate(),
                                  totalMessageCount
        ));

        resetScriptEngine();
        runScript(sharedScriptContent);
        runScript(config.getRunBeforeAllMessagesScript());
        kafkaSender.initiateFreshConnection(config.getRelatedConfig().getRelatedConfig().getHostInfo(),
                                            isSimulationModeEnabled);
        for (int i = 0; i < totalMessageCount; i++) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            sentMessagesNotifier.setMsgSentProgress(i + 1, totalMessageCount);
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
