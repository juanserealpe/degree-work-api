package co.edu.unicauca.fabrics;

import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;
import co.edu.unicauca.states.*;

public class ProcessStateFactory {

    public static IProcessState fromStatus(ProcessState status) {
        return switch (status) {
            case CREATED -> new CreatedState();
            case SUBMITTED -> new SubmittedState();
            case REJECTED -> new RejectedState();
            case APPROVED -> new ApprovedState();
            case ASSIGNED -> new AssignedState();
            case FAILED -> new FailedState();
            default -> throw new StateProcessException("Unsupported status: " + status);
        };
    }
}