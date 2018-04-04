package application.utils;

import application.customfxwidgets.ConfigEntriesView;

import java.util.Optional;

public interface UserInteractor {
    Optional<String> getTextFromUser(String contentText);

    void showError(String headerText, String contentText);

    void showError(String headerText, Throwable e);

    boolean getYesNoDecision(String title,
                             String header,
                             String content);

    void showWarning(String header, String msg);

    void showConfigEntriesInfoDialog(String title, String topicName, ConfigEntriesView entriesView);
}
