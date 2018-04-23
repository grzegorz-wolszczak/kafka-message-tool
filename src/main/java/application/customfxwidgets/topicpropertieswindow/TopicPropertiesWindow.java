package application.customfxwidgets.topicpropertieswindow;

import application.constants.ApplicationConstants;
import application.customfxwidgets.ConfigEntriesView;
import application.utils.GuiUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static application.customfxwidgets.CustomFxWidgetsLoader.loadAnchorPane;


public final class TopicPropertiesWindow extends AnchorPane {
    private static final String FXML_FILE = "TopicPropertiesView.fxml";
    private Stage stage = new Stage();
    @FXML
    private Button closeButton;

    @FXML
    private Label titleLabel;

    @FXML
    private AnchorPane topicPropertiesAnchorPane;

    @FXML
    private AnchorPane consumerGroupsAnchorPane;

    private static TopicPropertiesWindow instance;

    private double stageWidth = -1d;
    private double stageHeight = -1d;


    public static TopicPropertiesWindow get(String topicName, ConfigEntriesView entriesView) throws IOException {
        if(instance== null)
        {
            instance = new TopicPropertiesWindow();
        }
        instance.setup(topicName, entriesView);
        return instance;
    }

    private void setup(String topicName, ConfigEntriesView entriesView) {
        setupTitleLabel(topicName);
        setupTopicPropertiesAnchorPane(entriesView);
        restoreWidhtAndHeight();
    }

    private void restoreWidhtAndHeight() {
        if (stageWidth > 0)
        {
            stage.setWidth(stageWidth);
        }

        if(stageHeight>0)
        {
            stage.setHeight(stageHeight);
        }
    }

    private TopicPropertiesWindow() throws IOException {

        loadAnchorPane(this, FXML_FILE);
    }

    public void show() {
        stage.showAndWait();
    }


    @FXML
    private void initialize() {
        initializeStage();
    }

    private void setupTopicPropertiesAnchorPane(ConfigEntriesView entriesView) {
        topicPropertiesAnchorPane.getChildren().clear();
        topicPropertiesAnchorPane.getChildren().add(entriesView);
    }

    private void setupTitleLabel(String topicName) {
        titleLabel.setText("Information for topic: " + topicName);
        stage.setTitle(titleLabel.getText());
    }

    @FXML
    private void closeOnAction() {
        stage.close();
    }


    private void initializeStage() {
        final Scene scene = new Scene(this);
        scene.getStylesheets().add(getClass().getResource(ApplicationConstants.GLOBAL_CSS_FILE_NAME).toExternalForm());
        scene.setRoot(this);
        stage.setScene(scene);

//        stage.setResizable(false);
        stage.centerOnScreen();
//        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);

        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                stageWidth = newValue.doubleValue();
            }
        });

        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                stageHeight = newValue.doubleValue();
            }
        });
    }


}
