package application.customfxwidgets.mainviewcontroller;

import application.controllers.ControllerProvider;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

public interface ControllerRepositoryFactory {
    ControllerProvider createFor(TabPane leftViewTabPane,
                                 ListView<KafkaBrokerConfig> brokersListView,
                                 ListView<KafkaTopicConfig> topicsListView,
                                 ListView<KafkaSenderConfig> messagesListView,
                                 ListView<KafkaListenerConfig> listenersListView);
}
