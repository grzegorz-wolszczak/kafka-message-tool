package application.kafka.listener;

import java.util.ArrayList;
import java.util.List;

final public class AssignedPartitionsInfo {

    private AssignmentChangeReason changeReason;
    private boolean isValid;
    private List<Integer> partitionsList;

    private AssignedPartitionsInfo() {
        this.isValid = false;
        this.partitionsList = null;
    }

    private AssignedPartitionsInfo(List<Integer> list, AssignmentChangeReason reason) {
        changeReason = reason;
        isValid = true;
        partitionsList = new ArrayList<>(list);
    }

    public static AssignedPartitionsInfo invalid() {
        return new AssignedPartitionsInfo();

    }

    public static AssignedPartitionsInfo fromPartitionList(List<Integer> list,
                                                           AssignmentChangeReason reason) {
        if (list == null) {
            return AssignedPartitionsInfo.invalid();
        }
        return new AssignedPartitionsInfo(list, reason);
    }

    public boolean isValid() {
        return isValid;
    }

    public List<Integer> getPartitionsList() {
        return new ArrayList<>(partitionsList);
    }

    public AssignmentChangeReason getChangeReason() {
        return changeReason;
    }
}
