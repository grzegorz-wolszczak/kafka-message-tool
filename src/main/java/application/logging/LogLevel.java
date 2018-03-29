package application.logging;

public enum LogLevel {

    TRACE("Trace", 1),
    DEBUG("Debug", 2),
    INFO("Info", 3),
    WARN("Warn", 4),
    ERROR("Error", 5);

    public final String prefix;
    private final int levelValue;

    LogLevel(String prefix, int levelValue) {

        this.prefix = prefix;
        this.levelValue = levelValue;
    }

    public boolean isGreaterThan(LogLevel other) {
        return this.levelValue > other.levelValue;
    }
}
