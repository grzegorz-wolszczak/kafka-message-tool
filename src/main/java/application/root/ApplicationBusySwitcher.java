package application.root;

import javafx.scene.Node;

public interface ApplicationBusySwitcher {
    void setAppBusy(boolean value);

    void registerNodeForDisabledStageManagement(Node scene);
}
