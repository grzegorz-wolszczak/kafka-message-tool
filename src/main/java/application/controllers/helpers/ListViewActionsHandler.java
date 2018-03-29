package application.controllers.helpers;

import application.model.ModelConfigObject;
import application.utils.UserInteractor;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;

import java.util.Optional;

public class ListViewActionsHandler<AppModelObject extends ModelConfigObject> {

    private final ListView<AppModelObject> listView;
    private UserInteractor interactor;

    public ListViewActionsHandler(UserInteractor interactor,
                                  ListView<AppModelObject> listView) {
        this.interactor = interactor;
        this.listView = listView;
    }

    public Optional<AppModelObject> getSelectedModelObject() {
        final ObservableList<AppModelObject> selectedItems = getSelectedModelObjects();
        if (selectedItems.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(selectedItems.get(0));
        }
    }

    public void refresh() {
        listView.refresh();
    }

    public boolean deleteSelectedModelObject() {

        final ObservableList<AppModelObject> selectedModelObjects = getSelectedModelObjects();
        if (selectedModelObjects.isEmpty()) {
            return false;
        }

        if (selectedModelObjects.size() > 1) {
            interactor.showError("InternalError",
                                 String.format("Too many object selected ('%d')",
                                               selectedModelObjects.size()));
            return false;
        }
        final AppModelObject modelObject = selectedModelObjects.get(0);
        if (!didUserConfirmedDeletion(modelObject.getObjectTypeName(), modelObject.toString())) {
            return false;
        }

        return listView.getItems().remove(modelObject);
    }

    public void selectLastElement() {
        listView.getSelectionModel().selectLast();
    }

    private ObservableList<AppModelObject> getSelectedModelObjects() {
        final MultipleSelectionModel<AppModelObject> selectionModel = listView.getSelectionModel();
        return selectionModel.getSelectedItems();
    }

    private boolean didUserConfirmedDeletion(String objectTypeName, String objectName) {
        return interactor.getYesNoDecision("Confirmation",
                                           String.format("Deleting %s '%s'", objectTypeName, objectName),
                                           "Are you sure? ");
    }
}
