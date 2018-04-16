package application.exceptions;

public class KafkaToolError extends Exception {

    public KafkaToolError(String message) {
        super(message);
    }

    public KafkaToolError(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaToolError(Throwable e) {
        super(e);
    }
}
