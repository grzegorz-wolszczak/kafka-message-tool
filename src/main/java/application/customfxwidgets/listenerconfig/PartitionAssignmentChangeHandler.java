package application.customfxwidgets.listenerconfig;

import application.kafka.listener.AssignedPartitionsInfo;
import application.logging.Logger;
import application.model.modelobjects.KafkaListenerConfig;
import application.utils.gui.ColorChangableLabelWrapper;
import application.utils.gui.FXNodeBlinker;
import javafx.scene.control.Label;
import org.apache.commons.lang3.StringUtils;

public class PartitionAssignmentChangeHandler {
    public static final String DISCONNECTED_FROM_BROKER_STRING = "<DISCONNECTED FROM BROKER>";
    private FXNodeBlinker fxNodeBlinker;
    private KafkaListenerConfig config;
    private Label assignedPartitionsLabel;
    private String oldPartitionAssignmentsValue = DISCONNECTED_FROM_BROKER_STRING;

    public PartitionAssignmentChangeHandler(FXNodeBlinker fxNodeBlinker,
                                            KafkaListenerConfig config) {

        this.fxNodeBlinker = fxNodeBlinker;
        this.config = config;
    }

    public void setLabelToChange(Label assignedPartitionsLabel) {
        this.assignedPartitionsLabel = assignedPartitionsLabel;
        fxNodeBlinker.setNodeToBlink(new ColorChangableLabelWrapper(assignedPartitionsLabel));
    }

    public void updatePartitionsAssignmentLabelFor(AssignedPartitionsInfo newPartitionsAssignment) {

        String newValueToSet = DISCONNECTED_FROM_BROKER_STRING;

        if (newPartitionsAssignment != null && newPartitionsAssignment.isValid()) {
            newValueToSet = StringUtils.join(newPartitionsAssignment.getPartitionsList());
            Logger.info(String.format("Partitions assignments for config '%s' changed to %s, reason '%s'",
                    config.getName(), newValueToSet, newPartitionsAssignment.getChangeReason()));
        }

        if (oldPartitionAssignmentsValue.equals(newValueToSet)) {
            return;
        }

        assignedPartitionsLabel.setText(newValueToSet);
        fxNodeBlinker.blink();
        oldPartitionAssignmentsValue = newValueToSet;
    }
}
