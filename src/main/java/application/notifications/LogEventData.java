package application.notifications;

import application.exceptions.KafkaToolInternalError;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;


public final class LogEventData {
    private final LogLevel level;
    private final String shortMsg;
    private final String longMsg;
    private final Instant timestamp;
    private final Throwable cause;
    private final boolean shouldIgnoreLogLevel;

    public LogLevel getLevel() {
        return level;
    }

    public String getShortMsg() {
        return shortMsg;
    }

    public String getLongMsg() {
        return longMsg;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Throwable getCause() {
        return cause;
    }

    public LogEventData(LogLevel level,
                        String shortMsg,
                        String longMsg,
                        Instant timestamp,
                        Throwable cause,
                        boolean shouldIgnoreLogLevel) {
        this.level = level;
        this.shortMsg = shortMsg;
        this.longMsg = longMsg;
        this.timestamp = timestamp;
        this.cause = cause;
        this.shouldIgnoreLogLevel = shouldIgnoreLogLevel;
    }

    public static final class LogEventDataBuilder {
        private LogLevel level;
        private String shortMsg;
        private String longMsg;
        private Instant timestamp = Instant.now();
        private Throwable cause;
        private boolean shouldIgnoreLogLevel;


        public void withShouldIgnoreLogLevel(boolean shouldIgnoreLogLevel) {
            this.shouldIgnoreLogLevel = shouldIgnoreLogLevel;
        }

        private LogEventDataBuilder() {
        }

        public static LogEventDataBuilder builder() {
            return new LogEventDataBuilder();
        }

        public LogEventDataBuilder withLevel(LogLevel level) {
            this.level = level;
            return this;
        }

        public LogEventDataBuilder withShortMsg(String shortMsg) {
            this.shortMsg = shortMsg;
            return this;
        }

        public LogEventDataBuilder withLongMsg(String longMsg) {
            this.longMsg = longMsg;
            return this;
        }

        public LogEventDataBuilder withTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public LogEventDataBuilder withCause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public LogEventData build() {
            if (StringUtils.isBlank(shortMsg) && cause == null) {
                throw new KafkaToolInternalError("Short msg and cause cannot be null at the same time");
            }
            return new LogEventData(this.level,
                                    this.shortMsg,
                                    this.longMsg,
                                    this.timestamp,
                                    this.cause,
                                    this.shouldIgnoreLogLevel);
        }
    }
}
