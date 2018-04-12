package application.root;

import application.controllers.ControllerProvider;
import application.controllers.confighandlers.BrokerConfigGuiActionsHandler;
import application.controllers.confighandlers.ListenerConfigGuiActionsHandler;
import application.controllers.confighandlers.SenderConfigGuiActionsHandler;
import application.controllers.confighandlers.TopicConfigGuiActionsHandler;
import application.controllers.helpers.ListViewActionsHandler;
import application.controllers.helpers.TabPaneSelectionInformer;
import application.controllers.helpers.TemplateGuiActionsHandler;
import application.customfxwidgets.listenerconfig.ToFileSaver;
import application.model.ModelDataProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.UserInteractor;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DefaultActionHandlerFactory implements ActionHandlerFactory {
    private UserInteractor interactor;
    private ModelDataProxy modelDataProxy;
    private Stage appStage;
    private ApplicationPorts applicationPorts;

    public DefaultActionHandlerFactory(UserInteractor interactor,
                                       ModelDataProxy modelDataProxy,
                                       Stage appStage, ApplicationPorts applicationPorts) {
        this.interactor = interactor;
        this.modelDataProxy = modelDataProxy;
        this.appStage = appStage;
        this.applicationPorts = applicationPorts;
    }

    @Override
    public TemplateGuiActionsHandler<KafkaBrokerConfig> createBrokerConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                                TabPane masterTabPane,
                                                                                                Tab tab,
                                                                                                ListView<KafkaBrokerConfig> listView,
                                                                                                ControllerProvider repository) {
        return new BrokerConfigGuiActionsHandler(new TabPaneSelectionInformer(masterTabPane, tab),
                                                 new ListViewActionsHandler<>(interactor, listView),
                                                 interactor,
                                                 modelDataProxy,
                                                 repository,
                                                 rightContentPane,
                                                 appStage);
    }

    @Override
    public TemplateGuiActionsHandler<KafkaTopicConfig> createTopicConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                              TabPane masterTabPane,
                                                                                              Tab tab,
                                                                                              ListView<KafkaTopicConfig> listView,
                                                                                              ControllerProvider repository,
                                                                                              ListView<KafkaBrokerConfig> brokerConfigListView) {
        return new TopicConfigGuiActionsHandler(new TabPaneSelectionInformer(masterTabPane, tab),
                                                new ListViewActionsHandler<>(interactor, listView),
                                                modelDataProxy,
                                                repository,
                                                rightContentPane,
                                                brokerConfigListView);
    }


    @Override
    public TemplateGuiActionsHandler<KafkaSenderConfig> createSenderConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                                TabPane masterTabPane,
                                                                                                Tab tab,
                                                                                                ListView<KafkaSenderConfig> listView,
                                                                                                ListView<KafkaTopicConfig> topicConfigListView,
                                                                                                ControllerProvider repository) {
        return new SenderConfigGuiActionsHandler(new TabPaneSelectionInformer(masterTabPane, tab),
                                                 new ListViewActionsHandler<>(interactor, listView),
                                                 modelDataProxy,
                                                 repository,
                                                 rightContentPane,
                                                 topicConfigListView,
                                                 applicationPorts.getSender());
    }

    @Override
    public TemplateGuiActionsHandler<KafkaListenerConfig> createListenerConfigListViewActionHandler(AnchorPane rightContentPane,
                                                                                                    TabPane masterTabPane,
                                                                                                    Tab tab,
                                                                                                    ListView<KafkaListenerConfig> listView,
                                                                                                    ListView<KafkaTopicConfig> topicConfigListView,
                                                                                                    ControllerProvider repository) {
        return new ListenerConfigGuiActionsHandler(new TabPaneSelectionInformer(masterTabPane, tab),
                                                   new ListViewActionsHandler<>(interactor, listView),
                                                   modelDataProxy,
                                                   repository,
                                                   rightContentPane,
                                                   topicConfigListView,
                                                   applicationPorts.getListeners(),
                                                   new ToFileSaver(interactor));
    }


}
