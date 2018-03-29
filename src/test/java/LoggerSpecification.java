
import application.logging.LogLevel;
import autofixture.publicinterface.Any;
import application.logging.ToolLogger;
import application.logging.Logger;

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
        Logger.setLogLevel(LogLevel.ERROR);
        Logger.registerLogger(toolLogger);


        // WHEN
        Logger.error(anyLog);
        Logger.warn(anyLog);
        Logger.info(anyLog);
        Logger.debug(anyLog);


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
        Logger.setLogLevel(LogLevel.WARN);
        Logger.registerLogger(toolLogger);


        // WHEN
        Logger.error(anyLog);
        Logger.warn(anyLog);
        Logger.info(anyLog);
        Logger.debug(anyLog);


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
        Logger.setLogLevel(LogLevel.INFO);
        Logger.registerLogger(toolLogger);


        // WHEN
        Logger.error(anyLog);
        Logger.warn(anyLog);
        Logger.info(anyLog);
        Logger.debug(anyLog);


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
        Logger.registerLogger(toolLogger);
        Logger.setLogLevel(LogLevel.DEBUG);


        // WHEN
        Logger.error(anyLog);
        Logger.warn(anyLog);
        Logger.info(anyLog);
        Logger.debug(anyLog);


        // THEN
        verify(toolLogger, Mockito.times(1)).logError(anyLog);
        verify(toolLogger, Mockito.times(1)).logWarn(anyLog);
        verify(toolLogger, Mockito.times(1)).logInfo(anyLog);
        verify(toolLogger, Mockito.times(1)).logDebug(anyLog);

    }
}
