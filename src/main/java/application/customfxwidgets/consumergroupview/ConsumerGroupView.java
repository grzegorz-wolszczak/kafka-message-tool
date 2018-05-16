package application.customfxwidgets.consumergroupview;

import application.kafka.cluster.AggregatedConsumerGroupSummary;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Set;

import static application.customfxwidgets.CustomFxWidgetsLoader.loadAnchorPane;

public class ConsumerGroupView extends AnchorPane {
    private static final String FXML_FILE = "ConsumerGroupsView.fxml";
    private static ConsumerGroupView instnace;

    @FXML
    private TableView<AggregatedConsumerGroupSummary> consumerGroupNamesTableView;

    @FXML
    private TableColumn<AggregatedConsumerGroupSummary, String > consumerGroupIdTableColumn;

    @FXML
    private TableView<AggregatedConsumerGroupSummary> consumerGroupDetailsTableView;

    private ConsumerGroupView() throws IOException {
        loadAnchorPane(this, FXML_FILE);
        configureTableViews();
    }

    private void configureTableViews() {
        configureConsumerGroupNamesTableView();
    }

    private void configureConsumerGroupNamesTableView() {
        consumerGroupIdTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getConsumerGroupId()));
    }

    public static ConsumerGroupView get(Set<AggregatedConsumerGroupSummary> aggregatedConsumerGroupsSummary) throws IOException {
        if (instnace == null) {
            instnace = new ConsumerGroupView();
        }
        instnace.refresh(aggregatedConsumerGroupsSummary);
        return instnace;
    }

    private void refresh(Set<AggregatedConsumerGroupSummary> aggregatedConsumerGroupsSummary) {

        refreshConsumerGroupNamesTable(aggregatedConsumerGroupsSummary);
    }

    private void refreshConsumerGroupNamesTable(Set<AggregatedConsumerGroupSummary> aggregatedConsumerGroupsSummary) {
        consumerGroupNamesTableView.setItems(FXCollections.observableArrayList(aggregatedConsumerGroupsSummary));
    }
}
