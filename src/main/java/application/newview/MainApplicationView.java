package application.newview;

import application.newview.guiwrappers.MasterDetailPaneWrapper;
import application.newview.guiwrappers.PaneWrapper;
import application.newview.guiwrappers.ToggleButtonWrapper;
import application.newview.guiwrappers.ToggleGroupWrapper;
import application.utils.gui.ColorChangeableButtonWrapper;
import application.utils.gui.FXNodeBlinker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.MasterDetailPane;

import java.io.IOException;

public class MainApplicationView extends VBox {

    private static final Image IMAGE_CONSOLE = new Image(MainApplicationView
                                                             .class
                                                             .getResourceAsStream("/images/console.png"));

    private static final Image IMAGE_ERROR = new Image(MainApplicationView
                                                           .class
                                                           .getResourceAsStream("/images/error.png"));

    private static final ImageView CONSOLE_IMAGE_VIEW = new ImageView(IMAGE_CONSOLE);
    private static final ImageView ERROR_IMAGE_VIEW = new ImageView(IMAGE_ERROR);
    private final AppContentView contentView;
    private final MessagesPane appMessagesPane;
    private final FXNodeBlinker onErrorBlinker;
    private final FXNodeBlinker onWarningBlinker;
    private final ToggleButton messagesButton = new ToggleButton("Messages", CONSOLE_IMAGE_VIEW);
    private final ToggleButton problemsButton = new ToggleButton("Problems", ERROR_IMAGE_VIEW);
    private final ToggleGroup toggleGroup = new ToggleGroup();
    private Stage mainStage;
    private NotificationViewController notificationViewController;
    private ProblemsPane appProblemsPane;
    @FXML
    private MasterDetailPane masterPane;
    @FXML
    private HBox bottomBarBox;


    public MainApplicationView(Stage mainStage, AppContentView contentView,
                               NotificationViewController notificationViewController,
                               MessagesPane appMessagesPane,
                               ProblemsPane appProblemsPane,
                               FXNodeBlinker onErrorBlinker,
                               FXNodeBlinker onWarningBlinker) throws IOException {
        this.mainStage = mainStage;
        this.contentView = contentView;
        this.notificationViewController = notificationViewController;
        this.appMessagesPane = appMessagesPane;
        this.appProblemsPane = appProblemsPane;
        this.onErrorBlinker = onErrorBlinker;
        this.onWarningBlinker = onWarningBlinker;

    }

    public void setupControls() {
        connectStageGuiPropertiesFromSettings();
        setupMasterPane();

        setupBottomBarBox();
        setupButtonBlinkers();
        setupNotificationViewController();
        setupButtonToggleGroup();
    }

    private void connectStageGuiPropertiesFromSettings() {

        mainStage.setWidth(ApplicationConfig.mainWindowWidthProperty().getValue());
        ApplicationConfig.mainWindowWidthProperty().bind(mainStage.widthProperty());

        mainStage.setHeight(ApplicationConfig.mainWindowHeightProperty().getValue());
        ApplicationConfig.mainWindowHeightProperty().bind(mainStage.heightProperty());
    }

    private void setupNotificationViewController() {
        notificationViewController.setMessagesButton(new ToggleButtonWrapper(messagesButton));
        notificationViewController.setProblemsButton(new ToggleButtonWrapper(problemsButton));
        notificationViewController.setMasterDetailPane(new MasterDetailPaneWrapper(masterPane));
        notificationViewController.setMessagesPane(new PaneWrapper(appMessagesPane));
        notificationViewController.setProblemsPane(new PaneWrapper(appProblemsPane));
        notificationViewController.setButtonsToggleGroup(new ToggleGroupWrapper(toggleGroup));

//        messagesButton.setOnAction(event -> {
//            notificationViewController.messagesButtonStateChanged();
//        });
        messagesButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //notificationViewController.messagesButtonStateChanged();
            }
        });
        problemsButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //notificationViewController.problemsButtonStateChanged();
            }
        });

//        problemsButton.setOnAction(event -> {
//            notificationViewController.problemsButtonStateChanged();
//        });
    }

    @FXML
    private void initialize() {
        masterPane.dividerPositionProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("new value: " + newValue);
                //Exceptionutils.getFullStackTrace(e);


//                for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//                    System.out.println(ste);
//                }


            }
        });




    }

    private void setupButtonToggleGroup() {
        toggleGroup.getToggles().addAll(messagesButton, problemsButton);
        messagesButton.setSelected(ApplicationConfig.messagesViewButtonSelectedProperty().get());
        messagesButton.selectedProperty().bindBidirectional(ApplicationConfig.messagesViewButtonSelectedProperty());

        problemsButton.setSelected(ApplicationConfig.problemsViewButtonSelectedProperty().get());
        problemsButton.selectedProperty().bindBidirectional(ApplicationConfig.problemsViewButtonSelectedProperty());

    }

    private void setupButtonBlinkers() {
        onErrorBlinker.setNodeToBlink(new ColorChangeableButtonWrapper(problemsButton));
        onWarningBlinker.setNodeToBlink(new ColorChangeableButtonWrapper(problemsButton));
    }

    private void setupBottomBarBox() {

        bottomBarBox.getChildren().addAll(messagesButton, problemsButton);
        bottomBarBox.setAlignment(Pos.CENTER_LEFT);

    }


    private void setupMasterPane() {


        //masterPane.setDividerPosition();
        //masterPane.setMasterNode(contentView);

        //TextArea node = new TextArea();
        //masterPane.setDetailNode(node);

        Platform.runLater(()->{
            masterPane.dividerPositionProperty().bindBidirectional(ApplicationConfig.masterDetailDividerPositionProperty());
            masterPane.setDividerPosition(ApplicationConfig.masterDetailDividerPositionProperty().get());
            masterPane.setShowDetailNode(true);
        });
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
