package application.exceptions;

public class ClusterConfigurationError extends KafkaToolError {
    public ClusterConfigurationError(String message) {
        super(message);
    }

    public ClusterConfigurationError(String message, Throwable cause) {
        super(message, cause);
    }
}
