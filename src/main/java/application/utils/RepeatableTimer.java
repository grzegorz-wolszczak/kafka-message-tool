package application.utils;

import application.root.Executable;

import java.util.Timer;
import java.util.TimerTask;

public final class RepeatableTimer {
    private static final int EXECUTION_DELAY_MS = 0;
    private Timer timer = new Timer(this.getClass().getName());

    public void startExecutingRepeatedly(Executable executableTask, long repeatRateMs) {
        TimerTask timerTask = getTimerTask(executableTask);
        timer = new Timer(this.getClass().getName());
        timer.scheduleAtFixedRate(timerTask, EXECUTION_DELAY_MS, repeatRateMs);
    }

    private TimerTask getTimerTask(Executable executableTask) {
        return new TimerTask() {
            @Override
            public void run() {
                executableTask.execute();
            }
        };
    }

    public void cancel() {
        try {
            timer.cancel();
        } catch (java.lang.IllegalStateException ignored) {
            // don't complain about cancelling already cancelled
        }
    }
}