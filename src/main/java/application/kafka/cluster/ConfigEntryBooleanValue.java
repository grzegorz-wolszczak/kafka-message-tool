package application.kafka.cluster;

public enum ConfigEntryBooleanValue {
    True,
    False,
    Inconsistent;

    public static ConfigEntryBooleanValue of(boolean value) {
        if (value) {
            return ConfigEntryBooleanValue.True;
        }
        return ConfigEntryBooleanValue.False;
    }
}
