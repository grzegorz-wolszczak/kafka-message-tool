package application.exceptions;

public class TopicMarkedForDeletionError extends KafkaToolError {
    public TopicMarkedForDeletionError(String message) {
        super(message);
    }

}
