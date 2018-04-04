package application.utils;

import com.google.common.base.CaseFormat;
import application.constants.ApplicationConstants;
import application.customfxwidgets.ConfigEntriesView;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Window;

import java.util.Optional;

public class UserGuiInteractor implements UserInteractor {

    private final Window owner;
    private Alert configEntriesViewAlert;

    public UserGuiInteractor(Window owner) {
        this.owner = owner;
    }

    @Override
    public Optional<String> getTextFromUser(String contentText) {
        Optional<String> result = Optional.empty();

        boolean shouldContinue = true;
        while (shouldContinue) {
            final TextInputDialog dialog = new TextInputDialog("");
            dialog.initOwner(owner);
            dialog.setContentText(contentText);

            decorateWithCss(dialog);
            result = dialog.showAndWait();

            if (result.isPresent()) {
                final String name = result.get();
                if (!ValidatorUtils.isStringIdentifierValid(name)) {
                    showError("Invalid value");
                } else {
                    shouldContinue = false;
                }
            } else {
                shouldContinue = false;
            }
        }
        return result;
    }

    @Override
    public void showError(String headerText, String contentText) {
        showErrorDialog(headerText, contentText);
    }

    @Override
    public void showError(String headerText, Throwable e) {
        showError(headerText, ThrowableUtils.getMessage(e));
    }

    @Override
    public boolean getYesNoDecision(String title,
                                    String header,
                                    String content) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(owner);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(getTextNodeContent(content));

        decorateWithCss(alert);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    @Override
    public void showWarning(String header, String msg) {
        showWarningDialog(header, msg);
    }

    @Override
    public void showConfigEntriesInfoDialog(String title,
                                            String header,
                                            ConfigEntriesView entriesView) {
        final Alert alert = getConfigEntriesViewDialog(header);
        alert.setTitle(title);
        final DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(entriesView);
        alert.setResizable(true);
        alert.showAndWait();

    }

    public void showErrorDialog(String headerText, String contentText) {
        showAlertDialog(AlertType.ERROR, headerText, contentText);
    }

    private static void applyStylesheetTo(Dialog dialog) {
        DialogPane dialogPane = dialog.getDialogPane();

        dialogPane.getStylesheets().add(
            Thread.currentThread()
                .getClass()
                .getResource(ApplicationConstants.GLOBAL_CSS_FILE_NAME)
                .toExternalForm());
    }

    private static Node getTextNodeContent(String content) {
        Label l = new Label();
        l.setText(content);
        l.setWrapText(true);
        return l;
    }

    private void showAlertDialog(AlertType error, String headerText, String contentText) {
        showAlert(error, headerText, contentText, owner);
    }

    private static void showAlert(AlertType error, String headerText, String contentText, Window owner) {
        Platform.runLater(() -> {
            Alert alert = new Alert(error);
            alert.initOwner(owner);

            alert.setTitle(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, alert.getAlertType().name()));
            alert.setHeaderText(headerText);
            alert.getDialogPane().setContent(getTextNodeContent(contentText));

            decorateWithCss(alert);

            alert.showAndWait();
        });
    }

    public static void errorDialog(String headerText, String contentText) {
        showAlert(AlertType.ERROR, headerText, contentText, null);
    }


    private static void decorateWithCss(Dialog dialog) {
        applyStylesheetTo(dialog);
    }

    private Alert getConfigEntriesViewDialog(String header) {
        if (configEntriesViewAlert == null) {
            configEntriesViewAlert = new Alert(AlertType.INFORMATION);
            configEntriesViewAlert.initOwner(owner);
            decorateWithCss(configEntriesViewAlert);
        }
        configEntriesViewAlert.setHeaderText(header);
        return configEntriesViewAlert;
    }

    private void showError(String content) {
        showErrorDialog("", content);
    }

    private void showWarningDialog(String header, String msg) {
        showAlertDialog(AlertType.WARNING, header, msg);
    }


}
