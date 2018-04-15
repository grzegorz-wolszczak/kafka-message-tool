package application.root;

import javafx.application.Application;
import javafx.stage.Stage;

public interface ApplicationRoot {
    void start(Stage stage) throws Exception;
    void stop();
    Application getApplication();
}
