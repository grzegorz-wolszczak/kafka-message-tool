package application.customfxwidgets.aboutwindow;

import application.constants.ApplicationConstants;
import application.globals.StageRepository;
import application.utils.ApplicationVersionProvider;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

import static application.customfxwidgets.CustomFxWidgetsLoader.loadAnchorPane;

public class AboutWindow extends AnchorPane {
    private static final String FXML_FILE = "AboutWindow.fxml";
    public static final int BIG_FONT_SIZE = 20;
    public static final int MEDIUM_FONT_SIZE = 15;
    private final Hyperlink sourceCodePage = new Hyperlink(ApplicationConstants.GITHUB_WEBSITE);
    private final Stage stage = StageRepository.get();
    private final Application fxApplication;

    @FXML
    private Button closeButton;


    @FXML
    private TextFlow textFlow;

    public AboutWindow(Window owner, Application fxApplication) throws IOException {
        this.fxApplication = fxApplication;

        loadAnchorPane(this, FXML_FILE);
        setupStage(owner);
        initAboutContent();
        configureLinks();
        configureCloseButton();
    }

    public void show() {
        stage.showAndWait();
    }

    private void configureCloseButton() {
        closeButton.setOnAction((e) -> {
            stage.close();
        });
    }

    private void configureLinks() {
        sourceCodePage.setOnAction(event -> {
            fxApplication.getHostServices().showDocument(sourceCodePage.getText());
            event.consume();
        });
    }

    private void setupStage(Window owner) {
        final Scene scene = new Scene(this);
        scene.getStylesheets().add(getClass().getResource(ApplicationConstants.GLOBAL_CSS_FILE_NAME).toExternalForm());
        scene.setRoot(this);
        stage.setScene(scene);
        stage.setTitle("About...");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initOwner(owner);
        stage.initModality(Modality.APPLICATION_MODAL);
    }


    private void initAboutContent() {
        final Text appNameText = getAppNameText();
        final Text appVersionText = getAppVersionText();
        final Text authorText = getAuthorText();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.getChildren().addAll(appNameText, new Text("\n"),
                                      appVersionText,  new Text("\n\n"),
                                      authorText, new Text("\n\n"),
                                      new Text("Source: " ), sourceCodePage);

    }

    private Text getAppVersionText() {
        final Text text = new Text(String.format("[v %s]", ApplicationVersionProvider.get()));
        text.setFont(Font.font("Helvetica", FontWeight.BOLD, BIG_FONT_SIZE));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    private Text getAuthorText() {
        Text text2 = new Text("Author: " + ApplicationConstants.AUTHOR);
        text2.setFont(Font.font("Helvetica", FontWeight.BOLD, MEDIUM_FONT_SIZE));
        return text2;
    }

    private Text getAppNameText() {
        Text appVersionText = new Text(ApplicationConstants.APPLICATION_NAME);
        appVersionText.setFont(Font.font("Helvetica", FontWeight.BOLD, BIG_FONT_SIZE));
        appVersionText.setTextAlignment(TextAlignment.CENTER);
        return appVersionText;
    }


}
