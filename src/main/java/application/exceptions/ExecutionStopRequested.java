package application.exceptions;

public class ExecutionStopRequested extends Exception {

    public ExecutionStopRequested(Throwable cause) {
        super(cause);
    }
}
