package application.root;

import application.Main;
import application.constants.ApplicationConstants;
import application.customfxwidgets.CustomFxWidgetsLoader;
import application.customfxwidgets.mainviewcontroller.ControllerRepositoryFactory;
import application.customfxwidgets.mainviewcontroller.DefaultControllerRepositoryFactory;
import application.customfxwidgets.mainviewcontroller.MainApplicationController;
import application.globals.AppGlobals;
import application.kafka.cluster.ClusterStatusChecker;
import application.kafka.cluster.KafkaClusterProxies;
import application.kafka.listener.KafkaListeners;
import application.kafka.sender.DefaultKafkaMessageSender;
import application.logging.CyclicStringBuffer;
import application.logging.DefaultLogger;
import application.logging.FixedNumberRecordsCountLogger;
import application.logging.GuiWindowedLogger;
import application.logging.Logger;
import application.model.DataModel;
import application.model.DefaultModelDataProxy;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;
import application.persistence.ApplicationSettings;
import application.persistence.DefaultApplicationSettings;
import application.persistence.GlobalSettings;
import application.persistence.GuiSettings;
import application.persistence.XmlFileConfig;
import application.scripting.codearea.SyntaxHighlightingCodeAreaConfigurator;
import application.utils.ApplicationVersionProvider;
import application.utils.GuiUtils;
import application.utils.UserGuiInteractor;
import application.utils.kafka.KafkaProducers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class KafkaMessageToolApplication implements ApplicationRoot {

    private static final String MAIN_APPLICATION_VIEW_FXML_FILE = "MainApplicationView.fxml";
    private final Restartables restartables = new Restartables();
    private ApplicationPorts applicationPorts;
    private Stage mainStage;
    private ApplicationSettings applicationSettings;
    private Scene scene;
    private ExecutorService executorService;
    private Main mainApplication;

    public KafkaMessageToolApplication(Main mainApplication) {
        this.mainApplication = mainApplication;
    }

    @Override
    public void start(Stage stage) throws Exception {

        initialize(stage);

        PrerequisiteChecker.assertPrerequisites();

        createObjects();
        configureScene();
        configureStage();
        restartables.start();
        mainStage.show();
    }

    public void stopAll() {
        restartables.stop();
        applicationSettings.save();
        KafkaProducers.close();
        executorService.shutdown();
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
        final GuiSettings guiSettings = new GuiSettings();
        final GlobalSettings globalSettings = new GlobalSettings();
        applicationPorts = restartables.register(new DefaultApplicationPorts(new DefaultKafkaMessageSender(),
                                                                             new KafkaListeners()));

        final ModelDataProxy modelDataProxy = new DefaultModelDataProxy(dataModel);
        final XmlFileConfig xmlFileConfig = new XmlFileConfig(modelDataProxy,
                                                              new FromPojoConverter(modelDataProxy),
                                                              guiSettings,
                                                              globalSettings);


        final UserGuiInteractor interactor = new UserGuiInteractor(mainStage);
        final ApplicationBusySwitcher busySwitcher = new DefaultApplicationBusySwitcher(mainStage);

        final TextArea logTextArea = getTextAreaForLogging();
        final FixedNumberRecordsCountLogger fixedRecordsLogger = new FixedNumberRecordsCountLogger(logTextArea, new CyclicStringBuffer());
        restartables.register(fixedRecordsLogger);
        Logger.registerLogger(new GuiWindowedLogger(fixedRecordsLogger));
        applicationSettings = new DefaultApplicationSettings(xmlFileConfig);
        applicationSettings.load();
        Logger.setLogLevel(applicationSettings.appSettings().getLogLevel());

        executorService = Executors.newSingleThreadExecutor();
        final KafkaClusterProxies kafkaClusterProxies = new KafkaClusterProxies();
        final ControllerRepositoryFactory controllerRepositoryFactory =
            new DefaultControllerRepositoryFactory(new ClusterStatusChecker(busySwitcher, interactor, kafkaClusterProxies),
                                                   new SyntaxHighlightingCodeAreaConfigurator(executorService),
                                                   kafkaClusterProxies,
                                                   applicationSettings,
                                                   restartables);

        final DefaultActionHandlerFactory actionHandlerFactory = new DefaultActionHandlerFactory(interactor,
                                                                                                 modelDataProxy,
                                                                                                 mainStage,
                                                                                                 applicationPorts);

        final MainApplicationController mainController = new MainApplicationController(mainStage,
                                                                                       dataModel,
                                                                                       getApplication(),
                                                                                       applicationSettings,
                                                                                       logTextArea,
                                                                                       controllerRepositoryFactory,
                                                                                       actionHandlerFactory,
                                                                                       busySwitcher);

        CustomFxWidgetsLoader.loadOnAnchorPane(mainController, MAIN_APPLICATION_VIEW_FXML_FILE);



        mainController.setupControls();

        scene = new Scene(mainController);
    }

    private TextArea getTextAreaForLogging() {
        final TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        return textArea;
    }

}
