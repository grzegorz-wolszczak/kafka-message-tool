package application.customfxwidgets.mainviewcontroller;

import application.logging.AppLogger;
import application.notifications.StatusNotifier;
import application.customfxwidgets.aboutwindow.AboutWindow;
import application.root.ApplicationBusySwitcher;
import application.root.DefaultActionHandlerFactory;
import application.constants.GuiStrings;
import application.controllers.ControllerProvider;
import application.controllers.helpers.ModelObjectGuiActionsHandler;
import application.notifications.LogLevel;
import application.model.DataModel;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.persistence.ApplicationSettings;
import application.persistence.GuiSettings;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainApplicationController extends VBox {

    private static final ObservableList<LogLevel> ALL_LOG_LEVEL_ITEMS = FXCollections.observableArrayList(LogLevel.values());
    private final List<ModelObjectGuiActionsHandler> guiActionsHandlers = new ArrayList<>();
    private final Stage appStage;
    private final DataModel dataModel;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Tab brokersTab;
    @FXML
    private Tab topicsTab;
    @FXML
    private Tab sendMsgTab;
    @FXML
    private Tab receiveMsgTab;
    @FXML
    private TabPane leftViewTabPane;
    @FXML
    private ListView<KafkaBrokerConfig> brokersListView;
    @FXML
    private ListView<KafkaTopicConfig> topicsListView;
    @FXML
    private ListView<KafkaSenderConfig> sendersListView;
    @FXML
    private ListView<KafkaListenerConfig> listenersListView;
    @FXML
    private SplitPane mainSplitPane;
    @FXML
    private SplitPane upperSplitPane;
    @FXML
    private AnchorPane rightContentPane;
    @FXML
    private ComboBox<LogLevel> logSeverityCombobox;
    @FXML
    private GridPane loggingTabGridPane;

    @FXML
    private StatusBar statusBar;

    private ControllerProvider controllerProvider;
    private Application fxApplication;
    private ApplicationSettings appSettings;
    private VirtualizedScrollPane<StyleClassedTextArea> loggingPaneArea;
    private ControllerRepositoryFactory controllersRepositoryFactory;
    private DefaultActionHandlerFactory actionHandlerFactory;
    private ApplicationBusySwitcher busySwitcher;
    private StatusNotifier statusNotifier;

    public MainApplicationController(Stage mainStage,
                                     DataModel model,
                                     Application fxApplication,
                                     ApplicationSettings appSettings,
                                     VirtualizedScrollPane<StyleClassedTextArea> loggingPaneArea,
                                     ControllerRepositoryFactory controllersRepositoryFactory,
                                     DefaultActionHandlerFactory actionHandlerFactory,
                                     ApplicationBusySwitcher busySwitcher,
                                     StatusNotifier statusNotifier) {
        appStage = mainStage;
        dataModel = model;
        this.fxApplication = fxApplication;
        this.appSettings = appSettings;
        this.loggingPaneArea = loggingPaneArea;
        this.controllersRepositoryFactory = controllersRepositoryFactory;
        this.actionHandlerFactory = actionHandlerFactory;
        this.busySwitcher = busySwitcher;
        this.statusNotifier = statusNotifier;
    }


    public void setupControls() {
        connectStageGuiPropertiesFromSettings();
        bindGuiElementsProperties();
        setLoggingPane();
        setGuiElementsTextNames();
        connectModelToViews();
        initializeInternalDependencies();
        initializeListViewActionHandlers();
        registerNodesForBusyStateManagement();
        setupStatusBar();

    }

    private void setupStatusBar() {
        statusNotifier.configureStatusBar(statusBar);
    }

    private void registerNodesForBusyStateManagement() {
        busySwitcher.registerNodeForDisabledStageManagement(leftViewTabPane);
        busySwitcher.registerNodeForDisabledStageManagement(addButton);
        busySwitcher.registerNodeForDisabledStageManagement(deleteButton);
        busySwitcher.registerNodeForDisabledStageManagement(rightContentPane);
    }

    private void connectModelToViews() {
        final ObservableList<KafkaSenderConfig> observables = dataModel.getSenderConfigs().getObservables();
        sendersListView.setItems(observables);
        topicsListView.setItems(dataModel.getTopicConfigs().getObservables());
        listenersListView.setItems(dataModel.getListenerConfigs().getObservables());
        brokersListView.setItems(dataModel.getBrokerConfigs().getObservables());
    }

    private void connectStageGuiPropertiesFromSettings() {
        final GuiSettings guiSettings = appSettings.guiSettings();

        appStage.setWidth(guiSettings.mainWindowWidthProperty().getValue());
        guiSettings.mainWindowWidthProperty().bind(appStage.widthProperty());

        appStage.setHeight(guiSettings.mainWindowHeightProperty().getValue());
        guiSettings.mainWindowHeightProperty().bind(appStage.heightProperty());
    }

    private void initializeInternalDependencies() {

        controllerProvider = controllersRepositoryFactory.createFor(leftViewTabPane,
                                                                    brokersListView,
                                                                    topicsListView,
                                                                    sendersListView,
                                                                    listenersListView);
    }

    private void bindGuiElementsProperties() {

        final GuiSettings guiSettings = appSettings.guiSettings();

        mainSplitPane.getDividers()
            .get(0)
            .positionProperty()
            .bindBidirectional(guiSettings.mainWindowSplitPaneDividerPositionProperty());

        upperSplitPane.getDividers()
            .get(0)
            .positionProperty()
            .bindBidirectional(guiSettings.upperSplitPaneDividerPositionProperty());

        logSeverityCombobox.setItems(ALL_LOG_LEVEL_ITEMS);
        logSeverityCombobox.valueProperty().bindBidirectional(appSettings
                                                                  .appSettings()
                                                                  .logLevelProperty());

        logSeverityCombobox.valueProperty().addListener((ignored01, ignored02, t1) ->
                                                            AppLogger.setLogLevel(t1));

    }

    private void setLoggingPane() {
        loggingTabGridPane.add(loggingPaneArea, 0, 0, GridPane.REMAINING, 1);
    }

    private void setGuiElementsTextNames() {
        brokersTab.setText(GuiStrings.BROKER_TAB_NAME);
        topicsTab.setText(GuiStrings.TOPIC_CONFIGS_TAB_NAME);
        sendMsgTab.setText(GuiStrings.SENDER_MSG_TAB_NAME);
        receiveMsgTab.setText(GuiStrings.LISTENER_MSG_TAB_NAME);
    }

    private void initializeListViewActionHandlers() {
        guiActionsHandlers.add(actionHandlerFactory.createBrokerConfigListViewActionHandler(rightContentPane,
                                                                                            leftViewTabPane,
                                                                                            brokersTab,
                                                                                            brokersListView,
                                                                                            controllerProvider));


        guiActionsHandlers.add(actionHandlerFactory.createTopicConfigListViewActionHandler(rightContentPane,
                                                                                           leftViewTabPane,
                                                                                           topicsTab,
                                                                                           topicsListView,
                                                                                           controllerProvider,
                                                                                           brokersListView));


        guiActionsHandlers.add(actionHandlerFactory.createSenderConfigListViewActionHandler(rightContentPane,
                                                                                            leftViewTabPane,
                                                                                            sendMsgTab,
                                                                                            sendersListView,
                                                                                            topicsListView,
                                                                                            controllerProvider));

        guiActionsHandlers.add(actionHandlerFactory.createListenerConfigListViewActionHandler(rightContentPane,
                                                                                              leftViewTabPane,
                                                                                              receiveMsgTab,
                                                                                              listenersListView,
                                                                                              topicsListView,
                                                                                              controllerProvider));


    }

    @FXML
    private void addButtonOnAction() {
        guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::addNewConfig);
        refreshRightPane();
    }

    @FXML
    private void deleteButtonOnAction() {
        guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::deleteSelectedConfig);
        refreshRightPane();
    }

    @FXML
    private void clearLogsButtonOnAction() {
        AppLogger.clear();
    }

    @FXML
    private void leftViewTabPaneOnMouseClicked() {
        refreshRightPane();
    }

    @FXML
    private void menuItemSaveConfigOnAction() {
        appSettings.save();
    }

    @FXML
    private void menuItemShowAboutWindow(){
        showAboutWindow();
    }

    private void showAboutWindow() {
        try {
            final AboutWindow aboutWindow = new AboutWindow(appStage, fxApplication);
            aboutWindow.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onAnyListViewClicked() {
        refreshRightPane();
    }

    private void refreshRightPane() {
        rightContentPane.getChildren().clear();
        guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::resetRightContentPaneToSelectedConfig);


    }

}

