package application.controllers.confighandlers;

import application.constants.ApplicationConstants;
import application.controllers.ControllerProvider;
import application.controllers.helpers.ListViewActionsHandler;
import application.controllers.helpers.TabPaneSelectionInformer;
import application.controllers.helpers.TemplateGuiActionsHandler;
import application.customfxwidgets.topicconfig.TopicConfigGuiController;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;
import application.model.ToPojoConverter;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class TopicConfigGuiActionsHandler extends TemplateGuiActionsHandler<KafkaTopicConfig> {


    private final ListViewActionsHandler<KafkaTopicConfig> listViewActionsHandler;
    private final ModelDataProxy modelDataProxy;
    private final ControllerProvider controllerProvider;
    private final AnchorPane parentPane;
    private final ListView<KafkaBrokerConfig> brokerConfigs;
    private final FromPojoConverter fromPojoConverter;

    public TopicConfigGuiActionsHandler(TabPaneSelectionInformer tabSelectionInformer,
                                        ListViewActionsHandler<KafkaTopicConfig> listViewActionsHandler,
                                        ModelDataProxy modelDataProxy,
                                        ControllerProvider controllerProvider,
                                        AnchorPane parentPane,
                                        ListView<KafkaBrokerConfig> brokerConfigs) {

        super(tabSelectionInformer, listViewActionsHandler);

        this.listViewActionsHandler = listViewActionsHandler;
        this.modelDataProxy = modelDataProxy;
        this.controllerProvider = controllerProvider;
        this.parentPane = parentPane;
        this.brokerConfigs = brokerConfigs;
        this.fromPojoConverter = new FromPojoConverter(modelDataProxy);
    }

    @Override
    protected void loadController(KafkaTopicConfig config) {

        final TopicConfigGuiController controller = controllerProvider.getTopicConfigGuiController(config,
                                                                                     parentPane,
                                                                                     listViewActionsHandler::refresh,
                                                                                     brokerConfigs.getItems());

        controller.display();
    }


    @Override
    protected void addToModelData() {
        final String configName = ApplicationConstants.DEFAULT_TOPIC_CONFIG_NAME;
        KafkaTopicConfig newConfig = new KafkaTopicConfig(configName);
        final Optional<KafkaTopicConfig> selectedModelObject = listViewActionsHandler.getSelectedModelObject();
        if (selectedModelObject.isPresent()) {
            newConfig = fromPojoConverter.fromPojo(ToPojoConverter.toPojoFrom(selectedModelObject.get()));
            newConfig.assignNewUuid();
        }
        newConfig.setName(configName);
        modelDataProxy.addConfig(newConfig);
    }

}
