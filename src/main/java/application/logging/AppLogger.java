
package application.logging;

import application.notifications.LogEventData;
import application.notifications.LogLevel;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashSet;
import java.util.Set;

public class AppLogger {

    private static final Set<ToolLogger> LOGGERS = new HashSet<>();
    private static LogLevel currentLevel = LogLevel.INFO;


    public static void registerLogger(ToolLogger logger) {
        if (null == logger) {
            return;
        }
        logger.setLogLevel(currentLevel);
        LOGGERS.add(logger);
    }


    public static void processLogEvent(LogEventData logEvent) {
        LOGGERS.forEach(l->{
            l.processLogEvent(logEvent);
        });
    }

//    private static void logInfo(String msg) {
//        LOGGERS.forEach(l -> {
//            try {
//                l.logInfo(msg);
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    public static void debug(String msg) {
//        if (currentLevel.isGreaterThan(LogLevel.DEBUG)) {
//            return;
//        }
//        LOGGERS.forEach(l -> {
//            try {
//                l.logDebug(msg);
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    public static void trace(String msg) {
//        if (currentLevel.isGreaterThan(LogLevel.TRACE)) {
//            return;
//        }
//
//        final LogLocationInfo logLocationInfo1 = new LogLocationInfo().invoke();
//        final String location = getLogLocation(logLocationInfo1);
//        LOGGERS.forEach(l -> {
//            try {
//                l.logTrace(String.format("%s: %s", location, msg));
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }
//
//    private static String getLogLocation(LogLocationInfo info) {
//        return String.format("%s.%s(%s:%d)",
//                             info.getClassName(),
//                             info.getMethodName(),
//                             info.getFileName(),
//                             info.getLineNumber());
//    }
//
//
//    public static void trace(Throwable e) {
//        if (currentLevel.isGreaterThan(LogLevel.TRACE)) {
//            return;
//        }
//
//        final LogLocationInfo info = new LogLocationInfo().invoke();
//        final String location = getLogLocation(info);
//        final String msg = ExceptionUtils.getStackTrace(e);
//        LOGGERS.forEach(l -> {
//            try {
//                l.logTrace(String.format("%s: %s", location, msg));
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
//        });
//    }

    public static void clear() {
        LOGGERS.forEach(l -> {
            try {
                l.clear();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static void setLogLevel(LogLevel newLevel) {
        if (newLevel != currentLevel) {
            logInfo(String.format("Setting log level to:%s (from:%s) ", newLevel.name(), currentLevel.name()));
            currentLevel = newLevel;
            LOGGERS.forEach(l-> l.setLogLevel(currentLevel));
        }
    }

    private static class LogLocationInfo {
        public static final int STACK_FRAME_DEPTH = 3;
        private String className;
        private String methodName;
        private int lineNumber;
        private String fileName;

        String getFileName() {
            return fileName;
        }

        String getClassName() {
            return className;
        }

        String getMethodName() {
            return methodName;
        }

        int getLineNumber() {
            return lineNumber;
        }

        LogLocationInfo invoke() {
            final StackTraceElement element = Thread.currentThread().getStackTrace()[STACK_FRAME_DEPTH];
            final String fullClassName = element.getClassName();
            className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            fileName = element.getFileName();
            methodName = element.getMethodName();
            lineNumber = element.getLineNumber();
            return this;
        }
    }
}
