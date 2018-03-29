package application.displaybehaviour;

import application.model.ModelConfigObject;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaListenerConfig;
import application.model.modelobjects.KafkaSenderConfig;
import application.model.modelobjects.KafkaTopicConfig;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.List;

public class DefaultModelConfigObjectsGuiInformer implements ModelConfigObjectsGuiInformer {
    private final TabPane tabPane;
    private final ListView<KafkaBrokerConfig> brokersListView;
    private final ListView<KafkaTopicConfig> topicsListView;
    private final ListView<KafkaSenderConfig> messagesListView;
    private final ListView<KafkaListenerConfig> listenersListView;
    private final ObjectProperty<ModelConfigObject> lastRemovedObject = new SimpleObjectProperty<>();
    private final ListChangeListener<ModelConfigObject> listChangeListener = getListChangeListener();

    public DefaultModelConfigObjectsGuiInformer(TabPane tabPane,
                                                ListView<KafkaBrokerConfig> brokersListView,
                                                ListView<KafkaTopicConfig> topicsListView,
                                                ListView<KafkaSenderConfig> messagesListView,
                                                ListView<KafkaListenerConfig> listenersListView) {
        this.tabPane = tabPane;
        this.brokersListView = brokersListView;
        this.topicsListView = topicsListView;
        this.messagesListView = messagesListView;
        this.listenersListView = listenersListView;

        bindToRemovedEventsListeners();
    }

    @Override
    public ObjectProperty<ModelConfigObject> lastRemovedObjectProperty() {
        return lastRemovedObject;
    }

    @Override
    public ModelConfigObject selectedObject() {

        final Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        final ListView<?> listView = (ListView<?>) selectedTab.getContent();

        if (listView == brokersListView) {
            return brokersListView.getSelectionModel().getSelectedItem();
        } else if (listView == topicsListView) {
            return topicsListView.getSelectionModel().getSelectedItem();
        } else if (listView == messagesListView) {
            return messagesListView.getSelectionModel().getSelectedItem();
        } else if (listView == listenersListView) {
            return listenersListView.getSelectionModel().getSelectedItem();
        }

        return null;
    }

    private ListChangeListener<ModelConfigObject> getListChangeListener() {
        return c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    final List<? extends ModelConfigObject> removed = c.getRemoved();
                    removed.forEach(lastRemovedObject::setValue);
                }
            }
        };
    }

    private void bindToRemovedEventsListeners() {

        brokersListView.getItems().addListener(listChangeListener);
        topicsListView.getItems().addListener(listChangeListener);
        messagesListView.getItems().addListener(listChangeListener);
        listenersListView.getItems().addListener(listChangeListener);

    }

}
