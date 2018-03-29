
package application.exceptions;

public class InvalidTopicConfigurationError extends KafkaToolError {
    public InvalidTopicConfigurationError(String message) {
        super(message);
    }
}
