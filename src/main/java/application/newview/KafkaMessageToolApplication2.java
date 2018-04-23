package application.newview;

import application.constants.ApplicationConstants;
import application.customfxwidgets.CustomFxWidgetsLoader;
import application.globals.AppGlobals;
import application.logging.DefaultLogger;
import application.logging.Logger;
import application.model.DataModel;
import application.model.DefaultModelDataProxy;
import application.model.ModelDataProxy;
import application.root.ApplicationRoot;
import application.root.PrerequisiteChecker;
import application.utils.ApplicationVersionProvider;
import application.utils.GuiUtils;
import application.utils.gui.FXNodeBlinker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class KafkaMessageToolApplication2 implements ApplicationRoot {

    private static final String MAIN_APPLICATION_VIEW_FXML_FILE = "new/MainApplicationPane.fxml";
    //    private final Restartables restartables = new Restartables();
//    private ApplicationPorts applicationPorts;
    private Stage mainStage;
    //    private ApplicationSettings applicationSettings;
    private Scene scene;
    //    private ExecutorService executorService;
    private Main2 mainApplication;

    public KafkaMessageToolApplication2(Main2 mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Override
    public void start(Stage stage) throws Exception {

        initialize(stage);

        PrerequisiteChecker.assertPrerequisites();
//
        createObjects();
        configureScene();
        configureStage();
//        restartables.start();
        mainStage.show();
    }

    public void stopAll() {
//        restartables.stop();
//        applicationSettings.save();
//        KafkaProducers.close();
//        executorService.shutdown();
        ApplicationConfig.save();
    }

    @Override
    public void stop() {
        stopAll();
    }

    @Override
    public Application getApplication() {
        return mainApplication;
    }

    private void initialize(Stage stage) {
        mainStage = stage;
        Logger.registerLogger(new DefaultLogger());
        AppGlobals.initialize();
    }

    private void configureScene() {
        GuiUtils.loadCssIfPossible(scene, ApplicationConstants.GLOBAL_CSS_FILE_NAME);
        GuiUtils.loadCssIfPossible(scene, ApplicationConstants.GROOVY_KEYWORDS_STYLES_CSS);
        GuiUtils.loadCssIfPossible(scene, ApplicationConstants.JSON_STYLES_CSS);

    }

    private void configureStage() {
        configureStageBehaviourAndLook();
    }



    private void configureStageBehaviourAndLook() {
        mainStage.setOnCloseRequest(event -> {
            event.consume();
            stopAll();
            Platform.exit();
        });
        mainStage.setScene(scene);
        mainStage.setTitle(String.format(ApplicationConstants.APPLICATION_NAME + " (%s)", ApplicationVersionProvider.get()));
        GuiUtils.addApplicationIcon(mainStage);
    }

    private void createObjects() throws Exception {

        final DataModel dataModel = new DataModel();
        final ModelDataProxy modelDataProxy = new DefaultModelDataProxy(dataModel);
        ApplicationConfig.load(modelDataProxy);
//        applicationPorts = restartables.register(new DefaultApplicationPorts(new DefaultKafkaMessageSender(),
//                                                                             new KafkaListeners()));
//

//
//
//        final UserGuiInteractor interactor = new UserGuiInteractor(mainStage);
//        final ApplicationBusySwitcher busySwitcher = new DefaultApplicationBusySwitcher(mainStage);
//
//        final TextArea logTextArea = getTextAreaForLogging();
//        final FixedNumberRecordsCountLogger fixedRecordsLogger = new FixedNumberRecordsCountLogger(logTextArea, new CyclicStringBuffer());
//        restartables.register(fixedRecordsLogger);
//        Logger.registerLogger(new GuiWindowedLogger(fixedRecordsLogger));


        Logger.setLogLevel(ApplicationConfig.getLogLevel());
//
//        executorService = Executors.newSingleThreadExecutor();
//        final KafkaClusterProxies kafkaClusterProxies = new KafkaClusterProxies();
//        final ControllerRepositoryFactory controllerRepositoryFactory =
//            new DefaultControllerRepositoryFactory(new ClusterStatusChecker(busySwitcher, interactor, kafkaClusterProxies),
//                                                   new SyntaxHighlightingCodeAreaConfigurator(executorService),
//                                                   kafkaClusterProxies,
//                                                   applicationSettings,
//                                                   restartables);
//
//        final DefaultActionHandlerFactory actionHandlerFactory = new DefaultActionHandlerFactory(interactor,
//                                                                                                 modelDataProxy,
//                                                                                                 mainStage,
//                                                                                                 applicationPorts);
//
        final FXNodeBlinker onErrorButtonBlinker = new FXNodeBlinker(Color.RED);
        final FXNodeBlinker onWarningButtonBlinker = new FXNodeBlinker(Color.BLUE);
        final AppContentView contentView = new AppContentView();

        final NotificationViewController notificationViewController = new NotificationViewController();


        final MessagesPane appMessagesPane = new MessagesPane();
        final ProblemsPane appProblemsPane = new ProblemsPane();
        final MainApplicationView mainController = new MainApplicationView(mainStage,
                                                                           contentView,
                                                                           notificationViewController,
                                                                           appMessagesPane,
                                                                           appProblemsPane,
                                                                           onErrorButtonBlinker,
                                                                           onWarningButtonBlinker);

//        final MainApplicationController mainController = new MainApplicationController(mainStage,
//                                                                                       dataModel,
//                                                                                       getApplication(),
//                                                                                       applicationSettings,
//                                                                                       logTextArea,
//                                                                                       controllerRepositoryFactory,
//                                                                                       actionHandlerFactory,
//                                                                                       busySwitcher);
//
        CustomFxWidgetsLoader.loadOnAnchorPane(mainController, MAIN_APPLICATION_VIEW_FXML_FILE);
//
//
//
        mainController.setupControls();
        scene = new Scene(mainController);
//
    }
//
//    private TextArea getTextAreaForLogging() {
//        final TextArea textArea = new TextArea();
//        textArea.setWrapText(true);
//        textArea.setEditable(false);
//        return textArea;
//    }

}
