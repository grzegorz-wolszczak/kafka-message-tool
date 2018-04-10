package application.logging;

import application.notifications.LogLevel;
import application.utils.ThrowableUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LogEventDataFormatter {
    public String formatTimestamp(Instant timestamp) {
        return String.format("[%s]",
                             LocalDateTime.ofInstant(timestamp,
                                                     ZoneId.systemDefault()).toLocalTime());
    }

    public String formatLogLevel(LogLevel level) {
        return String.format("[%-5s]", level.prefix);
    }

    public String formatMessage(String shortMsg, String longMsg, Throwable cause) {
        if (StringUtils.isBlank(shortMsg) && StringUtils.isBlank(longMsg) && cause == null) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        if (StringUtils.isNotBlank(shortMsg)) {
            builder.append(shortMsg).append("\n");
        }

        if (StringUtils.isNotBlank(longMsg)) {
            builder.append(longMsg).append("\n");
        }
        if (cause != null) {
            final String message = ThrowableUtils.getMessage(cause);
            builder.append(message).append("\n");
        }
        return builder.toString();
    }
}
