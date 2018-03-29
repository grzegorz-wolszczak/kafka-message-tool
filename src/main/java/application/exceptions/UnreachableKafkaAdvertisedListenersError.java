
package application.exceptions;

public class UnreachableKafkaAdvertisedListenersError extends KafkaToolError {
    public UnreachableKafkaAdvertisedListenersError(String message) {
        super(message);
    }

    public UnreachableKafkaAdvertisedListenersError(String message, Throwable cause) {
        super(message, cause);
    }
}
