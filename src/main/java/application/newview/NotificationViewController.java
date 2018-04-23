package application.newview;

import application.newview.guiwrappers.MasterDetailPaneWrapper;
import application.newview.guiwrappers.PaneWrapper;
import application.newview.guiwrappers.ToggleButtonWrapper;
import application.newview.guiwrappers.ToggleGroupWrapper;

public class NotificationViewController {
    private ToggleButtonWrapper messagesButton;
    private ToggleButtonWrapper problemsButton;
    private PaneWrapper messagesPane;
    private PaneWrapper problemsPane;
    private MasterDetailPaneWrapper masterDetailPane;
    private ToggleGroupWrapper buttonsToggleGroup;

    public void setButtonsToggleGroup(ToggleGroupWrapper toggleGroupWrapper) {
        this.buttonsToggleGroup = toggleGroupWrapper;

    }

    public void messagesButtonStateChanged() {
        if (messagesButton.isSelected()) {
            masterDetailPane.setDetailNode(messagesPane);
            return;
        }
        if (!buttonsToggleGroup.isSelected()) {
            masterDetailPane.setDetailNode(null);
        }

    }

    public void setMessagesButton(ToggleButtonWrapper messagesButton) {
        this.messagesButton = messagesButton;
    }

    public void setProblemsButton(ToggleButtonWrapper problemsButton) {
        this.problemsButton = problemsButton;
    }


    public void setMessagesPane(PaneWrapper messagesPane) {
        this.messagesPane = messagesPane;
    }

    public void setProblemsPane(PaneWrapper problemsPane) {
        this.problemsPane = problemsPane;
    }

    public void setMasterDetailPane(MasterDetailPaneWrapper masterDetailPane) {
        this.masterDetailPane = masterDetailPane;
    }


    public void problemsButtonStateChanged() {

        if (problemsButton.isSelected()) {
            masterDetailPane.setDetailNode(problemsPane);
            return;
        }
        if (!buttonsToggleGroup.isSelected()) {
            masterDetailPane.setDetailNode(null);
        }

    }
}
