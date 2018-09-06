package application.customfxwidgets.mainviewcontroller;

import application.controllers.ControllerProvider;
import application.controllers.DefaultControllerProvider;
import application.displaybehaviour.DefaultModelConfigObjectsGuiInformer;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.cluster.ClusterStatusChecker;
import application.kafka.cluster.KafkaClusterProxiesBase;
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
    private KafkaClusterProxiesBase kafkaClusterProxiesBase;
    private ApplicationSettings applicationSettings;
    private Restartables restartables;

    public DefaultControllerRepositoryFactory(ClusterStatusChecker statusChecker,
                                              SyntaxHighlightingCodeAreaConfigurator syntaxHighlightingConfigurator,
                                              KafkaClusterProxiesBase kafkaClusterProxiesBase,
                                              ApplicationSettings applicationSettings,
                                              Restartables restartables) {
        this.statusChecker = statusChecker;
        this.syntaxHighlightingConfigurator = syntaxHighlightingConfigurator;
        this.kafkaClusterProxiesBase = kafkaClusterProxiesBase;
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
                kafkaClusterProxiesBase,
                                             applicationSettings,
                                             restartables);

    }
}
