package application.exceptions;

public class KafkaToolInternalError extends RuntimeException {


    public KafkaToolInternalError(String message) {
        super(message);
    }
}
