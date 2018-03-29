
package application.exceptions;

public class InvalidKafkaBrokerConfiguration extends KafkaToolError {
    public InvalidKafkaBrokerConfiguration(String message) {
        super(message);
    }
}
