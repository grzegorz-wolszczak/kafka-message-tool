package application.customfxwidgets.mainviewcontroller;

import application.controllers.ControllerProvider;
import application.controllers.DefaultControllerProvider;
import application.displaybehaviour.DefaultModelConfigObjectsGuiInformer;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.cluster.KafkaClusterProxies;
import application.kafka.cluster.ClusterStatusChecker;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.persistence.ApplicationSettings;
import application.root.Restartables;
import application.scripting.codearea.SyntaxHighlightingCodeAreaConfigurator;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

public class DefaultControllerRepositoryFactory implements ControllerRepositoryFactory {
    private ClusterStatusChecker statusChecker;
    private SyntaxHighlightingCodeAreaConfigurator syntaxHighlightingConfigurator;
    private KafkaClusterProxies kafkaClusterProxies;
    private ApplicationSettings applicationSettings;
    private Restartables restartables;

    public DefaultControllerRepositoryFactory(ClusterStatusChecker statusChecker,
                                              SyntaxHighlightingCodeAreaConfigurator syntaxHighlightingConfigurator,
                                              KafkaClusterProxies kafkaClusterProxies,
                                              ApplicationSettings applicationSettings,
                                              Restartables restartables) {
        this.statusChecker = statusChecker;
        this.syntaxHighlightingConfigurator = syntaxHighlightingConfigurator;
        this.kafkaClusterProxies = kafkaClusterProxies;
        this.applicationSettings = applicationSettings;
        this.restartables = restartables;
    }

    @Override
    public ControllerProvider createFor(TabPane leftViewTabPane,
                                        ListView<KafkaBrokerConfig> brokersListView,
                                        ListView<KafkaTopicConfig> topicsListView,
                                        ListView<KafkaSenderConfig> messagesListView,
                                        ListView<KafkaListenerConfig> listenersListView) {

        final ModelConfigObjectsGuiInformer guiInformer = new DefaultModelConfigObjectsGuiInformer(leftViewTabPane,
                                                                                                   brokersListView,
                                                                                                   topicsListView,
                                                                                                   messagesListView,
                                                                                                   listenersListView);
        return new DefaultControllerProvider(guiInformer,
                                             statusChecker,
                                             syntaxHighlightingConfigurator,
                                             kafkaClusterProxies,
                                             applicationSettings,
                                             restartables);

    }
}
