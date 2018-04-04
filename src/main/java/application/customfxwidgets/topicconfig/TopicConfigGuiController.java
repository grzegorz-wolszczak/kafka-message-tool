package application.customfxwidgets.topicconfig;

import application.customfxwidgets.CustomFxWidgetsLoader;
import application.customfxwidgets.Displayable;
import application.customfxwidgets.TopicConfigComboBoxConfigurator;
import application.displaybehaviour.DetachableDisplayBehaviour;
import application.displaybehaviour.DisplayBehaviour;
import application.displaybehaviour.ModelConfigObjectsGuiInformer;
import application.kafka.ClusterStatusChecker;
import application.kafka.KafkaClusterProxies;
import application.kafka.KafkaClusterProxy;
import application.kafka.TriStateConfigEntryValue;
import application.logging.Logger;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.utils.GuiUtils;
import application.utils.TooltipCreator;
import application.utils.ValidatorUtils;
import application.utils.kafka.KafkaBrokerHostInfo;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class TopicConfigGuiController extends AnchorPane implements Displayable {
    private static final String FXML_FILE = "TopicConfigView.fxml";
    private static final PseudoClass OK_TOPIC_EXISTS_PSEUDO_CLASS = PseudoClass.getPseudoClass("ok_topic_exists");
    private static final PseudoClass TOPIC_WILL_BE_AUTOCREATED_PSEUDO_CLASS = PseudoClass.getPseudoClass("warn_topic_will_be_autocreated");
    private static final PseudoClass CANNOT_USE_TOPIC_PSEUDO_CLASS = PseudoClass.getPseudoClass("error_cannot_use_topic");
    private final KafkaTopicConfig config;
    private final DisplayBehaviour displayBehaviour;
    private final ClusterStatusChecker statusChecker;
    private KafkaClusterProxies kafkaClusterProxies;
    private final TopicConfigComboBoxConfigurator<KafkaBrokerConfig> comboBoxConfigurator;

    @FXML
    private ComboBox<KafkaBrokerConfig> kafkaBrokerComboBox;

    @FXML
    private TextField topicConfigNameField;

    @FXML
    private TextField topicNameField;

    @FXML
    private Button connectionCheckButton;

    @FXML
    private ToggleButton detachPaneButton;

    private Runnable rerfeshCallback;

    public TopicConfigGuiController(KafkaTopicConfig config,
                                    AnchorPane parentPane,
                                    ModelConfigObjectsGuiInformer guiInformer,
                                    Runnable refreshCallback,
                                    ObservableList<KafkaBrokerConfig> brokerConfigs,
                                    ClusterStatusChecker statusChecker,
                                    KafkaClusterProxies kafkaClusterProxies) throws IOException {
        this.statusChecker = statusChecker;
        this.kafkaClusterProxies = kafkaClusterProxies;

        CustomFxWidgetsLoader.loadAnchorPane(this, FXML_FILE);

        this.rerfeshCallback = refreshCallback;
        this.config = config;

        StringExpression windowTitle = composeBrokerConfigWindowTitle();
        displayBehaviour = new DetachableDisplayBehaviour(parentPane,
                                                          windowTitle,
                                                          this,
                                                          detachPaneButton.selectedProperty(),
                                                          config,
                                                          guiInformer);

        configureComboBox(brokerConfigs);
        GuiUtils.configureComboBoxToClearSelectedValueIfItsPreviousValueWasRemoved(kafkaBrokerComboBox);
        comboBoxConfigurator = new TopicConfigComboBoxConfigurator<>(kafkaBrokerComboBox, config);
        comboBoxConfigurator.configure();
        configureTopicConfigNameField();
        configureTopicNameField();
        configureConnectionCheckButton();

        setCallbackToUpdateTopicNameAppearanceWhenTopicNameChanges();
        setCallbackForBrokerConfigChanges();
        resetBrokerConfigToFireUpAllCallbacksForTheFirstTimeToSetupControls();

    }

    @Override
    public void display() {
        displayBehaviour.display();
    }

    private void configureComboBox(ObservableList<KafkaBrokerConfig> brokerConfigs) {
        kafkaBrokerComboBox.setItems(brokerConfigs);
    }


    private void configureConnectionCheckButton() {
        connectionCheckButton.disableProperty().bind(Bindings.isNull(config.relatedConfigProperty()));
    }

    private StringExpression composeBrokerConfigWindowTitle() {
        return new ReadOnlyStringWrapper("Topic configuration")
            .concat(" '").concat(config.nameProperty()).concat("' (")
            .concat("topic:")
            .concat(config.topicNameProperty())
            .concat(")");
    }

    private void setCallbackToUpdateTopicNameAppearanceWhenTopicNameChanges() {
        topicNameField.textProperty().addListener((observable, oldValue, newValue) ->
                                                      updateTopicNameTextFieldAppearance(config.getRelatedConfig()));
    }

    private void configureTopicNameField() {
        topicNameField.setText(config.getTopicName());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(topicNameField,
                                                         config::setTopicName,
                                                         ValidatorUtils::isStringIdentifierValid,
                                                         rerfeshCallback);
    }

    private void resetBrokerConfigToFireUpAllCallbacksForTheFirstTimeToSetupControls() {
        final KafkaBrokerConfig c = config.getRelatedConfig();
        config.setRelatedConfig(null);
        config.setRelatedConfig(c);
    }

    private void setCallbackForBrokerConfigChanges() {

        config.relatedConfigProperty().addListener((ObservableValue<? extends KafkaBrokerConfig> observable,
                                                    KafkaBrokerConfig oldValue,
                                                    KafkaBrokerConfig newValue) -> {

            updateTopicNameTextFieldAppearance(newValue);
            setCallbackForChangingTopicNameAppearanceWhenBrokerConfigInternalPropertiesChanges(oldValue, newValue);
            setCallbackForUpdatingTopicNameTextFieldBackgroundIfCurrentClusterSummaryChanges(oldValue, newValue);
        });
    }

    private void stringPropertyCallbackForUpdatingTextFieldAppearance(ObservableValue<? extends String> ignored1,
                                                                      String ignored2,
                                                                      String ignored3) {
        updateTopicNameTextFieldAppearance(config.getRelatedConfig());
    }

    private void setCallbackForChangingTopicNameAppearanceWhenBrokerConfigInternalPropertiesChanges(KafkaBrokerConfig oldValue,
                                                                                                    KafkaBrokerConfig newValue) {

        if (oldValue != null) {
            oldValue.hostNameProperty().removeListener(this::stringPropertyCallbackForUpdatingTextFieldAppearance);
            oldValue.portProperty().removeListener(this::stringPropertyCallbackForUpdatingTextFieldAppearance);
        }
        if (newValue != null) {
            newValue.hostNameProperty().addListener(this::stringPropertyCallbackForUpdatingTextFieldAppearance);
            newValue.portProperty().addListener(this::stringPropertyCallbackForUpdatingTextFieldAppearance);
        }
    }

    private void updateTopicNameTextFieldAppearance(KafkaBrokerConfig brokerConfig) {
        if (brokerConfig == null) {
            setTopicNameTextFieldStylePropertiesBasedOnClusterConfig(null);
            return;
        }
        final KafkaBrokerHostInfo hostInfo = brokerConfig.getHostInfo();
        final KafkaClusterProxy proxy = kafkaClusterProxies.get(hostInfo);
        setTopicNameTextFieldStylePropertiesBasedOnClusterConfig(proxy);
    }

    private void setCallbackForUpdatingTopicNameTextFieldBackgroundIfCurrentClusterSummaryChanges(KafkaBrokerConfig oldValue,
                                                                                                  KafkaBrokerConfig newValue) {
        if (oldValue != null) {
            final ObjectProperty<KafkaClusterProxy> oldProxy = kafkaClusterProxies.getAsProperty(oldValue.getHostInfo());
            Logger.trace(String.format("removing listener for cluster proxy property for %s", oldValue.getHostInfo()));
            oldProxy.removeListener(this::udateTopicNameTextFieldAppearanceCallback);
        }

        if (newValue == null) {
            Logger.trace("not adding new listener for proxy property because new broker config is null");
            return;
        }
        final ObjectProperty<KafkaClusterProxy> newProxy = kafkaClusterProxies.getAsProperty(newValue.getHostInfo());
        Logger.trace(String.format("adding listener for cluster proxy property for %s", newValue.getHostInfo()));
        newProxy.addListener(this::udateTopicNameTextFieldAppearanceCallback);
    }


    private void udateTopicNameTextFieldAppearanceCallback(ObservableValue<? extends KafkaClusterProxy> observable,
                                                           KafkaClusterProxy oldValue,
                                                           KafkaClusterProxy newValue) {
        Logger.trace(String.format("setting new topic name appearance for clusterProxy change listener: old %s, new %s",
                                   oldValue, newValue));

        setTopicNameTextFieldStylePropertiesBasedOnClusterConfig(newValue);
    }


    private void configureTopicConfigNameField() {
        topicConfigNameField.setText(config.getName());
        GuiUtils.configureTextFieldToAcceptOnlyValidData(topicConfigNameField,
                                                         config::setName,
                                                         ValidatorUtils::isStringIdentifierValid,
                                                         rerfeshCallback);
    }

    @FXML
    private void checkButtonOnAction() {
        statusChecker.updateStatus(config.getRelatedConfig().getHostInfo(), true);
    }


    private void setTopicNameTextFieldStylePropertiesBasedOnClusterConfig(KafkaClusterProxy proxy) {

        resetTopicNameFieldTooltipAndCssState();
        final String topicName = topicNameField.getText();
        if (StringUtils.isBlank(topicName)) {
            return;
        }

        if (proxy == null) {
            return;
        }

        String toolTipMessage;
        PseudoClass psuedoClass;

        final boolean topicExists = proxy.hasTopic(topicName);
        if (topicExists) {
            psuedoClass = OK_TOPIC_EXISTS_PSEUDO_CLASS;
            toolTipMessage = String.format("Topic '%s' exists.", topicName);
        } else {

            if (proxy.isTopicAutoCreationEnabled() != TriStateConfigEntryValue.False) {
                psuedoClass = TOPIC_WILL_BE_AUTOCREATED_PSEUDO_CLASS;
                toolTipMessage = String.format("Topic '%s' does not exist yet " +
                                                   "but will be auto-created by first connected consumer/publisher.",
                                               topicName);
            } else {
                psuedoClass = CANNOT_USE_TOPIC_PSEUDO_CLASS;
                toolTipMessage = String.format("Topic '%s' does not exists. Must be created manually.", topicName);
            }
        }

        topicNameField.pseudoClassStateChanged(psuedoClass, true);
        topicNameField.setTooltip(TooltipCreator.createFrom(toolTipMessage));
    }

    private void resetTopicNameFieldTooltipAndCssState() {
        resetTopicnameTextFieldTooltip();
        resetTopicNameTextFieldBackgroundCss();
    }

    private void resetTopicNameTextFieldBackgroundCss() {
        topicNameField.pseudoClassStateChanged(OK_TOPIC_EXISTS_PSEUDO_CLASS, false);
        topicNameField.pseudoClassStateChanged(TOPIC_WILL_BE_AUTOCREATED_PSEUDO_CLASS, false);
        topicNameField.pseudoClassStateChanged(CANNOT_USE_TOPIC_PSEUDO_CLASS, false);
    }

    private void resetTopicnameTextFieldTooltip() {
        final String topicName = topicNameField.getText();
        if (StringUtils.isBlank(topicName)) {
            topicNameField.setTooltip(null);
            return;
        }
        topicNameField.setTooltip(TooltipCreator.createFrom(String.format("Topic '%s' existence undetermined.", topicName)));
    }

}
