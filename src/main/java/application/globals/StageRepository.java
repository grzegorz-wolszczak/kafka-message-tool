package application.globals;

import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class StageRepository {
    private static final Set<Stage> STAGES = new HashSet<>();

    public static Stage get(){
        final Stage stage = new Stage();
        STAGES.add(stage);
        return stage;
    }

    public static void closeAllStages(){
        STAGES.forEach(Stage::close);
    }
}
