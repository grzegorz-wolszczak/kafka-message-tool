package application.utils;

import application.constants.ApplicationConstants;
import application.globals.AppGlobals;
import application.logging.Logger;
import application.model.ModelConfigObject;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

public class GuiUtils {
    private static final PseudoClass INVALID_DATA_PSEUDO_CLASS =
        PseudoClass.getPseudoClass(ApplicationConstants.INVALID_TEXT_FIELD_INPUT_PSEUDO_CLASS_NAME);
    private static final Runnable NULL_CALLBACK = () -> {
    };

    public static void configureTextFieldChangeStyleOnInvalidValue(TextField textField,
                                                                   Function<String, Boolean> validator) {
        textField.textProperty().addListener((observableValue, s, newValue) -> {
            textField.pseudoClassStateChanged(INVALID_DATA_PSEUDO_CLASS, true);
            if (validator.apply(newValue)) {
                textField.pseudoClassStateChanged(INVALID_DATA_PSEUDO_CLASS, false);
            }
        });
    }

    public static void configureTextFieldToAcceptOnlyValidData(TextField textField,
                                                               Consumer<String> func,
                                                               Function<String, Boolean> validator) {
        configureTextFieldToAcceptOnlyValidData(textField,
                                                func,
                                                validator,
                                                NULL_CALLBACK);
    }


    public static void configureTextFieldToAcceptOnlyValidData(TextField textField,
                                                               Consumer<String> func,
                                                               Function<String, Boolean> validator,
                                                               Runnable valueChangeCallback) {
        textField.textProperty().addListener((observableValue, s, newValue) -> {
            textField.pseudoClassStateChanged(INVALID_DATA_PSEUDO_CLASS, true);
            if (validator.apply(newValue)) {
                func.accept(newValue.trim());
                textField.pseudoClassStateChanged(INVALID_DATA_PSEUDO_CLASS, false);
                valueChangeCallback.run();
            }
        });
    }

    public static <T> void resetComboboxValue(ComboBox<T> comboBox, T value) {
        // if value (reference to object) does not change as combobox value
        // but the internals of reference  will change (eg. some field of object)
        // combobox will not refresh it , need to to clear and reset it manually

        if (comboBox.getValue() == value) {
            comboBox.setValue(null);
        }
        comboBox.setValue(value);
    }

    public static void expandNodeToAnchorPaneBorders(Node child) {
        expandNodeToAnchorPaneBorders(child, 0.0);
    }

    public static void expandNodeToAnchorPaneBorders(Node child, double margins) {
        AnchorPane.setTopAnchor(child, margins);
        AnchorPane.setBottomAnchor(child, margins);
        AnchorPane.setLeftAnchor(child, margins);
        AnchorPane.setRightAnchor(child, margins);
    }

    public static void setOnParentPane(AnchorPane parent, Node child) {
        final ObservableList<Node> children = parent.getChildren();
        children.clear();
        children.add(child);
    }

    public static void configureComboBoxToClearSelectedValueIfItsPreviousValueWasRemoved(ComboBox<? extends ModelConfigObject> comboBox) {
        final ObservableList<? extends ModelConfigObject> observables = comboBox.itemsProperty().get();
        comboBox.getSelectionModel().selectedItemProperty().addListener((ChangeListener<ModelConfigObject>) (observable, oldValue, newValue) -> {
            if (oldValue != null && !observables.contains(oldValue)) {
                comboBox.setValue(null);
            }
        });
    }

    public static void loadCssIfPossible(Scene scene, String fileName) {
        final URL cssFileUrl = GuiUtils.class.getClass().getResource(fileName);
        if (cssFileUrl == null) {
            Logger.error(String.format("Could not find and load CSS file %s ", fileName));
        } else {
            scene.getStylesheets().add(cssFileUrl.toExternalForm());
        }
    }

    public static void addApplicationIcon(Stage stage) {
        final Image applicationIcon = AppGlobals.getApplicationIcon();
        if (applicationIcon == null) {
            return;
        }
        stage.getIcons().add(AppGlobals.getApplicationIcon());
    }
}
