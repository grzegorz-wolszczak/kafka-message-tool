package controllers;

import application.newview.NotificationViewController;
import application.newview.guiwrappers.MasterDetailPaneWrapper;
import application.newview.guiwrappers.PaneWrapper;
import application.newview.guiwrappers.ToggleButtonWrapper;
import application.newview.guiwrappers.ToggleGroupWrapper;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MessagesPaneControllerSpecificaiton {

    @Mock
    private ToggleButtonWrapper messagesButton;
    @Mock
    private ToggleButtonWrapper problemsButton;
    @Mock
    private ToggleGroupWrapper toggleGroupWrapper;
    @Mock
    private PaneWrapper messagesPane;
    @Mock
    private PaneWrapper problemsPane;
    @Mock
    private MasterDetailPaneWrapper masterDetailPane;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldDisplayMessagesViewWhenMessagesToggleButtonIsPressed() {

        // GIVEN
        NotificationViewController controller = getController();
        when(messagesButton.isSelected()).thenReturn(true);

        // WHEN
        controller.messagesButtonStateChanged();

        // THEN
        verify(masterDetailPane).setDetailNode(messagesPane);

    }

    @Test
    public void shouldDisplayProblemsViewWhenProblemsToggleButtonIsPressed() {

        // GIVEN
        NotificationViewController controller = getController();
        when(problemsButton.isSelected()).thenReturn(true);

        // WHEN
        controller.problemsButtonStateChanged();

        // THEN
        verify(masterDetailPane).setDetailNode(problemsPane);

    }

    @Test
    public void shouldHideDetailPaneWhenMessaesButtonIsNotPressedAndNoOtherButtonsArePressed() {

        // GIVEN
        NotificationViewController controller = getController();
        when(toggleGroupWrapper.isSelected()).thenReturn(false);

        // WHEN
        controller.messagesButtonStateChanged();

        // THEN
        verify(masterDetailPane).setDetailNode(null);

    }


    @Test
    public void shouldHideDetailPaneWhenProblemsButtonIsNotPressedAndNoOtherButtonsArePressed() {

        // GIVEN
        NotificationViewController controller = getController();
        when(toggleGroupWrapper.isSelected()).thenReturn(false);

        // WHEN
        controller.problemsButtonStateChanged();

        // THEN
        verify(masterDetailPane).setDetailNode(null);

    }


    private NotificationViewController getController() {
        final NotificationViewController controller = new NotificationViewController();
        controller.setMessagesButton(messagesButton);
        controller.setProblemsButton(problemsButton);
        controller.setMessagesPane(messagesPane);
        controller.setProblemsPane(problemsPane);
        controller.setButtonsToggleGroup(toggleGroupWrapper);
        controller.setMasterDetailPane(masterDetailPane);
        return controller;
    }
}
