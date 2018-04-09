package application.customfxwidgets.listenerconfig;

import application.customfxwidgets.CustomFxWidgetsLoader;
import application.customfxwidgets.Displayable;
import application.customfxwidgets.TopicConfigComboBoxConfigurator;
import application.displaybehaviour.DetachableDisplayBehaviour;
import application.displaybehaviour.DisplayBehaviour;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.listener.Listener;
import application.kafka.listener.Listeners;
import application.model.KafkaOffsetResetType;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.GuiUtils;
import application.utils.ValidatorUtils;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.Thread.sleep;

public class ListenerConfigGuiController extends AnchorPane implements Displayable {
    private static final String FXML_FILE = "ListenerConfigView.fxml";
    private final DisplayBehaviour displayBehaviour;
    private final TopicConfigComboBoxConfigurator comboBoxConfigurator;
    @FXML
    private TextField listenerNameTextField;
    @FXML
    private ComboBox<KafkaTopicConfig> topicConfigComboBox;
    @FXML
    private TextArea outputTextArea;
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
    private KafkaListenerConfig config;
    private Listeners activeConsumers;
    private Runnable refreshCallback;
    private ObservableList<KafkaTopicConfig> topicConfigs;


    public ListenerConfigGuiController(KafkaListenerConfig config,
                                       AnchorPane parentPane,
                                       ModelConfigObjectsGuiInformer guiInformer,
                                       Listeners activeConsumers,
                                       Runnable refreshCallback,
                                       ObservableList<KafkaTopicConfig> topicConfigs) throws IOException {

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
        resetKafkaListenerBinding();
        configureGuiControlDisableStateBasedOnStartButtonState();
        GuiUtils.configureComboBoxToClearSelectedValueIfItsPreviousValueWasRemoved(topicConfigComboBox);

        comboBoxConfigurator = new TopicConfigComboBoxConfigurator<>(topicConfigComboBox,
                config);
        comboBoxConfigurator.configure();
    }

    @Override
    public void display() {
        displayBehaviour.display();
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

    private void resetKafkaListenerBinding() {
        getActiveListenersForConfig().ifPresent(listener -> {
            listener.loggedTextProperty().addListener((observableValue, s, t1) -> appendTextScrolledToBottom(t1));

            startButton.disableProperty().bind(listener.isRunningProperty());
            stopButton.disableProperty().bind(listener.isRunningProperty().not());
        });
    }

    private void appendTextScrolledToBottom(String textToAppend) {
        try {
            // Slow down calls to appendText
            sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            outputTextArea.appendText(textToAppend);
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
            outputTextArea.clear();
        });
    }

    @FXML
    private void stopButtonOnAction() {
        getActiveListenersForConfig().ifPresent(Listener::stop);
    }

    @FXML
    private void startButtonOnAction() {
        getActiveListenersForConfig().ifPresent(Listener::start);

    }

    @FXML
    private void detachButtonOnAction() {

    }




}

