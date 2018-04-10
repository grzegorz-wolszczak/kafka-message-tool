
package application.logging;

import application.notifications.LogEventData;
import application.notifications.LogLevel;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintStream;

import static application.utils.TimestampUtils.nowTimestamp;

public class KafkaToolConsoleLogger extends AbstractToolLogger {

    private LogEventDataFormatter formatter;

    public KafkaToolConsoleLogger(LogEventDataFormatter formatter) {
        this.formatter = formatter;
    }

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

    @Override
    public void processLogEvent(LogEventData logEvent) {
        if(!shouldLogEventBasedOnLogLevel(logEvent))
        {
            return;
        }
        PrintStream streamToPrint = System.out;
        String formattedMessage = logRecord(logEvent);
        if(logEvent.getLevel().isGreaterThan(LogLevel.INFO))
        {
            streamToPrint= System.err;
        }

        streamToPrint.print(formattedMessage);
    }

    private String logRecord(LogEventData logEvent) {
        final String timestampAsString = formatter.formatTimestamp(logEvent.getTimestamp());
        final String logLevelAsString = formatter.formatLogLevel(logEvent.getLevel());
        final String logMsg = formatter.formatMessage(logEvent.getShortMsg(), logEvent.getLongMsg(), logEvent.getCause());
        return String.format("%s%s%s", timestampAsString,
                      logLevelAsString,
                      logMsg);
    }


}
