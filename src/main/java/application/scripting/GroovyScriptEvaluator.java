package application.scripting;

import application.exceptions.ExecutionStopRequested;
import application.exceptions.KafkaToolError;
import application.logging.Logger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GroovyScriptEvaluator {

    private static final String ENGINE_NAME = "groovy";
    private static final int EVALUATE_GROOVY_SCRIPT_TIMEOUT_SEC = 5;
    private final ScriptEngine engine;
    private SimpleScriptContext ctx;

    public GroovyScriptEvaluator() {
        engine = new ScriptEngineManager().getEngineByName(ENGINE_NAME);
        resetScriptContext();
    }
    public void resetScriptContext(){
        ctx = new SimpleScriptContext();
        engine.setContext(ctx);
    }

    public Object getBinding(String valueName) {
        return engine.get(valueName);
    }

    public void runScript(String script) throws Exception {
        Logger.trace(String.format("evaluation script: %s", script));
        final FutureTask<Object> evaluateScriptTask = new FutureTask<>(() -> engine.eval(script, ctx));
        // todo : consider thread pool, but remember that thead pool must be shutdown before application exit
        new Thread(evaluateScriptTask, "KMT-Thread-EvaluateGroovyScriptTask").start();

        try {
            final Object result = evaluateScriptTask.get(EVALUATE_GROOVY_SCRIPT_TIMEOUT_SEC, TimeUnit.SECONDS);
            Logger.trace(String.format("Evaluation result: %s", result));
        } catch (TimeoutException e) {
            throw new KafkaToolError(String.format("EvaluationTimeout. Could not evaluate groovy script within %d seconds.",
                                                   EVALUATE_GROOVY_SCRIPT_TIMEOUT_SEC));

        } catch (InterruptedException e) {
            // this exception can happen if user clicks "stop" button
            Logger.trace(String.format("Evaluation of script '%s' stopped. InterruptedException", script));
            throw new ExecutionStopRequested(e);

        } catch (Exception e) {
            throw new KafkaToolError(String.format("Could not evaluate groovy script '%s'", script), e);
        }
    }
}
