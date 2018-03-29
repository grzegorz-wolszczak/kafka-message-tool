package application.logging;

import javafx.application.Platform;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fxmisc.richtext.StyleClassedTextArea;

import static application.utils.TimestampUtils.nowTimestamp;


public class GuiWindowedLogger implements ToolLogger {

    private final StyleClassedTextArea logArea;


    public GuiWindowedLogger(StyleClassedTextArea logArea) {
        this.logArea = logArea;

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
        Platform.runLater(logArea::clear);
    }


    private String formattedText(LogLevel logLevel, String msg) {
        return String.format("[%s][%-5s] %s", nowTimestamp(), logLevel.prefix, msg);
    }

    private void logMessage(String formattedText, String styleClass) {
        try {
            appendTextWithStyleClass(formattedText, styleClass);
            scrollToBottom();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendTextWithStyleClass(String formattedText, String styleClass) {
        final int previousLength = logArea.getText().length();
        logArea.appendText(textToAppend(formattedText));
        logArea.setStyleClass(previousLength, logArea.getText().length(), styleClass);
    }

    private String textToAppend(String formattedText) {
        return formattedText + "\n";
    }

    private void scrollToBottom() {
        logArea.scrollYBy(Double.MAX_VALUE);
    }

    private void logLater(LogLevel logLevel, String msg) {
        Platform.runLater(() -> logMessage(formattedText(logLevel, msg), styleClassFor(logLevel)));
    }

    private String styleClassFor(LogLevel logLevel) {
        switch (logLevel) {
            case TRACE:
                return "log_line_gray";
            case DEBUG:
                return "log_line_gray";
            case INFO:
                return "log_line_black";
            case WARN:
                return "log_line_blue";
            case ERROR:
                return "log_line_red";
            default:
                throw new IllegalArgumentException(String.format("Unhandled log level %s", logLevel.prefix));
        }
    }
}
