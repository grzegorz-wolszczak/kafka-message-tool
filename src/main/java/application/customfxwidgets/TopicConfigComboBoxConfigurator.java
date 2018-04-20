package application.customfxwidgets;

import application.model.ModelConfigObject;
import application.model.RelatedConfigHolder;
import application.model.modelobjects.ToolTipInfoProvider;
import application.utils.GuiUtils;
import application.utils.TooltipCreator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class TopicConfigComboBoxConfigurator<RelatedConfig extends ModelConfigObject & ToolTipInfoProvider> {

    private final RelatedConfigHolder<RelatedConfig> config;
    private ComboBox<RelatedConfig> comboBox;
    private ObjectProperty<RelatedConfig> relatedConfigProperty;
    private ComboBoxUpdateMode comboBoxUpdateMode = ComboBoxUpdateMode.Normal;

    public TopicConfigComboBoxConfigurator(ComboBox<RelatedConfig> comboBox,
                                           RelatedConfigHolder<RelatedConfig> config) {
        this.comboBox = comboBox;
        this.relatedConfigProperty = config.relatedConfigProperty();
        this.config = config;
    }

    public void configure() {
        relatedConfigProperty.addListener(this::topicPropertyChangeCallback);
        setCallbackForComboBoxSelectionChanges();
        setInitialComboBoxValue();
        resetTopicConfigToFireUpAllCallbacksForTheFirstTimeToSetupControls();
    }

    private void setCallbackForComboBoxSelectionChanges() {

        comboBox.valueProperty().addListener(this::comboBoxSelectionChangedCallback);
    }

    private void updateComboBoxTooltip() {
        final RelatedConfig relatedConfig = relatedConfigProperty.get();
        if (null == relatedConfig) {
            return;
        }
        comboBox.setTooltip(TooltipCreator.createFrom(relatedConfig));
    }

    private void setCallbackToUpdateComboBoxValueAndToolTip(RelatedConfig oldValue,
                                                            RelatedConfig newValue) {
        if (oldValue != null) {
            oldValue.toolTipInfoProperty().removeListener(this::updateComboBoxToolTipCallback);
            oldValue.nameProperty().removeListener(this::updateComboBoxValueWithItemsCallback);

        }
        if (newValue != null) {
            newValue.toolTipInfoProperty().addListener(this::updateComboBoxToolTipCallback);
            newValue.nameProperty().addListener(this::updateComboBoxValueWithItemsCallback);
        }
    }

    private void comboBoxSelectionChangedCallback(ObservableValue<? extends RelatedConfig> ignored1,
                                                  RelatedConfig oldValue,
                                                  RelatedConfig newValue) {
        if (comboBoxUpdateMode == ComboBoxUpdateMode.Normal) {
            config.setRelatedConfig(newValue);
        }
    }

    private void topicPropertyChangeCallback(ObservableValue<? extends RelatedConfig> observableValue,
                                             RelatedConfig oldValue,
                                             RelatedConfig newValue) {
        updateComboBoxTooltip();
        setCallbackToUpdateComboBoxValueAndToolTip(oldValue, newValue);
    }

    private void updateComboBoxToolTipCallback(ObservableValue<? extends String> observable,
                                               String oldValue,
                                               String newValue) {
        updateComboBoxTooltip();
    }

    private void updateComboBoxValueWithItemsCallback(ObservableValue<? extends String> ignored1,
                                                      String oldValue,
                                                      String newValue) {
        final RelatedConfig value = config.getRelatedConfig();
        final ObservableList<RelatedConfig> originalItems = comboBox.getItems();
        try {
            comboBoxUpdateMode = ComboBoxUpdateMode.ValueReset;
            comboBox.setItems(FXCollections.observableArrayList());
            GuiUtils.resetComboboxValue(comboBox, value);
            comboBox.setItems(originalItems);
        } finally {
            comboBoxUpdateMode = ComboBoxUpdateMode.Normal;
        }

    }

    private void resetTopicConfigToFireUpAllCallbacksForTheFirstTimeToSetupControls() {
        final RelatedConfig c = config.getRelatedConfig();
        config.setRelatedConfig(null);
        config.setRelatedConfig(c);
    }

    private void setInitialComboBoxValue() {
        comboBox.getSelectionModel().select(config.getRelatedConfig());
    }

    private enum ComboBoxUpdateMode {
        Normal,
        ValueReset
    }
}
