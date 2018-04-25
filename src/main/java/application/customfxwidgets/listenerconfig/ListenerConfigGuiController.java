package application.customfxwidgets.listenerconfig;

import application.customfxwidgets.CustomFxWidgetsLoader;
import application.customfxwidgets.Displayable;
import application.customfxwidgets.TopicConfigComboBoxConfigurator;
import application.displaybehaviour.DetachableDisplayBehaviour;
import application.displaybehaviour.DisplayBehaviour;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.listener.AssignedPartitionsInfo;
import application.kafka.listener.Listener;
import application.kafka.listener.Listeners;
import application.logging.FixedNumberRecordsCountLogger;
import application.model.KafkaOffsetResetType;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.root.SwingTextAreaWrapper;
import application.utils.ConfigNameGenerator;
import application.utils.GuiUtils;
import application.utils.ValidatorUtils;
import application.utils.gui.FXNodeBlinker;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class ListenerConfigGuiController extends AnchorPane implements Displayable {

    public static final int ZERO_RECEIVED_MSGS = 0;
    public static final String TOTAL_RECEIVED_PREFIX = "Total received msgs: %s";
    private static final String FXML_FILE = "ListenerConfigView.fxml";
    private final DisplayBehaviour displayBehaviour;
    private final TopicConfigComboBoxConfigurator comboBoxConfigurator;

    private final AnchorPane parentPane;
    private final ModelConfigObjectsGuiInformer guiInformer;
    private final PartitionAssignmentChangeHandler partitionAssignmentHandler;
    private final MenuItem generateNameMenuItem = new MenuItem("Generate name");
    @FXML
    private TextField listenerNameTextField;
    @FXML
    private ComboBox<KafkaTopicConfig> topicConfigComboBox;
    //@FXML
    //private TextArea outputTextArea;
    @FXML
    private Button stopButton;
    @FXML
    private Button startButton;
    @FXML
    private TextField consumerGroupTextField;
    @FXML
    private TextField fetchTimeoutTextField;
    @FXML
    private ComboBox<KafkaOffsetResetType> offsetResetComboBox;
    @FXML
    private ToggleButton detachPaneButton;
    @FXML
    private TextField receiveMsgLimitTextField;
    @FXML
    private CheckBox receiveMsgLimitCheckBox;
    @FXML
    private Label assignedPartitionsLabel;
    @FXML
    private Label receivedTotalMsgLabel;
    @FXML
    private TitledPane outputTitlePane;
    private KafkaListenerConfig config;
    private Listeners activeConsumers;
    private Runnable refreshCallback;
    private ObservableList<KafkaTopicConfig> topicConfigs;
    private ToFileSaver toFileSaver;
    private FixedNumberRecordsCountLogger fixedRecordsLogger;
    private int totalReceivedMsgCounter = ZERO_RECEIVED_MSGS;


    public ListenerConfigGuiController(KafkaListenerConfig config,
                                       AnchorPane parentPane,
                                       ModelConfigObjectsGuiInformer guiInformer,
                                       Listeners activeConsumers,
                                       Runnable refreshCallback,
                                       ObservableList<KafkaTopicConfig> topicConfigs,
                                       ToFileSaver toFileSaver,
                                       FixedNumberRecordsCountLogger fixedRecordsLogger) throws IOException {
        this.parentPane = parentPane;
        this.guiInformer = guiInformer;
        this.toFileSaver = toFileSaver;
        this.fixedRecordsLogger = fixedRecordsLogger;
        partitionAssignmentHandler = new PartitionAssignmentChangeHandler(
            new FXNodeBlinker(Color.BLACK), config);

        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);

        this.config = config;
        this.activeConsumers = activeConsumers;
        this.refreshCallback = refreshCallback;
        this.topicConfigs = topicConfigs;

        final StringExpression windowTitle = new ReadOnlyStringWrapper("Kafka listener configuration");
        displayBehaviour = new DetachableDisplayBehaviour(parentPane,
                                                          windowTitle,
                                                          this,
                                                          detachPaneButton.selectedProperty(),
                                                          config,
                                                          guiInformer);
        configureTopicConfigComboBox();
        configureOffsetResetComboBox();
        configureMessageNameTextField();
        configureConsumerGroupField();
        configureFetchTimeoutField();
        configureReceiveMsgLimitControls();
        setKafkaListenerBinding();

        configureGuiControlDisableStateBasedOnStartButtonState();
        GuiUtils.configureComboBoxToClearSelectedValueIfItsPreviousValueWasRemoved(topicConfigComboBox);

        comboBoxConfigurator = new TopicConfigComboBoxConfigurator<>(topicConfigComboBox, config);
        comboBoxConfigurator.configure();

    }

    @Override
    public void display() {
        displayBehaviour.display();
    }

    @FXML
    private void initialize() {
        configureFixedRecordLogger();
        configureDisplayBehaviour();
        configureToFileSaver();
        addAdditionalOptionsToTextAreaPopupMenu();
        resetTotalReceivedLabeltext();
        configurePartitionsAssignmentsChangedLabel();
        configureNameGenerator();
        addAdditionalEntryToConfigNameContextMenu();
    }

    private void configureNameGenerator() {
        generateNameMenuItem.setOnAction(event -> {
            final String newName = ConfigNameGenerator.generateNewListenerConfigName(config);
            listenerNameTextField.setText(newName);
        });
    }

    private void addAdditionalEntryToConfigNameContextMenu() {
        TextFieldSkin customContextSkin = new TextFieldSkin(listenerNameTextField) {
            @Override
            public void populateContextMenu(ContextMenu contextMenu) {
                super.populateContextMenu(contextMenu);
                contextMenu.getItems().add(0, new SeparatorMenuItem());
                contextMenu.getItems().add(0, generateNameMenuItem);
            }
        };
        listenerNameTextField.setSkin(customContextSkin);

    }

    private void configurePartitionsAssignmentsChangedLabel() {
        partitionAssignmentHandler.setLabelToChange(assignedPartitionsLabel);
        partitionAssignmentHandler.updatePartitionsAssignmentLabelFor(AssignedPartitionsInfo.invalid());
    }

    private void configureFixedRecordLogger() {
        final SwingTextAreaWrapper logTextArea = new SwingTextAreaWrapper(new JTextArea());
        outputTitlePane.setContent(logTextArea.asNode());
        fixedRecordsLogger.setLogTextArea(logTextArea);
        //fixedRecordsLogger.setLogTextArea(new FxTextAreaWrapper(outputTextArea));
        fixedRecordsLogger.start();
    }

    private void configureDisplayBehaviour() {

    }

    private void configureToFileSaver() {
        toFileSaver.setContentSupplier(() -> fixedRecordsLogger.getText());
        fixedRecordsLogger.setSaveToFilePopupAction(toFileSaver::saveContentToFile);
    }

    private void addAdditionalOptionsToTextAreaPopupMenu() {

//        TextAreaSkin customContextSkin = new TextAreaSkin(outputTextArea) {
//            @Override
//            public void populateContextMenu(ContextMenu contextMenu) {
//                super.populateContextMenu(contextMenu);
//                contextMenu.getItems().add(0, new SeparatorMenuItem());
//                contextMenu.getItems().add(0, saveToFilePopupMenuItem);
//            }
//        };
//        outputTextArea.setSkin(customContextSkin);
    }

    private void configureReceiveMsgLimitControls() {

        receiveMsgLimitCheckBox.setSelected(config.getReceivedMsgLimitEnabled());
        receiveMsgLimitCheckBox.selectedProperty().bindBidirectional(config.receivedMsgLimitEnabledProperty());

        receiveMsgLimitTextField.disableProperty().bind(receiveMsgLimitCheckBox.selectedProperty().not());

        ValidatorUtils.configureTextFieldToAcceptOnlyDecimalValues(receiveMsgLimitTextField);
        receiveMsgLimitTextField.setText(config.getReceivedMsgLimitCount());
        config.receivedMsgLimitCountProperty().bind(receiveMsgLimitTextField.textProperty());

    }

    private void configureGuiControlDisableStateBasedOnStartButtonState() {
        final BooleanBinding disabledProperty = stopButton.disableProperty().not();
        consumerGroupTextField.disableProperty().bind(disabledProperty);
        fetchTimeoutTextField.disableProperty().bind(disabledProperty);
        topicConfigComboBox.disableProperty().bind(disabledProperty);
        offsetResetComboBox.disableProperty().bind(disabledProperty);
        listenerNameTextField.disableProperty().bind(disabledProperty);
    }

    private void configureFetchTimeoutField() {
        fetchTimeoutTextField.textProperty().set(config.getPollTimeout());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(fetchTimeoutTextField,
                                                         config::setPollTimeout,
                                                         ValidatorUtils::isTimeoutInMsValid);
    }

    private void configureConsumerGroupField() {
        consumerGroupTextField.setText(config.getConsumerGroup());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(consumerGroupTextField,
                                                         config::setConsumerGroup,
                                                         ValidatorUtils::isStringIdentifierValid);
    }

    private void setKafkaListenerBinding() {
        getActiveListenersForConfig().ifPresent(listener -> {
            listener.loggedTextProperty().addListener((observableValue, s, t1) -> appendTextScrolledToBottom(t1));
            listener.assignedPartitionsProperty().addListener(this::partitionsAssignmentChanged);
            startButton.disableProperty().bind(listener.isRunningProperty());
            stopButton.disableProperty().bind(listener.isRunningProperty().not());
        });
    }

    private void partitionsAssignmentChanged(ObservableValue<? extends AssignedPartitionsInfo> observable,
                                             AssignedPartitionsInfo oldValue,
                                             AssignedPartitionsInfo newValue) {
        Platform.runLater(() -> partitionAssignmentHandler
            .updatePartitionsAssignmentLabelFor(newValue));
    }


    private void appendTextScrolledToBottom(String textToAppend) {
        appendLogToTextArea(textToAppend);
        incrementReceivedMsgCount();
    }


    private void appendLogToTextArea(String textToAppend) {
        fixedRecordsLogger.appendText(textToAppend);
    }

    private void incrementReceivedMsgCount() {
        totalReceivedMsgCounter++;
        updateReceivedMsgLabel();
    }

    private void updateReceivedMsgLabel() {
        Platform.runLater(() -> {
            receivedTotalMsgLabel.setText(String.format(TOTAL_RECEIVED_PREFIX, totalReceivedMsgCounter));
        });
    }

    private void configureMessageNameTextField() {
        listenerNameTextField.setText(config.getName());
        listenerNameTextField.textProperty().addListener((observableValue, s, newValue) -> {

            final PseudoClass errorClass = PseudoClass.getPseudoClass("error");
            listenerNameTextField.pseudoClassStateChanged(errorClass, true);
            if (ValidatorUtils.isStringIdentifierValid(newValue)) {
                config.setName(newValue.trim());
                listenerNameTextField.pseudoClassStateChanged(errorClass, false);
            }
            refreshCallback.run();
        });
    }

    private Optional<Listener> getActiveListenersForConfig() {
        return Optional.of(activeConsumers.getListener(config));
    }

    private void configureOffsetResetComboBox() {
        offsetResetComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(KafkaOffsetResetType.values())));
        offsetResetComboBox.valueProperty().bindBidirectional(config.offsetResetConfigProperty());
    }

    private void configureTopicConfigComboBox() {
        topicConfigComboBox.setItems(topicConfigs);
    }

    @FXML
    private void clearButtonOnAction() {
        getActiveListenersForConfig().ifPresent(listener -> {
            fixedRecordsLogger.clear();
            if (!startButton.isDisable()) {
                resetTotalReceivedLabeltext();
            }
        });
    }

    @FXML
    private void stopButtonOnAction() {
        getActiveListenersForConfig().ifPresent(Listener::stop);
        partitionAssignmentHandler.updatePartitionsAssignmentLabelFor(null);
    }

    @FXML
    private void startButtonOnAction() {
        getActiveListenersForConfig().ifPresent(Listener::start);
        resetTotalReceivedLabeltext();
    }

    private void resetTotalReceivedLabeltext() {
        totalReceivedMsgCounter = ZERO_RECEIVED_MSGS;
        updateReceivedMsgLabel();
    }

    @FXML
    private void detachButtonOnAction() {
    }


}

