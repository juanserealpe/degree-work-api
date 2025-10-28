package co.edu.unicauca.fabrics;

import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.interfaces.IProcessState;
import co.edu.unicauca.states.ApprovedState;
import co.edu.unicauca.states.FinalRejectedState;
import co.edu.unicauca.states.PendingState;
import co.edu.unicauca.states.RejectedState;

public class ProcessStateFactory {

    public static IProcessState fromStatus(ProcessStatus status) {
        return switch (status) {
            case PENDING -> new PendingState();
            case APPROVATED -> new ApprovedState();
            case REJECTED_TEMP -> new RejectedState();
            case REJECTED_DEF -> new FinalRejectedState();
            default -> throw new IllegalArgumentException("Unsupported status: " + status);
        };
    }
}
