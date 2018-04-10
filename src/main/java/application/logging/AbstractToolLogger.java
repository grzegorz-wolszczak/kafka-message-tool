package application.logging;

import application.notifications.LogEventData;
import application.notifications.LogLevel;

abstract public class AbstractToolLogger implements ToolLogger{
    private LogLevel currentLevel = LogLevel.INFO;

    @Override
    public void setLogLevel(LogLevel level) {
        currentLevel = level;
    }

    protected boolean shouldLogEventBasedOnLogLevel(LogEventData logEvent) {
        return !currentLevel.isGreaterThan(logEvent.getLevel());
    }
}
