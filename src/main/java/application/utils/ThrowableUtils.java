package application.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtils {
    public static String getMessage(Throwable e) {

        String msg = getRootCauseMessage(e);
        if (StringUtils.isBlank(msg)) {
            msg = ClassUtils.getShortClassName(e.getClass());
        }
        return msg;
    }

    public static String getMessageWithRootCause(Exception e) {

        final String msg = e.getLocalizedMessage();
        final String rootCause = ExceptionUtils.getMessage(ExceptionUtils.getRootCause(e));
        if (StringUtils.isNotBlank(rootCause)) {
            return msg + " (root cause: " + rootCause + ")";
        }
        if (msg == null) {
            return e.getClass().getName();
        }
        return msg;
    }

    public static String getFullStackTrace(Throwable e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        ExceptionUtils.printRootCauseStackTrace(e, pw);
        return sw.getBuffer().toString();
    }

    private static String getRootCauseMessage(Throwable e) {
        final String msg = ExceptionUtils.getRootCauseMessage(e);
        return translateExceptionMsgToBeMoreUserReadableIfPossible(msg);
    }

    private static String translateExceptionMsgToBeMoreUserReadableIfPossible(String msg) {
        if (msg.equalsIgnoreCase("ConfigException: No resolvable bootstrap urls given in bootstrap.servers")) {
            return "Could not connect to host. Hostname could not be resolved.";
        }
        return msg;
    }
}
