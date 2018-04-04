package application.kafka;

public enum TopicCleanupPolicy {
    DELETE("delete"),
    COMPACT("compact");

    private final String value;

    TopicCleanupPolicy(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static TopicCleanupPolicy fromString(String value) {
        if (value.equalsIgnoreCase(COMPACT.value)) {
            return COMPACT;
        }
        return DELETE;
    }

    @Override
    public String toString() {
        return value;
    }

}
