package application.controllers.confighandlers;

import application.constants.ApplicationConstants;
import application.controllers.ControllerProvider;
import application.controllers.helpers.ListViewActionsHandler;
import application.controllers.helpers.TabPaneSelectionInformer;
import application.controllers.helpers.TemplateGuiActionsHandler;
import application.customfxwidgets.senderconfig.SenderConfigGuiController;
import application.kafka.sender.KafkaMessageSender;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;
import application.model.ToPojoConverter;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class SenderConfigGuiActionsHandler extends TemplateGuiActionsHandler<KafkaSenderConfig> {


    private final ListViewActionsHandler<KafkaSenderConfig> listViewActionsHandler;
    private final ModelDataProxy modelDataProxy;
    private final ControllerProvider controllerProvider;
    private final AnchorPane parentPane;
    private KafkaMessageSender sender;
    private ListView<KafkaTopicConfig> topicConfigs;
    private final FromPojoConverter fromPojoConverter;

    public SenderConfigGuiActionsHandler(TabPaneSelectionInformer tabSelectionInformer,
                                         ListViewActionsHandler<KafkaSenderConfig> listViewActionsHandler,
                                         ModelDataProxy modelDataProxy,
                                         ControllerProvider controllerProvider,
                                         AnchorPane parentPane,
                                         ListView<KafkaTopicConfig> topicConfigs,
                                         KafkaMessageSender sender) {
        super(tabSelectionInformer, listViewActionsHandler);

        this.listViewActionsHandler = listViewActionsHandler;
        this.modelDataProxy = modelDataProxy;
        this.controllerProvider = controllerProvider;
        this.parentPane = parentPane;

        this.sender = sender;
        this.topicConfigs = topicConfigs;
        this.fromPojoConverter = new FromPojoConverter(modelDataProxy);

    }

    @Override
    protected void loadController(KafkaSenderConfig config) {
        final SenderConfigGuiController controller = controllerProvider.getSenderConfigGuiController(config,
                                                                                      parentPane,
                                                                                      sender,
                                                                                      listViewActionsHandler::refresh,
                                                                                      topicConfigs.getItems());

        controller.display();
    }

    @Override
    protected void addToModelData() {
        final String configName = ApplicationConstants.DEFAULT_SENDER_CONFIG_NAME;
        KafkaSenderConfig newConfig = new KafkaSenderConfig(configName);

        final Optional<KafkaSenderConfig> selectedModelObject = listViewActionsHandler.getSelectedModelObject();
        if (selectedModelObject.isPresent()) {
            newConfig = fromPojoConverter.fromPojo(ToPojoConverter.toPojoFrom(selectedModelObject.get()));
            newConfig.assignNewUuid();
        }
        newConfig.setName(configName);
        modelDataProxy.addConfig(newConfig);
    }
}
