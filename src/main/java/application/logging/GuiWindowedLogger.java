package application.logging;

import application.utils.TimestampUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;


public class GuiWindowedLogger implements ToolLogger {

    private FixedNumberRecordsCountLogger fixedRecordsLogger;


    public GuiWindowedLogger(FixedNumberRecordsCountLogger fixedRecordsLogger) {
        this.fixedRecordsLogger = fixedRecordsLogger;
    }

    @Override
    public void logError(String msg) {
        logLater(LogLevel.ERROR, msg);
    }

    @Override
    public void logError(Throwable e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        logLater(LogLevel.ERROR, stacktrace);
    }

    @Override
    public void logError(String msg, Throwable e) {
        String stacktrace = ExceptionUtils.getStackTrace(e);
        logLater(LogLevel.ERROR, msg + "\n" + stacktrace);
    }

    @Override
    public void logWarn(String msg) {
        logLater(LogLevel.WARN, msg);
    }

    @Override
    public void logDebug(String msg) {
        logLater(LogLevel.DEBUG, msg);
    }

    @Override
    public void logTrace(String msg) {
        logLater(LogLevel.TRACE, msg);
    }

    @Override
    public void logInfo(String msg) {
        logLater(LogLevel.INFO, msg);
    }

    @Override
    public void clear() {
        fixedRecordsLogger.clear();
    }


    private String formattedText(LogLevel logLevel, String msg) {
        return String.format("%s [%-5s] %s\n", TimestampUtils.nowTimeTimestamp(), logLevel.prefix, msg);
    }

    private void logMessage(String formattedText) {
        try {
            appendTextWithStyleClass(formattedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendTextWithStyleClass(String formattedText) {
        fixedRecordsLogger.appendText(formattedText);

    }


    private void logLater(LogLevel logLevel, String msg) {
        logMessage(formattedText(logLevel, msg));
    }


}
