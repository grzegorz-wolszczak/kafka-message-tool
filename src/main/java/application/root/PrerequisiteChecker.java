package application.root;

import application.exceptions.KafkaToolError;
import application.exceptions.PrerequisiteNotSatisfiedError;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class PrerequisiteChecker {
    public static void assertPrerequisites() throws KafkaToolError {
        assertGroovyEngineIsAvailable();
    }

    private static void assertGroovyEngineIsAvailable() throws KafkaToolError {
        final ScriptEngine groovyEngine = new ScriptEngineManager().getEngineByName("groovy");
        if (groovyEngine == null) {
            throw new PrerequisiteNotSatisfiedError("Could not get groovy scripting engine.");
        }
    }
}
