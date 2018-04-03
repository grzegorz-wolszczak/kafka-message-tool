package application.utils;

public final class ValidationStatus {

    private final boolean status;
    private final String failureMessage;

    private ValidationStatus(boolean status, String failureMessage) {
        this.status = status;
        this.failureMessage = failureMessage;
    }

    public static ValidationStatus success() {
        return new ValidationStatus(true, null);
    }

    public boolean isSuccess() {
        return status;
    }

    public String validationFailureMessage() {
        return failureMessage;
    }

    public static ValidationStatus failure(String failReason) {
        return new ValidationStatus(false, failReason);
    }
}
