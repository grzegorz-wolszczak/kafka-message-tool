package application.newview;

import application.customfxwidgets.CustomFxWidgetsLoader;
import application.utils.gui.ColorChangeableButtonWrapper;
import application.utils.gui.FXNodeBlinker;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.MasterDetailPane;

import java.io.IOException;

public class MainApplicationController2 extends VBox {

    private static final Image IMAGE_CONSOLE = new Image(MainApplicationController2
                                                             .class
                                                             .getResourceAsStream("/images/console.png"));

    private static final Image IMAGE_ERROR = new Image(MainApplicationController2
                                                           .class
                                                           .getResourceAsStream("/images/error.png"));

    private static final ImageView CONSOLE_IMAGE_VIEW = new ImageView(IMAGE_CONSOLE);
    private static final ImageView ERROR_IMAGE_VIEW = new ImageView(IMAGE_ERROR);

    private static final String MAIN_APPLICATION_VIEW_FXML_FILE = "MainApplicationPane.fxml";
    private final AppContentView contentView;
    private final ApplicationNotificationView appNotificationView;
    private final FXNodeBlinker onErrorBlinker;
    private final FXNodeBlinker onWarningBlinker;

    @FXML
    private MasterDetailPane masterPane;

    @FXML
    private HBox bottomBarBox;

    private ToggleButton messagesButton = new ToggleButton("Messages", CONSOLE_IMAGE_VIEW);
    private ToggleButton problemsButton = new ToggleButton("Problems", ERROR_IMAGE_VIEW);


    public MainApplicationController2(AppContentView contentView,
                                      ApplicationNotificationView appNotificationView,
                                      FXNodeBlinker onErrorBlinker,
                                      FXNodeBlinker onWarningBlinker) throws IOException {
        this.contentView = contentView;
        this.appNotificationView = appNotificationView;
        this.onErrorBlinker = onErrorBlinker;
        this.onWarningBlinker = onWarningBlinker;


        CustomFxWidgetsLoader.load(this, MAIN_APPLICATION_VIEW_FXML_FILE);
        setupMasterPane();
        setupDetailPane();
        setupButtonToggleGroup();
        setupBottomBarBox();
        setupButtonBlinkers();
    }

    private void setupButtonToggleGroup() {
        new ToggleGroup().getToggles().addAll(messagesButton, problemsButton);

    }

    private void setupButtonBlinkers() {
        onErrorBlinker.setNodeToBlink(new ColorChangeableButtonWrapper(problemsButton));
        onWarningBlinker.setNodeToBlink(new ColorChangeableButtonWrapper(problemsButton));
    }

    private void setupBottomBarBox() {

        bottomBarBox.getChildren().addAll(messagesButton, problemsButton);
        bottomBarBox.setAlignment(Pos.CENTER_LEFT);

    }

    private void setupDetailPane() {
        masterPane.setDetailNode(appNotificationView);
    }

    private void setupMasterPane() {
        masterPane.setMasterNode(contentView);
    }

    @FXML
    private void menuItemSaveConfigOnAction() {
        //   appSettings.save();
    }

    @FXML
    private void menuItemShowAboutWindow() {
        //   showAboutWindow();
    }

//    @FXML
//    private void addButtonOnAction() {
//        // guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::addNewConfig);
//        // refreshRightPane();
//    }

//    @FXML
//    private void deleteButtonOnAction() {
//        guiActionsHandlers.forEach(ModelObjectGuiActionsHandler::deleteSelectedConfig);
//        refreshRightPane();
//    }
//
//    @FXML
//    private void clearLogsButtonOnAction() {
//        Logger.clear();
//    }
//
//    @FXML
//    private void leftViewTabPaneOnMouseClicked() {
//        refreshRightPane();
//    }
}
