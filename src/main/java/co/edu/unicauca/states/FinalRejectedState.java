package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class FinalRejectedState implements IProcessState {

    @Override
    public void approve(Process process) {
        throw new StatusProcessException("Final rejected process cannot be approved.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Process already finalized as rejected.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Cannot resubmit after final rejection.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.REJECTED_DEF;
    }
}
