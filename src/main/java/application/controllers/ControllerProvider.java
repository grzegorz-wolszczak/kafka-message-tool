package application.controllers;

import application.customfxwidgets.brokerconfig.BrokerConfigView;
import application.customfxwidgets.listenerconfig.ListenerConfigView;
import application.customfxwidgets.listenerconfig.ToFileSaver;
import application.customfxwidgets.senderconfig.SenderConfigView;
import application.customfxwidgets.topicconfig.TopicConfigView;
import application.kafka.sender.KafkaMessageSender;
import application.kafka.listener.Listeners;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.UserInteractor;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

public interface ControllerProvider {
    BrokerConfigView getBrokerConfigGuiController(KafkaBrokerConfig config,
                                                  AnchorPane parentPane,
                                                  Runnable refeshCallback,
                                                  UserInteractor guiInteractor,
                                                  Window parentWindow);

    ListenerConfigView getListenerConfigGuiController(KafkaListenerConfig config,
                                                      AnchorPane parentPane,
                                                      Listeners activeConsumers,
                                                      Runnable refreshCallback,
                                                      ObservableList<KafkaTopicConfig> topicConfigs,
                                                      ToFileSaver toFileSaver);


    TopicConfigView getTopicConfigGuiController(KafkaTopicConfig config,
                                                AnchorPane parentPane,
                                                Runnable refreshCallback,
                                                ObservableList<KafkaBrokerConfig> brokerConfigs);

    SenderConfigView getSenderConfigGuiController(KafkaSenderConfig config,
                                                  AnchorPane parentPane,
                                                  KafkaMessageSender sender,
                                                  Runnable refreshCallback,
                                                  ObservableList<KafkaTopicConfig> topicConfigs);
}
