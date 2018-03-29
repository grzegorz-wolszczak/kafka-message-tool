
package application.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public static String nowTimestamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now());
    }

    public static String timestampFromEpochMili(long epochMili) {
        return DATE_TIME_FORMATTER
            .format((LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMili),
                                             ZoneId.systemDefault())));
    }
}
