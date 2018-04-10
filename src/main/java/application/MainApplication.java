package application;

import application.logging.AppLogger;
import application.root.FxApplicationLogicRoot;
import application.root.FxApplicationRoot;
import javafx.application.Application;
import javafx.stage.Stage;


public class MainApplication extends Application {

    private static final int ERROR_EXIT_STATUS = 1;

    private final FxApplicationRoot app = new FxApplicationLogicRoot(this);

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            app.start(primaryStage);
        } catch (Throwable e) {
            AppLogger.error("Error while starting application", e);
            System.exit(ERROR_EXIT_STATUS);
        }
    }


}
