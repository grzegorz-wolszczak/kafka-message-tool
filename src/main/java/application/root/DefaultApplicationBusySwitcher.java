package application.root;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class DefaultApplicationBusySwitcher implements ApplicationBusySwitcher {

    private Stage stage;
    private final Set<Node> nodes = new HashSet<>();

    public DefaultApplicationBusySwitcher(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setAppBusy(boolean value) {
        if (value) {
            stage.getScene().setCursor(Cursor.WAIT);
            setControlsDisabledState(true);

        } else {
            stage.getScene().setCursor(Cursor.DEFAULT);
            setControlsDisabledState(false);
        }
    }

    @Override
    public void registerNodeForDisabledStageManagement(Node node) {
        nodes.add(node);
    }

    private void setControlsDisabledState(boolean value) {
        nodes.forEach(n->n.setDisable(value));
    }
}
