package application.controllers;

import application.customfxwidgets.brokerconfig.BrokerConfigGuiController;
import application.customfxwidgets.listenerconfig.ListenerConfigGuiController;
import application.customfxwidgets.listenerconfig.ToFileSaver;
import application.kafka.cluster.KafkaClusterProxies;
import application.persistence.ApplicationSettings;
import application.scripting.GroovyScriptEvaluator;
import application.scripting.MessageTemplateSender;
import application.customfxwidgets.senderconfig.SenderConfigGuiController;
import application.customfxwidgets.topicconfig.TopicConfigGuiController;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.cluster.ClusterStatusChecker;
import application.kafka.sender.KafkaMessageSender;
import application.kafka.listener.Listeners;
import application.logging.Logger;
import application.model.ModelConfigObject;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.AppUtils;
import application.utils.UserInteractor;
import application.scripting.codearea.SyntaxHighlightingCodeAreaConfigurator;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class DefaultControllerProvider implements ControllerProvider {
    private final Map<String /*uuid*/, BrokerConfigGuiController> brokerControllers = new HashMap<>();
    private final Map<String /*uuid*/, ListenerConfigGuiController> listenersControllers = new HashMap<>();
    private final Map<String /*uuid*/, TopicConfigGuiController> topicControllers = new HashMap<>();
    private final Map<String /*uuid*/, SenderConfigGuiController> messageControllers = new HashMap<>();
    private final ClusterStatusChecker statusChecker;
    private final ModelConfigObjectsGuiInformer guiInformer;
    private final SyntaxHighlightingCodeAreaConfigurator syntaxHighlightConfigurator;
    private KafkaClusterProxies kafkaClusterProxies;
    private ApplicationSettings applicationSettings;

    public DefaultControllerProvider(ModelConfigObjectsGuiInformer guiInformer,
                                     ClusterStatusChecker statusChecker,
                                     SyntaxHighlightingCodeAreaConfigurator syntaxHighlightConfigurator,
                                     KafkaClusterProxies kafkaClusterProxies,
                                     ApplicationSettings applicationSettings) {
        this.guiInformer = guiInformer;
        this.statusChecker = statusChecker;
        this.syntaxHighlightConfigurator = syntaxHighlightConfigurator;
        this.kafkaClusterProxies = kafkaClusterProxies;
        this.applicationSettings = applicationSettings;
    }


    @Override
    public BrokerConfigGuiController getBrokerConfigGuiController(KafkaBrokerConfig config,
                                                                  AnchorPane parentPane,
                                                                  Runnable refeshCallback,
                                                                  UserInteractor guiInteractor,
                                                                  Window parentWindow) {
        return getControllerFor(config, brokerControllers, () -> {
            try {
                return new BrokerConfigGuiController(config,
                                                     parentPane,
                                                     guiInformer,
                                                     parentWindow,
                                                     refeshCallback,
                                                     guiInteractor,
                                                     statusChecker,
                                                     kafkaClusterProxies);
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        });
    }

    @Override
    public ListenerConfigGuiController getListenerConfigGuiController(KafkaListenerConfig config,
                                                                      AnchorPane parentPane,
                                                                      Listeners activeConsumers,
                                                                      Runnable refreshCallback,
                                                                      ObservableList<KafkaTopicConfig> topicConfigs,
                                                                      ToFileSaver toFileSaver
    ) {

        return getControllerFor(config, listenersControllers, () -> {
            try {
                return new ListenerConfigGuiController(config,
                                                       parentPane,
                                                       guiInformer,
                                                       activeConsumers,
                                                       refreshCallback,
                                                       topicConfigs,
                                                       toFileSaver);
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        });
    }

    @Override
    public TopicConfigGuiController getTopicConfigGuiController(KafkaTopicConfig config,
                                                                AnchorPane parentPane,
                                                                Runnable refreshCallback,
                                                                ObservableList<KafkaBrokerConfig> brokerConfigs) {
        return getControllerFor(config, topicControllers, () -> {
            try {
                return new TopicConfigGuiController(config,
                                                    parentPane,
                                                    guiInformer,
                                                    refreshCallback,
                                                    brokerConfigs,
                                                    statusChecker,
                                                    kafkaClusterProxies);
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        });
    }


    @Override
    public SenderConfigGuiController getSenderConfigGuiController(KafkaSenderConfig config,
                                                                  AnchorPane parentPane,
                                                                  KafkaMessageSender sender,
                                                                  Runnable refreshCallback,
                                                                  ObservableList<KafkaTopicConfig> topicConfigs) {

        return getControllerFor(config, messageControllers, () -> {
            try {
                final MessageTemplateSender msgTemplateEvaluator = new MessageTemplateSender(sender,
                                                                                             new GroovyScriptEvaluator());

                final CodeArea beforeAllCodeAreaShared = new CodeArea();
                final VirtualizedScrollPane<StyleClassedTextArea> beforeAllMessagesSharedScriptScrollPane =
                        new VirtualizedScrollPane<>(beforeAllCodeAreaShared);

                final CodeArea beforeAllCodeArea = new CodeArea();
                final VirtualizedScrollPane<StyleClassedTextArea> beforeAllMessagesScriptScrollPane =
                    new VirtualizedScrollPane<>(beforeAllCodeArea);


                final CodeArea beforeEachCodeArea = new CodeArea();
                final VirtualizedScrollPane<StyleClassedTextArea> beforeEachMessageScriptScrollPane =
                    new VirtualizedScrollPane<>(beforeEachCodeArea);

                final CodeArea messageContentCodeArea = new CodeArea();
                final VirtualizedScrollPane<StyleClassedTextArea> messageContentScrollPane =
                    new VirtualizedScrollPane<>(messageContentCodeArea);

                syntaxHighlightConfigurator.configureGroovySyntaxHighlighting(beforeAllCodeAreaShared);
                syntaxHighlightConfigurator.configureGroovySyntaxHighlighting(beforeAllCodeArea);
                syntaxHighlightConfigurator.configureGroovySyntaxHighlighting(beforeEachCodeArea);
                syntaxHighlightConfigurator.configureJsonSyntaxHighlighting(messageContentCodeArea);

                return new SenderConfigGuiController(config,
                                                     parentPane,
                                                     guiInformer,
                                                     refreshCallback,
                                                     topicConfigs,
                                                     msgTemplateEvaluator,
                                                     beforeAllMessagesSharedScriptScrollPane,
                                                     beforeAllMessagesScriptScrollPane,
                                                     beforeEachMessageScriptScrollPane,
                                                     messageContentScrollPane,
                                                     kafkaClusterProxies,
                                                     applicationSettings);
            } catch (IOException e) {
                Logger.error(e);
                return null;
            }
        });
    }


    private <Controller, ModelObject extends ModelConfigObject> Controller getControllerFor(ModelObject config,
                                                                                            Map<String, Controller> controllerMap,
                                                                                            Supplier<Controller> controllerSupplier) {
        final String uuid = getModelObjectUuid(config);
        if (null == uuid) {
            return null;
        }

        if (!controllerMap.containsKey(uuid)) {
            final Controller controller = controllerMap.getOrDefault(uuid, controllerSupplier.get());
            Logger.trace(String.format("Creating new controller, hash: %s, (name: %s, {%s})",
                                       AppUtils.realHash(controller), config.getName(), uuid));
            controllerMap.put(uuid, controller);
        }
        final Controller controller = controllerMap.get(uuid);
        Logger.trace(String.format("Returning controller,    hash: %s, (name: %s, {%s})",
                                   AppUtils.realHash(controller), config.getName(), uuid));
        return controller;
    }


    private String getModelObjectUuid(ModelConfigObject config) {
        if (config == null) {
            return null;
        }

        return config.getUuid();
    }
}
