package application.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;

import static application.utils.TimestampUtils.nowTimeTimestamp;

public class DefaultLogger implements ToolLogger {

    @Override
    public void logError(String msg) {
        System.err.println(String.format("%s [Error]: %s", nowTimeTimestamp(), msg));
    }

    @Override
    public void logError(Throwable e) {
        System.err.println(String.format("%s [Error]: %s", nowTimeTimestamp(),
                ExceptionUtils.getStackTrace(e)));
    }

    @Override
    public void logError(String msg, Throwable e) {

        System.err.println(String.format("%s [Error]: %s%n%s", nowTimeTimestamp(), msg,
                ExceptionUtils.getStackTrace(e)));
    }

    @Override
    public void logWarn(String msg) {
        System.err.println(String.format("%s [Warn ]: %s", nowTimeTimestamp(), msg));
    }

    @Override
    public void logDebug(String msg) {
        System.err.println(String.format("%s [Debug]: %s", nowTimeTimestamp(), msg));
    }

    @Override
    public void logTrace(String msg) {
        System.err.println(String.format("%s [Trace]: %s", nowTimeTimestamp(), msg));
    }

    @Override
    public void logInfo(String msg) {
        System.err.println(String.format("%s [Info ]: %s", nowTimeTimestamp(), msg));
    }

    @Override
    public void clear() {

    }
}
