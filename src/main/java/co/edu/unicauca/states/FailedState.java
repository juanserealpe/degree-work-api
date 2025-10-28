package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessStatus;
import co.edu.unicauca.exceptions.StatusProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class FailedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StatusProcessException("Failed process cannot be submitted again.");
    }

    @Override
    public void approve(Process process) {
        throw new StatusProcessException("Failed process cannot be approved.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StatusProcessException("Process has already failed definitively.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StatusProcessException("Maximum attempts reached. Cannot resubmit.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StatusProcessException("Cannot assign jury to a failed process.");
    }

    @Override
    public ProcessStatus getStatus() {
        return ProcessStatus.FAILED;
    }
}