
package application.logging;

public interface ToolLogger {
    void logError(String msg);
    void logError(Throwable e);
    void logError(String msg, Throwable e);
    void logWarn(String msg);
    void logInfo(String msg);
    void logDebug(String msg);
    void logTrace(String msg);
    void clear();

}
