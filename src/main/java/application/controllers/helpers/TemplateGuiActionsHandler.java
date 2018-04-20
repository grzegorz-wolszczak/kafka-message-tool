package application.controllers.helpers;

import application.logging.Logger;
import application.model.ModelConfigObject;

import java.io.IOException;
import java.util.Optional;

abstract public class TemplateGuiActionsHandler<ModelObject extends ModelConfigObject> implements ModelObjectGuiActionsHandler {

    private final TabPaneSelectionInformer tabSelectionInformer;
    private final ListViewActionsHandler<ModelObject> listViewActionsHandler;

    public TemplateGuiActionsHandler(TabPaneSelectionInformer tabSelectionInformer,
                                     ListViewActionsHandler<ModelObject> listViewActionsHandler) {

        this.tabSelectionInformer = tabSelectionInformer;
        this.listViewActionsHandler = listViewActionsHandler;
    }

    protected abstract void loadController(ModelObject config) throws IOException;

    protected abstract void addToModelData();

    @Override
    public void addNewConfig() {
        if (!tabSelectionInformer.isMyTabSelected()) {
            return;
        }
        addToModelData();

        if (tabSelectionInformer.isMyTabSelected()) {
            listViewActionsHandler.selectLastElement();
        }
        resetRightContentPane();

    }

    @Override
    public void deleteSelectedConfig() {
        if (!tabSelectionInformer.isMyTabSelected()) {
            return;
        }
        if (listViewActionsHandler.deleteSelectedModelObject()) {
            resetRightContentPane();
        }
    }

    @Override
    public void resetRightContentPaneToSelectedConfig() {
        if (!tabSelectionInformer.isMyTabSelected()) {
            return;
        }
        resetRightContentPane();
    }


    private void resetRightContentPane() {
        getSelectedObject().ifPresent(this::setRightContentPaneFromConfig);
    }

    private void setRightContentPaneFromConfig(ModelObject config) {
        try {
            loadController(config);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Could not loadOnAnchorPane config pane", e);

        }
    }

    private Optional<ModelObject> getSelectedObject() {
        return listViewActionsHandler.getSelectedModelObject();
    }


}
