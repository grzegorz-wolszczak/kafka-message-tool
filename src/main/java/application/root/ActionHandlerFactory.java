package application.root;

import application.controllers.ControllerProvider;
import application.controllers.helpers.TemplateGuiActionsHandler;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public interface ActionHandlerFactory {
    TemplateGuiActionsHandler<KafkaBrokerConfig> createBrokerConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                         TabPane masterTabPane,
                                                                                         Tab tab,
                                                                                         ListView<KafkaBrokerConfig> listView,
                                                                                         ControllerProvider repository);

    TemplateGuiActionsHandler<KafkaTopicConfig> createTopicConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                       TabPane masterTabPane,
                                                                                       Tab tab,
                                                                                       ListView<KafkaTopicConfig> listView,
                                                                                       ControllerProvider repository,
                                                                                       ListView<KafkaBrokerConfig> brokerConfigListView);

    TemplateGuiActionsHandler<KafkaSenderConfig> createSenderConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                         TabPane masterTabPane,
                                                                                         Tab tab,
                                                                                         ListView<KafkaSenderConfig> listView,
                                                                                         ListView<KafkaTopicConfig> topicConfigListView,
                                                                                         ControllerProvider repository);

    TemplateGuiActionsHandler<KafkaListenerConfig> createListenerConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                             TabPane masterTabPane,
                                                                                             Tab tab,
                                                                                             ListView<KafkaListenerConfig> listView,
                                                                                             ListView<KafkaTopicConfig> topicConfigListView,
                                                                                             ControllerProvider repository);
}
