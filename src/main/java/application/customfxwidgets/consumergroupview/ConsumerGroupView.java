package application.customfxwidgets.consumergroupview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import application.kafka.cluster.KafkaClusterProxy;
import application.utils.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConsumerGroupView extends AnchorPane {
    private static final String FXML_FILE = "ConsumerGroupView.fxml";
    private final KafkaClusterProxy proxy;
    @FXML
    private TableView<ConsumerGroupName> consumerGroupNameTable;
    @FXML
    private TableColumn<ConsumerGroupName, String> consumerGroupNameColumn;
    @FXML
    private TableView<ConsumerGroupDetailRecord> consumerGroupPropertiesTable;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> topicNameColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> partitionColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> currentOffsetColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> logEndOffsetColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> consumerIdColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> hostColumn;

    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> lagColumn;
    @FXML
    private TableColumn<ConsumerGroupDetailRecord, String> clientIdColumn;
    public ConsumerGroupView(KafkaClusterProxy proxy) throws IOException {
        this.proxy = proxy;
        CustomFxWidgetsLoader.load(this, FXML_FILE);
        bindActionsToSelectedRow();
    }

    public void refresh() {
        final List<ConsumerGroupName> names = proxy.getConsumerGroupDetails().stream()
            .map(ConsumerGroupDetailRecord::getConsumerGroupId).distinct().map(ConsumerGroupName::new)
            .collect(Collectors.toList());
        consumerGroupNameTable.setItems(FXCollections.observableList(names));
    }

    @FXML
    private void initialize() {
        System.out.println("initialzed");
        initializeTableViews();
    }

    private void initializeTableViews() {
        initializeConsumerGroupInfoTableView();
        initializeConsumerGroupSummaryTableView();
    }

    private void initializeConsumerGroupSummaryTableView() {

        topicNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTopicName()));
        partitionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPartitionNum()));
        currentOffsetColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCurrentOffset()));
        logEndOffsetColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLogEndOffset()));
        consumerIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerId()));
        hostColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHost()));
        lagColumn.setCellValueFactory(param->new SimpleStringProperty(param.getValue().getLag()));
        clientIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getClientId()));


        TableUtils.installCopyPasteHandlerForSingleCell(consumerGroupPropertiesTable);
        TableUtils.autoResizeColumns(consumerGroupPropertiesTable);
    }

    private void initializeConsumerGroupInfoTableView() {
        consumerGroupNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        TableUtils.installCopyPasteHandlerForSingleCell(consumerGroupNameTable);
        TableUtils.autoResizeColumns(consumerGroupPropertiesTable);
    }

    private void bindActionsToSelectedRow() {

        consumerGroupNameTable.setRowFactory((TableView<ConsumerGroupName> tableView) -> {
            final TableRow<ConsumerGroupName> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    fillConsumerGroupDetailsViewForName(row.getItem().getName());
                }
            });
            return row;
        });
    }

    private static class ConsumerGroupName {
        private final String name;

        public ConsumerGroupName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    private void fillConsumerGroupDetailsViewForName(String consumerGroupId) {
        System.out.println("filtereing by consumer groupId: " + consumerGroupId);
        final List<ConsumerGroupDetailRecord> filteredByName = proxy.getConsumerGroupDetails().stream()
            .filter(e -> e.getConsumerGroupId().equals(consumerGroupId))
            .collect(Collectors.toList());
        System.out.println("setting filtered by name with size: " + filteredByName.size());
        consumerGroupPropertiesTable.setItems(FXCollections.observableArrayList(filteredByName));
    }

}
