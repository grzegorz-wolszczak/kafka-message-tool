package application.customfxwidgets.brokerconfig;

import application.customfxwidgets.AddTopicDialog;
import application.customfxwidgets.ConfigEntriesViewPreferences;
import application.customfxwidgets.CustomFxWidgetsLoader;
import application.customfxwidgets.Displayable;
import application.customfxwidgets.topicpropertieswindow.TopicPropertiesWindow;
import application.displaybehaviour.DetachableDisplayBehaviour;
import application.displaybehaviour.DisplayBehaviour;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.cluster.KafkaClusterProxies;
import application.kafka.dto.TopicToAdd;
import application.customfxwidgets.ConfigEntriesView;
import application.kafka.cluster.ClusterStatusChecker;
import application.kafka.cluster.KafkaClusterProxy;
import application.kafka.cluster.TriStateConfigEntryValue;
import application.kafka.dto.AssignedConsumerInfo;
import application.kafka.dto.ClusterNodeInfo;
import application.kafka.dto.TopicAggregatedSummary;
import application.kafka.dto.UnassignedConsumerInfo;
import application.logging.Logger;
import application.model.modelobjects.KafkaBrokerConfig;
import application.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import org.apache.kafka.clients.admin.ConfigEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;


public class BrokerConfigGuiController extends AnchorPane implements Displayable {

    private static final String FXML_FILE = "BrokerConfigView.fxml";
    private final ClusterStatusChecker statusChecker;
    private KafkaClusterProxies kafkaClusterProxies;
    @FXML
    private TextField brokerConfigNameField;

    @FXML
    private TextField kafkaBrokerHostnameField;

    @FXML
    private TextField kafkaBrokerPortField;

    @FXML
    private Button connectionCheckButton;

    @FXML
    private Tab topicsTab;

    @FXML
    private TabPane clusterSummaryTabPane;

    @FXML
    private TitledPane clusterStatusTitledPane;

    @FXML
    private ToggleButton detachPaneButton;

    /* topicsTableView and related gui controls f*/
    @FXML
    private TableView<TopicAggregatedSummary> topicsTableView;
    @FXML
    private TableColumn<TopicAggregatedSummary, String> topicNameColumn;
    @FXML
    private TableColumn<TopicAggregatedSummary, Integer> partitionCountColumn;
    @FXML
    private TableColumn<TopicAggregatedSummary, Integer> activeAssignedConsumersColumn;
    @FXML
    private TableColumn<TopicAggregatedSummary, Integer> consumerGroupsCountColumn;


    /*  assignedConsumerListTableView and related gui controls */
    @FXML
    private TableView<AssignedConsumerInfo> assignedConsumerListTableView;
    @FXML
    private TableColumn<AssignedConsumerInfo, String> assignedConsumerClientIdColumn;
    @FXML
    private TableColumn<AssignedConsumerInfo, String> assignedConsumerGroupColumn;
    @FXML
    private TableColumn<AssignedConsumerInfo, Integer> assignedConsumerPartitionColumn;
    @FXML
    private TableColumn<AssignedConsumerInfo, String> assignedConsumerNextMsgOffsetColumn;
    @FXML
    private TableColumn<AssignedConsumerInfo, String> assignedConsumerHostColumn;
    @FXML
    private TableColumn<AssignedConsumerInfo, String> assignedConsumerIdColumn;


    /* unassignedConsumerListTableView and related gui controls */
    @FXML
    private TableView<UnassignedConsumerInfo> unassignedConsumerListTableView;
    @FXML
    private TableColumn<UnassignedConsumerInfo, String> unassignedClientIdColumn;
    @FXML
    private TableColumn<UnassignedConsumerInfo, String> unassignedConsumerGroupColumn;
    @FXML
    private TableColumn<UnassignedConsumerInfo, String> unassignedConsumerHostColumn;
    @FXML
    private TableColumn<UnassignedConsumerInfo, String> unassignedConsumerIdColumn;
    @FXML
    private Tab unassignedConsumersTab;
    @FXML
    private TabPane clusterConfigEntriesTabPane;

    private KafkaBrokerConfig config;
    private Window parentWindow;
    private Runnable rerfeshCallback;

