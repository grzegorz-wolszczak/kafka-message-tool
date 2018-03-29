
package application.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;

import static application.utils.TimestampUtils.nowTimestamp;

public class DefaultLogger implements ToolLogger {

    @Override
    public void logError(String msg) {
        System.err.println(String.format("[%s][Error]: %s",nowTimestamp(),msg));
    }

    @Override
    public void logError(Throwable e) {
        System.err.println(String.format("[%s][Error]: %s",nowTimestamp(),
                                         ExceptionUtils.getStackTrace(e)));
    }

    @Override
    public void logError(String msg, Throwable e) {

        System.err.println(String.format("[%s][Error]: %s%n%s",nowTimestamp(),msg,
                                         ExceptionUtils.getStackTrace(e)));
    }

    @Override
    public void logWarn(String msg) {
        System.err.println(String.format("[%s][Warn ]: %s",nowTimestamp(),msg));
    }

    @Override
    public void logDebug(String msg) {
        System.err.println(String.format("[%s][Debug]: %s",nowTimestamp(),msg));
    }

    @Override
    public void logTrace(String msg) {
        System.err.println(String.format("[%s][Trace]: %s",nowTimestamp(),msg));
    }

    @Override
    public void logInfo(String msg) {
        System.err.println(String.format("[%s][Info ]: %s",nowTimestamp(),msg));
    }

    @Override
    public void clear() {

    }
}
