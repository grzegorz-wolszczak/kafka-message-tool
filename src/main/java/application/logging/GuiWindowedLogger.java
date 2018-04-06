package application.logging;

import javafx.application.Platform;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.ArrayDeque;

import static application.utils.TimestampUtils.nowTimestamp;


public class GuiWindowedLogger implements ToolLogger {

    public static final int MAX_ENTIRES = 100;
    private final StyleClassedTextArea logArea;
    private final ArrayDeque<Integer> snippetsSize = new ArrayDeque<>();


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
        final String text = textToAppend(formattedText);
        logArea.appendText(text);
        final int afterLength = logArea.getText().length();

        int snippetSize = afterLength - previousLength;

//        System.out.println(String.format("\n\nPrevious area text len: %d, text to add len: %d, now area textLen: %d",
//                                         previousLength, text.length(),  afterLength));

        snippetsSize.addFirst(snippetSize);
        //System.out.println(String.format("Adding snippet of length : %d", snippetSize));
        logArea.setStyleClass(previousLength, afterLength, styleClass);
        //System.out.println(String.format("Snippets count: %d", snippetsSize.size()));

        while(snippetsSize.size()> MAX_ENTIRES)
        {
          //  System.out.println(String.format("Deleting because snippets size: %d", snippetsSize.size()));
            final Integer snippetSizeToRemove = snippetsSize.removeLast();
            //final int areaTextLengthBefore = logArea.getText().length();
//            final int accessibleTextLenghtBefore = logArea.getAccessibleText().length();

            logArea.deleteText(0, snippetSizeToRemove);

            //final int areaTextLengthAfter = logArea.getText().length();
//            final int accessibleTextLenghtAfter = logArea.getAccessibleText().length();

            //System.out.println(String.format("Before area - text len:%d,  snippet size: %d, After area - text len:%d",
            //                   areaTextLengthBefore,  snippetSizeToRemove, areaTextLengthAfter));
        }
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
