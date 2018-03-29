package application.customfxwidgets.senderconfig;

import application.root.Executable;
import javafx.beans.property.BooleanProperty;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MessageSenderTaskExecutor {
    private final BooleanProperty startButtonDisabledProperty;
    private final BooleanProperty stopButtonDisabledProperty;
    private final ExecutorService executorService;
    private FutureTask<Void> futureTask;

    public MessageSenderTaskExecutor(BooleanProperty startButtonDisabledProperty,
                                     BooleanProperty stopButtonDisabledProperty) {
        this.startButtonDisabledProperty = startButtonDisabledProperty;
        this.stopButtonDisabledProperty = stopButtonDisabledProperty;
        executorService = Executors.newSingleThreadExecutor();
    }

    public void run(Executable e) {
        disableStartButton();
        if (futureTask != null) {
            stopTask();
        }

        futureTask = new FutureTask<>(() -> {
            e.execute();
            enableStartButton();
            return null;
        });

        // todo: consider thread pool - but remember that we must
        // stop all threadpool on application exit
        new Thread(futureTask, "MessageSenderTaskExecutor-Thread").start();
    }

    public void stop() {
        stopTask();
    }

    private void stopTask() {
        if (futureTask != null) {
            futureTask.cancel(true);
        }
    }

    private void disableStartButton() {
        startButtonDisabledProperty.set(true);
        stopButtonDisabledProperty.set(!startButtonDisabledProperty.get());
    }

    private void enableStartButton() {
        startButtonDisabledProperty.set(false);
        stopButtonDisabledProperty.set(!startButtonDisabledProperty.get());

    }
}

