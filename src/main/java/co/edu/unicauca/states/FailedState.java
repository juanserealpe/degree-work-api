package co.edu.unicauca.states;

import co.edu.unicauca.entities.Process;
import co.edu.unicauca.enums.ProcessState;
import co.edu.unicauca.exceptions.StateProcessException;
import co.edu.unicauca.interfaces.IProcessState;

public class FailedState implements IProcessState {

    @Override
    public void submit(Process process) {
        throw new StateProcessException("Failed process cannot be submitted again.");
    }

    @Override
    public void approve(Process process) {
        throw new StateProcessException("Failed process cannot be approved.");
    }

    @Override
    public void reject(Process process, String observation) {
        throw new StateProcessException("Process has already failed definitively.");
    }

    @Override
    public void resubmit(Process process, String newUrl) {
        throw new StateProcessException("Maximum attempts reached. Cannot resubmit.");
    }

    @Override
    public void assignJury(Process process) {
        throw new StateProcessException("Cannot assign jury to a failed process.");
    }

    @Override
    public ProcessState getStatus() {
        return ProcessState.FAILED;
    }
}