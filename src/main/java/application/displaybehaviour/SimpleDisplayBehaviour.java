package application.displaybehaviour;

import application.utils.GuiUtils;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class SimpleDisplayBehaviour implements DisplayBehaviour {
    private final AnchorPane parentPane;
    private final Node childNode;

    public SimpleDisplayBehaviour(AnchorPane parentPane, Node childNode) {
        this.parentPane = parentPane;
        this.childNode = childNode;
    }

    @Override
    public void display() {
        GuiUtils.setOnParentPane(parentPane, childNode);
    }
}
