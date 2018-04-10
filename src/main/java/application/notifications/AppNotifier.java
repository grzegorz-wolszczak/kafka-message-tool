package application.notifications;

import application.logging.AppLogger;

import java.time.Instant;


public class AppNotifier {

    public static StatusNotifier getStatusNotifier() {
        return STATUS_NOTIFIER;
    }

    private static final StatusNotifier STATUS_NOTIFIER = new StatusNotifier();

    public static void reportException(Throwable e)
    {
        final LogEventData logEventData = LogEventData.LogEventDataBuilder.builder()
            .withTimestamp(Instant.now())
            .withCause(e)
            .withLevel(LogLevel.ERROR)
            .build();
        AppLogger.processLogEvent(logEventData);
        STATUS_NOTIFIER.notifyAboutError();
    }
}
