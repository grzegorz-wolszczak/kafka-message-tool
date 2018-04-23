package application.newview.guiwrappers;

import org.controlsfx.control.MasterDetailPane;

public class MasterDetailPaneWrapper implements FXWrapper<MasterDetailPane> {
    private MasterDetailPane backingObject;

    public MasterDetailPaneWrapper(MasterDetailPane backingObject) {
        this.backingObject = backingObject;
    }

    public void setDetailNode(PaneWrapper messagesPane) {
        if (messagesPane == null) {
            backingObject.setShowDetailNode(false);
            //backingObject.setDetailNode(null);
            return;
        }
        backingObject.setShowDetailNode(true);
        backingObject.setDetailNode(messagesPane.getWrappedObject());
    }

    @Override
    public MasterDetailPane getWrappedObject() {
        return backingObject;
    }
}
