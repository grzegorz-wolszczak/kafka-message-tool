package application.root;

import java.util.ArrayList;
import java.util.List;

public class Restartables implements Restartable {

    private final List<Restartable> restartables = new ArrayList<>();

    public <T extends Restartable> T register(T object) {
        if(!restartables.contains(object)) {
            restartables.add(object);
        }
        return object;
    }

    @Override
    public void start() {
        restartables.forEach(Restartable::start);
    }

    @Override
    public void stop() {
        restartables.forEach(Restartable::stop);
    }
}
