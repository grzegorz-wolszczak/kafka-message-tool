package application.globals;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

public class Timers {

    private static Set<Timer> timers = new HashSet<Timer>();

    public static Timer newTimer(String name) {
        final Timer timer = new Timer(name);
        timers.add(timer);
        return timer;
    }

    public static void stop(){
        timers.forEach(Timer::cancel);
    }
}