    private ObjectProperty<KafkaClusterProxy> kafkaBrokerProxyProperty = new SimpleObjectProperty<>();

    private ConfigEntriesViewPreferences clusterPropertiesViewPreferences = new ConfigEntriesViewPreferences();
    private ConfigEntriesViewPreferences topicPropertiesViewPreferences = new ConfigEntriesViewPreferences();
    private UserInteractor userInteractor;

    private DisplayBehaviour displayBehaviour;

    public BrokerConfigGuiController(KafkaBrokerConfig config,
                                     AnchorPane parentPane,
                                     ModelConfigObjectsGuiInformer guiInformer,
                                     Window parentWindow,
                                     Runnable refeshCallback,
                                     UserInteractor guiInteractor,
                                     ClusterStatusChecker statusChecker,
                                     KafkaClusterProxies kafkaClusterProxies) throws IOException {

        this.statusChecker = statusChecker;
        this.kafkaClusterProxies = kafkaClusterProxies;
        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);

        this.config = config;
        this.parentWindow = parentWindow;
        this.rerfeshCallback = refeshCallback;
        this.userInteractor = guiInteractor;

        final StringExpression windowTitle = composeConfigWindowTitle();

        displayBehaviour = new DetachableDisplayBehaviour(parentPane,
                                                          windowTitle,
                                                          this,
                                                          detachPaneButton.selectedProperty(),
                                                          config,
                                                          guiInformer);
        GuiUtils.expandNodeToAnchorPaneBorders(this);

        configureGuiControls();

        startObservingNewKafkaBrokerProxyForNewHostInfo();
        setCallbackToStartListeningOnNewKafkaProxyChangesWhenHostnameOrPortPropertyChanges();

