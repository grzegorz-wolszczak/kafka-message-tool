
package application.model;

public enum KafkaOffsetResetType {
    EARLIEST("earliest"),
    LATEST("latest"),
    NONE("none");

    private final String value;

    KafkaOffsetResetType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }


}
