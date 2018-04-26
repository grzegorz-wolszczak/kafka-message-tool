package application.customfxwidgets;

import application.constants.ApplicationConstants;
import application.kafka.dto.TopicAlterableProperties;
import application.utils.GuiUtils;
import application.utils.ValidatorUtils;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class AlterTopicDialog extends AnchorPane {

    private static final int MIN_RETENTION_MS = 1;
    private static final int MAX_RETENTION_MS = 604800000;
    private static final String FXML_FILE = "AlterTopicDialog.fxml";
    private final Stage stage = new Stage();
    @FXML
    private TextField topicNameField;
    @FXML
    private Spinner<Integer> retentionMillisecondsSpinner;


    private ButtonType returnButtonType = ButtonType.CLOSE;
    private TopicAlterableProperties topicAlterableProperties;

    public AlterTopicDialog(Window owner) throws IOException {
        CustomFxWidgetsLoader.load(this, FXML_FILE);
        stage.initOwner(owner);
    }

    public ButtonType call(TopicAlterableProperties topicAlterableProperties) {
        this.topicAlterableProperties = topicAlterableProperties;
        configureRetentionMilisecondsSpinner(topicAlterableProperties);
        topicNameField.setText(topicAlterableProperties.getTopicName());

        prepareStage();
        stage.showAndWait();

        return returnButtonType;
    }


    private void configureRetentionMilisecondsSpinner(TopicAlterableProperties topicToAdd) {

        final IntegerProperty referenceProperty = topicToAdd.retentionMillisecondsProperty();
        ValidatorUtils.configureSpinner(retentionMillisecondsSpinner, referenceProperty,
                                        MIN_RETENTION_MS,
                                        MAX_RETENTION_MS);

    }

    @FXML
    private void initialize() {
        GuiUtils.addApplicationIcon(stage);
    }

    @FXML
    private void cancelButtonOnAction() {
        closeThisDialogWithCancelStatus();
    }

    private void closeThisDialogWithCancelStatus() {
        returnButtonType = ButtonType.CANCEL;
        stage.close();
    }

    @FXML
    private void okButtonOnAction() {
        closeThisDialogWithOkStatus();
    }

    private void closeThisDialogWithOkStatus() {
        stage.close();
        returnButtonType = ButtonType.OK;
    }


    private void prepareStage() {
        final Scene scene = new Scene(this);
        scene.getStylesheets().add(getClass().getResource(ApplicationConstants.GLOBAL_CSS_FILE_NAME).toExternalForm());
        scene.setRoot(this);
        stage.setScene(scene);
        stage.setTitle(String.format("Altering topic '%s'", topicAlterableProperties.getTopicName()));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
    }
}