        resetKafkaBrokerPropertyToFireUpCallbacksForTheFirstTime();
    }

    public void display() {
        displayBehaviour.display();
    }

    private void resetKafkaBrokerPropertyToFireUpCallbacksForTheFirstTime() {
        if (kafkaBrokerProxyProperty == null) {
            return;
        }
        final KafkaClusterProxy proxy = kafkaBrokerProxyProperty.get();
        if (proxy == null) {
            return;
        }

        kafkaBrokerProxyProperty.set(null);
        kafkaBrokerProxyProperty.set(proxy);
    }

    private void setCallbackToStartListeningOnNewKafkaProxyChangesWhenHostnameOrPortPropertyChanges() {
        kafkaBrokerHostnameField.textProperty().addListener((ignored1, ignored2, ignored3) -> {
            startObservingNewKafkaBrokerProxyForNewHostInfo();
        });

        kafkaBrokerPortField.textProperty().addListener((ignored1, ignored2, ignored3) -> {
            startObservingNewKafkaBrokerProxyForNewHostInfo();
        });
    }

    private void observedKafkaBrokerPropertyChanged(ObservableValue<? extends KafkaClusterProxy> observableValue,
                                                    KafkaClusterProxy oldProxy,
                                                    KafkaClusterProxy newProxy) {

        refreshGuiControlsContentCallback();
    }

    private void startObservingNewKafkaBrokerProxyForNewHostInfo() {
        if (kafkaBrokerProxyProperty != null) {
            kafkaBrokerProxyProperty.removeListener(this::observedKafkaBrokerPropertyChanged);
        }
        kafkaBrokerProxyProperty = kafkaClusterProxies.getAsProperty(config.getHostInfo());
        kafkaBrokerProxyProperty.addListener(this::observedKafkaBrokerPropertyChanged);
        clusterStatusTitledPane.visibleProperty().bind(Bindings.isNotNull(kafkaBrokerProxyProperty));

        refreshGuiControlsContentCallback();
    }

    private StringExpression composeConfigWindowTitle() {
        return new ReadOnlyStringWrapper("Broker configuration")
            .concat(" '").concat(config.nameProperty()).concat("' (")
            .concat(config.hostNameProperty())
            .concat(":")
            .concat(config.portProperty())
            .concat(")");
    }

    private void configureGuiControls() {
        brokerConfigNameField.setText(config.getName());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(brokerConfigNameField,
                                                         config::setName,
                                                         ValidatorUtils::isStringIdentifierValid,
                                                         rerfeshCallback);


        kafkaBrokerHostnameField.setText(config.getHostname());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(kafkaBrokerHostnameField,
                                                         config::setHostname,
                                                         ValidatorUtils::isStringIdentifierValid,
                                                         rerfeshCallback);


        kafkaBrokerPortField.setText(config.getPort());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(kafkaBrokerPortField,
                                                         config::setPort,
                                                         ValidatorUtils::isPortValid,
                                                         rerfeshCallback);
        ValidatorUtils.configureTextFieldToAcceptOnlyDecimalValues(kafkaBrokerPortField);


        unassignedConsumersTab.setTooltip(TooltipCreator.createFrom("Active consumers for which kafka broker did not assign any partition."));
        clusterStatusTitledPane.setVisible(false);
    }

    @FXML
    private void initialize() {
        initializeTopicDetailTableView();
        initializeAssignedConsumersTableView();
        initializeUnassignedConsumersTableView();
    }

    private void initializeUnassignedConsumersTableView() {
        TableUtils.installCopyPasteHandlerForSingleCell(unassignedConsumerListTableView);
        unassignedClientIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClientId()));
        unassignedConsumerGroupColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerGroupId()));
        unassignedConsumerHostColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHost()));
        unassignedConsumerIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerId()));
    }

    private void initializeAssignedConsumersTableView() {
        TableUtils.installCopyPasteHandlerForSingleCell(assignedConsumerListTableView);
        assignedConsumerClientIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClientId()));
        assignedConsumerGroupColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerGroupId()));
        assignedConsumerPartitionColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPartition()).asObject());
        assignedConsumerNextMsgOffsetColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getOffset()));
        assignedConsumerHostColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHost()));
        assignedConsumerIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerId()));
    }

    private void initializeTopicDetailTableView() {
        TableUtils.installCopyPasteHandlerForSingleCell(topicsTableView);
        topicNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTopicName()));
        partitionCountColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getPartitionsCount()).asObject());
        activeAssignedConsumersColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getConsumersCount()).asObject());
        consumerGroupsCountColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getConsumerGroupsCount()).asObject());
    }

    private void refreshGuiControlsContentCallback() {
        final KafkaClusterProxy proxy = kafkaBrokerProxyProperty.get();
        if (proxy == null) {
            return;
        }
        Platform.runLater(() -> this.refreshClusterSummaryPaneContent(proxy));
        Platform.runLater(() -> this.fillTopicInfoPane(proxy));
        Platform.runLater(() -> this.fillUnassignedConsumersTab(proxy));
    }

    private void fillUnassignedConsumersTab(KafkaClusterProxy proxy) {
        final Set<UnassignedConsumerInfo> info = proxy.getUnassignedConsumersInfo();
        clusterSummaryTabPane.getTabs().removeAll(unassignedConsumersTab);
        if (info.isEmpty()) {
            Logger.trace("Unassigned consumers not found.");
            return;
        }

        Logger.trace(String.format("Unassigned consumers found (count:%d)", info.size()));
        clusterSummaryTabPane.getTabs().add(unassignedConsumersTab);
        unassignedConsumerListTableView.setItems(FXCollections.observableArrayList(info));
        unassignedConsumerListTableView.getSortOrder().add(unassignedClientIdColumn);
    }


    @FXML
    private void checkButtonOnAction() {
        refreshBrokerStatus(true);
    }

    private void refreshBrokerStatus(boolean shouldShowWarningOnInvalidConfig) {
        statusChecker.updateStatus(config.getHostInfo(),
                                   shouldShowWarningOnInvalidConfig);
    }


    private void fillTopicInfoPane(KafkaClusterProxy proxy) {
        assignedConsumerListTableView.getItems().clear();
        refreshTopicTableView(proxy);
        bindActionsToSelectedRow(proxy);
        bindActionsTableView(proxy);
    }

    private void bindActionsTableView(KafkaClusterProxy proxy) {
        final MenuItem createTopicMenuItem = createMenuItemForCreatingNewTopic();
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().setAll(createTopicMenuItem);
        topicsTableView.contextMenuProperty().bind(new ReadOnlyObjectWrapper<>(contextMenu));
    }

    private void refreshTopicTableView(KafkaClusterProxy proxy) {

        final Set<TopicAggregatedSummary> aggregatedTopicSummary = proxy.getAggregatedTopicSummary();

        topicsTableView.getItems().clear();
        topicsTableView.setItems(FXCollections.observableArrayList(aggregatedTopicSummary));
        topicsTableView.getSortOrder().add(topicNameColumn);

    }

    private void bindActionsToSelectedRow(KafkaClusterProxy proxy) {

        topicsTableView.setRowFactory(tableView -> {
            final TableRow<TopicAggregatedSummary> row = new TableRow<>();

            bindPopupMenuToSelectedRow(proxy, row);

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    showAssignedConsumerInfo(row);
                } else if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    showTopicConfigPropertiesWindow(kafkaBrokerProxyProperty.get(), row.getItem().getTopicName());
                }
            });
            return row;
        });
    }

    private void showAssignedConsumerInfo(TableRow<TopicAggregatedSummary> row) {
        final String topicName = row.getItem().getTopicName();
        final KafkaClusterProxy currentProxy = kafkaBrokerProxyProperty.get();
        final Set<AssignedConsumerInfo> consumers = currentProxy.getConsumersForTopic(topicName);
        assignedConsumerListTableView.getItems().clear();
        assignedConsumerListTableView.setItems(FXCollections.observableArrayList(consumers));
    }

    private void bindPopupMenuToSelectedRow(KafkaClusterProxy proxy, TableRow<TopicAggregatedSummary> row) {

        final MenuItem deleteTopicMenuItem = createMenuItemForDeletingTopic();
        final MenuItem createTopicMenuItem = createMenuItemForCreatingNewTopic();
        final MenuItem topicPropertiesMenuItem = createMenuItemForShowingTopicProperties();

        final ContextMenu contextMenu = getTopicManagementContextMenu(deleteTopicMenuItem,
                                                                      createTopicMenuItem,
                                                                      topicPropertiesMenuItem);

        row.contextMenuProperty().bind(new ReadOnlyObjectWrapper<>(contextMenu));
        topicPropertiesMenuItem.disableProperty().bind(row.emptyProperty());

        if (proxy.isTopicDeletionEnabled() != TriStateConfigEntryValue.True) {
            deleteTopicMenuItem.setText("Delete topic (disabled by broker)");
            deleteTopicMenuItem.disableProperty().setValue(true);
        } else {
            deleteTopicMenuItem.disableProperty().bind(row.emptyProperty());
        }
    }

    private ContextMenu getTopicManagementContextMenu(MenuItem deleteTopicMenuItem,
                                                      MenuItem createTopicMenuItem,
                                                      MenuItem topicPropertiesMenuItem) {
        final ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().setAll(deleteTopicMenuItem,
                                      createTopicMenuItem,
                                      new SeparatorMenuItem(),
                                      topicPropertiesMenuItem);
        return contextMenu;
    }

    private MenuItem createMenuItemForShowingTopicProperties() {
        final MenuItem topicPropertiesMenuItem = new MenuItem("Topic properties");
        topicPropertiesMenuItem.setOnAction(event -> {
            final TopicAggregatedSummary summary = topicsTableView.getSelectionModel().selectedItemProperty().get();
            showTopicConfigPropertiesWindow(kafkaBrokerProxyProperty.get(), summary.getTopicName());
        });
        return topicPropertiesMenuItem;
    }

    private MenuItem createMenuItemForCreatingNewTopic() {
        final MenuItem createTopicMenuItem = new MenuItem("Create topic");
        createTopicMenuItem.setOnAction((ActionEvent event) -> {
            try {
                createNewTopicAction(kafkaBrokerProxyProperty.get());
            } catch (Exception e) {
                Logger.error("Could not createFrom config", e);
            }
        });
        return createTopicMenuItem;
    }

    private MenuItem createMenuItemForDeletingTopic() {
        final MenuItem deleteTopicMenuItem = new MenuItem("Delete topic");
        deleteTopicMenuItem.setOnAction(event -> {
            final TopicAggregatedSummary summary = topicsTableView.getSelectionModel().selectedItemProperty().get();
            try {
                deleteTopic(kafkaBrokerProxyProperty.get(), summary);
            } catch (Exception e) {
                Logger.error("Could not delete config", e);
            }
        });
        return deleteTopicMenuItem;
    }

    private void showTopicConfigPropertiesWindow(KafkaClusterProxy kafkaClusterProxy,
                                                 String topicName) {

        final Set<ConfigEntry> topicProperties = kafkaClusterProxy.getTopicProperties(topicName);
        try {
            ConfigEntriesView entriesView = new ConfigEntriesView("Topic properties", topicProperties, topicPropertiesViewPreferences);
            final TopicPropertiesWindow topicPropertiesWindow = TopicPropertiesWindow.get(topicName,
                                                                                          entriesView);
            topicPropertiesWindow.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTopicDeleteWarningMessageForUser() {
        return " *** NOTE ***\n" +
            " * Topic delete operation may not be immediate.\n" +
            " * It may take several seconds for all the brokers to become aware that the topic is gone.\n" +
            " * During this time kafka cluster may continue to return information about the deleted topics as if they still exist.";
    }

    private void deleteTopic(KafkaClusterProxy proxy,
                             TopicAggregatedSummary summary) throws Exception {
        final String topicName = summary.getTopicName();

        final boolean deletionConfired = userInteractor
            .getYesNoDecision("Deleting topic...",
                              String.format("Are you sure you want to delete topic '%s' ?", topicName),
                              getTopicDeleteWarningMessageForUser());
        if (!deletionConfired) {
            return;
        }
        proxy.deleteTopic(topicName);
        refreshBrokerStatus(false);
    }

    private void refreshClusterSummaryPaneContent(KafkaClusterProxy proxy) {
        Logger.trace("Refreshing cluster pane");
        clusterConfigEntriesTabPane.getTabs().clear();

        final Set<ClusterNodeInfo> nodeInfos = proxy.getNodesInfo();
        List<Tab> tabs = new ArrayList<>();

        for (ClusterNodeInfo nodeInfo : nodeInfos) {

            final Set<ConfigEntry> entries = nodeInfo.getEntries();
            if (entries == null || entries.isEmpty()) {
                continue;
            }

            ConfigEntriesView clusterPropertiesTableView;
            try {
                clusterPropertiesTableView = new ConfigEntriesView("Node properties", entries, clusterPropertiesViewPreferences);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            Tab nodeTab = getNodeTab(nodeInfo, clusterPropertiesTableView);
            tabs.add(nodeTab);

        }
        addTabsToClusterConfigPane(tabs);
    }

    private Tab getNodeTab(ClusterNodeInfo nodeInfo, ConfigEntriesView clusterPropertiesTableView) {
        Tab nodeTab = new Tab();
        AnchorPane nodeTabContentPane = new AnchorPane();
        nodeTabContentPane.getChildren().add(clusterPropertiesTableView);
        nodeTab.setContent(nodeTabContentPane);
        nodeTab.setText(String.format("Node id: %s", nodeInfo.getNodeId()));
        if (nodeInfo.isController()) {
            nodeTab.setText(String.format("Node id: %s [controller]", nodeInfo.getNodeId()));
        }
        return nodeTab;
    }

    private void addTabsToClusterConfigPane(List<Tab> sortedTabs) {
        sortedTabs.sort(Comparator.comparing(Tab::getText));
        if (!sortedTabs.isEmpty()) {
            clusterConfigEntriesTabPane.getTabs().addAll(sortedTabs);
        }
    }

    private void createNewTopicAction(KafkaClusterProxy proxy) throws Exception {
        final TopicToAdd topicDetails = new TopicToAdd();
        final ButtonType callType = new AddTopicDialog(getParentWindow()).call(topicDetails);
        if (callType == ButtonType.OK) {
            createTopicButShowGuiErrorOnFailure(proxy, topicDetails);
        }
    }

    private Window getParentWindow() {
        return parentWindow;
    }

    private void createTopicButShowGuiErrorOnFailure(KafkaClusterProxy proxy,
                                                     TopicToAdd topicToAdd) {
        Logger.debug("Adding topic " + topicToAdd);
        try {
            proxy.createTopic(topicToAdd);

            refreshBrokerStatus(false);
        } catch (Exception e) {
            userInteractor.showError("Adding new topic failed", e);
        }
    }
}

