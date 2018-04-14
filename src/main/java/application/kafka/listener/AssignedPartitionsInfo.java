package application.kafka.listener;

import java.util.ArrayList;
import java.util.List;

final public class AssignedPartitionsInfo {
    public boolean isValid() {
        return isValid;
    }

    private boolean isValid;
    private List<Integer> partitionsList;

    private AssignedPartitionsInfo(boolean isValid, List<Integer> partitionsList) {
        this.isValid = isValid;
        this.partitionsList = partitionsList;
    }

    private AssignedPartitionsInfo(List<Integer> list) {
        isValid = true;
        partitionsList = new ArrayList<>(list);
    }

    public static AssignedPartitionsInfo invalid() {
        return new AssignedPartitionsInfo(false, null);

    }

    public static AssignedPartitionsInfo fromPartitionList(List<Integer> list) {
        if (list == null) {
            return AssignedPartitionsInfo.invalid();
        }
        return new AssignedPartitionsInfo(list);
    }

    public List<Integer> getPartitionsList() {
        return new ArrayList<>(partitionsList);
    }
}
