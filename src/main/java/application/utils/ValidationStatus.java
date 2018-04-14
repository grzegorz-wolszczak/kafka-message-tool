package application.utils;

public final class ValidationStatus {

    private final boolean isSuccess;
    private final String failureMessage;

    private ValidationStatus(boolean isSuccess, String failureMessage) {
        this.isSuccess = isSuccess;
        this.failureMessage = failureMessage;
    }

    public static ValidationStatus success() {
        return new ValidationStatus(true, null);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public String validationFailureMessage() {
        return failureMessage;
    }

    public static ValidationStatus failure(String failReason) {
        return new ValidationStatus(false, failReason);
    }
}
