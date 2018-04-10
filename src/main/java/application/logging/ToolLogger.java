package application.logging;

import application.notifications.LogEventData;
import application.notifications.LogLevel;

public interface ToolLogger {
    void logError(String msg);

    void logError(Throwable e);

    void logError(String msg, Throwable e);

    void logWarn(String msg);

    void logInfo(String msg);

    void logDebug(String msg);

    void logTrace(String msg);

    void clear();

    void processLogEvent(LogEventData logEvent);

    void setLogLevel(LogLevel level);

}
