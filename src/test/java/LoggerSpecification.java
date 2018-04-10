
import application.logging.AppLogger;
import application.notifications.LogLevel;
import autofixture.publicinterface.Any;
import application.logging.ToolLogger;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LoggerSpecification {

    @Test
    public void shouldLogOnlyErrorIfServerityErrorIsSet() {
        // GIVEN
        final String anyLog = Any.string();
        ToolLogger toolLogger = mock(ToolLogger.class);
        AppLogger.setLogLevel(LogLevel.ERROR);
        AppLogger.registerLogger(toolLogger);


        // WHEN
        AppLogger.error(anyLog);
        AppLogger.warn(anyLog);
        AppLogger.info(anyLog);
        AppLogger.debug(anyLog);


        // THEN
        verify(toolLogger, Mockito.times(1)).logError(anyLog);
        verify(toolLogger, never()).logWarn(any());
        verify(toolLogger, never()).logInfo(any());
        verify(toolLogger, never()).logDebug(any());

    }

    @Test
    public void shouldLogWarningAndHigherIfServerityWarningIsSet() {
        // GIVEN
        final String anyLog = Any.string();
        ToolLogger toolLogger = mock(ToolLogger.class);
        AppLogger.setLogLevel(LogLevel.WARN);
        AppLogger.registerLogger(toolLogger);


        // WHEN
        AppLogger.error(anyLog);
        AppLogger.warn(anyLog);
        AppLogger.info(anyLog);
        AppLogger.debug(anyLog);


        // THEN
        verify(toolLogger, Mockito.times(1)).logError(anyLog);
        verify(toolLogger, Mockito.times(1)).logWarn(anyLog);
        verify(toolLogger, never()).logInfo(any());
        verify(toolLogger, never()).logDebug(any());

    }

    @Test
    public void shouldLogInfoAndHigherIfServerityInfoIsSet() {
        // GIVEN
        final String anyLog = Any.string();
        ToolLogger toolLogger = mock(ToolLogger.class);
        AppLogger.setLogLevel(LogLevel.INFO);
        AppLogger.registerLogger(toolLogger);


        // WHEN
        AppLogger.error(anyLog);
        AppLogger.warn(anyLog);
        AppLogger.info(anyLog);
        AppLogger.debug(anyLog);


        // THEN
        verify(toolLogger, Mockito.times(1)).logError(anyLog);
        verify(toolLogger, Mockito.times(1)).logWarn(anyLog);
        verify(toolLogger, Mockito.times(1)).logInfo(anyLog);
        verify(toolLogger, never()).logDebug(any());

    }

    @Test
    public void shouldLogDebugAndHigherIfServerityDebugIsSet() {
        // GIVEN
        final String anyLog = Any.string();
        ToolLogger toolLogger = mock(ToolLogger.class);
        AppLogger.registerLogger(toolLogger);
        AppLogger.setLogLevel(LogLevel.DEBUG);


        // WHEN
        AppLogger.error(anyLog);
        AppLogger.warn(anyLog);
        AppLogger.info(anyLog);
        AppLogger.debug(anyLog);


        // THEN
        verify(toolLogger, Mockito.times(1)).logError(anyLog);
        verify(toolLogger, Mockito.times(1)).logWarn(anyLog);
        verify(toolLogger, Mockito.times(1)).logInfo(anyLog);
        verify(toolLogger, Mockito.times(1)).logDebug(anyLog);

    }
}
