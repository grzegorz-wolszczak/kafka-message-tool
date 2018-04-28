package application.controllers.confighandlers;

import application.constants.ApplicationConstants;
import application.controllers.ControllerProvider;
import application.controllers.helpers.ListViewActionsHandler;
import application.controllers.helpers.TemplateGuiActionsHandler;
import application.customfxwidgets.brokerconfig.BrokerConfigView;
import application.controllers.helpers.TabPaneSelectionInformer;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;
import application.model.ToPojoConverter;
import application.model.modelobjects.KafkaBrokerConfig;
import application.utils.UserInteractor;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;

import java.util.Optional;

public class BrokerConfigGuiActionsHandler extends TemplateGuiActionsHandler<KafkaBrokerConfig> {


    private final ListViewActionsHandler<KafkaBrokerConfig> listViewActionsHandler;
    private final UserInteractor interactor;
    private final ModelDataProxy modelDataProxy;
    private final ControllerProvider controllerProvider;
    private final Window parentWindow;
    private final AnchorPane parentPane;
    private final FromPojoConverter fromPojoConverter;


    public BrokerConfigGuiActionsHandler(TabPaneSelectionInformer tabSelectionInformer,
                                         ListViewActionsHandler<KafkaBrokerConfig> listViewActionsHandler,
                                         UserInteractor interactor,
                                         ModelDataProxy modelDataProxy,
                                         ControllerProvider controllerProvider,
                                         AnchorPane parentPane,
                                         Window parentWindow) {
        super(tabSelectionInformer, listViewActionsHandler);

        this.listViewActionsHandler = listViewActionsHandler;
        this.interactor = interactor;
        this.modelDataProxy = modelDataProxy;
        this.controllerProvider = controllerProvider;
        this.parentWindow = parentWindow;
        this.parentPane = parentPane;
        this.fromPojoConverter = new FromPojoConverter(modelDataProxy);

    }


    @Override
    protected void loadController(KafkaBrokerConfig config) {
        final BrokerConfigView controller = controllerProvider.getBrokerConfigGuiController(config,
                                                                                            parentPane,
                                                                                            listViewActionsHandler::refresh,
                                                                                            interactor,
                                                                                            parentWindow);
        controller.display();
    }

    @Override
    protected void addToModelData() {
        final String configName = ApplicationConstants.DEFAULT_BROKER_CONFIG_NAME;
        KafkaBrokerConfig newConfig = new KafkaBrokerConfig(configName);
        final Optional<KafkaBrokerConfig> selectedModelObject = listViewActionsHandler.getSelectedModelObject();
        if (selectedModelObject.isPresent()) {
            newConfig = fromPojoConverter.fromPojo(ToPojoConverter.toPojoFrom(selectedModelObject.get()));
            newConfig.assignNewUuid();
        }
        newConfig.setName(configName);
        modelDataProxy.addConfig(newConfig);

    }

}
