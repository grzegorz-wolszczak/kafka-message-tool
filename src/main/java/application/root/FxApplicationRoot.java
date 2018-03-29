package application.root;

import javafx.stage.Stage;

public interface FxApplicationRoot {
    void start(Stage stage) throws Exception;
    void stop();
}
