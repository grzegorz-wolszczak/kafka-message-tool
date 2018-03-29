package application.controllers.helpers;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class TabPaneSelectionInformer {

    private final TabPane tabPane;
    private final Tab tab;

    public TabPaneSelectionInformer(TabPane tabPane,
                                    Tab tab) {
        this.tabPane = tabPane;
        this.tab = tab;
    }

    public boolean isMyTabSelected() {
        return tabPane.getSelectionModel().getSelectedItem() == tab;
    }
}
