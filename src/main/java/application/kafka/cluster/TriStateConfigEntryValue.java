package application.kafka.cluster;

public enum TriStateConfigEntryValue {
    True,
    False,
    Inconsistent;

    public static TriStateConfigEntryValue of(boolean value) {
        if (value) {
            return TriStateConfigEntryValue.True;
        }
        return TriStateConfigEntryValue.False;
    }
}
