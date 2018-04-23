package application.customfxwidgets.topicpropertieswindow;

import application.constants.ApplicationConstants;
import application.customfxwidgets.ConfigEntriesView;
import application.kafka.cluster.TopicsOffsetInfo;
import application.utils.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static application.customfxwidgets.CustomFxWidgetsLoader.loadAnchorPane;


public final class TopicPropertiesWindow extends AnchorPane {
    private static final String FXML_FILE = "TopicPropertiesView.fxml";
    private static TopicPropertiesWindow instance;
    private Stage stage = new Stage();
    @FXML
    private Button closeButton;
    @FXML
    private Label titleLabel;
    @FXML
    private AnchorPane topicPropertiesAnchorPane;
    @FXML
    private AnchorPane consumerGroupsAnchorPane;
    @FXML
    private TableColumn<TopicsOffsetInfo, String> consumerGroupColumn;

    @FXML
    private TableColumn<TopicsOffsetInfo, String> partitionColumn;

    @FXML
    private TableColumn<TopicsOffsetInfo, String> beginOffsetColumn;

    @FXML
    private TableColumn<TopicsOffsetInfo, String> endOffsetColumn;

    @FXML
    private TableView<TopicsOffsetInfo> topicOffsetsTableView;

    private double stageWidth = -1d;
    private double stageHeight = -1d;

    private TopicPropertiesWindow() throws IOException {

        loadAnchorPane(this, FXML_FILE);
    }

    public static TopicPropertiesWindow get(String topicName,
                                            ConfigEntriesView entriesView,
                                            List<TopicsOffsetInfo> topicOffsetsInfo) throws IOException {
        if (instance == null) {
            instance = new TopicPropertiesWindow();
        }
        instance.setup(topicName, entriesView, topicOffsetsInfo);
        return instance;
    }

    private void setup(String topicName,
                       ConfigEntriesView entriesView,
                       List<TopicsOffsetInfo> topicOffsetsInfo) {

        final List<TopicsOffsetInfo> filtered = topicOffsetsInfo
                .stream()
                .filter(e -> e.getTopicName().equals(topicName))
                .collect(Collectors.toList());


        setupTitleLabel(topicName);
        setupTopicPropertiesAnchorPane(entriesView);
        setupTopicOffsetsInfo(filtered);
        restoreWidthAndHeight();
    }

    private void setupTopicOffsetsInfo(List<TopicsOffsetInfo> topicOffsetsInfo) {
        setTableContent(topicOffsetsInfo);
        configureColumns(topicOffsetsInfo);
    }

    private void configureColumns(List<TopicsOffsetInfo> topicOffsetsInfo) {
        consumerGroupColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerGroup()));
        partitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPartition()));
        beginOffsetColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBeginOffset()));
        endOffsetColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEndOffset()));
    }

    private void setTableContent(List<TopicsOffsetInfo> topicOffsetsInfo) {
        topicOffsetsTableView.setItems(FXCollections.observableArrayList(topicOffsetsInfo));
        topicOffsetsTableView.getSortOrder().add(consumerGroupColumn);
        TableUtils.installCopyPasteHandlerForSingleCell(topicOffsetsTableView);
    }

    private void restoreWidthAndHeight() {
        if (stageWidth > 0) {
            stage.setWidth(stageWidth);
        }

        if (stageHeight > 0) {
            stage.setHeight(stageHeight);
        }
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
        stage.centerOnScreen();
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


    /*


    private void configureColumns() {
        configureNameColumn();
        configureValueColumn();
    }

    private void configureValueColumn() {
        valueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().value()));
        valueColumn.setResizable(true);

        if (columnWidths.valueColumnWidth != ConfigEntriesViewPreferences.INVALID_COLUMN_WIDHT) {
            valueColumn.setPrefWidth(columnWidths.nameColumnWidth);
        }
        valueColumn.widthProperty().addListener((observable, oldValue, newValue) -> columnWidths.valueColumnWidth = newValue.doubleValue());
    }

    private void configureNameColumn() {
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().name()));
        nameColumn.setResizable(true);

        if (columnWidths.nameColumnWidth != ConfigEntriesViewPreferences.INVALID_COLUMN_WIDHT) {
            nameColumn.setPrefWidth(columnWidths.nameColumnWidth);
        }
        nameColumn.widthProperty().addListener((observable, oldValue, newValue) -> columnWidths.nameColumnWidth = newValue.doubleValue());
    }
     */
}
