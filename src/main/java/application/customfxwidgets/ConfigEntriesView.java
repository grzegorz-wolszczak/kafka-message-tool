package application.customfxwidgets;

import application.utils.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import org.apache.kafka.clients.admin.ConfigEntry;

import java.io.IOException;
import java.util.Set;


public class ConfigEntriesView extends TitledPane {

    private static final String FXML_FILE = "ConfigEntriesView.fxml";
    private final ObservableList<ConfigEntry> observableEntries = FXCollections.observableArrayList();
    private final String title;
    @FXML
    private TableColumn<ConfigEntry, String> nameColumn;
    @FXML
    private TableColumn<ConfigEntry, String> valueColumn;
    @FXML
    private TableView<ConfigEntry> configEntriesTableView;

    private ConfigEntriesViewPreferences columnWidths;


    public ConfigEntriesView(String title,
                             Set<ConfigEntry> entries) throws IOException {
        this(title, entries, new ConfigEntriesViewPreferences());
    }

    public ConfigEntriesView(String title,
                             Set<ConfigEntry> entries,
                             ConfigEntriesViewPreferences columnWidths) throws IOException {
        this.title = title;
        observableEntries.setAll(entries);
        this.columnWidths = columnWidths;

        CustomFxWidgetsLoader.loadOnAnchorPane(this, FXML_FILE);
    }

    @FXML
    private void initialize() {
        setText(title);
        setCollapsible(false);
        configureColumns();
        setTableContent();
    }

    private void setTableContent() {
        configEntriesTableView.setItems(observableEntries);
        configEntriesTableView.getSortOrder().add(nameColumn);
        TableUtils.installCopyPasteHandlerForSingleCell(configEntriesTableView);
        TableUtils.autoResizeColumns(configEntriesTableView);
    }

    private void configureColumns() {
        configureNameColumn();
        configureValueColumn();
    }

    private void configureValueColumn() {
        valueColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().value()));
        valueColumn.setResizable(true);

        if (columnWidths.valueColumnWidth != ConfigEntriesViewPreferences.INVALID_COLUMN_WIDTH) {
            valueColumn.setPrefWidth(columnWidths.nameColumnWidth);
        }
        valueColumn.widthProperty().addListener((observable, oldValue, newValue) -> columnWidths.valueColumnWidth = newValue.doubleValue());
    }

    private void configureNameColumn() {
        nameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().name()));
        nameColumn.setResizable(true);

        if (columnWidths.nameColumnWidth != ConfigEntriesViewPreferences.INVALID_COLUMN_WIDTH) {
            nameColumn.setPrefWidth(columnWidths.nameColumnWidth);
        }
        nameColumn.widthProperty().addListener((observable, oldValue, newValue) -> columnWidths.nameColumnWidth = newValue.doubleValue());
    }

}
