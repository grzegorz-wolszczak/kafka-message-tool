package application.displaybehaviour;

import application.root.Executable;
import application.utils.GuiUtils;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class DetachedPaneContent extends BorderPane {

    private final Executable reattachPaneCallback;

    public DetachedPaneContent(Executable reattachPaneCallback) {
        this.reattachPaneCallback = reattachPaneCallback;
        final Label label = new Label("<Content detached, Double-click to attach>");
        //label.setFont();
        setCenter(label);
        GuiUtils.expandNodeToAnchorPaneBorders(this);

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    reattachPaneCallback.execute();
                }
            }
        });
    }
}
