package application.exceptions;


public class PrerequisiteNotSatisfiedError extends KafkaToolError {
    public PrerequisiteNotSatisfiedError(String message) {
        super(message);
    }

    public PrerequisiteNotSatisfiedError(String message, Throwable cause) {
        super(message, cause);
    }
}