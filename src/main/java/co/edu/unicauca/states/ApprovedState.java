package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class ApprovedState implements IProcessState {
    @Override
    public void approve(Process process) {
        throw new StatusProcessException("The process is already approved.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Cannot reject an approved process.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Approved process cannot be resubmitted.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.APPROVATED;
    }
}
