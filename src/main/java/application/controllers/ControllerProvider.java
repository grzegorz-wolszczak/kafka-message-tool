package application.controllers;

import application.customfxwidgets.brokerconfig.BrokerConfigGuiController;
import application.customfxwidgets.listenerconfig.ListenerConfigGuiController;
import application.customfxwidgets.listenerconfig.ToFileSaver;
import application.customfxwidgets.senderconfig.SenderConfigGuiController;
import application.customfxwidgets.topicconfig.TopicConfigGuiController;
import application.kafka.KafkaMessageSender;
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
    BrokerConfigGuiController getControllerFor(KafkaBrokerConfig config,
                                               AnchorPane parentPane,
                                               Runnable refeshCallback,
                                               UserInteractor guiInteractor,
                                               Window parentWindow);

    ListenerConfigGuiController getController(KafkaListenerConfig config,
                                              AnchorPane parentPane,
                                              Listeners activeConsumers,
                                              Runnable refreshCallback,
                                              ObservableList<KafkaTopicConfig> topicConfigs,
                                              ToFileSaver toFileSaver);


    TopicConfigGuiController getController(KafkaTopicConfig config,
                                           AnchorPane parentPane,
                                           Runnable refreshCallback,
                                           ObservableList<KafkaBrokerConfig> brokerConfigs);

    SenderConfigGuiController getController(KafkaSenderConfig config,
                                            AnchorPane parentPane,
                                            KafkaMessageSender sender,
                                            Runnable refreshCallback,
                                            ObservableList<KafkaTopicConfig> topicConfigs);
}
