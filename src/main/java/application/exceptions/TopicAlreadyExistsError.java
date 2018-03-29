package application.exceptions;

public class TopicAlreadyExistsError extends KafkaToolError {
    public TopicAlreadyExistsError(String message) {
        super(message);
    }

    public TopicAlreadyExistsError(String message, Throwable cause) {
        super(message, cause);
    }
}
