package application;

import application.logging.Logger;
import application.root.KafkaMessageToolApplication;
import application.root.ApplicationRoot;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    private static final int ERROR_EXIT_STATUS = 1;

    private final ApplicationRoot app = new KafkaMessageToolApplication(this);

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            app.start(primaryStage);
        } catch (Throwable e) {
            Logger.error("Error while starting application", e);
            System.exit(ERROR_EXIT_STATUS);
        }
    }


}
